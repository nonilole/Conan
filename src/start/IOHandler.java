package start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import model.Proof;
import view.ProofView;

public class IOHandler {

	public static ProofView openProof(TabPane tabPane) throws FileNotFoundException, ClassNotFoundException, IOException {
		System.out.println("opendProof");
		Window window = tabPane.getScene().getWindow();
		FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().addAll(
    	         new ExtensionFilter(".proof", "*.proof"),
    	         new ExtensionFilter("All Files", "*"));
    	
    	File file = fc.showOpenDialog(window);
    	if(file == null){
    		System.out.println("No file chosen.");
    		return null;
    	}
    	System.out.println("File chosen: "+file.getName());
    	Proof proof;
    	FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        proof = (Proof) in.readObject();
        in.close();
        fileIn.close();
        proof.load();
        String premiseStr = proof.getPremisesStr();
        String conclusionStr = proof.getConclusionStr();
        ProofView pv = new ProofView(tabPane, proof);
        pv.setPath(file.getPath());
        pv.setName(file.getName());
        pv.getTab().setText(pv.getName());
        pv.displayLoadedProof(premiseStr, conclusionStr);
        System.out.println("Deserialized proof: "+pv.getName());
        return pv;
	}
	
	public static void saveProof(Proof proof, String path) throws FileNotFoundException, IOException{
		System.out.println("saveProof");
    	FileOutputStream fos = new FileOutputStream(path);
    	ObjectOutputStream oout = new ObjectOutputStream(fos);
    	oout.writeObject(proof);
    	fos.close();
    	oout.close();
	}
}
