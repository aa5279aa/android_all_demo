package com.xt.client.function.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://www.baidu.com")
            .addConverterFactory(GsonConverterFactory.create())//这行代码是增加Gson的转换方法，可以直接把请求到的结果转换成Bean(根据需要，可以删除)
            .build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}
