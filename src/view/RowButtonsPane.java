package view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;


public class RowButtonsPane extends FlowPane {
    private Button deleteButton=new Button("X");
    private Button insertBeforeButton=new Button("\u2BC5");
    private Button insertAfterButton=new Button("\u2BC6");

    private final Tooltip deleteTooltip = new Tooltip("Delete");
    private final Tooltip insertBeforeTooltip = new Tooltip("Insert Above");
    private final Tooltip insertAfterTooltip = new Tooltip("Insert Below");

    public RowButtonsPane(){
        this.setMaxWidth(80);
        deleteButton.setTooltip(deleteTooltip);
        insertBeforeButton.setTooltip(insertBeforeTooltip);
        insertAfterButton.setTooltip(insertAfterTooltip);

        this.getChildren().addAll(deleteButton,insertBeforeButton,insertAfterButton);
    }

    public Button getDeleteButton(){
        return deleteButton;
    }

    public Button getInsertBeforeButton(){
        return insertBeforeButton;
    }
    public Button getInsertAfterButton(){
        return insertAfterButton;
    }

}