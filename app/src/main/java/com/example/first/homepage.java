package com.example.first;

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

public class homepage extends AppCompatActivity {
    private ScrollView scrollView;
    private ConstraintLayout scrollContent;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amitabhanandpd.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        scrollView = findViewById(R.id.rootScrollView);
        scrollContent = findViewById(R.id.scrollContent);

        Button logoutButton = findViewById(R.id.logoutButton);

        // Check login state
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false);

        if (isLoggedIn) {
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            logoutButton.setVisibility(View.GONE);
        }

        // Fetch and display products
        fetchProducts();
    }
    private void fetchProducts() {
        Call<ProductListResponse> call = apiService.getAllProducts("VEX96Amitabh");
        call.enqueue(new retrofit2.Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, retrofit2.Response<ProductListResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<Product> products = response.body().getProducts();
                    displayProducts(products);
                } else {
                    Toast.makeText(homepage.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayProducts(List<Product> products) {
        try {
            int lastViewId = R.id.HeaderView; // Start adding views below HeaderView

            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);

                String productName = product.getName();
                double price = product.getPrice();
                String category1 = product.getCategory1();
                String category2 = product.getCategory2();
                String image1Path = product.getImage1Path();

                // Product Title
                TextView productTitle = new TextView(this);
                productTitle.setId(View.generateViewId());
                productTitle.setText(productName);
                productTitle.setTextSize(18);
                productTitle.setTypeface(null, Typeface.BOLD);

                // Product Price
                TextView productPrice = new TextView(this);
                productPrice.setId(View.generateViewId());
                productPrice.setText(String.format("$%.2f", price));
                productPrice.setTextSize(16);

                // Categories
                TextView productCategories = new TextView(this);
                productCategories.setId(View.generateViewId());
                productCategories.setText(String.format("%s | %s", category1, category2));
                productCategories.setTextSize(14);

                // Image (if available)
                ImageView productImage = null;
                if (image1Path != null) {
                    productImage = new ImageView(this);
                    productImage.setId(View.generateViewId());
                    Glide.with(this).load(image1Path).into(productImage);
                    scrollContent.addView(productImage);
                    productImage.setOnClickListener(v -> {
                        //Toast.makeText(homepage.this, "Product_id: "+product.get_id(), Toast.LENGTH_SHORT).show();
                        //productClick(product.get_id());
                        int productId = product.getId();
                        Intent intent = new Intent(homepage.this, product_page.class);
                        intent.putExtra("product_id", productId);
                        startActivity(intent);
                    });
                    // Set constraints for the image
                    ConstraintLayout.LayoutParams imageParams = new ConstraintLayout.LayoutParams(
                            200, 200 // Width and height for the image
                    );
                    imageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                    imageParams.topToBottom = lastViewId;
                    imageParams.setMargins(16, 16, 16, 0);
                    productImage.setLayoutParams(imageParams);
                    lastViewId = productImage.getId(); // Update lastViewId
                }

                // Add Title
                scrollContent.addView(productTitle);
                ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                titleParams.topToBottom = lastViewId;
                titleParams.setMargins(16, 8, 16, 0);
                productTitle.setLayoutParams(titleParams);
                lastViewId = productTitle.getId();

                // Add Price
                scrollContent.addView(productPrice);
                ConstraintLayout.LayoutParams priceParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                priceParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                priceParams.topToBottom = lastViewId;
                priceParams.setMargins(16, 4, 16, 0);
                productPrice.setLayoutParams(priceParams);
                lastViewId = productPrice.getId();

                // Add Categories
                scrollContent.addView(productCategories);
                ConstraintLayout.LayoutParams categoryParams = new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );
                categoryParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                categoryParams.topToBottom = lastViewId;
                categoryParams.setMargins(16, 4, 16, 16);
                productCategories.setLayoutParams(categoryParams);
                lastViewId = productCategories.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productClick(int product_id){
        Intent intent = new Intent(this, product_page.class);
        intent.putExtra("product_id", product_id);
        startActivity(intent);
    }

    public void showSidebar(View v) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }

    public void loginPage(View v) {
        startActivity(new Intent(this, login.class));
    }
}
