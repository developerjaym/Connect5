package tictactoeGUI;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Stream;

import javafx.scene.layout.GridPane;

public class BoardGUI extends GridPane implements Subscriber<GameMessage> {
	Map<Point, BlockButton> blockButtons;
	public BoardGUI(int width, int height, Playable game) {
		blockButtons = new HashMap<>();
		Stream
		.iterate(new Point(0, 0), (p) -> p.getX()==width-1 ? new Point(0, p.getY()+1) : new Point(p.getX()+1, p.getY()))
		.limit(height*width)// a stream of height*width points
				.forEach(p -> {
					blockButtons.put(p, new BlockButton());
					blockButtons.get(p).setOnAction(e -> game.onClick(p, ChangeType.NEWCHANGE));
					add(blockButtons.get(p), p.getX(), p.getY());
				}
			);
		game.subscribe(this);
	}
	
	@Override
	public void onSubscribe(Subscription subscription) {
		subscription.request(Long.MAX_VALUE);
	}

	@Override
	public void onError(Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	public void onComplete() {
		blockButtons.values().stream().forEach(bb->bb.restore());
	}

	@Override
	public void onNext(GameMessage item) {
		Optional
		.of(item)
		.map(gameMessage->gameMessage.getPointOfPlacement())
		.ifPresent(point->blockButtons.get(point).fill(item.getOccupant()));
		
		Optional
		.of(item)
		.map(i->i.getPoints())
		.stream()
		.flatMap(lp->lp.stream())
		.forEach(point->
		blockButtons.get(point).setStyle(BlockButton.FILLED));
	}
}
