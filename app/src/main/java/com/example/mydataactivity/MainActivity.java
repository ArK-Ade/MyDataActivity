package com.example.mydataactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    static final int ACTION_PICK_REQUEST_EMAIL = 1 ;
    static final int ACTION_PICK_REQUEST_IMAGE = 2 ;
    TextView textViewEmail;
    ImageView imageView;
    Button buttonEmail;
    Button buttonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewEmail = this.findViewById(R.id.textView);
        imageView = this.findViewById(R.id.imageView);
        buttonEmail = this.findViewById(R.id.button);
        buttonImage = this.findViewById(R.id.button2);

        buttonEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI);
                startActivityForResult(contactPickerIntent, ACTION_PICK_REQUEST_EMAIL);
            }
        });

        buttonImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, ACTION_PICK_REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be using multiple startActivityForResult
            switch (requestCode) {
                case ACTION_PICK_REQUEST_EMAIL:
                    Cursor cursor = null;
                    try {
                        String email = null;

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();

                        int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                        email = cursor.getString(emailIndex);

                        cursor.close();

                        textViewEmail.setText(email);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ACTION_PICK_REQUEST_IMAGE:
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
}