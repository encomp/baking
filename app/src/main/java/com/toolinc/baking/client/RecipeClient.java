package com.toolinc.baking.client;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.client.model.Recipes;
import com.toolinc.baking.test.BakingIdlingResource;

import javax.inject.Inject;
import javax.inject.Provider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/** Performs a Rest API call to extract a {@link ImmutableList} of {@link Recipe}. */
public final class RecipeClient implements Callback<Recipes> {

  private final Provider<BakingClient> bakingClient;
  private Optional<BakingIdlingResource> bakingIdlingResource = Optional.absent();
  private RecipeCallback recipeCallback;
  private Call<Recipes> recipesCall;

  @Inject
  public RecipeClient(Provider<BakingClient> bakingClient) {
    this.bakingClient = checkNotNull(bakingClient, "BakingClient is missing.");
  }

  public void fetchMovies(
      RecipeCallback recipeCallback, Optional<BakingIdlingResource> bakingIdlingResource) {
    this.recipeCallback = checkNotNull(recipeCallback, "RecipeCallback is missing.");
    if (bakingIdlingResource.isPresent()) {
      this.bakingIdlingResource = bakingIdlingResource;
      this.bakingIdlingResource.get().idle();
    }
    if (Optional.fromNullable(recipesCall).isPresent()) {
      recipesCall.cancel();
    }
    recipesCall = bakingClient.get().recipes();
    recipesCall.enqueue(this);
  }

  @Override
  public void onResponse(Call<Recipes> call, Response<Recipes> response) {
    if (bakingIdlingResource.isPresent()) {
      bakingIdlingResource.get().completed();
    }
    recipeCallback.onSuccess(response.body().recipes());
  }

  @Override
  public void onFailure(Call<Recipes> call, Throwable t) {
    if (bakingIdlingResource.isPresent()) {
      bakingIdlingResource.get().completed();
    }
    recipeCallback.onFailure(t.getMessage());
  }

  /** Callback that is execute upon a request to retrieve a list of {@link Recipe}. */
  public interface RecipeCallback {

    /**
     * Provides the {@link ImmutableList} of {@link Recipe} if the request was successful.
     *
     * @param recipes the {@link ImmutableList} of recipes retrieved.
     */
    void onSuccess(ImmutableList<Recipe> recipes);

    /**
     * Provides the error message if a request was unsuccessful.
     *
     * @param message the error message returned.
     */
    void onFailure(String message);
  }
}
