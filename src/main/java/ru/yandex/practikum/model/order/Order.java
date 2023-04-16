package ru.yandex.practikum.model.order;

public class Order {
    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public static Order getOrder() {
        return new Order(
                new String[]
                        {"61c0c5a71d1f82001bdaaa6d"}
        );
    }
}
