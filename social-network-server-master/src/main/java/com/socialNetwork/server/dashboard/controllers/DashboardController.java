package com.socialNetwork.server.dashboard.controllers;

import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.dashboard.requests.CreatePostRequest;
import com.socialNetwork.server.dashboard.requests.UpdateProfileImageRequest;
import com.socialNetwork.server.dashboard.responses.CurrentUserResponse;
import com.socialNetwork.server.dashboard.responses.FeedResponse;
import com.socialNetwork.server.dashboard.services.CurrentUserService;
import com.socialNetwork.server.dashboard.services.DashboardService;
import com.socialNetwork.server.dashboard.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
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
    public CurrentUserResponse getCurrentUser(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return dashboardService.getCurrentUser(currentUserId);
        } catch (RuntimeException e) {
            return new CurrentUserResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @GetMapping("/me/posts")
    public FeedResponse getMyPosts(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return postService.getMyPosts(currentUserId);
        } catch (RuntimeException e) {
            return new FeedResponse(false, ErrorCodes.UNAUTHORIZED, null);
        }
    }

    @PutMapping("/profile-image")
    public BasicResponse updateProfileImage(@RequestBody UpdateProfileImageRequest requestBody,
                                            HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return dashboardService.updateProfileImage(currentUserId, requestBody.getProfileImageUrl());
        } catch (RuntimeException e) {
            return new BasicResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @PostMapping("/posts")
    public BasicResponse createPost(@RequestBody CreatePostRequest requestBody,
                                    HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return postService.createPost(currentUserId, requestBody.getContent());
        } catch (RuntimeException e) {
            return new BasicResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public BasicResponse deletePost(@PathVariable Long postId,
                                    HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return postService.deletePost(currentUserId, postId);
        } catch (RuntimeException e) {
            return new BasicResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @GetMapping("/feed")
    public FeedResponse getFeed(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return postService.getFeed(currentUserId);
        } catch (RuntimeException e) {
            return new FeedResponse(false, ErrorCodes.UNAUTHORIZED, null);
        }
    }
}