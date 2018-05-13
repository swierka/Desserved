package net.swierkowski.cookbook4.model;

public class Product {
    private long id;
    private String name;
    private boolean isRestricted;

    public Product(long id, String name, boolean isRestricted) {
        this.id = id;
        this.name = name;
        this.isRestricted = isRestricted;
    }

    public Product() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

