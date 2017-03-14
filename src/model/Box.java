package model;

public class Box {
	private boolean open;
	//public final int id; //needed?
	//public int nrOfLinesInBox; //needed?
	
	public Box(boolean open){
		this.open = open;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public void setOpen(boolean open){
		this.open = open;
	}
}
