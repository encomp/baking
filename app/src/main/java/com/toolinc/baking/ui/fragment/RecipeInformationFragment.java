package com.toolinc.baking.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.widget.IngredientListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Renders a specific {@link java.util.List} of ingredients. */
public final class RecipeInformationFragment extends Fragment {

  private final IngredientListAdapter ingredientListAdapter = new IngredientListAdapter();
  private Recipe recipe;

  @BindView(R.id.rv_ingredients)
  RecyclerView rvIngredientList;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_recipe_information, container, false);
    ButterKnife.bind(this, view);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    ingredientListAdapter.setIngredients(recipe.ingredients());
    rvIngredientList.setLayoutManager(linearLayoutManager);
    rvIngredientList.setAdapter(ingredientListAdapter);
    return view;
  }

  @Override
  public void setArguments(@Nullable Bundle bundle) {
    if (Optional.fromNullable(bundle).isPresent()) {
      recipe = (Recipe) bundle.getSerializable(Intent.EXTRA_KEY_EVENT);
    }
  }
}