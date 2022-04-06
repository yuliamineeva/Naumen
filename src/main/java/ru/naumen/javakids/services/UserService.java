package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.javakids.model.Lecture;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.repository.UserRepo;

import java.util.*;

/**
 * @author avzhukov
 * @since 17.03.2022
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User loadUserById(Long id) {
        Optional<User> userOp = userRepo.findById(id);

        if (userOp.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userOp.get();
    }

    @Transactional
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        Optional<User> userOp = userRepo.findById(id);

        if (userOp.isPresent()) {
            User userEntity = userOp.get();
            userEntity.setPassword(user.getPassword());
            userEntity.setEmail(user.getEmail());

            return userEntity;
        }

        return null;
    }

    public List<User> getUsersList() {
        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);
        Collections.sort(result, Comparator.comparingLong(User::getId));
        return result;
    }
}
