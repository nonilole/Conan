package model.rules;

import model.Box;

public class ForallElim implements Rule{
	private Integer rowRef;

	@Override
	public boolean hasCompleteInfo() {
		return rowRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index != 1) throw new IllegalArgumentException();
		try{
			rowRef = ReferenceParser.parseIntegerReference(newValue);
		}
		catch(NumberFormatException e){
			rowRef = null;
			throw new NumberFormatException();
		}
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		throw new RuntimeException("ForallElim.verify: not implemented");
		//return false;
	}
	
	@Override
	public String toString(){
		return "∀E ("+rowRef+")";
	}
}
