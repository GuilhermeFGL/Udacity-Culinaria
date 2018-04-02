package com.guilhermefgl.icook.views.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.views.recipe.RecipeActivity;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        views.setTextViewText(R.id.widget_title, context.getString(R.string.widget_default_title));
        views.setEmptyView(R.id.widget_recipe_layout, R.id.widget_empty_layout);
        views.setRemoteAdapter(R.id.widget_recipe_layout, new Intent(context, ListWidgetService.class));

        Bundle extras = new Bundle();
        extras.putParcelable(RecipeActivity.BUNDLE_RECIPE, null);
        Intent appIntent = new Intent(context, RecipeActivity.class);
        appIntent.putExtras(extras);
        PendingIntent appPendingIntent = PendingIntent
                .getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_recipe_layout, appPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}

