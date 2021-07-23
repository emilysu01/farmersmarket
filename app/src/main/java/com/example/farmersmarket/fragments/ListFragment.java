package com.example.farmersmarket.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.farmersmarket.R;
import com.example.farmersmarket.Utils;
import com.example.farmersmarket.models.Listing;
import com.example.farmersmarket.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment {

    // Tag for logging statements
    public static final String TAG = "ListFragment";

    // Result code for loading image (assigned arbitrarily)
    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    // UI components
    private Button btnUpload;
    private Button btnTakePic;
    private ImageView ivListingPic;
    private EditText etDescription;
    private Spinner spCategories;
    private Button btnList;

    // TODO: Refactor to make these fields private
    // Photo file information
    public File photoFile;
    public String photoFileName;

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
        spCategories = view.findViewById(R.id.spCategories);
        btnList = view.findViewById(R.id.btnList);

        // Configure spinner
        final String[] categorySelected = {""};
        ArrayList<String> allCategories = new ArrayList<String>();
        allCategories.add("Category");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String res) {
                try {
                    // Parse API call results to get all categories
                    JSONArray rawJsonArray = new JSONArray(res);
                    for (int i = 0; i < rawJsonArray.length(); i++) {
                        String category = rawJsonArray.getJSONObject(i).getString("name").toLowerCase();
                        allCategories.add(category);
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "Error with parsing Fruityvice data", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.i(TAG, "Error with retrieving Fruityvice data", t);
            }
        });
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, allCategories) {
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    // Disable the first item from spinner because first item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(spinnerAdapter);

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                saveListing(description, ParseUser.getCurrentUser(), photoFile, categorySelected[0]);
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

    private void saveListing(String description, ParseUser currentUser, File photoFile, String category) {
        // Create new listing
        // TODO: Update with actual data
        Listing listing = new Listing();
        listing.setAuthor(new User(ParseUser.getCurrentUser()));
        listing.setDescription(description);
        listing.setCoordinates(new double[]{37.484928, -122.148201});
        listing.setPrice(10);
        listing.setUnits(1);
        listing.setCategory(category);
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("yellow");
        listing.setColors(colors);
        listing.setSellBy(new Date());
        listing.setDelivery(false);
        Bitmap selectedImage = ((BitmapDrawable) ivListingPic.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        // TODO: Update to upload multiple pictures
        ArrayList<ParseFile> imagesArray = new ArrayList<ParseFile>();
        imagesArray.add(new ParseFile(byteArray));
        listing.setImages(imagesArray);

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
    }
}