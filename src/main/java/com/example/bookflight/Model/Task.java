package com.example.bookflight.Model;

public class Task extends BaseEntity {
    public String id;
    public String employeesId;
    public String taskName;
    public String taskDescription;
    public String dueDate;
    public String status;

    public Task(String id, String employeesId, String taskName, String taskDescription, String dueDate, String status) {
        super(id);
        this.employeesId = employeesId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getEmployeesId() {
        return this.employeesId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskDescription() {
        return this.taskDescription;
    }

    public String getDueDate() {
        return this.dueDate;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String getDisplayInfo() {
        return "Task " + this.taskName + " dengan tenggat " + this.dueDate + " berstatus " + this.status;
    }
}
