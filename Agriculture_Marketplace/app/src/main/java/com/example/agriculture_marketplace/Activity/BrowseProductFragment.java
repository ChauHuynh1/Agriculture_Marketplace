package com.example.agriculture_marketplace.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.agriculture_marketplace.Helpers.RenderImageHelper;
import com.example.agriculture_marketplace.Product.Product;
import com.example.agriculture_marketplace.Product.ProductRepository;
import com.example.agriculture_marketplace.R;
import com.example.agriculture_marketplace.databinding.BrowseProductBinding;

import java.util.ArrayList;

public class BrowseProductFragment extends Fragment {
    private BrowseProductBinding binding;
    private ProductRepository productRepository = new ProductRepository();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.browse_product, container, false);
        binding = BrowseProductBinding.inflate(getLayoutInflater());

        return view;
    }
    private void init() {
        productRepository.getAllProducts().thenAccept(products -> {
            renderList(products);
        });

    }

    private void renderList(ArrayList<Product> products) {
     ProductAdapter productAdapter = new ProductAdapter(products);
     binding.browseProductListView.setAdapter(productAdapter);
    }
    public class ProductAdapter extends BaseAdapter {
        private ArrayList<Product> products;
        private Context context;
        public ProductAdapter(ArrayList<Product> products) {
            this.products = products;
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
        public View getView(int position,View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.list_item_product, parent, false);
                TextView nameTextView = view.findViewById(R.id.list_item_product_name);
                TextView priceTextView = view.findViewById(R.id.list_item_product_price);
                TextView quantityTextView = view.findViewById(R.id.list_item_product_quantity);
                ImageView imageView = view.findViewById(R.id.list_item_product_image);
                Product product = products.get(position);
                nameTextView.setText(product.getName());
                priceTextView.setText(product.getPrice());
                quantityTextView.setText(product.getQuantity());
                RenderImageHelper.renderImage(product.getImage(), imageView);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("product", product);
                    startActivity(intent);
                });
            }
            return view;
        }
    }
}
