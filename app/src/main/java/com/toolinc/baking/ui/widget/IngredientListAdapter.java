package com.toolinc.baking.ui.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.toolinc.baking.client.model.Ingredient;
import com.toolinc.baking.databinding.ItemIngredientBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecipeListAdapter provides a binding from an {@link ImmutableList} of {@link Ingredient} to the
 * view {@code R.layout.item_ingredient} displayed within a RecyclerView.
 */
public final class IngredientListAdapter
    extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {

  private static final String ORDER = "%s.";
  private static final ImmutableList<Ingredient> EMPTY = ImmutableList.copyOf(Lists.newArrayList());
  private ImmutableList<Ingredient> ingredients = EMPTY;

  @NonNull
  @Override
  public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
    ItemIngredientBinding itemIngredientBinding =
        ItemIngredientBinding.inflate(inflater, viewGroup, false);
    return new IngredientViewHolder(itemIngredientBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int position) {
    ingredientViewHolder.itemIngredientBinding.setIngredient(ingredients.get(position));
    ingredientViewHolder.itemIngredientBinding.tvIngredientOrder.setText(
        String.format(ORDER, position + 1));
  }

  @Override
  public int getItemCount() {
    return ingredients.size();
  }

  public void setIngredients(@Nullable ImmutableList<Ingredient> ingredients) {
    if (Optional.fromNullable(ingredients).isPresent()) {
      this.ingredients = ingredients;
    } else {
      this.ingredients = EMPTY;
    }
  }

  /** Describes a {@link Ingredient} item about its place within the RecyclerView. */
  public final class IngredientViewHolder extends RecyclerView.ViewHolder {

    private ItemIngredientBinding itemIngredientBinding;

    public IngredientViewHolder(@NonNull ItemIngredientBinding itemIngredientBinding) {
      super(itemIngredientBinding.getRoot());
      this.itemIngredientBinding = itemIngredientBinding;
    }
  }
}
