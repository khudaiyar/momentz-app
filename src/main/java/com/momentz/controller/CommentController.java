package com.momentz.controller;

import com.momentz.dto.CommentRequest;
import com.momentz.dto.CommentResponse;
import com.momentz.model.Comment;
import com.momentz.model.Post;
import com.momentz.model.User;
import com.momentz.repository.CommentRepository;
import com.momentz.repository.PostRepository;
import com.momentz.repository.UserRepository;
import com.momentz.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setUser(user);

        commentRepository.save(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        return ResponseEntity.ok(convertToResponse(comment));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        List<CommentResponse> commentResponses = comments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentResponses);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("You can only delete your own comments");
        }

        Post post = comment.getPost();
        commentRepository.delete(comment);

        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);

        return ResponseEntity.ok("Comment deleted successfully");
    }

    private CommentResponse convertToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setUsername(comment.getUser().getUsername());
        response.setUserProfilePicture(comment.getUser().getProfilePicture());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
