package com.example.first;

import android.app.Activity;
import android.content.*;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class product_page extends AppCompatActivity {
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amitabhanandpd.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        Intent intent = getIntent();
        int product_id = intent.getIntExtra("product_id", -1);
        Call<Product> call = apiService.getProduct(product_id, "VEX96Amitabh");
        call.enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, retrofit2.Response<Product> response) {
                if (response.code() == 200 && response.body() != null) {
                    Product product = response.body();
                    try {
                        set_product(product);
                    } catch (Exception e) {
                        Toast.makeText(product_page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(product_page.this, "Failed to fetch product", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(product_page.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void set_product(Product product){
        ImageView productImage = findViewById(R.id.product_image);
        TextView productName = findViewById(R.id.product_name);
        TextView productPrice = findViewById(R.id.product_price);
        RadioGroup sizeSelection = findViewById(R.id.size_selection);
        Button buyNowButton = findViewById(R.id.buy_now_button);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);

        productName.setText(product.getName());
        productPrice.setText("Rs. "+product.getPrice());
        Glide.with(this).load(product.getImage1Path()).into(productImage);

        buyNowButton.setOnClickListener(v -> {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        addToCartButton.setOnClickListener(v -> {
            Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        // Handle size selection
        sizeSelection.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedSize = "";
            if (checkedId == R.id.size_small) {
                selectedSize = "Small";
            } else if (checkedId == R.id.size_medium) {
                selectedSize = "Medium";
            } else if (checkedId == R.id.size_large) {
                selectedSize = "Large";
            }
            Toast.makeText(this, "Selected Size: " + selectedSize, Toast.LENGTH_SHORT).show();
        });
    }

}
