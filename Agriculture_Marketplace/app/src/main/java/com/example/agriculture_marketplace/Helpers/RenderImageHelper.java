package com.example.agriculture_marketplace.Helpers;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
public class RenderImageHelper {
    public static void renderImage(String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "https://t3.ftcdn.net/jpg/02/96/26/82/360_F_296268274_ytY0Jo0RFnJip1M2KSE9SXzC4Hw7m9kb.jpg";
        }
        imageView.setImageResource(android.R.color.transparent);
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerCrop()
                .into(imageView);
    }
}
