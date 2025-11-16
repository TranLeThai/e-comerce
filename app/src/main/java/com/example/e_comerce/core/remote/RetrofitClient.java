// core/data/remote/RetrofitClient.java
package com.example.e_comerce.core.remote;

import com.example.e_comerce.core.remote.api.FoodApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // THAY ĐỔI BASE URL THEO SERVER CỦA BẠN
    private static final String BASE_URL = "http://10.0.2.2:3000/"; // Dùng cho Android Emulator
    // Nếu dùng thiết bị thật: http://192.168.x.x:3000/
    // Nếu dùng server thật: https://your-api.com/

    private static Retrofit retrofit = null;

    public static FoodApiService getApiService() {
        if (retrofit == null) {
            // Tạo logging để xem request/response
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(FoodApiService.class);
    }
}