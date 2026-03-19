package edu.smartcampus.smartcampusnavigation.controller;

import edu.smartcampus.smartcampusnavigation.model.Student;
import edu.smartcampus.smartcampusnavigation.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody Student student) {
        try {
            if (studentRepository.existsById(student.getStudentId())) {
                return ResponseEntity.badRequest().body("Student ID already exists!");
            }
            studentRepository.save(student);
            return ResponseEntity.ok("Registration Successful");
        } catch (Exception e) {
            log.error("Signup error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Error");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody Student loginRequest) {
        try {
            Optional<Student> student = studentRepository.findById(loginRequest.getStudentId());

            // The .getPassword() usage here connects to your Student class
            if (student.isPresent() && student.get().getPassword().equals(loginRequest.getPassword())) {
                log.info("Login successful for: {}", loginRequest.getStudentId());
                return ResponseEntity.ok("Success"); // JavaScript looks for this exact string
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email) {
        try {
            // Efficiently search for the student by email in the repository
            Optional<Student> student = studentRepository.findByEmail(email);

            if (student.isPresent()) {
                log.info("Email verification successful for: {}", email);
                return ResponseEntity.ok("User exists");
            } else {
                log.warn("Email verification failed: {} not found", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn't exist");
            }
        } catch (Exception e) {
            // Log the exact error in your IntelliJ console for debugging
            log.error("Database error during email verification: {}", e.getMessage());

            // Return a professional error message to the frontend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error. Please check your database connection.");
        }
    }
}