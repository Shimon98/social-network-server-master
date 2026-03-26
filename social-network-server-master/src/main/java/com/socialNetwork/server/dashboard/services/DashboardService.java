package com.socialNetwork.server.dashboard.services;

import com.socialNetwork.server.auth.database.DBManager;
import com.socialNetwork.server.auth.database.UserRepository;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.dashboard.responses.CurrentUserResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

//    private final DBManager dbManager;
    private final UserRepository userRepository;

    public DashboardService( UserRepository userRepository) {
//        this.dbManager = dbManager;
        this.userRepository = userRepository;
    }

    public CurrentUserResponse getCurrentUser(Long currentUserId) {
        User user = userRepository.findUserById(currentUserId);
//        User user = dbManager.findUserById(currentUserId);

        if (user == null) {
            return null;
        }

        return new CurrentUserResponse(
                true,
                null,
                user.getId(),
                user.getUsername(),
                user.getProfileImageUrl()
        );
    }

    public BasicResponse updateProfileImage(Long currentUserId, String profileImageUrl) {
        boolean updated = userRepository.updateProfileImage(currentUserId, profileImageUrl);
//        boolean updated = dbManager.updateProfileImage(currentUserId, profileImageUrl);

        if (!updated) {
            return new BasicResponse(false, ErrorCodes.INVALID_PROFILE_IMAGE);
        }

        return new BasicResponse(true, ErrorCodes.IMAGE_CHANGE_SUCCESS);
    }
}