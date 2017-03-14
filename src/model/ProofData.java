package model;

import java.util.ArrayList;
import java.util.List;

import model.formulas.Formula;

public class ProofData {
	
	List<ProofRow> rows = new ArrayList<ProofRow>();
	Box topLevelBox = new Box(true);
	
	public void insertRow(int index, BoxReference br){
		ProofRow referenceRow = rows.get(index);
		List<Box> boxes = new ArrayList<Box>( referenceRow.getBoxes() );
		int insertionIndex = br == BoxReference.BEFORE ? index : index+1;
		rows.add(insertionIndex, new ProofRow(boxes));
	}
	
	//add a new row at the end of the proof
	public void addRow(){
		ProofRow lastRow = getLastRow();
		List<Box> openBoxes;
		if(lastRow != null){
			openBoxes = lastRow.getOpenBoxes();
		}
		else{
			openBoxes = new ArrayList<Box>();
			openBoxes.add(topLevelBox);
		}
		ProofRow newRow = new ProofRow(openBoxes);
		rows.add(newRow);
	}
	
	public ProofRow getRow(int index){
		return rows.get(index);
	}
	
	//returns true if the string can be parsed, false otherwise
	public boolean updateRow(int index, String userInput){
		Parser parser = new Parser();
		ProofRow row = rows.get(index);
		row.setUserInput(userInput);
		try{
			Formula formula = parser.parse(userInput);
			row.setFormula(formula);
			row.setWellformed(true);
			return true;
		}catch(ParseException e){
			row.setFormula(null);
			row.setWellformed(false);
			return false;
		}
	}
	
	//public void updateRow(int index, rule...){}
	//public void updateRow(int index, formula&rule..?){}
	
	public void deleteRow(int index){
		rows.remove(index);
	}
	
	public boolean isInScopeOf( int indexReferenceRow, int indexReferencingRow){
		System.out.println("ProofData.isInScope not implemented!");
		return true;
	}
	
	//creates a new innermost box in the row given by index
	//if this is the last row and it's not within an already closed box,
	//this new box will be open.
	public void openBox(int index){
		ProofRow row = rows.get(index);
		boolean openBox = index == rows.size()-1;
		for(Box box : row.getBoxes()){ //check that all boxes contaioning row are open
			if(box.isOpen() == false){
				openBox = false;
				break;
			}
		}
		row.getBoxes().add(new Box(openBox));
	}
	
	//closes the innermost open box of the last line
	public void closeBox(){
		ProofRow row = getLastRow();
		if(row != null){
			Box box = row.getInnermostOpenBox();
			if(box != topLevelBox){
				box.setOpen(false);
			}
		}
	}
	
	public int size(){
		return rows.size();
	}
	
	private ProofRow getLastRow(){
		return rows.isEmpty() ? null : rows.get(rows.size()-1);
	} 
	
	//for debugging
	//TODO: print the rule...
	public void printProof(){
		for(int i = 0; i < rows.size(); i++){
			ProofRow row = rows.get(i);
			StringBuilder strB = new StringBuilder();
			strB.append((i+1)+".\t"); //put a tab char here?
			for(Box b : row.getBoxes()){
				strB.append('|');
			}
			strB.append(" "+row.getUserInput());
			System.out.println(strB.toString());
		}
	}
}
