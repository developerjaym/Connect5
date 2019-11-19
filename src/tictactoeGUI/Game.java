package tictactoeGUI;

//@formatter:off
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Flow.Subscriber;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game implements Playable {
	private List<List<Block>> grid;
	private GameChangeStack gameChangeStack;
	private Set<Subscriber<? super GameMessage>> subscribers;
	private Occupant upPlayer = Occupant.X;
	private Occupant downPlayer = Occupant.O;
	private int width;
	private int height;
	public static final int WINAMOUNT = 5;

	public Game(int width, int height) {
		this.width = width;
		this.height = height;
		this.gameChangeStack = new GameChangeStack();
		this.grid = Stream.iterate(new Block[this.height][], (ba) -> new Block[this.height][]).limit(this.height).map(
				ba -> Stream.iterate(new Block(), (b) -> new Block()).limit(this.width).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	@Override
	public void subscribe(Subscriber<? super GameMessage> subscriber) {
		subscribers = Optional.ofNullable(subscribers).orElse(new HashSet<>());
		subscribers.add(subscriber);
		subscriber.onSubscribe(new GameSubscription(subscriber, gameChangeStack.stream().collect(Collectors.toList())));
	}

	@Override
	public void onClick(Point p, ChangeType changeType, boolean isRemote) {
		Optional.ofNullable(this.grid).map(grid -> grid.get(p.getY()).get(p.getX()))
				.filter(b -> b.getOccupant() == Occupant.EMPTY).ifPresent(block -> {
					block.setOccupant(upPlayer);
					Occupant previous = togglePlayer();
					var sequences = findSequences(p);
					this.gameChangeStack.push(
							new GameChange(p, 
									convertMapToList(sequences), 
									block.getOccupant(), isRemote), changeType);
					subscribers.stream().forEach(subscriber -> subscriber
							.onNext(new GameMessage(p, convertMapToList(sequences), previous, isRemote)));
				});
	}

	private List<Point> convertMapToList(Map<Direction, List<Point>> sequences) {
		return sequences
		.values()
		.stream()
		.flatMap(collectionOfLists -> collectionOfLists
				.stream())
		.collect(Collectors.toList());
	}

	private Occupant togglePlayer() {
		Occupant previous = upPlayer;
		upPlayer = downPlayer;
		downPlayer = previous;
		return previous;
	}

	private Map<Direction, List<Point>> findSequences(Point recent) {
		Map<Direction, List<Point>> myMap = Stream.of(
				new SimpleEntry<>(new Direction(0, 1, false, false), Stream.of(recent).collect(Collectors.toList())),
				new SimpleEntry<>(new Direction(-1, 1, false, false), Stream.of(recent).collect(Collectors.toList())),
				new SimpleEntry<>(new Direction(1, 0, false, false), Stream.of(recent).collect(Collectors.toList())),
				new SimpleEntry<>(new Direction(1, 1, false, false), Stream.of(recent).collect(Collectors.toList())))
				.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

		myMap.keySet().forEach(
				(direction) -> Stream.iterate(1, i -> i + 1).limit(height > width ? height : width).forEach(i -> {
					Optional.ofNullable(
							getBlockInQuestion(recent, new Point(direction.getX() * i, direction.getY() * i)))
							.filter(block -> !direction.isPositive())// stop if we've already reached a bad square in the
																	// positive direction
							.filter(block -> block.getOccupant()
									.equals(this.grid.get(recent.getY()).get(recent.getX()).getOccupant()))
							.ifPresentOrElse(
									block -> myMap.get(direction)
											.add(new Point(recent.getX() + (direction.getX() * i),
													recent.getY() + (direction.getY() * i))),
									() -> direction.setPositive(true));

					Optional.ofNullable(
							getBlockInQuestion(recent, new Point(-direction.getX() * i, -direction.getY() * i)))
							.filter(block -> !direction.isNegative())// stop if we've already reached a bad square in the
																	// negative direction
							.filter(block -> block.getOccupant()
									.equals(this.grid.get(recent.getY()).get(recent.getX()).getOccupant()))
							.ifPresentOrElse(
									b -> myMap.get(direction)
											.add(new Point(recent.getX() + (-direction.getX() * i),
													recent.getY() + (-direction.getY() * i))),
									() -> direction.setNegative(true));
				}));
		return myMap
				.entrySet()
				.stream()
				.filter(entry -> entry.getValue().size() >= WINAMOUNT)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	private Block getBlockInQuestion(Point recent, Point difference) {
		return (recent.getY() + difference.getY() >= height || recent.getX() + difference.getX() >= width
				|| recent.getY() + difference.getY() < 0 || recent.getX() + difference.getX() < 0) ? null
						: this.grid.get(recent.getY() + difference.getY()).get(recent.getX() + difference.getX());
	}

	@Override
	public void onEnd() {
		this.gameChangeStack = new GameChangeStack();
		resetPlayers();
		clearBoard();
	}

	private void clearBoard() {
		this.grid.stream().flatMap(blockArray -> blockArray.stream()).forEach(block -> block.setOccupant(Occupant.EMPTY));
		subscribers.stream().forEach(subscriber -> subscriber.onComplete());
	}

	@Override
	public void undo() {
		resetPlayers();
		clearBoard();
		this.gameChangeStack
		.pop()
		.ifPresent(gameChange -> this.gameChangeStack
				.stream()
				.forEach(pastGameChange -> onClick(pastGameChange.getPointOfPlacement(), ChangeType.UNDO, pastGameChange.isRemote())));
	}
	
	private void resetPlayers() {
		this.upPlayer = Occupant.X;
		this.downPlayer = Occupant.O;
	}
}

class Direction extends Point {
	private boolean positive;
	private boolean negative;

	public Direction(int x, int y, boolean positive, boolean negative) {
		super(x, y);
		this.positive = positive;
		this.negative = negative;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}
}