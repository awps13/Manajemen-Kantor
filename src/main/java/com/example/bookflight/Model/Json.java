package com.example.bookflight.Model;

import java.io.*;

public class Json {
    private String filePath;

    public Json(String filePath) {
        this.filePath = filePath;
    };

    public String readJSON() {
        StringBuilder jsonBuilder = new StringBuilder();
        File file = new File("data/" + this.filePath);  // Pastikan filePath adalah nama file, bukan jalur penuh

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonBuilder.toString();
    }
}
