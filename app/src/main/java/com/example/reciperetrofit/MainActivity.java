package com.example.reciperetrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        searchedIngredient = searchBar.getText().toString();


        // This creates an instance of the retrofit client
        GetDataService service = RetrofitManager.getRetrofitInstance().create(GetDataService.class);

        getRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Grab the list of recipes from the JSON that was fetched using the GET parameters defined in the GetDataService interface
                //create the URL for getting ingredients
                Call<List<Recipes>> recipesList = service.getAllRecipes("ffec6a5d973042fdb526406895cf0c77","tomato, lettuce", 2);


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
        //Toast.makeText(this, recipesList.body().get(0).getId().toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, recipesList.body().get(0).getTitle(), Toast.LENGTH_SHORT).show();
        String ingredientList = "Here is the full ingredient list:\n";
        String recipeList = "Recipes you can make: \n" + "\n";


        //printing out ingredient list for the first recipe right now

        for(int i = 0; i < recipesList.body().get(0).getUsedIngredients().size(); ++i) {
            ingredientList += recipesList.body().get(0).getUsedIngredients().get(i).get("originalName") + ": " + recipesList.body().get(0).getUsedIngredients().get(i).get("amount") + "\n";
        }

        for(int j = 0; j < recipesList.body().get(0).getMissedIngredients().size(); ++j) {
            ingredientList += recipesList.body().get(0).getMissedIngredients().get(j).get("originalName") + ": " + recipesList.body().get(0).getMissedIngredients().get(j).get("amount") + "\n";
        }



        //print out 2 recipes for given items
        for(int i = 0; i < recipesList.body().size(); ++i){
            recipeList += recipesList.body().get(i).getTitle() + "\n";
        }

        //tvitemID.setText(recipesList.body().get(0).getTitle());
        tvitemID.setText(recipeList);
        tvIngredient.setText(ingredientList);
    }

}