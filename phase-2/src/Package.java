public class Package {
    private Ingredient ingredient;
    private Integer price;
    private Integer quantity;

    public Package(Ingredient ingredient, Integer price, Integer quantity) {
        this.ingredient = ingredient;
        this.price = price;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void decrementQuantity(Integer quantity) {
        this.quantity -= 1;
    }
}
