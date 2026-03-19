package edu.smartcampus.smartcampusnavigation.model;

import edu.smartcampus.smartcampusnavigation.dto.LogInRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentAndRequestTest {

    @Test
    void testStudentGettersSetters() {
        Student student = new Student();
        student.setFullName("John Doe");
        student.setEmail("john@unt.edu");

        assertEquals("John Doe", student.getFullName());
        assertEquals("john@unt.edu", student.getEmail());
    }

    @Test
    void testLogInRequest() {
        LogInRequest request = new LogInRequest();
        request.setStudentId("1155");

        assertEquals("1155", request.getStudentId());
    }
    @Test
    void testLogInRequestGettersSetters() {
        LogInRequest request = new LogInRequest();

        // Testing Setters
        request.setStudentId("S12345");
        request.setPassword("securePass");

        // Testing Getters to verify values and clear coverage lines
        assertEquals("S12345", request.getStudentId());
        assertEquals("securePass", request.getPassword());
    }
}