package testServer;

import testClient.User;
import testClient.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUserInfo(String name) {
        return User.builder().name(name).build();
    }
}
