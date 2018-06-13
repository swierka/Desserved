package net.swierkowski.cookbook4.model;


import java.util.ArrayList;

public class Recipe {
    private long mId;
    private String mName;
    private String mDescription;
    private String mShortDescription;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Tag> mTags;
    private String mPhoto;
    private boolean mIsVegan;
    private boolean mIsLactoseFree;
    private boolean mIsGlutenFree;
    private int mPrepTime;
    private int mCookingTime;


    public Recipe() {
    }

    public Recipe(long id, String name, String description, String shortDescription, ArrayList<Ingredient> ingredients, ArrayList<Tag> tags, String photo, boolean isVegan, boolean isLactoseFree, boolean isGlutenFree, int prepTime, int cookingTime) {
        mId = id;
        mName = name;
        mDescription = description;
        mShortDescription = shortDescription;
        mIngredients = ingredients;
        mTags = tags;
        mPhoto = photo;
        mIsVegan = isVegan;
        mIsLactoseFree = isLactoseFree;
        mIsGlutenFree = isGlutenFree;
        mPrepTime = prepTime;
        mCookingTime = cookingTime;
    }

    public Recipe(String shortDescription, ArrayList<Tag> tags, int prepTime, int cookingTime) {
        mShortDescription = shortDescription;
        mTags = tags;
        mPrepTime = prepTime;
        mCookingTime = cookingTime;
    }

    public Recipe(Long id, String name, String photo) {
        mId=id;
        mName = name;
        mPhoto = photo;
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
        return mShortDescription;
    }

    public void setDescription(String description) {
        mShortDescription = description;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public ArrayList<Tag> getTags() {
        return mTags;
    }

    public void setTags(ArrayList<Tag> tags) {
        mTags = tags;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
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

    @Override
    public String toString() {
        return getName()+" "+getShortDescription()+"(ID"+getId()+")";
    }
}
