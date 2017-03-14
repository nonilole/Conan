package view;
import javafx.scene.control.Tab;

public class ViewTab extends Tab {
    private View view;
    public ViewTab(String s, View view) {
        super(s);
        this.view = view;
    }
    public View getView() {
        return this.view;
    }
}
