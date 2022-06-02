public class Ingredient {
    private String init_barcode;
    private String init_name;
    private Integer init_weight;

    public Ingredient(String init_barcode, String init_name, Integer init_weight) {
        this.init_barcode = init_barcode;
        this.init_name = init_name;
        this.init_weight = init_weight;
    }

    public String getInit_barcode() {
        return init_barcode;
    }

    public Integer getInit_weight() {
        return init_weight;
    }

    public String getInit_name() {
        return init_name;
    }
}
