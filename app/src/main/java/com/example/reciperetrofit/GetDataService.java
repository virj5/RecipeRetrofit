package com.example.reciperetrofit;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {
    //QUERY parameters to set endpoints
    @GET("recipes/findByIngredients")
    Call<List<Recipes>> getAllRecipes(
            @Query("apiKey") String apiKey,
            @Query("ingredients") String ingredients,
            @Query("number") int number);
}
