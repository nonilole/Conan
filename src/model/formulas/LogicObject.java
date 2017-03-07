
package model.formulas;

public class LogicObject implements Term{
    public final String id;

    public LogicObject(String id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof LogicObject){
    		return id.equals(((LogicObject)o).id);
    	}
    	return false;
    }

    public String toString(){
        return id;
    }
}
