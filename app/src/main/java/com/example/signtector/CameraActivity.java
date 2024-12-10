package com.example.signtector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int UPLOAD_REQUEST_CODE = 101;
    private static final String TAG = "CameraActivity";

    private ImageView imageView;
    private TextView resultText;
    private ImageButton captureButton;
    private ImageButton uploadButton;
    private ImageButton reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Initialize UI components
        imageView = findViewById(R.id.image_view);
        resultText = findViewById(R.id.result_text);
        captureButton = findViewById(R.id.btn_open_camera);
        uploadButton = findViewById(R.id.btn_upload_image);
        reportButton = findViewById(R.id.btn_report);

        // Set onClickListener for capture button
        captureButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } catch (Exception e) {
                Log.e(TAG, "Error starting camera intent", e);
                resultText.setText("Failed to open camera: " + e.getMessage());
            }
        });

        // Set onClickListener for upload button
        uploadButton.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            try {
                startActivityForResult(uploadIntent, UPLOAD_REQUEST_CODE);
            } catch (Exception e) {
                Log.e(TAG, "Error starting upload intent", e);
                resultText.setText("Failed to open gallery: " + e.getMessage());
            }
        });

        // Set onClickListener for report button
        reportButton.setOnClickListener(v -> {
            showReportDialog();
        });
    }

    private void showReportDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Report Issue")
                .setMessage("Thank you for reporting an issue. Our team will review it shortly.")
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = null;

            try {
                File file = new File(getFilesDir(), "captured_image.jpg");

                if (requestCode == CAMERA_REQUEST_CODE) {
                    // Capture image from camera
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        photo = (Bitmap) extras.get("data");
                    }
                } else if (requestCode == UPLOAD_REQUEST_CODE) {
                    // Pick image from gallery
                    if (data.getData() != null) {
                        photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    }
                }

                if (photo != null) {
                    // Display image in ImageView
                    imageView.setImageBitmap(photo);

                    // Save image locally
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (Exception e) {
                        Log.e(TAG, "Error saving image locally", e);
                        resultText.setText("Failed to save image: " + e.getMessage());
                        return;
                    }

                    // Run inference in a background thread
                    File finalFile = file;
                    new Thread(() -> {
                        try {
                            String predictionResult = InferenceLocal.runInference(finalFile);
                            runOnUiThread(() -> resultText.setText(predictionResult));
                        } catch (Exception e) {
                            Log.e(TAG, "Error during inference", e);
                            runOnUiThread(() -> resultText.setText("Inference failed: " + e.getMessage()));
                        }
                    }).start();
                } else {
                    resultText.setText("No image data found");
                }

            } catch (Exception e) {
                Log.e(TAG, "Error processing image", e);
                resultText.setText("Error: " + e.getMessage());
            }
        } else {
            resultText.setText("Operation canceled or no data received");
        }
    }
}
