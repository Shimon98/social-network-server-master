package com.socialNetwork.server.dashboard.controllers;

import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.dashboard.requests.CreatePostRequest;
import com.socialNetwork.server.dashboard.requests.UpdateProfileImageRequest;
import com.socialNetwork.server.dashboard.responses.CurrentUserResponse;
import com.socialNetwork.server.dashboard.responses.FeedResponse;
import com.socialNetwork.server.dashboard.services.CurrentUserService;
import com.socialNetwork.server.dashboard.services.DashboardService;
import com.socialNetwork.server.dashboard.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final PostService postService;
    private final CurrentUserService currentUserService;

    public DashboardController(DashboardService dashboardService,
                               PostService postService,
                               CurrentUserService currentUserService) {
        this.dashboardService = dashboardService;
        this.postService = postService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            CurrentUserResponse response = dashboardService.getCurrentUser(currentUserId);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me/posts")
    public ResponseEntity<?> getMyPosts(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            FeedResponse response = postService.getMyPosts(currentUserId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(@RequestBody UpdateProfileImageRequest requestBody,
                                                HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            BasicResponse response = dashboardService.updateProfileImage(currentUserId, requestBody.getProfileImageUrl());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest requestBody,
                                        HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            BasicResponse response = postService.createPost(currentUserId, requestBody.getContent());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            BasicResponse response = postService.deletePost(currentUserId, postId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            FeedResponse response = postService.getFeed(currentUserId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}