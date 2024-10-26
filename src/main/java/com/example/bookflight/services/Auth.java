package com.example.bookflight.services;

import com.example.bookflight.Model.Employee;

public class Auth {

    private static Employee loggedInUser = null;

    public boolean login(String username, String password){
        Employee existingPerson = Employee.searchEmployee(username);

        // If person with that username not found then return false
        if(existingPerson == null){
            return false;
        }

        // Check password
        if(!existingPerson.validatePassword(password)){
            return false;
        }

        Auth.loggedInUser = existingPerson;
        return true;
    }

    public static void logout() {
        Auth.loggedInUser = null;
    }

    public static Employee getLoggedInUser() {
        return Auth.loggedInUser;
    }
}
