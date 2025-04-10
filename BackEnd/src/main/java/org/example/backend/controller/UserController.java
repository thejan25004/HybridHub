package org.example.backend.controller;


import org.example.backend.dto.UserDTO;
import org.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDTO userDTO) {
        boolean res = userService.addUser(userDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", res ? "User saved successfully" : "Failed to save user");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        boolean res = userService.updateUser(userDTO);
        return ResponseEntity.ok(res ? "User updated successfully" : "Failed to update user");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        boolean res = userService.deleteUser(id);
        return ResponseEntity.ok(res ? "User deleted successfully" : "Failed to delete user");
    }

    @GetMapping("getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("getByName/{userName}")
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String userName) {
        UserDTO userDTO = userService.getUserByName(userName);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("getEmailByCustomerId/{customerId}")
    public ResponseEntity<String> getEmailByCustomerId(@PathVariable Integer customerId) {
        String email = userService.getEmailByCustomerId(customerId);
        return ResponseEntity.ok(email != null ? email : "");
    }

//    @GetMapping("/currentUser")
//    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
//        // Convert UserDetails to your UserDTO and return
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail(userDetails.getUsername());
//        // Set other necessary fields
//
//        return ResponseEntity.ok(userDTO);
//    }
}
