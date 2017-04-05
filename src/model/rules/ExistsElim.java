package model.rules;

import model.Box;

public class ExistsElim implements Rule{
	private Integer rowRef;
	private Interval intervalRef;
	
	@Override
	public boolean hasCompleteInfo(){
		return (intervalRef == null && rowRef == null) == false;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 2) throw new IllegalArgumentException();
		
		if(index == 1){
			try{
				rowRef = ReferenceParser.parseIntegerReference(newValue);
			}
			catch(NumberFormatException e){
				rowRef = null;
				throw new NumberFormatException();
			}
		}
		else{//index == 2
			try{
				intervalRef = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				intervalRef = null;
				throw new NumberFormatException();
			}
		}
		
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		return false;
	}
}
