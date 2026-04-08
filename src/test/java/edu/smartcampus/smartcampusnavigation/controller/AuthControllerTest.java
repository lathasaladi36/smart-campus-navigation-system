package edu.smartcampus.smartcampusnavigation.controller;

import edu.smartcampus.smartcampusnavigation.model.Student;
import edu.smartcampus.smartcampusnavigation.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        Student student = new Student();
        student.setStudentId("S101");

        when(studentRepository.existsById("S101")).thenReturn(false);

        ResponseEntity<String> response = authController.register(student);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration Successful", response.getBody());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testLogin_Success() {
        Student savedStudent = new Student();
        savedStudent.setStudentId("S101");
        savedStudent.setPassword("pass123");
        savedStudent.setFullName("Madhavi Latha");

        Student loginRequest = new Student();
        loginRequest.setStudentId("S101");
        loginRequest.setPassword("pass123");

        when(studentRepository.findById("S101")).thenReturn(Optional.of(savedStudent));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Student);
        assertEquals("S101", ((Student) response.getBody()).getStudentId());
    }

    @Test
    void testRegister_UserAlreadyExists() {
        Student student = new Student();
        student.setStudentId("S101");

        // Force the mock to say the user already exists
        when(studentRepository.existsById("S101")).thenReturn(true);

        ResponseEntity<String> response = authController.register(student);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Student ID already exists!", response.getBody());
    }

    @Test
    void testLogin_InvalidCredentials() {
        Student loginRequest = new Student();
        loginRequest.setStudentId("S999");
        loginRequest.setPassword("wrong-pass");

        when(studentRepository.findById("S999")).thenReturn(Optional.empty());


        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testLogin_Failure() {
        // This test will cover the "Invalid credentials" line in AuthController
        Student wrongRequest = new Student();
        wrongRequest.setStudentId("nonexistent");
        wrongRequest.setPassword("wrong");

        when(studentRepository.findById("nonexistent")).thenReturn(Optional.empty());


        ResponseEntity<?> response = authController.login(wrongRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testRegister_DatabaseException() {
        Student student = new Student();
        student.setStudentId("S123");

        // Force the repository to throw an exception when checking the ID
        when(studentRepository.existsById(anyString())).thenThrow(new RuntimeException("DB Connection Failed"));

        ResponseEntity<String> response = authController.register(student);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Database Error", response.getBody());
    }

    @Test
    void testLogin_DatabaseException() {
        Student request = new Student();
        request.setStudentId("S123");

        // Force the repository to throw an exception when searching for the student
        when(studentRepository.findById(anyString())).thenThrow(new RuntimeException("Internal Error"));


        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server Error", response.getBody());
    }

    @Test
    void testLogin_WrongPassword() {
        // 1. Setup a "Saved" student in our mock database
        Student savedStudent = new Student();
        savedStudent.setStudentId("S123");
        savedStudent.setPassword("correct_password");

        // 2. Setup a login request with the SAME ID but WRONG password
        Student loginRequest = new Student();
        loginRequest.setStudentId("S123");
        loginRequest.setPassword("wrong_password");

        // 3. Tell the mock to find the student (Condition 1: student.isPresent() is TRUE)
        when(studentRepository.findById("S123")).thenReturn(Optional.of(savedStudent));

        // 4. Run the login (Condition 2: password.equals() will be FALSE)
        ResponseEntity<?> response = authController.login(loginRequest);

        // 5. Verify it returns Unauthorized
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void testVerifyEmail_Success() {
        // 1. Arrange: Create a student and mock the repository to return it
        Student student = new Student();
        student.setEmail("test@unt.edu");

        when(studentRepository.findByEmail("test@unt.edu")).thenReturn(Optional.of(student));

        // 2. Act: Call the method
        ResponseEntity<String> response = authController.verifyEmail("test@unt.edu");

        // 3. Assert: Verify response is OK and body is "User exists"
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User exists", response.getBody());
        verify(studentRepository, times(1)).findByEmail("test@unt.edu");
    }

    @Test
    void testVerifyEmail_NotFound() {
        // 1. Arrange: Mock the repository to return an empty Optional
        when(studentRepository.findByEmail("unknown@unt.edu")).thenReturn(Optional.empty());

        // 2. Act
        ResponseEntity<String> response = authController.verifyEmail("unknown@unt.edu");

        // 3. Assert: Verify 404 status and "User doesn't exist" message
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User doesn't exist", response.getBody());
    }

    @Test
    void testVerifyEmail_Exception() {
        // 1. Arrange: Force the repository to throw an exception (triggers the catch block)
        when(studentRepository.findByEmail(anyString())).thenThrow(new RuntimeException("Database down"));

        // 2. Act
        ResponseEntity<String> response = authController.verifyEmail("error@unt.edu");

        // 3. Assert: Verify 500 status and the server error message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Server error. Please check your database connection.", response.getBody());
    }
}