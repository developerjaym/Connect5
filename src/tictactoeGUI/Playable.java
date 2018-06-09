package tictactoeGUI;

import java.util.concurrent.Flow.Publisher;

public interface Playable extends Publisher<GameMessage>{
	void onClick(Point p, ChangeType changeType);
	void onEnd();
	void undo();
}
