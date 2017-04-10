package model.rules;

import model.Box;

public class NegationIntro implements Rule{

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
		return false;
	}
	
	//implementera negationElim först
	//lägg till contradiction-symbolen
	//kolla så att alla rader i boxen är verified
	//kolla så att uttrycket är den negerade versionen av det som stod överst i boxen.
	//kolla så att det är en contradiction längst ner i boxen.

}
