package com.socialNetwork.server.dashboard.controllers;

import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.dashboard.requests.FollowUserRequest;
import com.socialNetwork.server.dashboard.requests.SearchRequest;
import com.socialNetwork.server.dashboard.responses.FollowingResponse;
import com.socialNetwork.server.dashboard.responses.SearchUsersResponse;
import com.socialNetwork.server.dashboard.services.CurrentUserService;
import com.socialNetwork.server.dashboard.services.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;
    private final CurrentUserService currentUserService;

    public FollowController(FollowService followService, CurrentUserService currentUserService) {
        this.followService = followService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody SearchRequest textRequest,
                                         HttpServletRequest request) {
        String text = textRequest.getText();
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            SearchUsersResponse response = followService.searchUsers(currentUserId, text);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> followUser(@RequestBody FollowUserRequest requestBody,
                                        HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            BasicResponse response = followService.followUser(currentUserId, requestBody.getFollowedUserId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowingUsers(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            FollowingResponse response = followService.getFollowingUsers(currentUserId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> unfollowUser(@RequestBody FollowUserRequest requestBody,
                                          HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            BasicResponse response = followService.unfollowUser(currentUserId, requestBody.getFollowedUserId());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}