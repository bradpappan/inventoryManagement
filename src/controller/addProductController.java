package controller;

import Inventory.Part;
import Inventory.Product;
import Inventory.mainInventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class controller that runs the add product screen.
 */
public class addProductController implements Initializable {

    @FXML private TextField searchByPart;

    @FXML private TextField productId;
    @FXML private TextField productName;
    @FXML private TextField productInv;
    @FXML private TextField productPrice;
    @FXML private TextField productMax;
    @FXML private TextField productMin;

    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partInv;
    @FXML private TableColumn<Part, Double> partPrice;

    @FXML private TableView<Part> assocPartTableView;
    @FXML private TableColumn<Part, Integer> assocPartId;
    @FXML private TableColumn<Part, String> assocPartName;
    @FXML private TableColumn<Part, Integer> assocPartInv;
    @FXML private TableColumn<Part, Double> assocPartPrice;

    private final ObservableList<Part> assocParts = FXCollections.observableArrayList();

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
     * When save button is pressed this saves the product inputted. RUNTIME ERROR CAN'T GET PRODUCT TO ASSOCIATE TO PART, CLICKING SAVE DISPLAYS THE ALERT WITHIN METHOD NO MATTER WHAT. Added if statements and included throw execptions to error when if is entered.
     *
     * @param event Saves the product, attaches associated parts and displays the product on the main menu
     * @throws IOException displays errors
     */
    @FXML
    public void saveHandler(ActionEvent event) throws IOException {

        try {
            int id = mainInventory.getNewProductId();
            String name = productName.getText();

            if (productName.getText().trim().isEmpty() || productName.getText() ==  null) {
                displayCustomAlert("Error", "Name field is blank");
            }

            int stock = tryParseInventory(productInv.getText());
            int min = tryParseMin(productMin.getText());
            int max = tryParseMax(productMax.getText());
            double price = tryParsePrice(productPrice.getText());

            if (min != -1 && max != -1 && stock != -1 && min <= max && price > 0.0) {
                Product newProduct = new Product(id, name, price, stock, min, max);

                for (Part part : assocParts) {
                    newProduct.addAssociatedPart(part);
                }

                newProduct.setId(mainInventory.getNewProductId());
                mainInventory.addProduct(newProduct);
                returnToinventorygui(event);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cancels any changes and returns to main menu.
     *
     * @param event exits add product
     * @throws IOException goes back to the main menu
     */
    @FXML
    public void cancelHandler(ActionEvent event) throws IOException {

        returnToinventorygui(event);

    }

    /**
     * When called returns to main menu.
     *
     * @param event is called to return back to main menu
     * @throws IOException exception
     */
    private void returnToinventorygui(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/inventorygui.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * When pressed removes the part from the associated part table.
     *
     */
    @FXML
    public void removeAssocPartHandler() {
        Part selectedPart = assocPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayCustomAlert("No part selected", "Please select a part");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setContentText("Do you want to remove the associated part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTableView.setItems(assocParts);
            }
        }
    }

    /**
     * When save button is pressed this saves the part inputted for both outsourced or in house radio button selected. RUNTIME ERROR did not include else statement after if, did not function correctly. Added else statement and assoc parts piece
     *
     */
    @FXML
    public void addHandler() {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayCustomAlert("No selected part", "Please select a part");
        } else {
            assocParts.add(selectedPart);
            assocPartTableView.setItems(assocParts);
        }
    }

    /**
     * Searches by part id or name.
     *
     */
    @FXML
    public void searchOnAction() {

        ObservableList<Part> allParts = mainInventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        String searchString = searchByPart.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }
        partTableView.setItems(partsFound);

        if (partsFound.size() == 0) {
            displayCustomAlert("Add a value", "Please check values");
        }
    }

    /**
     * Searches by part id or name.
     *
     */
    @FXML
    public void searchPartKeyPressed() {

        if (searchByPart.getText().isEmpty()) {
            partTableView.setItems(mainInventory.getAllParts());
        }
    }

    /**
     * Initializes controller
     *
     * @param location  used to resolve paths for the root object
     * @param resources used to localize root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partTableView.setItems(mainInventory.getAllParts());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partTableView.setItems(mainInventory.getAllParts());

        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Displays an alert when called and can input any message in both parameters
     *
     * @param header  custom text alert field
     * @param content custom text alert field
     */
    private void displayCustomAlert(String header, String content) {
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
        alertError.setContentText("Please ensure inv is an integer, less than max, or greater than min.");
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
}
