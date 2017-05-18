
package model.formulas;

public class LogicObject implements Term{
    public final String id;

    public LogicObject(String id){
        this.id = id;
    }
    
    @Override
    public Term replace(String newId,String oldId){
    	return id.equals(oldId) ? new LogicObject(newId) : new LogicObject(id);
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof LogicObject){
    		return id.equals(((LogicObject)o).id);
    	}
    	return false;
    }

    @Override
    public String toString(){
        return id;
    }

    @Override
	public boolean containsObjectId(String id) {
		return this.id.equals(id);
	}
}
