package com.example.agriculture_marketplace.Activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agriculture_marketplace.Helpers.OnProductClickListener;
import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Product.ProductRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.Rating.Repository.ProductRatingRepository;
import com.example.agriculture_marketplace.databinding.BrowseProductBinding;

import java.util.ArrayList;

public class MyProductActivity extends AppCompatActivity implements OnProductClickListener {
    BrowseProductBinding binding;
    private ProductRepository productRepository = new ProductRepository();
    private static final String TAG = "[MyProductActivity]";
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BrowseProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        someActivityResultLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                        init();
                    }
                });


    }
    private void init() {
        productRepository.getAllProducts().thenAccept(products -> {
            renderList(products);
            String result = products.size() + "results";
            binding.browseProductResultAmount.setText(result);
            binding.browseProductAddButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, CreateProductActivity.class);
                someActivityResultLauncher.launch(intent);
            });
        });
    }
    private void renderList(ArrayList<Product> products) {
        MyProductAdapter productAdapter = new MyProductAdapter(this, products, this);
        binding.browseProductListView.setAdapter(productAdapter);
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, UpdateProductActivity.class);
        intent.putExtra("product", product);
        someActivityResultLauncher.launch(intent);
    }

    public class MyProductAdapter extends BaseAdapter {
        private ArrayList<Product> products;
        private final AppCompatActivity activity;
        private OnProductClickListener listener;
        public MyProductAdapter(AppCompatActivity activity, ArrayList<Product> products, OnProductClickListener listener) {
            this.activity = activity;
            this.products = products;
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int position) {
            return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view==null){
                view = activity.getLayoutInflater().inflate(R.layout.list_item_product, null);
            }
            Product product = products.get(position);
            ImageView imageView = view.findViewById(R.id.list_item_product_image);
            RenderImageHelper.renderImage(product.getImage(), imageView);
            TextView nameTextView = view.findViewById(R.id.list_item_product_name);
            nameTextView.setText(product.getName());
            TextView priceTextView = view.findViewById(R.id.list_item_product_price);
            String price = product.getPrice() + " $";
            priceTextView.setText(price);
            TextView quantityTextView = view.findViewById(R.id.list_item_product_quantity);
            quantityTextView.setText(product.getQuantity());
            TextView ratingTextView = view.findViewById(R.id.list_item_product_rating);
            TextView amountTextView = view.findViewById(R.id.list_item_product_rating_amount);
            ProductRatingRepository productRatingRepository = new ProductRatingRepository();
            productRatingRepository.getProductRatingAndAmount(product.getId()).thenAccept(result -> {
                ratingTextView.setText(result.get(0));
                String amount = " (" + result.get(1) + "ratings )";
                amountTextView.setText(amount);
            });
            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
            return view;
        }
    }
}
