package com.toolinc.baking.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.databinding.ItemRecipeBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RecipeListAdapter provides a binding from an {@link ImmutableList} of {@link Recipe} to the view
 * {@code R.layout.item_recipe} displayed within a RecyclerView.
 */
public final class RecipeListAdapter
    extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

  private static final ImmutableList<Recipe> EMPTY = ImmutableList.copyOf(Lists.newArrayList());
  private final OnRecipeSelected onRecipeSelected;
  private ImmutableList<Recipe> recipes = EMPTY;

  public RecipeListAdapter(OnRecipeSelected onRecipeSelected) {
    this.onRecipeSelected = checkNotNull(onRecipeSelected, "OnRecipeSelected is missing.");
  }

  @NonNull
  @Override
  public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemRecipeBinding itemRecipeBinding = ItemRecipeBinding.inflate(inflater, viewGroup, false);
    return new RecipeViewHolder(itemRecipeBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
    recipeViewHolder.itemRecipeBinding.setRecipe(recipes.get(position));
    recipeViewHolder.itemRecipeBinding.ivRecipeIcon.setImageResource(
        Recipe.loadIcon(recipes.get(position)));
  }

  @Override
  public int getItemCount() {
    return recipes.size();
  }

  public void setRecipes(@Nullable ImmutableList<Recipe> recipes) {
    if (Optional.fromNullable(recipes).isPresent()) {
      this.recipes = recipes;
    } else {
      this.recipes = EMPTY;
    }
  }

  /** Specifies the behavior upon selection of a {@link Recipe}. */
  public interface OnRecipeSelected {

    /** Specifies the recipe that has been selected by the user. */
    void onSelected(Recipe recipe);
  }

  /** Describes a {@link Recipe} item about its place within the RecyclerView. */
  public final class RecipeViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private final ItemRecipeBinding itemRecipeBinding;

    public RecipeViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding) {
      super(itemRecipeBinding.getRoot());
      this.itemRecipeBinding = checkNotNull(itemRecipeBinding, "ItemRecipeBinding is missing.");
      this.itemRecipeBinding.mcvRecipe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      onRecipeSelected.onSelected(recipes.get(getAdapterPosition()));
    }
  }
}
