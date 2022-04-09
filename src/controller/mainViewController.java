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
 * Class controller that runs the main menu screen.
 */
public class mainViewController implements Initializable {

    public static Part selectedPartToModify;
    public static Product selectedProductToModify;

    @FXML public TableView<Part> partTable;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partInventory;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, Double> partPrice;

    @FXML public TableView<Product> productTable;
    @FXML private TableColumn<Part, Integer> productId;
    @FXML private TableColumn<Part, String> productName;
    @FXML private TableColumn<Part, Integer> productInventory;
    @FXML private TableColumn<Part, Double> productPrice;

    @FXML private TextField searchPart;
    @FXML private TextField searchProduct;


    /**
     * selects the part to modify
     *
     * @return part to modify
     */
    public static Part getPartToModify() {

        return selectedPartToModify;
    }

    /**
     * selects the product to modify
     *
     * @return product to modify
     */
    public static Product getProductToModify() {

        return selectedProductToModify;
    }

    /**
     * loads the part add page
     *
     * @param event changes to the add part screen
     */
    @FXML
    public void partAddOnAction(ActionEvent event) {

        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addPart.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Switches to modify part screen
     *
     * @param event changes to modify product screen
     * @throws IOException displays error if needed
     */
    @FXML
    public void partModify(ActionEvent event) throws IOException {
        selectedPartToModify = partTable.getSelectionModel().getSelectedItem();

        if (selectedPartToModify == null) {
            displayCustomAlert("Part not selected", "Please select a part");
        } else {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyPartinHouse.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Will display confirmation message when deleting
     *
     */
    @FXML
    public void partDelete() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayCustomAlert("Part not selected", "Please select a part");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                mainInventory.deletePart(selectedPart);
            }
        }
    }

    /**
     * Exits the program when this button is pressed
     *
     */
    @FXML
    public void exitHandler() {

        System.exit(0);
    }

    /**
     * loads the add product page
     *
     * @param event adds product
     */
    @FXML
    public void productAdd(ActionEvent event) {
        Parent parent;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/addProduct.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            displayCustomAlert("Formatting Error", e.getMessage());
        }

    }

    /**
     * Loads the product modify page. RUNTIME ERROR NULL POINTER EXCEPTION, added null check to method called.
     *
     * @param event modifies product
     * @throws IOException displays error message if needed
     */
    @FXML
    public void productModify(ActionEvent event) throws IOException {
        selectedProductToModify = productTable.getSelectionModel().getSelectedItem();

        if (selectedProductToModify == null) {
            displayCustomAlert("Product not selected", "Please select a product");
            return;
        }

        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/modifyProduct.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Deletes a selected product. RUNTIME ERROR, NEEDED NULL CHECK LINE 211, had to change if statements parameter
     *
     */
    @FXML
    public void productDelete() {

        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayCustomAlert("Product not selected", "Please select a product");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                ObservableList<Part> associatedParts = selectedProduct.getAllAssociatedParts();


                if (associatedParts.size() >= 1) {
                    displayAssocPartAlert();

                } else {
                    mainInventory.deleteProduct(selectedProduct);
                }


            }
        }
    }

    /**
     * Can search for single letters and part ids.
     *
     */
    @FXML
    public void searchProduct() {
        ObservableList<Product> allProducts = mainInventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        String searchString = searchProduct.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }
        productTable.setItems(productsFound);

        if (productsFound.size() == 0) {
            displayCustomAlert("Error", "Product not found");
        }

    }

    /**
     * When button pressed it searches products.
     *
     */
    @FXML
    public void searchProductPressed() {
        if (searchProduct.getText().isEmpty()) {
            productTable.setItems(mainInventory.getAllProducts());
        }
    }

    /**
     * Can search for single letters and part ids.
     *
     */
    @FXML
    public void searchPart() {
        ObservableList<Part> allParts = mainInventory.getAllParts();
        ObservableList<Part> foundPart = FXCollections.observableArrayList();
        String searchString = searchPart.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                foundPart.add(part);
            }
        }
        partTable.setItems(foundPart);

        if (foundPart.size() == 0) {
            displayCustomAlert("Part not found", "Please try again");
        }
    }

    /**
     * When button pressed it searches parts.
     *
     */
    @FXML
    public void searchPartPressed() {
        if (searchPart.getText().isEmpty()) {
            partTable.setItems(mainInventory.getAllParts());
        }
    }

    /**
     * Displays an alert when called and can input any message in both parameters.
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
     * Displays an error when trying to delete a product with associated part.
     */
    private void displayAssocPartAlert() {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText("Parts are Associated");
        alertError.setContentText("All associated parts must be removed before being deleted.");
        alertError.showAndWait();
    }

    /**
     * Initializes controller.
     *
     * @param location  used to resolve paths for the root object
     * @param resources used to localize root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partTable.setItems(mainInventory.getAllParts());
        productTable.setItems(mainInventory.getAllProducts());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
