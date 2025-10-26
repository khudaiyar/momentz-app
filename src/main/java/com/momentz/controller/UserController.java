package com.momentz.controller;

import com.momentz.dto.MessageResponse;
import com.momentz.dto.UserProfileResponse;
import com.momentz.dto.UserUpdateRequest;
import com.momentz.model.User;
import com.momentz.repository.PostRepository;
import com.momentz.repository.UserRepository;
import com.momentz.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateProfile(
            @RequestBody UserUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (request.getFullName() != null) {
                user.setFullName(request.getFullName().trim());
            }

            if (request.getBio() != null) {
                user.setBio(request.getBio().trim());
            }

            if (request.getWebsite() != null && !request.getWebsite().trim().isEmpty()) {
                user.setWebsite(request.getWebsite().trim());
            } else if (request.getWebsite() != null) {
                user.setWebsite(null);
            }

            if (request.getProfilePicture() != null && !request.getProfilePicture().trim().isEmpty()) {
                user.setProfilePicture(request.getProfilePicture().trim());
            }

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Profile updated successfully!"));

        } catch (Exception e) {
            logger.error("Error updating profile: ", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating profile: " + e.getMessage()));
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> getUserSuggestions(
            @RequestParam(defaultValue = "5") int limit,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            User currentUser = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<User> allUsers = userRepository.findAll();

            List<UserProfileResponse> suggestions = allUsers.stream()
                    .filter(user -> !user.getId().equals(currentUser.getId()))
                    .filter(user -> !currentUser.getFollowing().contains(user))
                    .limit(limit)
                    .map(user -> {
                        UserProfileResponse profile = new UserProfileResponse();
                        profile.setId(user.getId());
                        profile.setUsername(user.getUsername());
                        profile.setFullName(user.getFullName());
                        profile.setProfilePicture(user.getProfilePicture());
                        profile.setIsVerified(user.getIsVerified());
                        return profile;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(suggestions);

        } catch (Exception e) {
            logger.error("Error getting suggestions: ", e);
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<User> users = userRepository.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/follow/{userId}")
    @Transactional
    public ResponseEntity<?> followUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            logger.info("Follow request - Current user: {}, Target user: {}", userDetails.getId(), userId);

            // Prevent self-follow
            if (userId.equals(userDetails.getId())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("You cannot follow yourself"));
            }

            User currentUser = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            User userToFollow = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User to follow not found"));

            // Check if already following
            if (currentUser.getFollowing().contains(userToFollow)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Already following this user"));
            }

            // ✅ FIXED: Only add to following (bidirectional is automatic)
            currentUser.getFollowing().add(userToFollow);

            // Save only current user
            userRepository.save(currentUser);
            userRepository.flush();

            logger.info("Successfully followed user {} by user {}", userId, userDetails.getId());

            return ResponseEntity.ok(new MessageResponse("Followed successfully"));

        } catch (Exception e) {
            logger.error("Error following user: ", e);
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/unfollow/{userId}")
    @Transactional
    public ResponseEntity<?> unfollowUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            logger.info("Unfollow request - Current user: {}, Target user: {}", userDetails.getId(), userId);

            User currentUser = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Current user not found"));

            User userToUnfollow = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

            // Remove relationship both ways
            currentUser.getFollowing().remove(userToUnfollow);
            userToUnfollow.getFollowers().remove(currentUser);

            // Save both users
            userRepository.save(currentUser);
            userRepository.save(userToUnfollow);

            logger.info("Successfully unfollowed user {} by user {}", userId, userDetails.getId());

            return ResponseEntity.ok(new MessageResponse("Unfollowed successfully"));

        } catch (Exception e) {
            logger.error("Error unfollowing user: ", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}