package com.example.bookflight.Model;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class Employee extends BaseEntity {
    public String name;
    public String username;
    protected String password;
    public String role;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Constructor Employee
    public Employee(String id, String name, String username, String password, String role) {
        super(id);
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }

    // Overloading
    public static Employee searchEmployee(String username){
        try{
            Json json = new Json("employees.json");
            String jsonString = json.readJSON();
            // If file Persons.json doesn't exist then return null
            if(jsonString == null){
                return null;
            }
            JSONArray employees = new JSONArray(jsonString);

            // Search person by email
            for(int i = 0; i < employees.length(); i++){
                JSONObject employee = employees.getJSONObject(i);
                if(employee.getString("username").equals(username)){
                    // create new person object
                    if(employee.getString("role").equals("staff")){
                        Staff staff = new Staff(employee.getString("id"), employee.getString("name"), employee.getString("username"), employee.getString("password"), employee.getString("role"));
                        return staff;
                    } else{
                        Manager manager = new Manager(employee.getString("id"), employee.getString("name"), employee.getString("username"), employee.getString("password"), employee.getString("role"));
                        return manager;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Employee searchEmployee(String name, String username){
        try{
            Json json = new Json("employees.json");
            String jsonString = json.readJSON();
            // If file Persons.json doesn't exist then return null
            if(jsonString == null){
                return null;
            }
            JSONArray employees = new JSONArray(jsonString);

            // Search person by name and username
            for(int i = 0; i < employees.length(); i++){
                JSONObject employee = employees.getJSONObject(i);
                if(employee.getString("name").equals(name) && employee.getString("username").equals(username)){
                    // create new person object
                    if(employee.getString("role").equals("staff")){
                        Staff staff = new Staff(employee.getString("id"), employee.getString("name"), employee.getString("username"), employee.getString("password"), employee.getString("role"));
                        return staff;
                    } else {
                        Manager manager = new Manager(employee.getString("id"), employee.getString("name"), employee.getString("username"), employee.getString("password"), employee.getString("role"));
                        return manager;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return this.role;
    }

    @Override
    public String getDisplayInfo() {
        return this.name + " " + this.username + " " + this.role;
    }
}
