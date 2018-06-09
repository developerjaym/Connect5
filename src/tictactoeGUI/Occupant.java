package tictactoeGUI;

public enum Occupant {
	X("black", "B"),
	O("white", "W"),
	EMPTY("transparent", "");
	private String color;
	private String text;
	private Occupant(String color, String text) {
		this.color = color;
		this.text = text;
	}
	public String getColor() {
		return color;
	}
	public String getText() {
		return text;
	}
	
}
