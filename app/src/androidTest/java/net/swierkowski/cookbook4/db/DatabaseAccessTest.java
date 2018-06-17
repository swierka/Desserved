package net.swierkowski.cookbook4.db;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseAccessTest {

    private DatabaseAccess mDatabaseAccess;

    @Before
    public void setUp(){
        mDatabaseAccess = new DatabaseAccess(InstrumentationRegistry.getTargetContext());
        mDatabaseAccess.open();
    }

    @After
    public void finish(){
        mDatabaseAccess.close();
    }

    @Test
    public void shouldReturnAllProducts() {
        //given
        int productCount = 68;

        //when
        Cursor cursor = mDatabaseAccess.fetchAllProducts();

        //then
        assertThat(cursor.getCount(),is(productCount));

    }

    @Test
    public void shouldReturnRetrieve4products() {
        //given
        String product = "Mle";

        //when
        Cursor cursor = mDatabaseAccess.fetchProductsByName(product);
        cursor.getCount();

        //then
        assertThat(cursor.getCount(),is(4));
    }

    @Test
    public void updateProduct() {
        //given
        int productId = 15;
        int isExcluded = 1;

        //when
       boolean result = mDatabaseAccess.updateProduct(productId,isExcluded);

       //then
        assertThat(result,is(true));
    }

    @Test
    public void shouldReturnAllRecipes() {
        //given
        int recipesCount = 21;

        //when
        Cursor cursor = mDatabaseAccess.fetchAllRecipes();

        //then
        assertThat(cursor.getCount(),is(recipesCount));
    }


    @Test
    public void shouldReturn5Recipes() {
        //when
        Cursor cursor = mDatabaseAccess.getRecipes(1,1,1);

        //then
        assertThat(cursor.getCount(),is(9));

    }

}