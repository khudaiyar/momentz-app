package com.momentz.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final OkHttpClient client = new OkHttpClient();

    // Example: Fetch all rows from "posts" table
    public String getPosts() throws Exception {
        String url = supabaseUrl + "/rest/v1/posts?select=*";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed: " + response);
            }
            return response.body().string();
        }
    }

    // Example: Insert a new post
    public String addPost(String title, String content) throws Exception {
        String url = supabaseUrl + "/rest/v1/posts";
        String json = "{\"title\": \"" + title + "\", \"content\": \"" + content + "\"}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .addHeader("Prefer", "return=representation")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed: " + response);
            }
            return response.body().string();
        }
    }
}
