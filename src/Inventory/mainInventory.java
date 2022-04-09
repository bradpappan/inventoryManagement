package Inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that models the main menu screen
 */
public class mainInventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart adds part
     */
    public static void addPart(Part newPart) {

        allParts.add(newPart);
    }

    /**
     * @param newProduct adds product
     */
    public static void addProduct(Product newProduct) {

        allProducts.add(newProduct);
    }


    /**
     * RUNTIME ERROR wanted a final or atomic variable, had to change to enhanced for loop to clear
     *
     * @param partId looks up parts
     * @return part found
     */
    public static Part lookupPart(int partId) {
        Part partFound = null;

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * // RUNTIME ERROR wanted a final or atomic variable, had to change to enhanced for loop to clear
     *
     * @param productId looks up products
     * @return product found
     */
    public static Product lookupProduct(int productId) {

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * @param partName Observable list for lookup part
     * @return parts found
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * @param productName Observable list for lookup product
     * @return products found
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (productName.compareTo(product.getName()) == 0) {
                productsFound.add(product);
            }
        }
        return productsFound;
    }

    /**
     * @param index        sets index for update part
     * @param selectedPart selected part for index
     */
    public static void updatePart(int index, Part selectedPart) {

        allParts.set(index, selectedPart);

    }

    /**
     * @param index           sets index for update product
     * @param selectedProduct selected product for index
     */
    public static void updateProduct(int index, Product selectedProduct) {

        allProducts.set(index, selectedProduct);

    }

    /**
     * @param selectedPart selected part for deletion
     * @return deletes part
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        return false;
    }

    /**
     * @param selectedProduct selected product for deletion
     * @return deletes product
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        return false;
    }

    /**
     * @return gets all parts in its observable list
     */
    public static ObservableList<Part> getAllParts() {

        return allParts;
    }

    /**
     * @return gets all products in observable list
     */
    public static ObservableList<Product> getAllProducts() {

        return allProducts;
    }

    /**
     * @return gives a new product id
     */
    public static int getNewProductId() {
        int available = 1;
        if (allProducts.size() > 0) {

            for (Product p : allProducts) {

                if (available > p.getId()) {
                    return available;
                }
                if (p.getId() - 1 != available) {
                    available = p.getId();
                }
                available++;
            }
        }
        return available;
    }

    /**
     * @return gives a new product id
     */
    public static int getNewPartId() {
        int available = 1;
        if (allParts.size() > 0) {

            for (Part p : allParts) {

                if (available > p.getId()) {
                    return available;
                }
                if (p.getId() - 1 != available) {
                    available = p.getId();
                }
                available++;
            }
        }
        return available;
    }
}

