package Inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class the models the product
 */
public class Product {
    private int id;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return gets id
     */
    public int getId() {

        return this.id;
    }

    /**
     * @param id sets id
     */
    public void setId(int id) {

        this.id = id;
    }

    /**
     * @return gets name
     */
    public String getName() {

        return this.name;
    }

    /**
     * @param name sets name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return gets stock
     */
    public double getPrice() {

        return this.price;
    }

    /**
     * @param price sets price
     */
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * @return gets stock
     */
    public int getStock() {

        return this.stock;
    }

    /**
     * @param stock sets stock
     */
    public void setStock(int stock) {

        this.stock = stock;
    }

    /**
     * @return gets min
     */
    public int getMin() {

        return this.min;
    }

    /**
     * @param min sets min
     */
    public void setMin(int min) {

        this.min = min;
    }

    /**
     * @return gets max
     */
    public int getMax() {

        return this.max;
    }

    /**
     * @param max sets max
     */
    public void setMax(int max) {

        this.max = max;
    }

    /**
     * Adds associated parts.
     * @param part picks the part to add
     */
    public void addAssociatedPart(Part part) {

        this.associatedParts.add(part);
    }

    /**
     * Deletes associated part.
     * @param selectedAssociatedPart selects associated part
     * @return whether the part is removed
     */
    public boolean deleteAssociatePart(Part selectedAssociatedPart) {
        if (this.associatedParts.contains(selectedAssociatedPart)) {
            this.associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        return false;
    }

    /**
     * Gets all associated parts.
     * @return returns all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {

        return this.associatedParts;
    }
}
