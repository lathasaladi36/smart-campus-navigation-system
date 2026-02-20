package edu.smartcampus.smartcampusnavigation.dto;

public class LogInRequest {
    private String studentId;
    private String password;

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
