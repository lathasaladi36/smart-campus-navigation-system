package edu.smartcampus.smartcampusnavigation.repository;

import edu.smartcampus.smartcampusnavigation.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    // JpaRepository provides findById, save, and deleteById automatically
}
