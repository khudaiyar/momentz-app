package com.momentz.controller;

import com.momentz.service.SupabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupabaseController {

    private final SupabaseService supabaseService;

    public SupabaseController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @GetMapping("/supabase/posts")
    public String getPosts() throws Exception {
        return supabaseService.getPosts();
    }
}
