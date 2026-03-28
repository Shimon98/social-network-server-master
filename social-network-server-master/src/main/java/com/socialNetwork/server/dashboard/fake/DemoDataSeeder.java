package com.socialNetwork.server.dashboard.fake;
import com.socialNetwork.server.auth.database.PostRepository;
import com.socialNetwork.server.auth.database.UserRepository;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.services.AuthCommonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthCommonService authCommonService;

    public DemoDataSeeder(UserRepository userRepository,
                          PostRepository postRepository,
                          AuthCommonService authCommonService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.authCommonService = authCommonService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findUserByUsername("demo_user1") != null) {
            return;
        }

        createDemoUser("demo_user1", "demo1@empire.com", "123456", "https://i.pravatar.cc/300?img=1",
                "Glory to The Empire… or at least to my profile.",
                "The Empire grows stronger. Mostly because of me");

        createDemoUser("demo_user2", "demo2@empire.com", "123456", "https://i.pravatar.cc/300?img=2",
                "Building my legacy one post at a time",
                "Welcome to The Empire. Your data is safe… probably");

        createDemoUser("demo_user3", "demo3@empire.com", "123456", "https://i.pravatar.cc/300?img=3",
                "They said don’t join The Empire… so I did",
                "I came, I saw… I got confused");

        createDemoUser("demo_user4", "demo4@empire.com", "123456", "https://i.pravatar.cc/300?img=4",
                "Just joined The Empire. Still waiting for my crown \uD83D\uDC51",
                "Welcome to The Empire. Snacks are not included");

        createDemoUser("demo_user5", "demo5@empire.com", "123456", "https://i.pravatar.cc/300?img=5",
                "Every empire starts with one post. This is mine",
                "Who’s in charge here? I have questions");
    }

    private void createDemoUser(String username,
                                String email,
                                String password,
                                String imageUrl,
                                String post1,
                                String post2) {

        String normalizedUsername = authCommonService.normalizeUsername(username);
        String normalizedEmail = authCommonService.normalizeEmail(email);
        String normalizedPassword = authCommonService.normalizePassword(password);
        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);

        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);

        boolean created = userRepository.createUser(user);
        if (!created) {
            return;
        }

        User savedUser = userRepository.findUserByUsername(normalizedUsername);
        if (savedUser == null) {
            return;
        }

        userRepository.updateProfileImage(savedUser.getId(), imageUrl);

        postRepository.createPost(savedUser.getId(), post1);
        postRepository.createPost(savedUser.getId(), post2);
    }
}