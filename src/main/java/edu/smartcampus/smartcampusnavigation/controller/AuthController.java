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
@CrossOrigin(origins = "*") // Allows your HTML file to connect
public class AuthController {

    private static final Logger log = LogManager.getLogger(AuthController.class);
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody Student student) {
        if (studentRepository.existsById(student.getStudentId())) {
            return ResponseEntity.badRequest().body("Student ID already exists!");
        }
        studentRepository.save(student);
        return ResponseEntity.ok("Registration Successful");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody Student loginRequest) {
        Optional<Student> student = studentRepository.findById(loginRequest.getStudentId());

        if (student.isPresent() && student.get().getPassword().equals(loginRequest.getPassword())) {
            log.info("Success");
            return ResponseEntity.ok("Success");

        }
        log.info("Invalid ID or Password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID or Password");
    }
}