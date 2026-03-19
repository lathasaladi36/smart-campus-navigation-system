package edu.smartcampus.smartcampusnavigation.repository;

import edu.smartcampus.smartcampusnavigation.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
// This line tells Spring NOT to look for an in-memory database and use your MySQL instead
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testSaveAndFindStudent() {
        Student student = new Student();
        student.setStudentId("test-123");
        student.setFullName("Test Student");
        student.setPassword("password123");

        studentRepository.save(student);

        Optional<Student> found = studentRepository.findById("test-123");
        assertTrue(found.isPresent());
        assertEquals("Test Student", found.get().getFullName());
    }
    @Test
    void testStudentEntityCoverage() {
        // Testing the Default Constructor (often a hidden uncovered line)
        Student student = new Student();

        // Testing all Setters
        student.setStudentId("115599");
        student.setFullName("Scrappy Eagle");
        student.setEmail("scrappy@unt.edu");
        student.setContactNumber("9405652000");
        student.setPassword("untPassword");

        // Testing all Getters to ensure 100% line coverage
        assertEquals("115599", student.getStudentId());
        assertEquals("Scrappy Eagle", student.getFullName());
        assertEquals("scrappy@unt.edu", student.getEmail());
        assertEquals("9405652000", student.getContactNumber());
        assertEquals("untPassword", student.getPassword());
    }
}