public class Ingredient {
    private String barcode;
    private String name;
    private Integer weight;

    public Ingredient(String barcode, String name, Integer weight) {
        this.barcode = barcode;
        this.name = name;
        this.weight = weight;
    }

    public String getBarcode() {
        return barcode;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }
}
