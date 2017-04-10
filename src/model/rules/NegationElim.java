package model.rules;

import model.Box;

public class NegationElim implements Rule{

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

	//lägg till contradiction-symbolen
	//kolla att man har valt två rader som funkar
	//kolla så att de två raderna är negationen av varandra
	//kolla så att själva raden innehåller en contradiction 
}
