package com.example.signtector;

import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class InferenceLocal {

    @RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static String runInference(File file) {
        String result = "";

        try {
            // Base64 Encode
            String encodedFile;
            try (FileInputStream fileInputStreamReader = new FileInputStream(file)) {
                byte[] bytes = new byte[(int) file.length()];
                fileInputStreamReader.read(bytes);
                encodedFile = new String(Base64.getEncoder().encode(bytes), StandardCharsets.US_ASCII);
            }

            // URL dan API Key
            String API_KEY = "qbKYgEtoRT4pEKuE7NuU";
            String MODEL_ENDPOINT = "sign-language-detection-fpqyh/1";
            String uploadURL = "https://detect.roboflow.com/" + MODEL_ENDPOINT + "?api_key=" + API_KEY;

            // Konfigurasi HTTP Request
            HttpURLConnection connection = (HttpURLConnection) new URL(uploadURL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // Kirim Data
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(encodedFile);
            }

            // Baca Response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Parsing JSON Response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray predictions = jsonResponse.getJSONArray("predictions");

            if (predictions.length() > 0) {
                JSONObject firstPrediction = predictions.getJSONObject(0);
                String predictedClass = firstPrediction.getString("class");
                double confidence = firstPrediction.getDouble("confidence");

                result = "Class: " + predictedClass + "\nAccuracy: " + (confidence * 100) + "%";

                // Log untuk debugging (opsional)
                Log.d("Inference Result", result);
            } else {
                result = "No predictions found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        }

        return result;
    }
}
