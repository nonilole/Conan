
package model.formulas;

public class LogicObject implements Term{
    public final String id;

    public LogicObject(String id){
        this.id = id;
    }

    //TODO: equals()

    public String toString(){
        return id;
    }
}
