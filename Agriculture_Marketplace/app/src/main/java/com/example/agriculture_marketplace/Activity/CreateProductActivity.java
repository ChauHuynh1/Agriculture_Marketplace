package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agriculture_marketplace.Activity.Dialog.ForumImageUrlDialog;
import com.example.agriculture_marketplace.Config.ProductConfig;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Helpers.UserManager;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Product.ProductRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.databinding.CreateProductBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CreateProductActivity extends AppCompatActivity {
    private CreateProductBinding binding;
    private String imageUrl;

    private static final String TAG = "[CreateProductActivity]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }

    private void init() {
        binding.createProductChooseImage.setOnClickListener(v -> {
            ForumImageUrlDialog dialog = new ForumImageUrlDialog(this, imageUrl -> {
                renderImage(imageUrl);
                this.imageUrl = imageUrl;
            });
            dialog.show();
        });
        binding.createProductCreateButton.setOnClickListener(v -> {
            createProduct();
        });
        renderSpinner();
    }
    private void renderImage(String imageUrl){
        //? Consider changing to allow user to upload image from their device
        RenderImageHelper.renderImage(imageUrl,binding.createProductImage);
    }
    private void createProduct() {
        String name = binding.createProductName.getText().toString();
        String description = binding.createProductDescription.getText().toString();
        String price = binding.createProductPrice.getText().toString();
        String category = binding.createProductCategorySpinner.getSelectedItem().toString();
        String quantity = binding.createProductUnit.getText().toString();
        String userId = UserManager.getInstance().getCurrentUser().getId();
        Product product = new Product(null, name, description, price, imageUrl, userId, category, quantity);
        Log.d(TAG, "createProduct: " + product.toString());
        ProductRepository productRepository = new ProductRepository();
        productRepository.saveProductToFirebase(product).thenAccept(aVoid -> {
            Log.d(TAG, "createProduct: success");
            Intent intent = new Intent(this, MyProductActivity.class);
            setResult( RESULT_OK, intent);
            finish();
        }).exceptionally(throwable -> {
            Log.d(TAG, "createProduct: failed");
            return null;
        });

    }
    private void renderSpinner(){
        String[] category = ProductConfig.PRODUCT_CATEGORY_LIST;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, category);
        binding.createProductCategorySpinner.setAdapter(adapter);
        binding.createProductCategorySpinner.setSelection(0);
    }

}
