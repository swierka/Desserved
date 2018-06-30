package net.swierkowski.cookbook4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private static final String TAG = "DatabaseAccess";

        private SQLiteOpenHelper mOpenHelper;
        private SQLiteDatabase mDb;
        private static DatabaseAccess mInstance;
        private Cursor mCursor;

        //must have changed to public to be able to run tests
      public DatabaseAccess(Context context) {
            this.mOpenHelper = new DatabaseOpenHelper(context);
        }


        public static DatabaseAccess getInstance (Context context) {
            if (mInstance == null) {
                mInstance = new DatabaseAccess(context);
            }
            return mInstance;
        }

        public void open() {
            this.mDb = mOpenHelper.getWritableDatabase();
        }


        public void close() {
            if (mDb != null) {
                this.mDb.close();
            }
        }

    public static class Products implements BaseColumns {
        public static final String TABLE_PRODUCTS_NAME = "products";
        public static final String COLUMN_NAME_PRODUCTS_ID = "_id";
        public static final String COLUMN_NAME_PRODUCTS_NAME = "productsName";
        public static final String COLUMN_NAME_PRODUCTS_RESTRICTION = "productsRestriction";
    }

    public static class Ingredients implements BaseColumns {
        public static final String TABLE_INGREDIENTS_NAME = "ingredients";
        public static final String COLUMN_NAME_INGREDIENTS_ID = "_id";
        public static final String COLUMN_NAME_INGREDIENTS_AMOUNT = "ingredientsAmount";
        public static final String COLUMN_NAME_INGREDIENTS_ID_PRODUCT = "ingredientsIdProduct";
        public static final String COLUMN_NAME_INGREDIENTS_ID_RECIPE = "ingredientsIdRecipe";
    }

    public static class Recipes implements BaseColumns {
        public static final String TABLE_RECIPES_NAME = "recipes";
        public static final String COLUMN_NAME_RECIPES_ID = "_id";
        public static final String COLUMN_NAME_RECIPES_NAME = "recipesName";
        public static final String COLUMN_NAME_RECIPES_DESCRIPTION = "recipesDescription";
        public static final String COLUMN_NAME_RECIPES_SHORT_DESCRIPTION = "recipesShortDescription";
        public static final String COLUMN_NAME_RECIPES_IMAGE = "recipesImage";
        public static final String COLUMN_NAME_RECIPES_IS_VEGAN = "recipesIsVegan";
        public static final String COLUMN_NAME_RECIPES_IS_GLUTEN_FREE = "recipesIsGlutenFree";
        public static final String COLUMN_NAME_RECIPES_IS_LACTOSE_FREE = "recipesIsLactoseFree";
        public static final String COLUMN_NAME_RECIPES_PREP_TIME = "recipesPrepTime";
        public static final String COLUMN_NAME_RECIPES_COOKING_TIME = "recipesCookTime";
        public static final String COLUMN_NAME_RECIPES_FAVORITE = "recipesIsFavorite";
    }

    public static class Tags implements BaseColumns {
        public static final String TABLE_TAGS_NAME = "tags";
        public static final String COLUMN_NAME_TAGS_ID = "_id";
        public static final String COLUMN_NAME_TAGS_NAME = "tagsName";
    }

    public static class TagsRecipes implements BaseColumns {
        public static final String TABLE_TAGSRECIPES_NAME = "tagsRecipes";
        public static final String COLUMN_NAME_TAGSRECIPES_ID = "_id";
        public static final String COLUMN_NAME_TAGSRECIPES_ID_TAG = "tagsRecipesIdTag";
        public static final String COLUMN_NAME_TAGSRECIPES_ID_RECIPE = "tagsRecipesIdRecipe";
    }

    public Cursor fetchAllProducts() {
        mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[]{Products.COLUMN_NAME_PRODUCTS_ID, Products.COLUMN_NAME_PRODUCTS_NAME, Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, Products.COLUMN_NAME_PRODUCTS_NAME+" ASC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchProductsByName(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products.COLUMN_NAME_PRODUCTS_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, Products.COLUMN_NAME_PRODUCTS_NAME+" ASC", null);

        } else {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products.COLUMN_NAME_PRODUCTS_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION},
                    Products.COLUMN_NAME_PRODUCTS_NAME + " like '%" + inputText + "%'",
                    null, null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateProduct(long productID, int isExcluded) {
        ContentValues args = new ContentValues();
        args.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, isExcluded);
        return mDb.update(Products.TABLE_PRODUCTS_NAME, args, Products.COLUMN_NAME_PRODUCTS_ID + " = " + productID, null) > 0;
    }

    public Cursor fetchAllRecipes() {
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME, new String[]{Recipes.COLUMN_NAME_RECIPES_ID,Recipes.COLUMN_NAME_RECIPES_NAME,Recipes.COLUMN_NAME_RECIPES_DESCRIPTION,
                Recipes.COLUMN_NAME_RECIPES_IMAGE,Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE,Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE,Recipes.COLUMN_NAME_RECIPES_IS_VEGAN,Recipes.COLUMN_NAME_RECIPES_PREP_TIME, Recipes.COLUMN_NAME_RECIPES_COOKING_TIME}, null, null, null, null, Recipes.COLUMN_NAME_RECIPES_NAME, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String [] getExcludedTab (){
        String [] args ={"1"};

        Cursor mExcluded = mDb.query(Products.TABLE_PRODUCTS_NAME,new String[] {Products.COLUMN_NAME_PRODUCTS_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION},Products.COLUMN_NAME_PRODUCTS_RESTRICTION+"=?",args,null,null,null,null);
        String [] excludedProducts = new String[mExcluded.getCount()];

        if (mExcluded != null) {
            //    mExcluded.moveToFirst();
            int index = 0;
            while (mExcluded.moveToNext()) {
                excludedProducts[index] = mExcluded.getString(mExcluded.getColumnIndex("productsName"));
                index++;
            }
        }

        String text= "length: "+excludedProducts.length;
        Log.e("Excl tab length",text);
        return excludedProducts;
    }

    public String buildQueryExcluded(String [] excludedProducts,int vegan, int glutenFree, int lactoseFree){

        int index = 0;

        String query =
                "SELECT r." +
                        Recipes.COLUMN_NAME_RECIPES_ID+ ", "+
                        Recipes.COLUMN_NAME_RECIPES_NAME+ ", "+
                        Recipes.COLUMN_NAME_RECIPES_IMAGE+
                        " FROM " + Recipes.TABLE_RECIPES_NAME+" r";

        if(vegan==1 || glutenFree==1 || lactoseFree==1) {
            query+=" WHERE ";
        }

        if(vegan==1) {
            query +=Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+"="+vegan;
            index++;
        }
        if(lactoseFree==1) {
            if(index>0){ query+=" AND ";}
            query +=Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+"="+lactoseFree;
            index++;
        }
        if(glutenFree==1 ) {
            if(index>0){ query+=" AND ";}
            query +=Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+"="+glutenFree;
        }
        if (vegan==0 && glutenFree==0 && lactoseFree==0 && excludedProducts.length!=0) {
            query+=" WHERE r."+Recipes.COLUMN_NAME_RECIPES_ID;
        }

        if ((vegan==1 || glutenFree==1 || lactoseFree==1) && excludedProducts.length!=0) {
            query += " AND r."+Recipes.COLUMN_NAME_RECIPES_ID;
        }

        if(excludedProducts.length!=0){
            query+=" NOT IN (select r."+Recipes.COLUMN_NAME_RECIPES_ID+" " +
                    "from "+ Recipes.TABLE_RECIPES_NAME+
                    " r JOIN "+Ingredients.TABLE_INGREDIENTS_NAME+
                    " i ON r."+Recipes.COLUMN_NAME_RECIPES_ID+
                    "= i."+Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+
                    " JOIN "+Products.TABLE_PRODUCTS_NAME +
                    " p on i."+Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT+
                    "=p."+Products.COLUMN_NAME_PRODUCTS_ID+
                    " where ";
        }

        StringBuilder stringBuilder = new StringBuilder(query);
        index = 0;

        while(excludedProducts.length!=0 && index < excludedProducts.length){
            stringBuilder.append("p."+Products.COLUMN_NAME_PRODUCTS_NAME+"=\""+excludedProducts[index]+"\"");
            if(index<excludedProducts.length-1){
                stringBuilder.append(" OR ");
            } else {
                stringBuilder.append(") group by r."+Recipes.COLUMN_NAME_RECIPES_ID);
            }
            index++;
        }

        query = stringBuilder.toString();

        Log.e("DATABSE:","NEW QUERY :D "+query);

        return query;
    }

    public Cursor getRecipes(int vegan, int glutenFree, int lactoseFree){
        String[] excludedProducts = getExcludedTab();
        String query = buildQueryExcluded(excludedProducts, vegan, glutenFree, lactoseFree);
        mCursor = mDb.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getDescriptionForRecipeId(String idRecipe){
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME,new String[]{Recipes.COLUMN_NAME_RECIPES_ID, Recipes.COLUMN_NAME_RECIPES_DESCRIPTION},Recipes.COLUMN_NAME_RECIPES_ID+"="+idRecipe,null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSummaryForRecipe(String idRecipe){
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME,new String[]{Recipes.COLUMN_NAME_RECIPES_ID, Recipes.COLUMN_NAME_RECIPES_SHORT_DESCRIPTION, Recipes.COLUMN_NAME_RECIPES_PREP_TIME, Recipes.COLUMN_NAME_RECIPES_COOKING_TIME},Recipes.COLUMN_NAME_RECIPES_ID+"="+idRecipe,null,null,null,null,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getImageAndNameForRecipeId(String idRecipe){
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME,new String[]{Recipes.COLUMN_NAME_RECIPES_NAME, Recipes.COLUMN_NAME_RECIPES_IMAGE},Recipes.COLUMN_NAME_RECIPES_ID+"="+idRecipe,null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getTagsForRecipe(String idRecipe){

        String query =
                "SELECT "+Tags.COLUMN_NAME_TAGS_NAME+
                        " FROM " + Tags.TABLE_TAGS_NAME +
                        " t JOIN "+TagsRecipes.TABLE_TAGSRECIPES_NAME +
                        " tr ON t." +Tags.COLUMN_NAME_TAGS_ID +"=tr."+TagsRecipes.COLUMN_NAME_TAGSRECIPES_ID_TAG+
                        " WHERE tr."+TagsRecipes.COLUMN_NAME_TAGSRECIPES_ID_RECIPE+"="+idRecipe;

        mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    };

    public Cursor getIngredientsForRecipe(String idRecipe) throws SQLException {
        String query =
                "SELECT r."+Recipes.COLUMN_NAME_RECIPES_ID+","+Ingredients.COLUMN_NAME_INGREDIENTS_AMOUNT+" FROM " +
                        Ingredients.TABLE_INGREDIENTS_NAME +
                        " i JOIN "+Recipes.TABLE_RECIPES_NAME +
                        " r ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+"=r."+Recipes.COLUMN_NAME_RECIPES_ID+
                        " JOIN "+ Products.TABLE_PRODUCTS_NAME +
                        " p ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT+"=p."+Products.COLUMN_NAME_PRODUCTS_ID+
                        " WHERE i."+Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+"="+idRecipe+";";


        mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getFavoritesRecipes(){
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME,
                new String[]{Recipes.COLUMN_NAME_RECIPES_ID,
                        Recipes.COLUMN_NAME_RECIPES_NAME,
                        Recipes.COLUMN_NAME_RECIPES_IMAGE},
                Recipes.COLUMN_NAME_RECIPES_FAVORITE+"=1",null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateRecipeWithFavorite(long productID, int isFavorite) {
        ContentValues args = new ContentValues();
        args.put(Recipes.COLUMN_NAME_RECIPES_FAVORITE, isFavorite);
        return mDb.update(Recipes.TABLE_RECIPES_NAME, args,
                Recipes.COLUMN_NAME_RECIPES_ID + " = " + productID, null) > 0;
    }

    public Cursor isRecipeFavorite(String idRecipe){
        mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME,
                new String[]{Recipes.COLUMN_NAME_RECIPES_FAVORITE},
                Recipes.COLUMN_NAME_RECIPES_ID+"="+idRecipe,null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Boolean clearRestriction(){
        ContentValues args = new ContentValues();
        args.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, "0");
        return mDb.update(Products.TABLE_PRODUCTS_NAME, args, null, null) > 0;
    }
}

