package com.example.bookflight.Model;

public class Assignment {
    private String id;
    private String employeeId;
    private String taskId;
    private String assignmentFile;
    private String assignDate;

    public Assignment(String id, String employeeId, String taskId, String assignmentFile, String assignDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.taskId = taskId;
        this.assignmentFile = assignmentFile;
        this.assignDate = assignDate;
    }
}
