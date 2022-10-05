package com.example.reciperetrofit;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Recipes {

    private Integer id;
    private String title;
    private ArrayList<JsonObject> missedIngredients;
    private ArrayList<JsonObject> usedIngredients;

    //getter functions for ingredients

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public ArrayList<JsonObject> getMissedIngredients() {
        return this.missedIngredients;
    }

    public ArrayList<JsonObject> getUsedIngredients() {
        return this.usedIngredients;
    }
}
