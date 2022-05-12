package ru.naumen.javakids.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.naumen.javakids.model.Role;
import ru.naumen.javakids.model.User;
import ru.naumen.javakids.repository.UserRepo;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        // По умолчанию пользователь с ролью USER создается
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(roles);

        userRepo.save(user);
    }

    @Transactional
    public Optional<User> updateUser(Long id, User user) {
        Optional<User> userOp = userRepo.findById(id);

        if (userOp.isPresent()) {
            user.setId(id);
            user.setActive(userOp.get().isActive());
            user.setRoles(userOp.get().getRoles());

            // Обновляем пароль пользователю, если он изменился
            if (!user.getPassword().equals(userOp.get().getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            userRepo.save(user);
            return userRepo.findById(id);
        } else {
            return userOp;
        }
    }

    public List<User> getUsersList() {
        List<User> result = new ArrayList<>();
        userRepo.findAll().forEach(result::add);
        result.sort(Comparator.comparingLong(User::getId));
        return result;
    }
}
