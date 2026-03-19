package edu.smartcampus.smartcampusnavigation.repository;

import edu.smartcampus.smartcampusnavigation.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // JpaRepository provides findById, save, and deleteById automatically
        // Adding this allows JPA to find a student by email automatically
        Optional<Student> findByEmail(String email);

}
