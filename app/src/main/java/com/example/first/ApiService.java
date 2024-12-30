package com.example.first;

import java.util.*;

import retrofit2.Call;
import retrofit2.http.*;

class LoginRequest {
    private String email;
    private String password;

    // Constructor
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters (Optional, if needed)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginResponse {
    private String message;
    private User user; // Include user details

    public LoginResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

class GetUser {
    private User user;
    public GetUser(User user) {this.user = user;}

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
class GetProduct {
    Product product;
    public GetProduct(Product product) {
        this.product = product;
    }
    public Product getProduct(){
        return this.product;
    }
    public void setProduct(Product product){
        this.product = product;
    }
    public String getProductString(){
        return this.product.toString();
    }

}
class UserListResponse {
    private List<User> users;
    public UserListResponse(List<User> users) {
        this.users = users;
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
class ProductListResponse{
    List<Product> products;
    public ProductListResponse(List<Product> products){
        this.products = products;
    }
    public List<Product> getProducts(){
        return this.products;
    }
    public void setProducts(List<Product> products){
        this.products = products;
    }
}

// Retrofit interface
public interface ApiService {
    @Headers({
            "X-Amitabh-Header: VEX96Amitabh",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @POST("api/users/login") // Adjust the endpoint based on your backend
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("api/users/get_user_id_by_email/{user_email}/{access_code}")
    Call<GetUser> getUserByEmail(@Path("user_email") String email, @Path("access_code") String access_code);

    @GET("api/users/{user_id}/{access_code}")
    Call<User> getUserById(@Path("user_id") int userId, @Path("access_code") String accessCode);

    @GET("api/users/{access_code}")
    Call<UserListResponse> getAllUsers(@Path("access_code") String accessCode);

    @GET("api/products/{access_code}")
    Call<ProductListResponse> getAllProducts(@Path("access_code") String accessCode);

    @GET("api/products/{product_id}/{access_code}")
    Call<GetProduct> getProductById(@Path("product_id") int productId, @Path("access_code") String accessCode);

    @GET("api/products/{id}/{access_code}")
    Call<Product> getProduct(@Path("id") int productId, @Path("access_code") String accessCode);
}
