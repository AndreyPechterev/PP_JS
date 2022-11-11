package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found",username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), mapRolesToAuthorities(user.getRolesOfUser()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList());
    }

    public  User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDao.findByUsername(auth.getName());
    }

    public Collection<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        Set<Role> r= new HashSet<>();
        r.add(new Role("ROLE_USER"));
        user.setRolesOfUser(r);
        userDao.save(user);
    }



    public void changeUser(Long id, User user) {
        User newUser = userDao.findById(id).get();
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setSurname(user.getSurname());
        newUser.setPassword(user.getPassword());
    }

    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }

    public User getUserById(Long id) {
        return userDao.getById(id);
    }
}
