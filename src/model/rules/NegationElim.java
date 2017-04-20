package model.rules;

import model.Box;

public class NegationElim implements Rule{

	private Integer rowRef1;
	private Integer rowRef2;

	public NegationElim() {
		super();
	}
	
	public NegationElim(Integer rowRef1, Integer rowRef2) {
		super();
		this.rowRef1 = rowRef1;
		this.rowRef2 = rowRef2;
	}
	
	@Override
	public boolean hasCompleteInfo() {
		return rowRef1 != null && rowRef2 != null;
	}

	/**
	 * Updates the values for the three rule prompts. 
	 * @param index: the index for the rule prompt box.
	 * @param newValue: the new value for that specific rule prompt box.
	 */
	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 2) throw new IllegalArgumentException();

		if(index == 1){
			try{
				rowRef1 = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef1 = null;
				throw new NumberFormatException();
			}
		}
		else{//index == 2
			try{
				rowRef2 = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef2 = null;
				throw new NumberFormatException();
			}
		}
	}
	
	@Override
	public String toString(){
		String p1 = rowRef1 == null ? "" : rowRef1.toString();
		String p2 = rowRef2 == null ? "" : rowRef2.toString();
		return String.format("¬E (%s),(%s)",p1,p2);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	//lägg till contradiction-symbolen
	//kolla att man har valt två rader som funkar
	//kolla så att de två raderna är negationen av varandra
	//kolla så att själva raden innehåller en contradiction 
}
