package model.rules;

import model.Box;

public class NegationIntro implements Rule{

	private Interval interval;

	public NegationIntro() {
		super();
	}

	public NegationIntro(Interval interval) {
		super();
		this.interval = interval;
	}

	@Override
	public boolean hasCompleteInfo() {
		return interval != null;
	}

	@Override
	public void updateReference(int index, String newValue) {
		if(index < 1 || index > 1) throw new IllegalArgumentException();

		if(index == 1){
			try{
				interval = ReferenceParser.parseIntervalReference(newValue);
			}
			catch(NumberFormatException e){
				interval = null;
				throw new NumberFormatException();
			}
		}
	}
	
	@Override
	public String toString(){
		return String.format("¬I (%s)", interval);
	}

	@Override
	public boolean verify(Box data, int rowIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	//implementera negationElim först
	//kolla så att uttrycket är den negerade versionen av det som stod överst i boxen.
	//kolla så att det är en contradiction längst ner i boxen.

}
