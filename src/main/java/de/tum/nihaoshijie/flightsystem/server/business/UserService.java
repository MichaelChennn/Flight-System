package de.tum.nihaoshijie.flightsystem.server.business;

import de.tum.nihaoshijie.flightsystem.server.persistence.User;
import de.tum.nihaoshijie.flightsystem.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        final Optional<User> optionalUser = userRepository.findUserByUserName(user.getUserName());
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
            return user;
        } else {
            return optionalUser.orElse(null);
        }
    }

    public User updateUser(User user, long userId) {
        final Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
            return user;
        } else {
            var existingUser = optionalUser.get();
            userRepository.update(existingUser.getId(), user.getUserName(),
                    user.getBirthDate(), user.getEmail(),
                    user.getPassword());
            return userRepository.findById(userId).orElse(null);
        }
    }

    public void deleteUser(long id) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    public Optional<User> findUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }
}
