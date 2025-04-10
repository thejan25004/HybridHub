package org.example.backend.service;



import org.example.backend.dto.UserDTO;
import org.example.backend.entity.Customer;
import org.example.backend.entity.User;
import org.example.backend.repo.CustomerRepo;
import org.example.backend.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private CustomerRepo customerRepo;

    public UserDTO getUserByName(String userName) {
        User user = userRepo.findByName(userName); // Assuming there's a method like this in your repo.
        return modelMapper.map(user, UserDTO.class);
    }



    public boolean addUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepo.save(user);
        return true;
    }

    public boolean updateUser(UserDTO userDTO) {
        if (userRepo.existsById(userDTO.getUserId())) {
            User user = modelMapper.map(userDTO, User.class);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteUser(Integer id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUsersByEmail(email);

        String password;
        if (user.getPassword() == null) {
            password = "";
        } else {
            password = user.getPassword();
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), password, getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.getUsersByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }


    public String getEmailByCustomerId(Integer customerId) {
        // Find the customer first
        Optional<Customer> customerOptional = customerRepo.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            // Assuming the Customer entity has a user relationship
            User user = customer.getUser(); // You'll need to add this method to Customer entity
            return user != null ? user.getEmail() : null;
        }

        return null;
    }


}


//package org.example.backend.service;
//
//
//
//import org.example.backend.dto.UserDTO;
//import org.example.backend.entity.Customer;
//import org.example.backend.entity.User;
//import org.example.backend.repo.CustomerRepo;
//import org.example.backend.repo.UserRepo;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//@Service
//public class UserService implements UserDetailsService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private CustomerRepo customerRepo;
//
//    // Add this method to get user by email
//    public UserDTO getUserByEmail(String email) {
//        User user = userRepo.getUsersByEmail(email);
//        if (user == null) {
//            return null;
//        }
//        return modelMapper.map(user, UserDTO.class);
//    }
//
//    public UserDTO getUserByName(String userName) {
//        User user = userRepo.findByName(userName);
//        return modelMapper.map(user, UserDTO.class);
//    }
//
//    public boolean addUser(UserDTO userDTO) {
//        User user = modelMapper.map(userDTO, User.class);
//        userRepo.save(user);
//        return true;
//    }
//
//    public boolean updateUser(UserDTO userDTO) {
//        if (userRepo.existsById(userDTO.getUserId())) {
//            User user = modelMapper.map(userDTO, User.class);
//            userRepo.save(user);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean deleteUser(Integer id) {
//        if (userRepo.existsById(id)) {
//            userRepo.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//    public List<UserDTO> getAllUsers() {
//        return userRepo.findAll().stream()
//                .map(user -> modelMapper.map(user, UserDTO.class))
//                .collect(Collectors.toList());
//    }
//
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepo.getUsersByEmail(email);
//
//        String password;
//        if (user.getPassword() == null) {
//            password = "";
//        } else {
//            password = user.getPassword();
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), password, getAuthority(user));
//    }
//
//    private Set<SimpleGrantedAuthority> getAuthority(User user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        authorities.add(new SimpleGrantedAuthority(user.getRole()));
//        return authorities;
//    }
//
//    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.getUsersByEmail(username);
//        return modelMapper.map(user, UserDTO.class);
//    }
//
//    public String getEmailByCustomerId(Integer customerId) {
//        Optional<Customer> customerOptional = customerRepo.findById(customerId);
//
//        if (customerOptional.isPresent()) {
//            Customer customer = customerOptional.get();
//            User user = customer.getUser();
//            return user != null ? user.getEmail() : null;
//        }
//
//        return null;
//    }
//}