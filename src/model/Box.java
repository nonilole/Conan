package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.ProofListener.RowInfo;
import model.rules.Interval;
import model.rules.Rule;

public class Box implements ProofEntry, Serializable{
	private boolean open;
	private int size; // only alter this value through incSize and decSize methods, they will also update parent boxes
	private Box parent;
	List<ProofEntry> entries = new ArrayList<ProofEntry>();
	
	public Box(Box parentBox, boolean open){
		this.parent = parentBox;
		this.open = open;
	}
	
	public void insertRow(int index, BoxReference br, int depth){
		assert(index < size);
		if (index == -1 ) { // Adding to beginning
			this.entries.add(0, new ProofRow(this));
			this.incSize();
			return;
		}
		ProofRow referenceRow = getRow(index);
		Box parent = referenceRow.getParent();
		if (br.equals(BoxReference.BEFORE)) {
            int internalReferenceIndex = parent.entries.indexOf(referenceRow);
            int insertionIndex = br == BoxReference.BEFORE ? internalReferenceIndex : internalReferenceIndex+1;
            parent.entries.add(insertionIndex, new ProofRow(parent));
            parent.incSize();
            return;
        }
        ProofEntry child = referenceRow;
		for (int i = 0; i < depth; i++) {
		    child = parent;
		    parent = parent.getParent();
        }
        parent.entries.add(parent.entries.indexOf(child)+1, new ProofRow(parent));
        parent.incSize();
	}
	public void deleteRowAfterBox(int index) { // Only allowed as an inverse!
		ProofRow referenceRow = getRow(index);
		Box parentBox = referenceRow.getParent();
		Box metaBox = parentBox.getParent();
		List<ProofEntry> metaBoxList = metaBox.entries;
		int insertionIndex = metaBoxList.indexOf(parentBox) + 1;
		metaBoxList.remove(insertionIndex);
		metaBox.decSize();
	}
	public void insertBox(int index){
		assert(index < size);
		ProofRow referenceRow = getRow(index);
		Box boxToInsertInto = referenceRow.getParent();
		int internalReferenceIndex = boxToInsertInto.entries.indexOf(referenceRow);
		Box closedBox = new Box(boxToInsertInto, false);
		closedBox.entries.add(referenceRow);
        closedBox.incSize();
        boxToInsertInto.decSize();
        referenceRow.setParent(closedBox);
		//boxToInsertInto.entries.remove(internalReferenceIndex);
        boxToInsertInto.entries.remove(referenceRow);
		boxToInsertInto.entries.add(internalReferenceIndex, closedBox);
	}
    public void removeBox(int index){
        assert(index < size);
        ProofRow referenceRow = getRow(index);
        Box boxToDelete = referenceRow.getParent();
        Box metaBox = boxToDelete.getParent();
        List<ProofEntry> peList = metaBox.entries;
        int idx = peList.indexOf(boxToDelete);
        peList.remove(idx);
        referenceRow.setParent(metaBox);
        peList.add(idx,referenceRow);
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
		//assert(steps < size) : "getRow: index="+steps+" size="+size;
		
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
	
	public int deleteRow(int index){
		assert(index < size);
		ProofRow referenceRow = getRow(index);
		Box parent = referenceRow.getParent();
        int idxRefRow = parent.entries.indexOf(referenceRow);
        if (idxRefRow == 0 && idxRefRow+1 < parent.entries.size() && parent.entries.get(idxRefRow+1) instanceof Box) {
            if (parent.isTopLevelBox()) {
				parent.entries.remove(referenceRow);
				parent.decSize();
				return -3;
			}
            return -1;
		}
        int depth = 0;
		if (index != 0) {
		    Box cur = getRow(index-1).getParent();
		    while (!cur.equals(parent)) {
		        depth++;
		        if (cur.isTopLevelBox()) {
		            depth = -2;
                    break;
                }
                cur = cur.getParent();
            }
        }
		parent.entries.remove(referenceRow);
        parent.decSize();
        return depth;
	}
	
	//check if referenceRowIndex is in scope of referencingRowIndex
	/**
	 * Check if the referencingRow can refer to the referenceRow
	 * @param referenceRowIndex
	 * @param referencingRowIndex
	 * @return
	 */
	public boolean isInScopeOf(int referenceRowIndex, int referencingRowIndex) throws VerificationInputException {
		if( referenceRowIndex > referencingRowIndex) throw new VerificationInputException("A row is out of scope.");
		if( referenceRowIndex == referencingRowIndex) throw new VerificationInputException("A row may not reference itself.");
		ProofRow referenceRow = getRow(referenceRowIndex);
		ProofRow referencingRow = getRow(referencingRowIndex);
		Box referenceParent = referenceRow.getParent();
		Box referencingRowAncestorBox = referencingRow.getParent();
		
		//check if the reference is verified to be correct
		if( referenceRow.isVerified() == false ) throw new VerificationInputException("A reference is not verified.");

		//For referenceRow to be in scope of the referencingRow, the parent of the referenceRow must be 
		//in the ancestral hierarchy of the referencingRow. A simpler way to put it: The innermost box 
		//containing the referenceRow must also contain the referencingRow
		while(referencingRowAncestorBox != null){
			if( referenceParent == referencingRowAncestorBox){
				return true;
			}
			referencingRowAncestorBox = referencingRowAncestorBox.getParent();
		}
		throw new VerificationInputException("A row is out of scope.");
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
		if (end < start || referencingRow <= end) throw new VerificationInputException("An interval is out of scope.");
		//System.out.println("end < start || referencingRow <= end : true");
		
		//check if the rows inthe box are verified
		for (int i = start; i <= end; i++) {
			if (getRow(i).isVerified() == false) throw new VerificationInputException("An interval is not verified.");
		}
		
		//ProofRow row1 = getRow(start);
		//ProofRow row2 = getRow(end);
		Box theBox = getBox(interval);
		if( theBox == null) throw new VerificationInputException("An interval needs to specify an entire box.");
		Box parentOfIntervalBox = theBox.getParent();
		//System.out.println("theBox == null : false");
		
		//TODO:
		//Check that the box doesn't end with a box
		if(theBox.entries.get(theBox.entries.size()-1) instanceof Box) throw new VerificationInputException("Box may not end in a box.");

		//check if the box is an ancestor of the referencingRow
		Box referencingRowAncestorBox = getRow(referencingRow).getParent();
		while(referencingRowAncestorBox != null){
			if( parentOfIntervalBox == referencingRowAncestorBox) return true;
			referencingRowAncestorBox = referencingRowAncestorBox.getParent();
		}
		throw new VerificationInputException("A interval is out of scope.");
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
	
	public int indexOf(ProofRow row){
		return this.entries.indexOf(row);
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
	public String rowsToString(int depth) {
		String ret = "";
		for(ProofEntry entry : entries){
			if(entry instanceof Box){
				ret += ((Box)entry).rowsToString(depth+1);
			}
			else{
				ret += Integer.toString(depth);
				ret += "::" + entry +"\n";
			}
		}
		return ret;
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
	
	public void fillList(ArrayList<ProofListener.RowInfo> list){
		boolean startOfBox = ! isTopLevelBox();

		for(ProofEntry entry : this.entries){
			if(entry instanceof Box){
				((Box)entry).fillList(list);
			}
			else{
				ProofRow row = (ProofRow) entry;
				String expression = row.getFormula() == null ? row.getUserInput() : row.getFormula()+"";
				Rule rule = row.getRule();
				String ruleStr;
				String[] refs = {"","",""};
				if(rule == null){
					ruleStr = "";
				}
				else{
					ruleStr = rule.getDisplayName();
					String[] refsx = rule.getReferenceStrings();
					for(int i = 0; i < refsx.length; i++){
						refs[i] = refsx[i];
					}
				}
				boolean endOfBox = entries.indexOf(entry) == entries.size()-1;
				list.add(new RowInfo(expression, ruleStr, refs[0], refs[1], refs[2], 
						startOfBox, endOfBox, row.isWellFormed(), row.isVerified()));
			}
			startOfBox = false;
		}
	}
}
