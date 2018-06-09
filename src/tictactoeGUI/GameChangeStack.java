package tictactoeGUI;

import java.util.Optional;
import java.util.Stack;
import java.util.stream.Stream;

public class GameChangeStack {
	private Stack<GameChange> stack;
	
	public GameChangeStack() {
		this.stack = new Stack<>();
	}

	public Optional<GameChange> pop() {
		return Optional.of(stack).filter(s -> !s.isEmpty()).map(s->s.pop());
	}
	
	public void push(GameChange gameChange, ChangeType changeType) {
		Optional.of(changeType).filter(type -> type == ChangeType.NEWCHANGE).ifPresent(type -> stack.push(gameChange));
	}

	public Stream<GameChange> stream() {
		return stack.stream();
	}
}
