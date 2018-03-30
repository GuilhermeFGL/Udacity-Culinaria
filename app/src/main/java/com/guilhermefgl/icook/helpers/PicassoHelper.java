package com.guilhermefgl.icook.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.guilhermefgl.icook.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoHelper {

    private PicassoHelper() { }

    public static void loadImage(Context context, String url, final ImageView imageView) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.icon_placeholder)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() { }

                    @Override
                    public void onError() {
                        imageView.setImageResource(R.drawable.icon_connection_error);
                    }
                });
    }
}
