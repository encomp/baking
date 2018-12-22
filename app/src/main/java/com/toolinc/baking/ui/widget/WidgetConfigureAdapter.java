package com.toolinc.baking.ui.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigureAdapter
    extends RecyclerView.Adapter<WidgetConfigureAdapter.RecipesViewHolder> {

  public interface RecipeClickListener {
    void onItemClick(Recipe recipe);
  }

  @NonNull
  @Override
  public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {}

  @Override
  public int getItemCount() {
    return 0;
  }

  public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textStart)
    TextView recipeNameTextView;

    RecipesViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      int adapterPosition = getAdapterPosition();
    }
  }
}
