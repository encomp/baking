package com.toolinc.baking.client.inject;

import com.google.gson.GsonBuilder;
import com.toolinc.baking.BuildConfig;
import com.toolinc.baking.client.BakingClient;
import com.toolinc.baking.client.RecipeClient;
import com.toolinc.baking.client.model.Recipes;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Specifies the object graph to create a {@link BakingClient} to perform Rest API calls. */
@Module
public class BakingClientModule {

  @Provides
  OkHttpClient.Builder providesOkHttpClientBuilder() {
    return new OkHttpClient.Builder();
  }

  @Provides
  GsonBuilder providesGsonBuilder() {
    return new GsonBuilder().registerTypeAdapter(Recipes.class, Recipes.builder());
  }

  @Provides
  GsonConverterFactory providesGsonConverterFactory(GsonBuilder gsonBuilder) {
    return GsonConverterFactory.create(gsonBuilder.create());
  }

  @Provides
  Retrofit providesRetrofit(
      GsonConverterFactory gsonConverterFactory, OkHttpClient.Builder okHttpClientBuilder) {
    return new Retrofit.Builder()
        .baseUrl(BuildConfig.END_POINT)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClientBuilder.build())
        .build();
  }

  @Provides
  BakingClient providesBakingClient(Retrofit retrofit) {
    return retrofit.create(BakingClient.class);
  }

  @Provides
  RecipeClient providesRecipeClient(Provider<BakingClient> bakingClientProvider) {
    return new RecipeClient(bakingClientProvider);
  }
}
