package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.entity.LoginAttemptInfo;
import com.socialNetwork.server.auth.entity.User;
import org.springframework.stereotype.Service;

import static com.socialNetwork.server.auth.utils.Constants.LOGIN_MAX_ATTEMPTS;

@Service
public class BlockManager {
    private final BlockService blockService;

    public BlockManager(BlockService blockService) {
        this.blockService = blockService;
    }

    public boolean isUserBlocked(User user) {
        if (user == null) {
            return false;
        }
        return blockService.isBlocked((long) user.getId());
    }

    public boolean handleFailedLogin(User user) {
        if (user == null) {
            return false;
        }
        String username = user.getUsername();
        LoginAttemptInfo loginAttemptInfo = blockService.getAttempts(username);

        if (loginAttemptInfo == null) {
            loginAttemptInfo = new LoginAttemptInfo();
        }
        loginAttemptInfo.addAttempt();

        if (loginAttemptInfo.getAttempts() >= LOGIN_MAX_ATTEMPTS) {
            blockService.clearAttempts(username);
            return blockService.blockUser((long) user.getId());
        }

        blockService.saveAttempts(username, loginAttemptInfo);
        return false;
    }

    public void handleSuccessfulLogin(User user) {
        if (user == null) {
            return;
        }
        blockService.clearAttempts(user.getUsername());
    }

    public boolean unblockUser(User user) {
        if (user == null) {
            return false;
        }
        blockService.clearAttempts(user.getUsername());
        return blockService.unblockUser((long) user.getId());
    }
}