package com.toolinc.baking.client;

import com.google.gson.GsonBuilder;
import com.toolinc.baking.BuildConfig;
import com.toolinc.baking.client.model.Recipes;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Specifies the contract of the information that will be retrieve information from the recipe api
 */
public interface BakingClient {

  static final GsonBuilder gsonBuilder =
      new GsonBuilder().registerTypeAdapter(Recipes.class, Recipes.builder());
  static final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
  static final Retrofit retrofitClient =
      new Retrofit.Builder()
          .baseUrl(BuildConfig.END_POINT)
          .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
          .client(okHttpClientBuilder.build())
          .build();

  static BakingClient create() {
    return retrofitClient.create(BakingClient.class);
  }

  @GET("2017/May/59121517_baking/baking.json")
  Call<Recipes> recipes();
}
