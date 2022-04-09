package Inventory;

/**
 * Class that models the outsourced part
 */
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return get the company name
     */
    public String getCompanyName() {

        return this.companyName;
    }

    /**
     * @param companyName sets company name
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }
}
