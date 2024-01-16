package com.example.agriculture_marketplace.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Rating.Model.ProductRating;
import com.example.agriculture_marketplace.Rating.Repository.ProductRatingRepository;
import com.example.agriculture_marketplace.databinding.RatingProductBinding;

public class RatingProductActivity extends AppCompatActivity {
    RatingProductBinding binding;
    Float rating;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RatingProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        product = (Product) getIntent().getSerializableExtra("product");
        init();

    }
    private void init() {
        binding.simpleRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
        });
        binding.ratingItemProductName.setText(product.getName());
        binding.ratingItemProductPrice.setText(product.getPrice()+"$");
        binding.submitRating.setOnClickListener(v -> {
            submitRating();
        });
        RenderImageHelper.renderImage(product.getImage(),binding.ratingProductImage );
    }
    private void submitRating() {
        ProductRatingRepository productRatingRepository = new ProductRatingRepository();
        ProductRating productRating = new ProductRating(UserManager.getInstance().getCurrentUser().getId(), product.getId(), String.valueOf(rating), binding.ratingItemProductDescription.getText().toString());
    }
}
