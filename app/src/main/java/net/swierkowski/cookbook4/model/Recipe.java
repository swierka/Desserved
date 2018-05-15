package net.swierkowski.cookbook4.model;

import java.util.ArrayList;

public class Recipe {
    private long mId;
    private String mName;
    private String description;
    private ArrayList<Ingridient> mIngredients;
    private String photo;
    private boolean mIsVegan;
    private boolean mIsLactoseFree;
    private boolean mIsGlutenFree;
    private int mPrepTime;
    private int mCookingTime;


    public Recipe() {
    }

    public Recipe(long id, String name, String description, ArrayList<Ingridient> ingredients, String photo, boolean isVegan, boolean isLactoseFree, boolean isGlutenFree, int prepTime, int cookingTime) {
        mId = id;
        mName = name;
        this.description = description;
        mIngredients = ingredients;
        this.photo = photo;
        mIsVegan = isVegan;
        mIsLactoseFree = isLactoseFree;
        mIsGlutenFree = isGlutenFree;
        mPrepTime = prepTime;
        mCookingTime = cookingTime;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Ingridient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingridient> ingredients) {
        mIngredients = ingredients;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isVegan() {
        return mIsVegan;
    }

    public void setVegan(boolean vegan) {
        mIsVegan = vegan;
    }

    public boolean isLactoseFree() {
        return mIsLactoseFree;
    }

    public void setLactoseFree(boolean lactoseFree) {
        mIsLactoseFree = lactoseFree;
    }

    public boolean isGlutenFree() {
        return mIsGlutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        mIsGlutenFree = glutenFree;
    }

    public int getPrepTime() {
        return mPrepTime;
    }

    public void setPrepTime(int prepTime) {
        mPrepTime = prepTime;
    }

    public int getCookingTime() {
        return mCookingTime;
    }

    public void setCookingTime(int cookingTime) {
        mCookingTime = cookingTime;
    }
}
