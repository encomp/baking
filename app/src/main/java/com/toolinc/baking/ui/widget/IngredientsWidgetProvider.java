package com.toolinc.baking.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.toolinc.baking.R;
import com.toolinc.baking.client.model.Recipe;
import com.toolinc.baking.ui.RecipeDetailActivity;

/**
 * Display a {@link com.google.common.collect.ImmutableList} of {@link
 * com.toolinc.baking.client.model.Step}.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

  static void updateAppWidget(
      Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {

    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(Intent.EXTRA_KEY_EVENT, recipe);
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Construct the RemoteViews object
    RemoteViews views =
        new RemoteViews(context.getPackageName(), R.layout.widget_provider_ingredients);
    views.setTextViewText(R.id.tv_recipe_name, recipe.name());
    views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {}

  @Override
  public void onEnabled(Context context) {}

  @Override
  public void onDisabled(Context context) {}
}
