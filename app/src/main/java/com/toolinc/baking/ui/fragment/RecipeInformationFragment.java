package com.toolinc.baking.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.adapter.IngredientListAdapter;
import com.toolinc.baking.ui.adapter.InstructionListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Renders a specific {@link java.util.List} of ingredients and instructions. */
public final class RecipeInformationFragment extends Fragment {

  private static final String RECIPE_ARG = "RECIPE";
  private final IngredientListAdapter ingredientListAdapter = new IngredientListAdapter();
  @BindView(R.id.rv_ingredients)
  RecyclerView rvIngredients;
  @BindView(R.id.rv_instructions)
  RecyclerView rvInstructions;
  private InstructionListAdapter instructionListAdapter;
  private InstructionListAdapter.OnStepSelected onStepSelected;
  private Recipe recipe;

  public static final RecipeInformationFragment create(Recipe recipe) {
    RecipeInformationFragment recipeInformationFragment = new RecipeInformationFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(RECIPE_ARG, recipe);
    recipeInformationFragment.setArguments(bundle);
    return recipeInformationFragment;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_recipe_information, container, false);
    ButterKnife.bind(this, view);

    ingredientListAdapter.setIngredients(recipe.ingredients());
    rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
    rvIngredients.setAdapter(ingredientListAdapter);

    instructionListAdapter = new InstructionListAdapter(onStepSelected);
    instructionListAdapter.setInstructions(recipe.steps());
    rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));
    rvInstructions.setAdapter(instructionListAdapter);
    return view;
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    onStepSelected = (InstructionListAdapter.OnStepSelected) context;
  }

  @Override
  public void setArguments(@Nullable Bundle bundle) {
    if (Optional.fromNullable(bundle).isPresent()
        && Optional.fromNullable(bundle.get(RECIPE_ARG)).isPresent()) {
      recipe = (Recipe) bundle.getSerializable(RECIPE_ARG);
    } else {
      throw new IllegalArgumentException("Unable to find a recipe.");
    }
  }
}
