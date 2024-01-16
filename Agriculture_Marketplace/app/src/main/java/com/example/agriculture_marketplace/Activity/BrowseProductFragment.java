package com.example.agriculture_marketplace.Activity;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
    //TODO: Fix this
    private void renderList(ArrayList<Product> products) {
//        ProductAdapter productAdapter = new ProductAdapter(products);
//        binding.browseProductListView.setAdapter(productAdapter);
    }
    //TODO: Fix this
//    public class ProductApdapter extends BaseAdapter {
//        private ArrayList<Product> products;
//        private Context context;
//        public ProductApdapter(ArrayList<Product> products) {
//            this.products = products;
//        }
//        @Override
//        public int getCount() {
//            return products.size();
//        }
//        @Override
//        public Object getItem(int position) {
//            return products.get(position);
//        }
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//        @Override
//        public View getView(int position, android.view.View convertView, ViewGroup parent) {
////            LayoutInflater layoutInflater = LayoutInflater.from(context);
////            View view = layoutInflater.inflate(R.layout.list_item_forum, parent, false);
////            TextView nameTextView = view.findViewById(R.id.product_list_item_name);
////            TextView priceTextView = view.findViewById(R.id.product_list_item_price);
////            TextView quantityTextView = view.findViewById(R.id.product_list_item_quantity);
////            TextView descriptionTextView = view.findViewById(R.id.product_list_item_description);
////            ImageView imageView = view.findViewById(R.id.product_list_item_image);
////            Product product = products.get(position);
////            nameTextView.setText(product.getName());
////            priceTextView.setText(product.getPrice());
////            quantityTextView.setText(product.getQuantity());
////            descriptionTextView.setText(product.getDescription());
////            RenderImageHelper.renderImage(product.getImage(), imageView);
////            view.setOnClickListener(v -> {
////                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
////                intent.putExtra("product", product);
////                startActivity(intent);
////            });
//            return view;
//        }
//    }
}
