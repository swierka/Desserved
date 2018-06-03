package net.swierkowski.cookbook4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import net.swierkowski.cookbook4.model.Product;
import net.swierkowski.cookbook4.model.Recipe;

public class RecipesDbAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    private static final String SQL_CREATE_ENTRIES_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS " + Products.TABLE_PRODUCTS_NAME + " (" +
                    Products.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Products.COLUMN_NAME_PRODUCTS_NAME + " TEXT," +
                    Products.COLUMN_NAME_PRODUCTS_RESTRICTION + " INTEGER);";

    private static final String SQL_CREATE_ENTRIES_RECIPES =
            "CREATE TABLE " + Recipes.TABLE_RECIPES_NAME + " (" +
                    Recipes.COLUMN_NAME_RECIPES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Recipes.COLUMN_NAME_RECIPES_NAME + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_DESCRIPTION + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_IMAGE + " TEXT," +
                    Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_IS_VEGAN + " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_PREP_TIME+ " INTEGER," +
                    Recipes.COLUMN_NAME_RECIPES_COOKING_TIME + " INTEGER);";

    private static final String SQL_CREATE_ENTRIES_INGREDIENTS =
            "CREATE TABLE " + Ingredients.TABLE_INGREDIENTS_NAME + " (" +
                    Ingredients.COLUMN_NAME_INGREDIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Ingredients.COLUMN_NAME_INGREDIENTS_AMOUNT + " TEXT," +
                    Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT + " INTEGER," +
                    Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+ " INTEGER);";

    public static class Products implements BaseColumns {
        public static final String TABLE_PRODUCTS_NAME = "produkty";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_PRODUCTS_NAME = "nazwa_produkty";
        public static final String COLUMN_NAME_PRODUCTS_RESTRICTION = "restrykcje";
    }

    public static class Ingredients implements BaseColumns {
        public static final String TABLE_INGREDIENTS_NAME = "skladniki";
        public static final String COLUMN_NAME_INGREDIENTS_ID = "_id";
        public static final String COLUMN_NAME_INGREDIENTS_AMOUNT = "ilosc";
        public static final String COLUMN_NAME_INGREDIENTS_ID_PRODUCT = "produkty_id";
        public static final String COLUMN_NAME_INGREDIENTS_ID_RECIPE = "przepisy_id";
    }

    public static class Recipes implements BaseColumns {
        public static final String TABLE_RECIPES_NAME = "przepisy";
        public static final String COLUMN_NAME_RECIPES_ID = "_id";
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

        private static final String SQL_DELETE_ENTRIES_INGREDIENTS =
                "DROP TABLE IF EXISTS " + Ingredients.TABLE_INGREDIENTS_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES_PRODUCTS);
            db.execSQL(SQL_CREATE_ENTRIES_INGREDIENTS);
            db.execSQL(SQL_CREATE_ENTRIES_RECIPES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES_PRODUCTS);
            db.execSQL(SQL_DELETE_ENTRIES_RECIPES);
            db.execSQL(SQL_DELETE_ENTRIES_INGREDIENTS);
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

    public long createProduct(String nazwa) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Products.COLUMN_NAME_PRODUCTS_NAME, nazwa);
        initialValues.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, 0);
        return mDb.insert(Products.TABLE_PRODUCTS_NAME, null, initialValues);
    }

    public long createIngredient(String amount, int idProduct, int idRecipe) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Ingredients.COLUMN_NAME_INGREDIENTS_AMOUNT, amount);
        initialValues.put(Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT, idProduct);
        initialValues.put(Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE, idRecipe);
        return mDb.insert(Ingredients.TABLE_INGREDIENTS_NAME, null, initialValues);
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

    public boolean deleteAllIngredients() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Ingredients.TABLE_INGREDIENTS_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchProductsByName(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products.COLUMN_NAME_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[] {Products.COLUMN_NAME_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION},
                    Products.COLUMN_NAME_PRODUCTS_NAME + " like '%" + inputText + "%'",
                    null, null, null, orderBy, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllProducts() {
        Cursor mCursor = mDb.query(Products.TABLE_PRODUCTS_NAME, new String[]{Products.COLUMN_NAME_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllRecipes() {
        Cursor mCursor = mDb.query(Recipes.TABLE_RECIPES_NAME, new String[]{Recipes.COLUMN_NAME_RECIPES_ID,Recipes.COLUMN_NAME_RECIPES_NAME,Recipes.COLUMN_NAME_RECIPES_DESCRIPTION,
        Recipes.COLUMN_NAME_RECIPES_IMAGE,Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE,Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE,Recipes.COLUMN_NAME_RECIPES_IS_VEGAN,Recipes.COLUMN_NAME_RECIPES_PREP_TIME, Recipes.COLUMN_NAME_RECIPES_COOKING_TIME}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Boolean isProductsEmpty() {
        int count = countRecords(Products.TABLE_PRODUCTS_NAME);

        Log.e("Count ....","Count = "+count);

        if(count==0){
            return true;
        }
        return false;
    }

    public Boolean isRecipesEmpty() {
        int count = countRecords(Recipes.TABLE_RECIPES_NAME);

        Log.e("Count ....","Count = "+count);

        if(count==0){
            return true;
        }
        return false;
    }

    public Boolean isIngredientsEmpty() {
        int count = countRecords(Ingredients.TABLE_INGREDIENTS_NAME);

        Log.e("Count ....","Count = "+count);

        if(count==0){
            return true;
        }
        return false;
    }

    public void insertSomeProducts() {
        createProduct("mleko krowie");
        createProduct("mleko migdałowe");
        createProduct("mąka pszenna");
        createProduct("mąka kokosowa");
        createProduct("mąka ryżowa");
        createProduct("mąka gryczana");
        createProduct("orzechy włoskie");
        createProduct("orzechy ziemne");
        createProduct("orzechy nerkowca");
        createProduct("jajka");
    }

    public void insertSomeRecipes() {
        createRecipe("Ciastko kokosowe","jsbjcbmhfcAS","no photo",1,1,1,30,20);
        createRecipe("Ciastko czekoladowe","ddddss","no photo",0,1,1,40,60);
        createRecipe("Ciasteczka","dsfasaasafaf","no photo",0,0,1,40,60);
        createRecipe("Muffiny","dsffsarettthaf","no photo",0,0,0,40,60);
        createRecipe("Ciastko marchewkowe","dsffhhrhyrhf","no photo",0,0,1,40,60);
        createRecipe("Pudding chia","dsfryhbryhryfaf","no photo",0,0,1,40,60);
        createRecipe("Ciastko malinowe","dsffrhyrhyaf","no photo",0,0,1,40,60);
        createRecipe("Sałatka owocowa","dsffadrhryydf","no photo",1,1,1,15,0);
    }

    public void insertIngredients() {
        createIngredient("3 szt.",10,2);
        createIngredient("2 szt.",10,3);
        createIngredient("2 szt.",10,5);
        createIngredient("2 szklanki",3,2);
        createIngredient("2 szklanki",3,5);
        createIngredient("250 ml",1,2);
        createIngredient("250 ml",1,5);
        createIngredient("100 g",7,5);
        createIngredient("2 szklanki",4,1);
        createIngredient("300 ml",2,1);
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }

    public boolean updateProduct(long productID, int isExcluded) {
        ContentValues args = new ContentValues();
        args.put(Products.COLUMN_NAME_PRODUCTS_RESTRICTION, isExcluded);
        return mDb.update(Products.TABLE_PRODUCTS_NAME, args, Products.COLUMN_NAME_ID + " = " + productID, null) > 0;
    }

    public int countRecords(String tableName){
        Cursor mCount = mDb.query(tableName,null,null,null,null,null,null);
         int count = mCount.getCount();
         return count;
    }

    public int countExcluded (){
        String [] args ={"1"};

        Cursor mCount = mDb.query(Products.TABLE_PRODUCTS_NAME,null,Products.COLUMN_NAME_PRODUCTS_RESTRICTION+"=?",args,null,null,null);
        int excludedCount = mCount.getCount();
        return excludedCount;
    }


    public String [] getExcludedTab (){
        String [] args ={"1"};

        Cursor mCount = mDb.query(Products.TABLE_PRODUCTS_NAME,null,Products.COLUMN_NAME_PRODUCTS_RESTRICTION+"=?",args,null,null,null);
        int excludedCount = mCount.getCount();
        String [] excludedProducts = new String[excludedCount];

        Cursor mExcluded = mDb.query(Products.TABLE_PRODUCTS_NAME,new String[] {Products.COLUMN_NAME_ID,Products.COLUMN_NAME_PRODUCTS_NAME,Products.COLUMN_NAME_PRODUCTS_RESTRICTION},Products.COLUMN_NAME_PRODUCTS_RESTRICTION+"=?",args,null,null,null,null);

        if (mExcluded != null) {
        //    mExcluded.moveToFirst();
            int index = 0;
            while (mExcluded.moveToNext()) {
                excludedProducts[index] = mExcluded.getString(mExcluded.getColumnIndex("nazwa_produkty"));
                index++;
            }
        }

        String text= "length: "+excludedProducts.length;
        Log.e("Excl tab length",text);
        return excludedProducts;
    }


    public String buildQueryExcluded(String [] excludedProducts,int vegan, int glutenFree, int lactoseFree){
        String query =
        "SELECT r." +
                Recipes.COLUMN_NAME_RECIPES_ID+ ", "+
                Recipes.COLUMN_NAME_RECIPES_NAME+ ", "+
                Recipes.COLUMN_NAME_RECIPES_DESCRIPTION+ ", "+
                Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+ ", "+
                Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+ ", "+
                Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+ ", "+
                Recipes.COLUMN_NAME_RECIPES_PREP_TIME+ ", "+
                Recipes.COLUMN_NAME_RECIPES_COOKING_TIME+
                " FROM " +
        Ingredients.TABLE_INGREDIENTS_NAME+
        " i JOIN "+Recipes.TABLE_RECIPES_NAME+
        " r ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+"=r."+Recipes.COLUMN_NAME_RECIPES_ID+
        " JOIN "+ Products.TABLE_PRODUCTS_NAME+
        " p ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT+"=p."+Products.COLUMN_NAME_ID;

        if(vegan==1 && glutenFree==1 && lactoseFree==1){
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+"="+vegan
                    +" AND "+Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+"="+lactoseFree
                    +" AND "+Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+"="+glutenFree;
        } else if (vegan==1 && glutenFree==0 && lactoseFree==0){
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+"="+vegan;
        } else if (vegan==1 && glutenFree==1 && lactoseFree==0){
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+"="+vegan+" AND "+Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+"="+glutenFree;
        } else if (vegan==1 && glutenFree==0 && lactoseFree==1){
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_VEGAN+"="+vegan+" AND "+Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+"="+lactoseFree;
        } else if (vegan==0 && glutenFree==1 && lactoseFree==1) {
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+"="+glutenFree+" AND "+Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+"="+lactoseFree;
        } else if (vegan==0 && glutenFree==1 && lactoseFree==0) {
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_GLUTEN_FREE+"="+glutenFree;
        } else if (vegan==0 && glutenFree==0 && lactoseFree==1) {
            query+=" WHERE "+Recipes.COLUMN_NAME_RECIPES_IS_LACTOSE_FREE+"="+lactoseFree;
        } else if (vegan==0 && glutenFree==0 && lactoseFree==0 && excludedProducts.length!=0) {
            query+=" WHERE NOT ";
        }

        if ((vegan==1 || glutenFree==1 || lactoseFree==1) && excludedProducts.length!=0) {
            query += " AND NOT ";
        }

        StringBuilder stringBuilder = new StringBuilder(query);
        int index = 0;

        while(excludedProducts.length!=0 && index < excludedProducts.length){
            stringBuilder.append(Products.COLUMN_NAME_PRODUCTS_NAME+"=\""+excludedProducts[index]+"\"");
            if(index<excludedProducts.length-1){
                stringBuilder.append(" AND NOT ");
            }
            index++;
        }
        stringBuilder.append(";");
        query = stringBuilder.toString();

        return query;
    }


    public Cursor getRecipes(int vegan, int glutenFree, int lactoseFree){
        String[] excludedProducts = getExcludedTab();
        String query = buildQueryExcluded(excludedProducts, vegan, glutenFree, lactoseFree);
        Cursor cursor = mDb.rawQuery(query, null);
        Log.e("query", query);
        return cursor;
    }


    public Cursor getIngredientsForRecipe(String idRecipe) throws SQLException {

            Cursor mCursor = null;
            String query =
                    "SELECT "+Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+", "+Products.COLUMN_NAME_PRODUCTS_NAME+", "+Ingredients.COLUMN_NAME_INGREDIENTS_AMOUNT+" FROM " + Ingredients.TABLE_INGREDIENTS_NAME +
                    " i JOIN "+Recipes.TABLE_RECIPES_NAME +
                    " r ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+"=r."+Recipes.COLUMN_NAME_RECIPES_ID+
                    " JOIN "+ Products.TABLE_PRODUCTS_NAME +
                    " p ON i." + Ingredients.COLUMN_NAME_INGREDIENTS_ID_PRODUCT+"=p."+Products.COLUMN_NAME_ID+
                    " WHERE i."+Ingredients.COLUMN_NAME_INGREDIENTS_ID_RECIPE+"="+idRecipe+";";

            Log.e("query", query);
            mCursor = mDb.rawQuery(query,null);

            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
    }


}
