package model;

import java.util.ArrayList;
import java.util.List;

import model.rules.Intervall;

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
	
	//TODO: messy code, can be shortened a lot
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
			}
			else{
				entries.add(new ProofRow(this));
				incSize();
			}
		}
		else{
			entries.add(new ProofRow(this));
			incSize();
		}
	}
	
	
	public ProofRow getRow(int steps){
		//System.out.println("Box.getRow("+steps+")");
		assert(steps < size) : "index="+steps+" size="+size;
		
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
	
	public boolean updateFormulaRow(int index, String userFormulaInput){
		System.out.println("Box.updateRow("+index+", "+userFormulaInput+")");
		ProofRow row = getRow(index);
		row.setUserInput(userFormulaInput);
		return false;
	}
	
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
	
	//paramaters should be indexes, not row numbers
	public boolean isInScopeOf(int referenceRow, int referencingRow){
		return false;
	}
	
	//paramaters should be indexes, not row numbers
		public boolean isInScopeOf(Intervall intervall, int referencingRow){
			return false;
		}
	
	public int size(){
		return size;
	}
	
	public boolean isEmpty(){ 
		return false; 
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
	//if this box is now empty, remove it from the parent entry list
	public void decSize(){
		if(--size == 0){
			getParent().entries.remove(this);
		}
		if( parent != null) parent.decSize();
	}
	
	public boolean isTopLevelBox(){
		return parent == null;
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
}
