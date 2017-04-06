package model.rules;

import model.Box;

public class ForallIntro implements Rule{
	private Interval intervalRef;

	@Override
	public boolean hasCompleteInfo() {
		return intervalRef != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index != 1) throw new IllegalArgumentException();
		try{
			intervalRef = ReferenceParser.parseIntervalReference(newValue);
		}
		catch(NumberFormatException e){
			intervalRef = null;
			throw new NumberFormatException();
		}
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		throw new RuntimeException("ForallIntro.verify: not implemented");
		//return false;
	}
	
	@Override
	public String toString(){
		return "∀I ("+intervalRef+")";
	}

}
