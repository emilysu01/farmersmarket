package com.example.farmersmarket.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farmersmarket.R;
import com.example.farmersmarket.Utils;
import com.example.farmersmarket.models.Listing;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment";

    // Result code for loading image (given arbitrarily)
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    // UI components
    private Button btnUpload;
    private Button btnTakePic;
    private ImageView ivListingPic;
    private EditText etDescription;
    private Button btnList;

    File photoFile;
    String photoFileName;

    // Required empty public constructor
    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve UI components
        btnUpload = view.findViewById(R.id.btnUpload);
        btnTakePic = view.findViewById(R.id.btnTakePic);
        ivListingPic = view.findViewById(R.id.ivListingPic);
        etDescription = view.findViewById(R.id.etDescription);
        btnList = view.findViewById(R.id.btnList);

        // Set onClickListeners
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUploader();
            }
        });
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Error checking
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.length() > 500) {
                    Toast.makeText(getContext(), "Description can't exceed 500 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save listing to database
                saveListing(description, ParseUser.getCurrentUser(), photoFile);
            }
        });
    }

    private void launchUploader() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        photoFile = Utils.getPhotoFileUri(getContext(), photoFileName, TAG);
    }

    private void launchCamera() {
        // Create intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a file reference for future access
        photoFileName = (new Date()).toString() + ".jpg";
        photoFile = Utils.getPhotoFileUri(getContext(), photoFileName, TAG);

        // Wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // Start the image capture intent to take photo
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            // Set up file
            Uri photoUri = data.getData();
            Bitmap selectedImage = Utils.loadFromUri(getContext(), photoUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ParseFile tempPhotoFile = new ParseFile(byteArray);

            // Update UI with photo
            ivListingPic.setImageBitmap(selectedImage);
        }
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Update UI with photo
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivListingPic.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveListing(String description, ParseUser currentUser, File photoFile) {
        // Set up new listing
        Listing listing = new Listing();
        listing.setAuthor(ParseUser.getCurrentUser());
        listing.setDescription(description);


        Bitmap selectedImage = ((BitmapDrawable) ivListingPic.getDrawable()).getBitmap();
        // Bitmap selectedImage = Utils.loadFromUri(getContext(), picUri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        listing.setImage(new ParseFile(byteArray));

        // Save new listing to database
        listing.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error while saving listing. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error while saving listing", e);
                }
                Log.i(TAG, "Listing made successfully!");
                // Reset UI after saving
                etDescription.setText("");
                ivListingPic.setImageResource(0);
            }
        });
        // Parse.enableLocalDatastore(getContext());
    }
}