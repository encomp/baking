package com.toolinc.baking.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.toolinc.baking.R;
import com.toolinc.baking.client.RecipeClient;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.RecipeDetailActivity;

import java.util.Random;

/**
 * Display a {@link com.google.common.collect.ImmutableList} of {@link
 * com.toolinc.baking.client.model.Step}.
 */
public final class IngredientsWidgetProvider extends AppWidgetProvider
    implements RecipeClient.RecipeCallback {

  private final RecipeClient recipeClient = new RecipeClient(this);

  private Context context;

  static void updateAppWidget(
      Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {

    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(Intent.EXTRA_KEY_EVENT, recipe);
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    RemoteViews views =
        new RemoteViews(context.getPackageName(), R.layout.widget_provider_ingredients);
    views.setTextViewText(R.id.tv_recipe_name, recipe.name());
    views.setTextViewText(R.id.tv_ingredients, recipe.getIngredientsString());
    views.setOnClickPendingIntent(R.id.tv_ingredients, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    this.context = context;
    recipeClient.fetchMovies(Optional.absent());
  }

  @Override
  public void onEnabled(Context context) {}

  @Override
  public void onDisabled(Context context) {}

  @Override
  public void onSuccess(ImmutableList<Recipe> recipes) {
    AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
    int[] widgetIds =
        widgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidgetProvider.class));

    Random random = new Random();
    int index = random.nextInt(recipes.size());
    for (int appWidgetId : widgetIds) {
      updateAppWidget(context, widgetManager, appWidgetId, recipes.get(index));
    }
  }

  @Override
  public void onFailure(String message) {
    Log.e("Widget", message);
  }
}
