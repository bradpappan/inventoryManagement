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
 * Class controller that runs the modify product screen.
 */
public class modifyProductController implements Initializable {

    Product selectedProduct;

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

    private ObservableList<Part> associateParts = FXCollections.observableArrayList();

    @FXML private TableView<Part> assocPartTableView;
    @FXML private TableColumn<Part, Integer> assocPartId;
    @FXML private TableColumn<Part, String> assocPartName;
    @FXML private TableColumn<Part, Integer> assocPartInv;
    @FXML private TableColumn<Part, Double> assocPartPrice;

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
     * When save button is pressed this saves the product inputted.
     *
     * @param event saves product
     */
    public void saveHandler(ActionEvent event) {

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

                for (Part part : associateParts) {
                    newProduct.addAssociatedPart(part);
                }

                newProduct.setId(mainInventory.getNewProductId());
                mainInventory.addProduct(newProduct);
                mainInventory.deleteProduct(selectedProduct);
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
    public void removePartHandler() {
        Part selectedPart = assocPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayCustomAlert("No part selected.", "Please select a part.");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setContentText("Are you sure you want to remove this associated part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associateParts.remove(selectedPart);
                assocPartTableView.setItems(associateParts);
            }
        }
    }

    /**
     * When pressed adds the part to the associated parts tableview. RUNTIME ERROR did not include else statement after if, did not function correctly. Added else statement and assoc parts piece
     *
     */
    public void addHandler() {
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayCustomAlert("Attention", "Please select a part.");
        } else {
            associateParts.add(selectedPart);
            assocPartTableView.setItems(associateParts);
        }
    }

    /**
     * Searches by part id or name.
     *
     */
    public void searchByPart() {

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
        if (partsFound.isEmpty()) {
            displayCustomAlert("Invalid Values or blank fields", "Check for blank or invalid values");
        }
    }

    /**
     * Searches by part id or name.
     *
     */
    public void searchPartPressed() {
        if (searchByPart.getText().isEmpty()) {
            partTableView.setItems(mainInventory.getAllParts());
        }
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


    /**
     * Initializes controller
     *
     * @param location  used to resolve paths for the root object
     * @param resources used to localize root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedProduct = mainViewController.getProductToModify();
        associateParts = selectedProduct.getAllAssociatedParts();
        assocPartTableView.setItems(associateParts);
        partTableView.setItems(mainInventory.getAllParts());

        productId.setText(String.valueOf(selectedProduct.getId()));
        productName.setText(selectedProduct.getName());
        productInv.setText(String.valueOf(selectedProduct.getStock()));
        productPrice.setText(String.valueOf(selectedProduct.getPrice()));
        productMax.setText(String.valueOf(selectedProduct.getMax()));
        productMin.setText(String.valueOf(selectedProduct.getMin()));

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
