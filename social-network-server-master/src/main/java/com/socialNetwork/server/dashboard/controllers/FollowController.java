package com.socialNetwork.server.dashboard.controllers;

import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.dashboard.requests.FollowUserRequest;
import com.socialNetwork.server.dashboard.requests.SearchRequest;
import com.socialNetwork.server.dashboard.responses.FollowingResponse;
import com.socialNetwork.server.dashboard.responses.SearchUsersResponse;
import com.socialNetwork.server.dashboard.services.CurrentUserService;
import com.socialNetwork.server.dashboard.services.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class FollowController {

    private final FollowService followService;
    private final CurrentUserService currentUserService;

    public FollowController(FollowService followService,
                            CurrentUserService currentUserService) {
        this.followService = followService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/search")
    public SearchUsersResponse searchUsers(@RequestBody SearchRequest requestBody,
                                           HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return followService.searchUsers(currentUserId, requestBody.getText());
        } catch (RuntimeException e) {
            return new SearchUsersResponse(false, ErrorCodes.UNAUTHORIZED, null);
        }
    }

    @PostMapping("/follow")
    public BasicResponse followUser(@RequestBody FollowUserRequest requestBody,
                                    HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return followService.followUser(currentUserId, requestBody.getFollowedUserId());
        } catch (RuntimeException e) {
            return new BasicResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/follow")
    public BasicResponse unfollowUser(@RequestBody FollowUserRequest requestBody,
                                      HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return followService.unfollowUser(currentUserId, requestBody.getFollowedUserId());
        } catch (RuntimeException e) {
            return new BasicResponse(false, ErrorCodes.UNAUTHORIZED);
        }
    }

    @GetMapping("/following")
    public FollowingResponse getFollowingUsers(HttpServletRequest request) {
        try {
            Long currentUserId = currentUserService.extractCurrentUserId(request);
            return followService.getFollowingUsers(currentUserId);
        } catch (RuntimeException e) {
            return new FollowingResponse(false, ErrorCodes.UNAUTHORIZED, null);
        }
    }
}