package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {
	
	    private Model model;

	    @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private TextArea txtOut;

	    @FXML
	    private ChoiceBox<String> box;

	    @FXML
	    private TextField txtYears;

	    @FXML
	    private TextField txtHours;

	    @FXML
	    private Button btnAnalysis;

	    @FXML
	    void doAnalysis(ActionEvent event) {
	    	
	    	txtOut.clear();
	    	
	    	Nerc nerc = null;
	    	int maxYears;
	    	int maxHours;
	    	
	    	try {
	    	    maxYears = Integer.parseInt(txtYears.getText());
	    		maxHours = Integer.parseInt(txtHours.getText());
	    	} catch (NumberFormatException nfe) {
	    		txtOut.setText("Max years and Max hours must be positive integer!");
	    		return;
	    	}
	    	
	    	if (box.getValue() == null) {
				txtOut.setText("A NERC must be selected!");
				return;
			}
	    		
	    	for ( Nerc n : model.getNercList() )
	    		if ( n.getValue().compareTo(box.getValue()) == 0 ) {
	    			nerc = n;
	    		}
	    	
	    	txtOut.setText(
					String.format("Computing the worst case analysis... for %d hours and %d years", maxHours, maxYears));
			List<PowerOutage> worstCase = model.doWorstCaseAnalysis(maxYears,maxHours,nerc);

			txtOut.clear();
			
			txtOut.appendText("Tot people affected: " + model.sumAffectedCustomers(worstCase) + "\n");
			txtOut.appendText("Tot hours of outage: " + model.sumHours(worstCase) + "\n");

			for (PowerOutage po : worstCase) {
				txtOut.appendText(String.format("%s %s %d %d", po.getDateEventBegan(),
						po.getDateEventFinished(), po.getDuration(), po.getCustomersAffected()));
				txtOut.appendText("\n");
			}
			
	    }

	    @FXML
	    void initialize() {
	        assert txtOut != null : "fx:id=\"textOut\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert box != null : "fx:id=\"box\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'PowerOutages.fxml'.";
	        assert btnAnalysis != null : "fx:id=\"btnAnalysis\" was not injected: check your FXML file 'PowerOutages.fxml'.";

	    }

		public void setModel(Model model) {
			this.model = model;
			box.getItems().addAll(model.getNercNameList());
		}
}
