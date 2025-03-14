package com.m2ibank.controller;

import com.m2ibank.model.Customer;
import com.m2ibank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/api/auth/register")
    public ResponseEntity<Map<String, Object>> registerCustomer(@RequestBody Customer newCustomer) {
        Map<String, Object> response = new HashMap<>();

        // Verify if the customer already exists
        if (customerService.checkCustomerExists(newCustomer.getEmail())) {
            response.put("status", "failed");
            response.put("message", "Customer already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Create the new customer
        if (customerService.createCustomer(newCustomer)) {
            response.put("status", "success");
            response.put("message", "Customer registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("status", "failed");
            response.put("message", "Failed to register customer");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, Object>> loginCustomer(@RequestBody Map<String, String> loginDetails) {
        Map<String, Object> response = new HashMap<>();
        String email = loginDetails.get("email");
        String password = loginDetails.get("password");

        // Verify if the email exists
        if (customerService.checkCustomerExists(email)) {
            // Verify if the password matches
            if (customerService.verifyCustomer(email, password)) {
                // Generate a JWT token
                String token = customerService.generateToken(email, password);

                response.put("status", "success");
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "failed");
                response.put("message", "Invalid password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } else {
            response.put("status", "failed");
            response.put("message", "Customer not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}