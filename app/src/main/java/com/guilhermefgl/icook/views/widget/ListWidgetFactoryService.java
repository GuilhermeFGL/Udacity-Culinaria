package com.guilhermefgl.icook.views.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.guilhermefgl.icook.R;
import com.guilhermefgl.icook.models.DataBase;
import com.guilhermefgl.icook.models.entitys.Ingredient;

import java.util.List;

public class ListWidgetFactoryService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this.getApplicationContext());
    }

    class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final Context mContext;
        private List<Ingredient> mIngredients;

        IngredientRemoteViewsFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() { }

        @Override
        public void onDataSetChanged() {
            mIngredients = DataBase.getInstance(getApplicationContext()).ingredientDao().getAll();
        }

        @Override
        public void onDestroy() {
            mIngredients = null;
        }

        @Override
        public int getCount() {
            return mIngredients != null ? mIngredients.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Ingredient ingredient = mIngredients.get(position);
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_ingredient);
            view.setTextViewText(R.id.widget_item_ingredient_name, ingredient.getIngredient());
            view.setTextViewText(R.id.widget_item_ingredient_measure,
                    mContext.getString(R.string.recipe_ingredient_list_quantity,
                            String.valueOf(ingredient.getQuantity()),
                            ingredient.getMeasure()));
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
