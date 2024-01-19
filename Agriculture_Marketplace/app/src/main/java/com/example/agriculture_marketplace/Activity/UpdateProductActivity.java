package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Activity.Dialog.ForumDeleteConfirmDialog;
import com.example.agriculture_marketplace.Activity.Dialog.ForumImageUrlDialog;
import com.example.agriculture_marketplace.Config.ProductConfig;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.MainActivity;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Product.ProductRepository;
import com.example.agriculture_marketplace.databinding.UpdateProductBinding;

public class UpdateProductActivity extends AppCompatActivity {
    private UpdateProductBinding binding;
    private Product product;
    private String imageUrl;
    private static final String TAG = "[UpdateProductActivity]";
    private final ProductRepository productRepository = new ProductRepository();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdateProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        init();
    }

    private void init(){
        binding.updateProductName.setText(product.getName());
        binding.updateProductDescription.setText(product.getDescription());
        binding.updateProductPrice.setText(String.valueOf(product.getPrice()));
        binding.updateProductQuantity.setText(String.valueOf(product.getQuantity()));
        binding.updateProductCreateButton.setOnClickListener(v -> {
            updateProduct();
        });
        binding.updateProductDeleteButton.setOnClickListener(v -> {
            deleteProduct();
        });
        if(product.getImage() != null){
            imageUrl = product.getImage();
            renderImage(imageUrl);
        }
        else{
            imageUrl = "";
        }
        binding.updateProductChooseImage.setOnClickListener(v -> {
            ForumImageUrlDialog dialog = new ForumImageUrlDialog(this, imageUrl -> {
                this.imageUrl = imageUrl;
                RenderImageHelper.renderImage(imageUrl, binding.updateProductImage);
            });
            dialog.show();
        });
        renderSpinner();
    }
    private void updateProduct() {
        String name = binding.updateProductName.getText().toString();
        String description = binding.updateProductDescription.getText().toString();
        String price = binding.updateProductPrice.getText().toString();
        String quantity = binding.updateProductQuantity.getText().toString();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setImage(imageUrl);
        productRepository.updateProduct(product).thenAccept(product -> {
            Intent intent = new Intent(this, MyProductActivity.class);
            Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, intent);
            finish();
        });
    }
    private void deleteProduct() {
        ForumDeleteConfirmDialog dialog = new ForumDeleteConfirmDialog(this, "Are you sure you want to delete this product?", () -> {
            productRepository.deleteProduct(product.getId()).thenAccept(product -> {
                Intent intent = new Intent(this, MyProductActivity.class);
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            });
        });
        dialog.show();
    }
    private void renderImage(String imageUrl){
        //? Consider changing to allow user to upload image from their device
        RenderImageHelper.renderImage(imageUrl,binding.updateProductImage);
    }
    private void renderSpinner(){
        String[] categories = ProductConfig.PRODUCT_CATEGORY_LIST;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        binding.updateProductCategorySpinner.setAdapter(adapter);
        binding.updateProductCategorySpinner.setSelection(adapter.getPosition(product.getCategory()));
    }

}
