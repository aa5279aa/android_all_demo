package com.xt.client.function.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("s?wd=android")
    Call<ResponseBody> getAndroidResult();
}
