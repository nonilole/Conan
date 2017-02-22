package View;
import java.util.Stack;

import Model.ProofListener;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ProofView implements ProofListener{
	
	
    public void boxOpened(){}
    public void boxClosed(){}
    public void rowUpdated(){}
    public void conclusionReached(){}
    public void rowDeleted(){}
    public void rowInserted(){}
    
    public static void newProof(TabPane tabPane ) {
    	Tab tab = new Tab("hej");
    	
    	TableColumn tableColumnLeft = new TableColumn();
    	tableColumnLeft.setText("Expression");
    	tableColumnLeft.setMaxWidth(5000);
    	tableColumnLeft.setMinWidth(10);
    	tableColumnLeft.setEditable(true);
    	tableColumnLeft.setPrefWidth(356);
    	
    	
    	TableColumn tableColumnRight = new TableColumn();
    	tableColumnRight.setText("Type");
    	tableColumnRight.setMaxWidth(5000);
    	tableColumnRight.setMinWidth(10);
    	tableColumnRight.setEditable(true);
    	tableColumnRight.setPrefWidth(356);
    	
    	
    	TableView tableView = new TableView();
    	tableView.setPadding(new Insets(0, 0, 0, 0));
    	tableView.getColumns().addAll(tableColumnLeft, tableColumnRight);
    	
    	AnchorPane anchorPane = new AnchorPane(tableView);
    	anchorPane.setPrefWidth(714);
    	anchorPane.setPrefHeight(460);
    	anchorPane.setPadding(new Insets(0, 0, 0, 0));
    	anchorPane.setScaleShape(true);
    	anchorPane.setCacheShape(true);
    	anchorPane.setCenterShape(true);
    	
    	tab.setContent(anchorPane);
    	tabPane.setPadding(new Insets(0, 0, 0, 0));
        tabPane.getTabs().add(tab);
    }
}
