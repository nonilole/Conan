package model.rules;

public class Intervall {
	public int startIndex;//inclusive
	public int endIndex;//inclusive
	
	public Intervall(int start, int end){ 
		startIndex = start;
		endIndex = end;
	}
	
	public int size(){
		return endIndex-startIndex+1;
	}
	
	@Override
	public String toString(){
		return startIndex+"-"+endIndex;
	}
}
