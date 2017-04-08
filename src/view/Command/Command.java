package view.Command;

public interface Command {
    public boolean execute();
    public void undo();
}
