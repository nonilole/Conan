package model;

import java.util.ArrayList;
import java.util.List;

import model.rules.Interval;

public class Box implements ProofEntry{
	private boolean open;
	private int size; // only alter this value through incSize and decSize methods, they will also update parent boxes
	private Box parent;
	List<ProofEntry> entries = new ArrayList<ProofEntry>();
	
	public Box(Box parentBox, boolean open){
		this.parent = parentBox;
		this.open = open;
	}
	
	public void insertRow(int index, BoxReference br){
		assert(index < size);
		ProofRow referenceRow = getRow(index);
		Box boxToInsertInto = referenceRow.getParent();
		int internalReferenceIndex = boxToInsertInto.entries.indexOf(referenceRow);
		int insertionIndex = br == BoxReference.BEFORE ? internalReferenceIndex : internalReferenceIndex+1;
		boxToInsertInto.entries.add(insertionIndex, new ProofRow(boxToInsertInto));
		boxToInsertInto.incSize();
	}
	
	public void addRow(){
		//System.out.println("Box.addRow(");
		if(entries.isEmpty()) {
			entries.add(new ProofRow(this));
			incSize();
			return;
		}
		ProofEntry lastEntry = entries.get(entries.size()-1);
		if(lastEntry instanceof Box){
			Box box = (Box) lastEntry;
			if(box.isOpen()){
				box.addRow();
				return;
			}
		}
		entries.add(new ProofRow(this));
		incSize();
	}
	
	public ProofRow getRow(int steps){
		//System.out.println("Box.getRow("+steps+")");
		assert(steps < size) : "getRow: index="+steps+" size="+size;
		
		for(int i = 0; i < size ; i++ ){
			ProofEntry entry = entries.get(i);
			if(entry instanceof Box){
				Box box = (Box) entry;
				if(box.size() > steps)  return box.getRow(steps);
				else                    steps -= box.size();
			}
			else if(steps == 0){
				return (ProofRow)entry;
			}
			else{
				steps--;
			}
		}
		System.out.println("getRow: returning null");
		return null;
	}
	
	public void deleteRow(int index){
		assert(index < size);
		ProofRow referenceRow = getRow(index);
		Box parent = referenceRow.getParent();
        parent.entries.remove(referenceRow);
        parent.decSize();
	}
	
	/*public boolean updateFormulaRow(int index, String userFormulaInput){
		System.out.println("Box.updateRow("+index+", "+userFormulaInput+")");
		ProofRow row = getRow(index);
		row.setUserInput(userFormulaInput);
		return false;
	}*/
	
	public void openNewBox(){
		ProofEntry lastEntry = entries.get(entries.size()-1);
		if(lastEntry instanceof Box){
			Box box = (Box)lastEntry;
			if(box.isOpen()) {
				box.openNewBox();
				return;
			}
		}
		entries.add(new Box(this,true));
	}
	
	public void closeBox(){
		ProofEntry lastEntry = entries.get(entries.size()-1);
		if(lastEntry instanceof Box && ((Box)lastEntry).isOpen()){
			((Box)lastEntry).closeBox();	
		}
		else{
			if( this.isTopLevelBox() == false) open = false;
		}
	}
	
	//check if referenceRowIndex is in scope of referencingRowIndex
	/**
	 * Check if the referencingRow can refer to the referenceRow
	 * @param referenceRowIndex
	 * @param referencingRowIndex
	 * @return
	 */
	public boolean isInScopeOf(int referenceRowIndex, int referencingRowIndex){
		if( referenceRowIndex >= referencingRowIndex) return false;
		ProofRow referenceRow = getRow(referenceRowIndex);
		ProofRow referencingRow = getRow(referencingRowIndex);
		Box referenceParent = referenceRow.getParent();
		Box referencingRowAncestorBox = referencingRow.getParent();
		
		//check if the reference is verified to be correct
		if( referenceRow.isVerified() == false ) return false;
		
		//For referenceRow to be in scope of the referencingRow, the parent of the referenceRow must be 
		//in the ancestral hierarchy of the referencingRow. A simpler way to put it: The innermost box 
		//containing the referenceRow must also contain the referencingRow
		while(referencingRowAncestorBox != null){
			if( referenceParent == referencingRowAncestorBox){
				return true;
			}
			referencingRowAncestorBox = referencingRowAncestorBox.getParent();
		}
		return false;
	}
	
