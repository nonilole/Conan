package model;

import java.util.ArrayList;
import java.util.List;

import model.formulas.Formula;

public class ProofData {
	
	List<ProofRow> rows = new ArrayList<ProofRow>();
	Box topLevelBox = new Box();
	
	public void insertRow(int index, BoxReference br){
		System.out.println("ProofData.insertRow not implemented!");
	}
	public void addRow(){
		System.out.println("ProofData.addRow not implemented!");
	}
	public ProofRow getRow(int index){ 
		System.out.println("ProofData.getRow not implemented!");
		//TODO: verify paramater
		return rows.get(index);
	}
	public void updateRow(int index, Formula formula){
		System.out.println("ProofData.updateRow not implemented!");
	}
	//public void updateRow(int index, rule...){}
	//public void updateRow(int index, formula&rule..?){}
	public void deleteRow(int index){
		System.out.println("ProofData.deleteRow not implemented!");
	}
	
	public boolean isInScope( int indexReferenceRow, int indexReferencingRow){
		System.out.println("ProofData.isInScope not implemented!");
		return true;
	}
	
	public void openBox(){
		System.out.println("ProofData.openBox not implemented!");
	}
	public void openBox(int index){
		System.out.println("ProofData.openBox(index) not implemented!");
	}
	
	public int size(){
		return rows.size();
	}
	
	//for debugging
	public void printProof(){
		for(int i = 0; i < rows.size(); i++){
			//TODO: print usefule stuff
		}
	}
}
