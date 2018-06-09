package tictactoeGUI;


public class Block{
	private Occupant occupant;
	public Block() {
		this.occupant = Occupant.EMPTY;
	}
	public Block(Block b) {
		this.setOccupant(b.getOccupant());
	}
	public Occupant getOccupant() {
		return occupant;
	}
	public void setOccupant(Occupant upPlayer) {
		this.occupant = upPlayer;
	}
	
}
