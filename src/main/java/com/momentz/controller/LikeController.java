package com.momentz.controller;

import com.momentz.model.Like;
import com.momentz.model.Post;
import com.momentz.model.User;
import com.momentz.repository.LikeRepository;
import com.momentz.repository.PostRepository;
import com.momentz.repository.UserRepository;
import com.momentz.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (likeRepository.existsByPostAndUser(post, user)) {
            return ResponseEntity.badRequest().body("Post already liked");
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        return ResponseEntity.ok("Post liked successfully");
    }

    @DeleteMapping("/post/{postId}")
    @Transactional
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!likeRepository.existsByPostAndUser(post, user)) {
            return ResponseEntity.badRequest().body("Post not liked yet");
        }

        likeRepository.deleteByPostAndUser(post, user);

        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);

        return ResponseEntity.ok("Post unliked successfully");
    }
}