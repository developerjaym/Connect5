package tictactoeGUI;

import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

public class GameSubscription implements Subscription {

	private final Subscriber<? super GameMessage> subscriber;
	private final List<GameChange> gameChanges;
	public GameSubscription(Subscriber<? super GameMessage> subscriber2, List<GameChange> gameChanges) {
		this.gameChanges = gameChanges;
		this.subscriber = subscriber2;
	}

	@Override
	public void request(long n) {
		this.gameChanges
		.subList(n < this.gameChanges.size() ? (int) (this.gameChanges.size()-n) : 0, this.gameChanges.size())
		.stream()
		.forEachOrdered(gameChange -> this.subscriber.onNext(new GameMessage(gameChange.getPointOfPlacement(), gameChange.getPointsInSequences(), gameChange.getOccupant())));
	}

	@Override
	public void cancel() {
	}

}
