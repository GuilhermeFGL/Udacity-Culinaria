package com.guilhermefgl.icook.views.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.models.entitys.Recipe;
import com.guilhermefgl.icook.services.tasks.SelectCurrentWidgetRecipe;
import com.guilhermefgl.icook.views.recipe.RecipeActivity;

public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static final int INTENT_REQUEST_CODE = 0;

    public static void update(Context context) {
        Intent intent = new Intent(context, IngredientsWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, IngredientsWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(final Context context,
                         final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        new SelectCurrentWidgetRecipe(context, new SelectCurrentWidgetRecipe.SelectRecipeCallBack() {
            @Override
            public void onSelect(Recipe recipe) {
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
                }
            }
        }).execute();
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        views.setEmptyView(R.id.widget_recipe_list, R.id.widget_empty_layout);
        views.setRemoteAdapter(R.id.widget_recipe_list,
                new Intent(context, ListWidgetFactoryService.class));
        if (recipe != null) {
            views.setTextViewText(R.id.widget_title, recipe.getName());
        }

        Bundle extras = new Bundle();
        extras.putParcelable(RecipeActivity.BUNDLE_RECIPE, recipe);
        Intent appIntent = new Intent(context, RecipeActivity.class);
        appIntent.putExtras(extras);
        PendingIntent appPendingIntent = PendingIntent
                .getActivity(context, INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_recipe_layout, appPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_list);
    }
}

