package com.toolinc.baking.ui;

import android.os.Bundle;

import com.toolinc.baking.R;

import androidx.appcompat.app.AppCompatActivity;

/** Main activity that displays a {@link com.toolinc.baking.client.model.Recipes}. */
public final class RecipesActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipes);
  }
}
