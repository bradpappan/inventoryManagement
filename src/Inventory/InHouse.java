package Inventory;

/**
 * Class that models in house part
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return gets machine id
     */
    public int getMachineId() {

        return this.machineId;
    }

    /**
     * @param machineId sets machine id
     */
    public void setMachineId(int machineId) {

        this.machineId = machineId;
    }
}
