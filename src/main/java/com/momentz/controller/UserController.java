package com.momentz.controller;

import com.momentz.dto.UserProfileResponse;
import com.momentz.model.User;
import com.momentz.repository.PostRepository;
import com.momentz.repository.UserRepository;
import com.momentz.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponse profile = new UserProfileResponse();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setFullName(user.getFullName());
        profile.setBio(user.getBio());
        profile.setProfilePicture(user.getProfilePicture());
        profile.setWebsite(user.getWebsite());
        profile.setPostsCount(postRepository.countByUser(user));
        profile.setFollowersCount((long) user.getFollowers().size());
        profile.setFollowingCount((long) user.getFollowing().size());
        profile.setIsPrivate(user.getIsPrivate());
        profile.setIsVerified(user.getIsVerified());

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileResponse profile = new UserProfileResponse();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getFullName());
        profile.setBio(user.getBio());
        profile.setProfilePicture(user.getProfilePicture());
        profile.setWebsite(user.getWebsite());
        profile.setPostsCount(postRepository.countByUser(user));
        profile.setFollowersCount((long) user.getFollowers().size());
        profile.setFollowingCount((long) user.getFollowing().size());
        profile.setIsPrivate(user.getIsPrivate());
        profile.setIsVerified(user.getIsVerified());

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<User> users = userRepository.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User userToFollow = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(userToFollow);

        return ResponseEntity.ok("Followed successfully");
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User currentUser = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User userToUnfollow = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(currentUser);

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);

        return ResponseEntity.ok("Unfollowed successfully");
    }
}