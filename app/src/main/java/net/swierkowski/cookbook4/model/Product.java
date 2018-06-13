package net.swierkowski.cookbook4.model;

public class Product {
    private String name;
    private boolean isRestricted;

    public Product(String name) {
        this.name = name;
        this.isRestricted = false;
    }

    public Product() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

    public void setRestricted(boolean restricted) {
        isRestricted = restricted;
    }
}

