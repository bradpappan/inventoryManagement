package controller;

import Inventory.InHouse;
import Inventory.Outsourced;
import Inventory.Part;
import Inventory.mainInventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class controller that runs the modify part screen.
 */
public class modifyPartController implements Initializable {


    @FXML private TextField partId;
    @FXML private TextField partName;
    @FXML private TextField partInv;
    @FXML private TextField partPrice;
    @FXML private TextField partMax;
    @FXML private TextField partMachineId;
    @FXML private TextField partMin;

    @FXML private Label partIdChange;

    @FXML private RadioButton inHouseHandler;
    @FXML private RadioButton outSourcedHandler;

    @FXML private Part selectedPart;


    /**
     * Checks for errors in inv textfield
     * @param value used to parse
     * @return the display error if called
     */
    public int tryParseInventory(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            displayStockAlert();
            return -1;
        }
    }

    /**
     * Checks for errors in min textfield
     * @param value used to parse
     * @return the display error if called
     */
    public int tryParseMin(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            displayMinAlert();
            return -1;
        }
    }

    /**
     * Checks for errors in max textfield
     * @param value used to parse
     * @return the display error if called
     */
    public int tryParseMax(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            displayMinAlert();
            return -1;
        }
    }

    /**
     * Checks for errors in price textfield
     * @param value used to parse
     * @return the display error if called
     */
    public Double tryParsePrice(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            displayPriceAlert();
            return -1.0;
        }
    }

    /**
     * When save button is pressed this saves the part inputted for both outsourced or in house radio button selected. RUNTIME ERROR kept getting NULL POINTER EXCEPTION, added null check
     *
     * @param event saves the part if outsourced or by company name
     * @throws IOException displays errors
     */
    public void saveHandler(ActionEvent event) throws IOException {
        try {
            Part newPart;
            int id = mainInventory.getNewPartId();
            String name = partName.getText();

            if (partName.getText().trim().isEmpty() || partName.getText() ==  null) {
                displayNameAlert("Error", "Name field is blank");
            }

            int stock = tryParseInventory(partInv.getText());
            int min = tryParseMin(partMin.getText());
            int max = tryParseMax(partMax.getText());
            double price = tryParsePrice(partPrice.getText());
            int machineId;
            String companyName;
            boolean partAdd;

            if (outSourcedHandler.isSelected()) {
                companyName = partMachineId.getText();
                newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                partAdd = true;

            } else {
                machineId = Integer.parseInt(partMachineId.getText());
                newPart = new InHouse(id, name, price, stock, min, max, machineId);
                partAdd = true;
            }

            if ( min != -1 && max != -1 && stock != -1 && min <= max && price > 0.0) {
                if (partAdd) {
                    mainInventory.deletePart(selectedPart);
                    mainInventory.addPart(newPart);
                    returnToinventorygui(event);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cancels any changes and returns to main menu. RUNTIME ERROR Did not add IOExecption to method at first
     *
     * @param event exits add product
     * @throws IOException goes back to the main menu
     */
    public void cancelHandler(ActionEvent event) throws IOException {

        returnToinventorygui(event);

    }

    /**
     * When called returns to main menu.
     *
     * @param event is called to return back to main menu
     * @throws IOException throws exception
     */
    private void returnToinventorygui(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/inventorygui.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * When selected changes Company name back to machine id.
     *
     */
    @FXML
    public void inHouseOnAction() {

        outSourcedHandler.setSelected(false);
        partIdChange.setText("MachineID");
        inHouseHandler.setSelected(true);

    }

    /**
     * When selected changes Machine id to Company name.
     *
     */
    @FXML
    public void outSourcedOnAction() {

        outSourcedHandler.setSelected(true);
        inHouseHandler.setSelected(false);
        partIdChange.setText("Company Name");

    }

    /**
     * Displays an alert when called and can input any message in both parameters
     *
     * @param header  custom text alert field
     * @param content custom text alert field
     */
    private void displayNameAlert(String header, String content) {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText(header);
        alertError.setContentText(content);
        alertError.showAndWait();
    }

    /**
     * Displays an alert if price is not valid and ensures its a double
     */
    private void displayPriceAlert() {

        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText("Price is not valid");
        alertError.setContentText("Please ensure price is a double.");
        alertError.showAndWait();
    }

    /**
     * Displays an alert if inv is not valid, less than max, and greater than min
     */
    private void displayStockAlert() {

        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText("Inv is not valid");
        alertError.setContentText("Please ensure stock is an inv, less than max, or greater than min.");
        alertError.showAndWait();
    }

    /**
     * Displays an alert if min and max is empty and ensures they are an int
     */
    private void displayMinAlert() {

        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText("Min or max is not valid");
        alertError.setContentText("Please ensure min and max is an integer and min is greater than 0 or less than max.");
        alertError.showAndWait();
    }

    /**
     * Initializes controller
     *
     * @param location  used to resolve paths for the root object
     * @param resources used to localize root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedPart = mainViewController.getPartToModify();
        if (selectedPart instanceof InHouse) {
            inHouseHandler.setSelected(true);
            partIdChange.setText("Machine ID");
            partMachineId.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced) {
            outSourcedHandler.setSelected(true);
            partIdChange.setText("Company Name");
            partMachineId.setText(((Outsourced) selectedPart).getCompanyName());
        }

        partId.setText(String.valueOf(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partInv.setText(String.valueOf(selectedPart.getStock()));
        partPrice.setText(String.valueOf(selectedPart.getPrice()));
        partMax.setText(String.valueOf(selectedPart.getMax()));
        partMin.setText(String.valueOf(selectedPart.getMin()));
    }
}

