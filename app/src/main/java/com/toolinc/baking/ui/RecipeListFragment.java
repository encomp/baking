package com.toolinc.baking.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toolinc.baking.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Renders a specific {@link java.util.List} of recipes. */
public final class RecipeListFragment extends Fragment {

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
    ButterKnife.bind(rootView);

    return rootView;
  }
}
