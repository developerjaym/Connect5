package tictactoeGUI;

import java.util.List;

public class GameMessage {
	private Point pointOfPlacement;
	private List<Point> points;
	private Occupant occupant;
	public GameMessage(Point pointOfPlacement, List<Point> points, Occupant occupant) {
		this.pointOfPlacement = pointOfPlacement;
		this.points = points;
		this.occupant = occupant;
	}
	
	public Point getPointOfPlacement() {
		return pointOfPlacement;
	}

	public List<Point> getPoints() {
		return points;
	}
	public Occupant getOccupant() {
		return occupant;
	}
	
}