	/**
	 * Checks if the interval is a box that is within scope of the row at index referencingRow
	 * @param interval: should be an interval of the start and end index of a box
	 * @param referencingRow
	 * @return
	 */
	public boolean isInScopeOf(Interval interval, int referencingRow){
		int start = interval.startIndex;
		int end = interval.endIndex;
		if( end < start || referencingRow <= end) return false;
		//System.out.println("end < start || referencingRow <= end : true");
		
		//check if the rows inthe box are verified
		for(int i = start; i <= end; i++){
			if( getRow(i).isVerified() == false ) return false;
		}
		
		//ProofRow row1 = getRow(start);
		//ProofRow row2 = getRow(end);
		Box theBox = getBox(interval);
		if( theBox == null) return false;
		Box parentOfIntervalBox = theBox.getParent();
		//System.out.println("theBox == null : false");
		
		//check if the box is an ancestor of the referencingRow
		Box referencingRowAncestorBox = getRow(referencingRow).getParent();
		while(referencingRowAncestorBox != null){
			if( parentOfIntervalBox == referencingRowAncestorBox) return true;
			referencingRowAncestorBox = referencingRowAncestorBox.getParent();
		}
		System.out.println("reached the end");
		return false;
	}
	
	/**
	 * If this interval represents a box in this proof, return that box
	 * @param interval: the indexes of the box you want to get/check
	 * @return the box if the indexes given by the interval represents a box in the proof, otherwise null
	 */
	public Box getBox(Interval interval){
		ProofRow startRow = getRow(interval.startIndex);
		ProofRow endRow   = getRow(interval.endIndex);
		Box parent = startRow.getParent();
		
		while(parent != null){
			if( parent.getRow(0) == startRow && parent.getRow( parent.size()-1 ) == endRow ){
				return parent;
			}
			parent = parent.getParent();
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public boolean isEmpty(){ 
		return size == 0; 
	}
	
	public Box(boolean open){
		this.open = open;
	}
	
	public boolean isOpen(){
		return open;
	}
	
	public void setOpen(boolean open){
		this.open = open;
	}
	
	public Box getParent(){
		return parent;
	}
	
	//increment size variable of this box and do the same for parent box
	//this will propagate all the way to the top box
	public void incSize(){
		size++;
		if( parent != null) parent.incSize();
	}
	
	//decrement size variable of this box and do the same for parent box
	//this will propagate all the way to the top box
	//if this box is now empty, remove it from the parent's entry list
	public void decSize(){
		if(--size == 0){
			getParent().entries.remove(this);
		}
		if( parent != null) parent.decSize();
	}
	
	public boolean isTopLevelBox(){
		return parent == null;
	}
	
	public void printBoxes(){
		List<Interval> foundBoxes = new ArrayList<Interval>();
		for(int i = 0; i < size(); i++){
			for( int j = i; j < size(); j++){
				Interval interval = new Interval(i,j);
				Box box = getBox(interval);
				if( box != null) foundBoxes.add(interval);
			}
		}
		//printRows(1,0);
		System.out.println("Found boxes: "+foundBoxes);
	}
	
	public void printRows(int depth, int startNr){
		int currentNr = startNr;
		//System.out.println("size:"+size);
		for(ProofEntry entry : entries){
			if(entry instanceof Box){
				((Box)entry).printRows(depth+1,currentNr);
				currentNr += ((Box)entry).size();
			}
			else{
				System.out.print(currentNr++ +". ");
				if(currentNr < 11) System.out.print(" ");
				for(int i = 0; i < depth; i++) System.out.print("|");
				System.out.println("\t"+entry);
			}
		}
	}
	
	public void printRowScopes(boolean zeroBasedNumbering){
		int offset = zeroBasedNumbering ? 0 : 1;
		for(int i = 0; i < size; i++){
			System.out.print("Rows in scope of line "+i+": ");
			for(int j = 0; j < i; j++){
				if(isInScopeOf(j,i)) System.out.print(""+(j+offset)+", ");
			}
			System.out.println("");
		}
		
	}
	
	public void printIntervalScopes(boolean zeroBasedNumbering){
		int offset = zeroBasedNumbering ? 0 : 1;
		for(int rowI = 0; rowI < size; rowI++){
			System.out.print("Intervals in scope for row "+(rowI+offset)+": ");
			for(int i = 0; i < size; i++){
				for(int j = 0; j < size; j++){
					Interval inter  = new Interval(i,j);
					if( isInScopeOf(inter, rowI) ) System.out.print(""+inter+", ");
				}
			}
			System.out.println("");
		}
	}
}
