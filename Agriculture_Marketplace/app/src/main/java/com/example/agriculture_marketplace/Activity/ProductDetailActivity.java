package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.MainActivity;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Product.ProductRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.databinding.ProductDetailBinding;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductDetailBinding binding;
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        init();
    }

    private void init() {
        RenderImageHelper.renderImage(product.getImage(), binding.productImage);
        binding.productPrice.setText(product.getPrice()+"$");
        binding.productName.setText(product.getName());
        binding.productDetailDescription.setText(product.getDescription());
        binding.productDetailQuantity.setText(product.getQuantity());
        binding.selectedQuantity.setText("1");
        binding.addProduct.setOnClickListener(v -> {
            addProductQuantity();
        });
        binding.removeProduct.setOnClickListener(v -> {
            subtractProductQuantity();
        });
        binding.totalMoney.setText(product.getPrice()+"$");
        binding.buyProduct.setOnClickListener(v -> {
            addProductToCart();
        });
        binding.productDetailMore.setText(product.getDescription());
    }
    private void addProductQuantity() {
        int quantity = Integer.parseInt(binding.selectedQuantity.getText().toString());
        if (quantity >= Integer.parseInt(product.getQuantity())) {
            return;
        }
        quantity++;
        binding.totalMoney.setText(String.valueOf(quantity * Double.parseDouble(product.getPrice())));
        binding.selectedQuantity.setText(String.valueOf(quantity));
    }
    private void subtractProductQuantity() {
        int quantity = Integer.parseInt(binding.selectedQuantity.getText().toString());
        if (quantity > 1) {
            quantity--;
            binding.totalMoney.setText(String.valueOf(quantity * Double.parseDouble(product.getPrice())));
            binding.selectedQuantity.setText(String.valueOf(quantity));
        }
    }

    private void addProductToCart() {
        //TODO: Switch to rating activity
        int buy_quantity = Integer.parseInt(binding.selectedQuantity.getText().toString());
        product.setQuantity(String.valueOf(Integer.parseInt(product.getQuantity()) - buy_quantity));
        ProductRepository productRepository = new ProductRepository();
        productRepository.updateProduct(product).thenAccept(product -> {
            Intent intent = new Intent(this, RatingProductActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
            finish();
        });
    }
}
