package model.rules;

import model.Box;

public class DisjunctionElim implements Rule{

	private Integer rowRef;
	private Interval interval1;
	private Interval interval2;

	public DisjunctionElim() {
		super();
	}

	public DisjunctionElim(Integer rowRef, Interval interval1, Interval interval2) {
		super();
		this.rowRef = rowRef;
		this.interval1 = interval1;
		this.interval2 = interval2;
	}

	@Override
	public boolean hasCompleteInfo() {
		return rowRef != null && interval1 != null && interval2 != null;
	}

	/**
	 * Updates the values for the three rule prompts. 
	 * @param index: the index for the rule prompt box.
	 * @param newValue: the new value for that specific rule prompt box. 
	 */
	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 3) throw new IllegalArgumentException();

		if(index == 1){
			try{
				rowRef = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef = null;
				throw new NumberFormatException();
			}
		} else if (index == 2){
			try{
				interval1 = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval1 = null;
				throw new NumberFormatException();
			}
		} else {
			try{
				interval2 = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval2 = null;
				throw new NumberFormatException();
			}
		}
	}
	
	@Override
	public String toString(){
		//behövs något liknande här? 
		//String p1 = premise1 == null ? "" : premise1.toString();
		
		return String.format("∨E (%s) (%s) (%s)", rowRef, interval1, interval2);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		//
		if(data.isInScopeOf(rowRef, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval1, rowIndex) == false ) return false;
		if(data.isInScopeOf(interval2, rowIndex) == false ) return false;
		return true;
	}

	//kolla att en rad och två intervall anges. 
	//kolla så att alla rader/boxar som används är verified. 
	//kolla så att båda boxar avslutas med det som ska bevisas.
	//kolla så att den ena boxen börjar med ena sidan av ursprungliga OR-uttrycket.
	//kolla att den andra boxen börjar med andra sidan av OR-uttrycket. 

	//fundering: hur har vi koll på boxarna just nu? 

}
