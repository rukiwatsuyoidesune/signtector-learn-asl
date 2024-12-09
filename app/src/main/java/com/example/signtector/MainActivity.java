package com.example.signtector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private ImageView imageView;
    private TextView resultText;
    private Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        resultText = findViewById(R.id.result_text);
        captureButton = findViewById(R.id.btn_open_camera);

        captureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            try {
                // Simpan Gambar ke Internal Storage
                File file = new File(getFilesDir(), "captured_image.jpg");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }

                // Panggil Model untuk Prediksi
                new Thread(() -> {
                    String predictionResult = InferenceLocal.runInference(file);
                    runOnUiThread(() -> resultText.setText(predictionResult));
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
                resultText.setText("Error: " + e.getMessage());
            }
        }
    }
}
