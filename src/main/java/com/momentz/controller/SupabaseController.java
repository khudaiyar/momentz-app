package com.momentz.controller;

import com.momentz.service.SupabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/supabase")
public class SupabaseController {

    private final SupabaseService supabaseService;

    public SupabaseController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ SupabaseController loaded successfully!");
    }

    @GetMapping("/posts")
    public String getPosts() throws Exception {
        return supabaseService.getPosts();
    }
}
