package tictactoeGUI;

import java.util.List;

public class GameChange {
	private Point pointOfPlacement;
	private List<Point> pointsInSequences;
	private Occupant occupant;
	public GameChange(Point pointOfPlacement, List<Point> pointsInSequences, Occupant occupant) {
		super();
		this.pointOfPlacement = pointOfPlacement;
		this.pointsInSequences = pointsInSequences;
		this.occupant = occupant;
	}
	public Point getPointOfPlacement() {
		return pointOfPlacement;
	}
	public List<Point> getPointsInSequences() {
		return pointsInSequences;
	}
	public Occupant getOccupant() {
		return occupant;
	}
}
