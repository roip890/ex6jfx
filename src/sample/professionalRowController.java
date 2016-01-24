package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by roi on 14/01/16.
 */
public class professionalRowController {
    String professionalInfo;
    public professionalRowController(String professionalInfo) {
        ArrayList<String> professionalInfoAsList = new ArrayList<String>(Arrays.asList(professionalInfo.split(" ")));
        StringBuilder strBldr = new StringBuilder("");
        boolean isFirst = true;
        for (int i = 1; i < professionalInfoAsList.size(); ++i) {
            if (isFirst) {
                strBldr.append(professionalInfoAsList.get(i));
                isFirst = false;
            } else {
                strBldr.append(" " + professionalInfoAsList.get(i));
            }
        }
        this.professionalInfo = strBldr.toString();
    }

    @FXML
    Label lblProfessionalInfo;

    @FXML
    public void initialize() {
        lblProfessionalInfo.setText(this.professionalInfo);
    }
}
