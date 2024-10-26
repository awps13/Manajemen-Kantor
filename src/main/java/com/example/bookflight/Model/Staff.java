package com.example.bookflight.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Staff extends Employee {
    public Staff(String id, String name, String username, String password, String role) {
        super(id, name, username, password, role);
    }

    @Override
    public String getDisplayInfo() {
        return "Nama ini " + this.name + " dengan username " + this.username + " merupakan seorang " + this.role;
    }

    public static final String JSON_PATH = "data/";

    public static void save(Staff staff) {
        JSONArray jsonArray;

        try {
            // Buat objek JSON untuk staff
            JSONObject json = new JSONObject();
            json.put("id", staff.getId());
            json.put("name", staff.getName());
            json.put("username", staff.getUsername());
            json.put("password", staff.getPassword());
            json.put("role", staff.getRole());

            // Membaca konten file JSON jika ada
            String staffContent = new String(Files.readAllBytes(Paths.get(JSON_PATH + "employees.json")));
            if (staffContent.isEmpty()) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(staffContent);
            }

            // Tambahkan objek JSON staff ke dalam array
            jsonArray.put(json);

            // Tulis kembali data ke dalam file JSON
            try (FileWriter fileWriter = new FileWriter(JSON_PATH + "employees.json")) {
                fileWriter.write(jsonArray.toString(4)); // Indentasi 4 spasi
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
