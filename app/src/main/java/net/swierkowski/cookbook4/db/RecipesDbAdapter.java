package net.swierkowski.cookbook4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import net.swierkowski.cookbook4.model.Ingridient;
import net.swierkowski.cookbook4.model.Product;

import java.util.ArrayList;

public class RecipesDbAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    private static final String SQL_CREATE_ENTRIES_PRODUCTS =
            "CREATE TABLE " + Products.TABLE_PRODUCTS_NAME + " (" +
                    Products._ID + " INTEGER PRIMARY KEY," +
                    Products.COLUMN_NAME_PRODUCTS_NAME + " TEXT," +
                    Products.COLUMN_NAME_PRODUCTS_RESTRICTION + " INTEGER);";

    private static final String SQL_CREATE_ENTRIES_RECIPES =
            "CREATE TABLE " + Recipes.TABLE_RECIPES_NAME + " (" +
                    Recipes._ID + " INTEGER PRIMARY KEY," +
                    Recipes.COLUMN_NAME_RECIPES_NAME + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_DESCRIPTION + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_IMAGE + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_IS_VEGAN + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_PREP_TIME+ " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_COOKING_TIME + " INTEGER);";

    private static final String SQL_CREATE_ENTRIES_INGRIDIENTS =
            "CREATE TABLE " + Ingridients.TABLE_INGRIDIENTS_NAME + " (" +
                    Ingridients._ID + " INTEGER PRIMARY KEY," +
                    Ingridients.COLUMN_NAME_INGRIDIENTS_AMOUNT + " TEXT," +
                    Ingridients.COLUMN_NAME_INGRIDIENTS_ID_PRODUCT + " INTEGER," +
                    Ingridients.COLUMN_NAME_INGRIDIENTS_ID_RECIPE+ " INTEGER);";

    public static class Products implements BaseColumns {
        public static final String TABLE_PRODUCTS_NAME = "produkty";
        public static final String COLUMN_NAME_PRODUCTS_NAME = "nazwa_produkty";
        public static final String COLUMN_NAME_PRODUCTS_RESTRICTION = "restrykcje";
    }

    public static class Ingridients implements BaseColumns {
        public static final String TABLE_INGRIDIENTS_NAME = "skladniki";
        public static final String COLUMN_NAME_INGRIDIENTS_AMOUNT = "ilosc";
        public static final String COLUMN_NAME_INGRIDIENTS_ID_PRODUCT = "produkty_id";
        public static final String COLUMN_NAME_INGRIDIENTS_ID_RECIPE = "przepisy_id";
    }

    public static class Recipes implements BaseColumns {
        public static final String TABLE_RECIPES_NAME = "przepisy";
        public static final String COLUMN_NAME_RECIPES_NAME = "nazwa_przepisy";
        public static final String COLUMN_NAME_RECIPES_DESCRIPTION = "opis";
        public static final String COLUMN_NAME_RECIPES_IMAGE = "obraz";
        public static final String COLUMN_NAME_RECIPES_IS_VEGAN = "weganskie";
        public static final String COLUMN_NAME_RECIPES_IS_GLUTEN_FREE = "bez_glutenu";
        public static final String COLUMN_NAME_RECIPES_IS_LACTOSE_FREE = "bez_laktozy";
        public static final String COLUMN_NAME_RECIPES_PREP_TIME = "czas_przygotowania";
        public static final String COLUMN_NAME_RECIPES_COOKING_TIME = "czas_pieczenia";
    }

    private static final String TAG = "RecipesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    private String orderBy = null;

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES_PRODUCTS =
                "DROP TABLE IF EXISTS " + Products.TABLE_PRODUCTS_NAME;

        private static final String SQL_DELETE_ENTRIES_RECIPES =
                "DROP TABLE IF EXISTS " + Recipes.TABLE_RECIPES_NAME;

        private static final String SQL_DELETE_ENTRIES_INGRIDIENTS =
                "DROP TABLE IF EXISTS " + Ingridients.TABLE_INGRIDIENTS_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES_PRODUCTS);
            db.execSQL(SQL_CREATE_ENTRIES_INGRIDIENTS);
            db.execSQL(SQL_CREATE_ENTRIES_RECIPES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES_PRODUCTS);
            db.execSQL(SQL_DELETE_ENTRIES_RECIPES);
            db.execSQL(SQL_DELETE_ENTRIES_INGRIDIENTS);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public RecipesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public RecipesDbAdapter open() throws SQLException {
        mDbHelper = new RecipesDbAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createProduct(String nazwa, int restrykcjeDefault) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Products.COLUMN_NAME_PRODUCTS_NAME, nazwa);
        initialValues.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, restrykcjeDefault);
        return mDb.insert(Products.TABLE_PRODUCTS_NAME, null, initialValues);
    }

    public long createIngridient(String amount, int idProduct, int idRecipe) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Ingridients.COLUMN_NAME_INGRIDIENTS_AMOUNT, amount);
        initialValues.put(Ingridients.COLUMN_NAME_INGRIDIENTS_ID_PRODUCT, idProduct);
        initialValues.put(Ingridients.COLUMN_NAME_INGRIDIENTS_ID_RECIPE, idRecipe);
        return mDb.insert(Ingridients.TABLE_INGRIDIENTS_NAME, null, initialValues);
    }

    public long createRecipe(String name,String desc, String image, int isVegan, int isGlutenFree, int isLactoseFree, int prepTime, int cookTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_NAME, name);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_DESCRIPTION, desc);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_IMAGE, image);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE, isGlutenFree);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE, isLactoseFree);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_IS_VEGAN, isVegan);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_PREP_TIME, prepTime);
        initialValues.put(Recipes.COLUMN_NAME_RECIPES_COOKING_TIME, cookTime);
        return mDb.insert(Recipes.TABLE_RECIPES_NAME, null, initialValues);
    }

    public boolean deleteAllProducts() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Products.TABLE_PRODUCTS_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteAllRecipes() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Recipes.TABLE_RECIPES_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public boolean deleteAllIngridients() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Ingridients.TABLE_INGRIDIENTS_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchProductsByName(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products._ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products._ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION},
                    Products.COLUMN_NAME_PRODUCTS_NAME + " like '%" + inputText + "%'",
                    null, null, null, orderBy, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllProducts() {
        Cursor mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[]{Products._ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Boolean isProductsEmpty() {
        Cursor mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[]{Products._ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            return false;
        }
        return true;
    }

    public void insertSomeProducts() {
        createProduct("mleko krowie",0);
        createProduct("mleko migdałowe",0);
        createProduct("mąka pszenna",0);
        createProduct("mąka kokosowa",0);
        createProduct("mąka ryżowa",0);
        createProduct("mąka gryczana",0);
        createProduct("orzechy włoskie",0);
        createProduct("orzechy ziemne",0);
        createProduct("orzechy nerkowca",0);
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }

    public boolean updateProduct(long productID, int isExcluded) {
        ContentValues args = new ContentValues();
        args.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, isExcluded);
        return mDb.update(Products.TABLE_PRODUCTS_NAME, args, Products._ID + " = " + productID, null) > 0;
    }

}
