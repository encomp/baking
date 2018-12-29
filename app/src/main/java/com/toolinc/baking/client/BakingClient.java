package com.toolinc.baking.client;

import com.toolinc.baking.client.model.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Specifies the contract of the information that will be retrieve information from the recipe api
 */
public interface BakingClient {

  /**
   * Retrieve a collection of recipes from the back end.
   *
   * @return a new instance of {@link Recipes}.
   */
  @GET("2017/May/59121517_baking/baking.json")
  Call<Recipes> recipes();
}
