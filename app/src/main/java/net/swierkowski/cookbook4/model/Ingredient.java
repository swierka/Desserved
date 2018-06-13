package net.swierkowski.cookbook4.model;

public class Ingredient {
    private Product mProduct;
    private String mAmount;


    public Ingredient() {
    }

    public Ingredient(Product product, String amount) {
        mProduct = product;
        mAmount = amount;
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }
}
