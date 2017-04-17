package model.rules;

import model.Box;

public class DisjunctionElim implements Rule{

	private Integer rowRef;
	private Interval interval1;
	private Interval interval2;
	
	@Override
	public boolean hasCompleteInfo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateReference(int index, String newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		if(data.isInScopeOf(rowRef, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval1, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval2, rowIndex) == false ) return false;
		return false;
	}
	
	//kolla att en rad och två intervall anges. 
	//kolla så att alla rader/boxar som används är verified. 
	//kolla så att båda boxar avslutas med det som ska bevisas.
	//kolla så att den ena boxen börjar med ena sidan av ursprungliga OR-uttrycket.
	//kolla att den andra boxen börjar med andra sidan av OR-uttrycket. 
	
	//fundering: hur har vi koll på boxarna just nu? 

}
