package com.example.reciperetrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView tvitemID;
    TextView tvIngredient;
    EditText searchBar;
    Button getRecipe;
    String searchedIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvitemID = findViewById(R.id.tvRecipeName);
        tvIngredient = findViewById(R.id.tvIngredients);
        searchBar = findViewById(R.id.searchIngredient);
        getRecipe = findViewById(R.id.buttonSearch);

        //searchedIngredient = searchBar.getText().toString();


        // This creates an instance of the retrofit client
        GetDataService service = RetrofitManager.getRetrofitInstance().create(GetDataService.class);

        getRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedIngredient = searchBar.getText().toString();
                // Grab the list of recipes from the JSON that was fetched using the GET parameters defined in the GetDataService interface
                //create the URL for getting ingredients
                Call<List<Recipes>> recipesList = service.getAllRecipes("ffec6a5d973042fdb526406895cf0c77",searchedIngredient, 3);


                // Asynchronously send the request and notify callback of its response or if an error occurred talking to the server,
                // creating the request, or processing the response.
                recipesList.enqueue(new Callback<List<Recipes>>() {
                    @Override
                    public void onResponse(Call<List<Recipes>> call, Response<List<Recipes>> response) {
                        generateDataList(response);
                    }

                    @Override
                    public void onFailure(Call<List<Recipes>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    private void generateDataList(Response<List<Recipes>> recipesList) {
        String ingredientList = "Here is the full ingredient list:\n";

        String recipeList = "Recipes you can make: \n" + "\n";


        //adding ingredient list for recipes into a string; we have all ingredients here and are separating
        //them by hyphens
        for(int k = 0; k < recipesList.body().size(); ++k){

            for(int i = 0; i < recipesList.body().get(k).getUsedIngredients().size(); ++i) {
                ingredientList += recipesList.body().get(k).getUsedIngredients().get(i).get("originalName") + ": " + recipesList.body().get(k).getUsedIngredients().get(i).get("amount");
                //adding units
                ingredientList += " " + recipesList.body().get(k).getUsedIngredients().get(i).get("unit") + "\n";
            }

            for(int j = 0; j < recipesList.body().get(k).getMissedIngredients().size(); ++j) {
                ingredientList += recipesList.body().get(k).getMissedIngredients().get(j).get("originalName") + ": " + recipesList.body().get(k).getMissedIngredients().get(j).get("amount");
                //adding units
                ingredientList += " " + recipesList.body().get(k).getMissedIngredients().get(j).get("unit") + "\n";
            }
            //we want to use this hyphen in order to separate each ingredient
            ingredientList += "--";
        }

        ingredientList = ingredientList.replaceAll("\"","");

        //create list to take ingredientList and split by hyphen to separate the different ingredients by recipe index
        //holds the ingredients as well
        List<String> holder = Arrays.asList(ingredientList.split("--"));

        //print out recipes with ingredient list below each recipe
        for(int i = 0; i < recipesList.body().size(); ++i){
            recipeList += recipesList.body().get(i).getTitle() + "\n";
            recipeList += holder.get(i) + "\n";
        }

        tvitemID.setText(recipeList);
        //tvIngredient.setText(ingredientList);
    }

}