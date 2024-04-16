package com.zjw.utils;

import okhttp3.*;

import java.io.IOException;

/**
 * @author 朱俊伟
 * @since 2024/04/13 07:54
 */
public class HttpUtil {

    private static final String token ;

    static {
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken == null) {
            throw new RuntimeException("GITHUB_TOKEN not found in environment variables");
        }
        token = "Bearer " + githubToken;
    }

    public static String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String result = "";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        result = response.body().string();
        return result;
    }
}
