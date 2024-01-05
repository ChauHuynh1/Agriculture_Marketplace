package com.example.agriculture_marketplace.Helpers;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
public class RenderImageHelper {
    public static void renderImage(String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        imageView.setImageResource(android.R.color.transparent);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(imageView);
    }
}
