package view;

import javafx.scene.layout.BorderPane;

public class RowPane extends BorderPane{
	
	private boolean isFirstRowInBox;
	//nrOfClosingBoxes tracks how many boxes that this row is the last line of
	private int nrOfClosingBoxes;
	
	public RowPane(boolean isFirstRowInBox, int nrOfClosingBoxes){
		super();
		this.isFirstRowInBox = isFirstRowInBox;
		this.setNrOfClosingBoxes(nrOfClosingBoxes);
	}
	
	public void setIsFirstRowInBox(boolean b){
		isFirstRowInBox = b;
	}
	
	public boolean getIsFirstRowInBox(){
		return isFirstRowInBox;
	}

	public int getNrOfClosingBoxes() {
		return nrOfClosingBoxes;
	}

	public void setNrOfClosingBoxes(int nrOfClosingBoxes) {
		this.nrOfClosingBoxes = nrOfClosingBoxes;
	}
	
	public void incrementNrOfClosingBoxes(){
		nrOfClosingBoxes++;
	}
}
