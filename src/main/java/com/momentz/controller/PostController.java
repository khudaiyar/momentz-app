package com.momentz.controller;

import com.momentz.dto.PostRequest;
import com.momentz.dto.PostResponse;
import com.momentz.model.Post;
import com.momentz.model.User;
import com.momentz.repository.LikeRepository;
import com.momentz.repository.PostRepository;
import com.momentz.repository.UserRepository;
import com.momentz.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    // ---------------- CREATE POST ----------------
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setCaption(postRequest.getCaption());
        post.setImageUrl(postRequest.getImageUrl());
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        postRepository.flush();
        Post refreshedPost = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new RuntimeException("Post not found after save"));

        return ResponseEntity.ok(convertToResponse(refreshedPost, user));
    }

    // ---------------- FOLLOWING FEED ----------------
    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> followingUsers = new ArrayList<>(currentUser.getFollowing());
        followingUsers.add(currentUser);

        Page<Post> postsPage = postRepository.findFeedPosts(followingUsers, PageRequest.of(page, size));
        List<PostResponse> posts = postsPage.getContent().stream()
                .map(post -> convertToResponse(post, currentUser))
                .collect(Collectors.toList());

        return ResponseEntity.ok(posts);
    }

    // ---------------- USER POSTS ----------------
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserPosts(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponse> postResponses = posts.stream()
                .map(post -> convertToResponse(post, user))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
    }

    // ---------------- SINGLE POST ----------------
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(convertToResponse(post, currentUser));
    }

    // ---------------- DELETE POST ----------------
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("You can only delete your own posts");
        }

        postRepository.delete(post);
        return ResponseEntity.ok("Post deleted successfully");
    }

    // ---------------- PUBLIC FEED ----------------
    @GetMapping("/all")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            User currentUser = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Post> postsPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

            List<PostResponse> posts = postsPage.getContent().stream()
                    .map(post -> mapToPostResponse(post, currentUser))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(posts);

        } catch (Exception e) {
            logger.error("Error getting all posts: ", e);
            return ResponseEntity.ok(List.of());
        }
    }

    // ---------------- HELPERS ----------------
    private PostResponse convertToResponse(Post post, User currentUser) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setCaption(post.getCaption());
        response.setImageUrl(post.getImageUrl());
        response.setUsername(post.getUser().getUsername());
        response.setUserProfilePicture(post.getUser().getProfilePicture());
        response.setLikesCount(post.getLikesCount());
        response.setCommentsCount(post.getCommentsCount());
        response.setCreatedAt(post.getCreatedAt());
        response.setLikedByCurrentUser(likeRepository.existsByPostAndUser(post, currentUser));
        return response;
    }

    private PostResponse mapToPostResponse(Post post, User currentUser) {
        return convertToResponse(post, currentUser);
    }
}
