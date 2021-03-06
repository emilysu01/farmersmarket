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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.farmersmarket.LocationUtils;
import com.example.farmersmarket.R;
import com.example.farmersmarket.SearchAlgorithm;
import com.example.farmersmarket.Utils;
import com.example.farmersmarket.models.Listing;
import com.google.android.gms.maps.model.LatLng;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    private EditText etPrice;
    private EditText etUnits;
    private EditText etSellBy;
    private Spinner spDelivery;
    private Button btnList;
    private ImageButton ibRed;
    private ImageButton ibOrange;
    private ImageButton ibYellow;
    private ImageButton ibGreen;
    private ImageButton ibBlue;
    private ImageButton ibPurple;
    private ImageButton ibWhite;
    private ImageButton ibBrown;
    private ImageButton ibBlack;
    private ImageView ivRedClicked;
    private ImageView ivOrangeClicked;
    private ImageView ivYellowClicked;
    private ImageView ivGreenClicked;
    private ImageView ivBlueClicked;
    private ImageView ivPurpleClicked;
    private ImageView ivWhiteClicked;
    private ImageView ivBrownClicked;
    private ImageView ivBlackClicked;

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
        etPrice = view.findViewById(R.id.etPrice);
        etUnits = view.findViewById(R.id.etUnits);
        etSellBy = view.findViewById(R.id.etSellBy);
        spDelivery = view.findViewById(R.id.spDelivery);
        btnList = view.findViewById(R.id.btnList);
        ibRed = view.findViewById(R.id.ibRed);
        ibOrange = view.findViewById(R.id.ibOrange);
        ibYellow = view.findViewById(R.id.ibYellow);
        ibGreen = view.findViewById(R.id.ibGreen);
        ibBlue = view.findViewById(R.id.ibBlue);
        ibPurple = view.findViewById(R.id.ibPurple);
        ibWhite = view.findViewById(R.id.ibWhite);
        ibBrown = view.findViewById(R.id.ibBrown);
        ibBlack = view.findViewById(R.id.ibBlack);
        ivRedClicked = view.findViewById(R.id.ivRedClicked);
        ivOrangeClicked = view.findViewById(R.id.ivOrangeClicked);
        ivYellowClicked = view.findViewById(R.id.ivYellowClicked);
        ivGreenClicked = view.findViewById(R.id.ivGreenClicked);
        ivBlueClicked = view.findViewById(R.id.ivBlueClicked);
        ivPurpleClicked = view.findViewById(R.id.ivPurpleClicked);
        ivWhiteClicked = view.findViewById(R.id.ivWhiteClicked);
        ivBrownClicked = view.findViewById(R.id.ivBrownClicked);
        ivBlackClicked = view.findViewById(R.id.ivBlackClicked);

        ibRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibRed.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivRedClicked);
                ivRedClicked.bringToFront();
            }
        });

        ibOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibOrange.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivOrangeClicked);
                ivOrangeClicked.bringToFront();
            }
        });

        ibYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibYellow.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivYellowClicked);
                ivYellowClicked.bringToFront();
            }
        });

        ibGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibGreen.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivGreenClicked);
                ivGreenClicked.bringToFront();
            }
        });

        ibBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibBlue.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivBlueClicked);
                ivBlueClicked.bringToFront();
            }
        });

        ibPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibPurple.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivPurpleClicked);
                ivPurpleClicked.bringToFront();
            }
        });

        ibWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibWhite.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_black_check))
                        .into(ivWhiteClicked);
                ivWhiteClicked.bringToFront();
            }
        });

        ibBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibBrown.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivBrownClicked);
                ivBrownClicked.bringToFront();
            }
        });

        ibBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibBlack.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                Glide.with(getContext())
                        .load(getResources().getDrawable(R.drawable.ic_white_check))
                        .into(ivBlackClicked);
                ivBlackClicked.bringToFront();
            }
        });

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        // Configure categories spinner
        final String[] categorySelected = {""};
        ArrayList<String> allCategories = new ArrayList<String>();
        allCategories.add("Category");
        final AsyncHttpClient[] client = {new AsyncHttpClient()};
        RequestParams params = new RequestParams();
        client[0].get("https://www.fruityvice.com/api/fruit/all", params, new TextHttpResponseHandler() {
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
        ArrayAdapter<String> categorySpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, allCategories) {
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
                if(position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategories.setAdapter(categorySpinnerAdapter);
        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
                categorySelected[0] = allCategories.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set delivery spinner
        final String[] deliveryOptionSelected = {""};
        ArrayList<String> allDeliveryOptions = new ArrayList<String>();
        allDeliveryOptions.add("Delivery");
        allDeliveryOptions.add("Available");
        allDeliveryOptions.add("Unavailable");
        ArrayAdapter<String> deliverySpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, allDeliveryOptions) {
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
                if(position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        deliverySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDelivery.setAdapter(deliverySpinnerAdapter);
        spDelivery.setOnItemSelectedListener(listener);
        spDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
                deliveryOptionSelected[0] = allDeliveryOptions.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        // Set onClickListeners
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpload.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                launchUploader();
            }
        });
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTakePic.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                launchCamera();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnList.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce_button));
                // Error checking
                try {
                    String description = etDescription.getText().toString();
                    int price = Integer.parseInt(etPrice.getText().toString());
                    int units = Integer.parseInt(etUnits.getText().toString());
                    DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                    Date sellBy = format.parse(etSellBy.getText().toString());
                    LatLng coordinates = LocationUtils.getCoordinates(getContext(), getActivity());
                    if (!checkForErrors(description, price, units, sellBy, coordinates)) {
                        return;
                    }
                    Log.i("HERE YE HERE YE", deliveryOptionSelected[0]);
                    // Save listing to database
                    saveListing(description, ParseUser.getCurrentUser(), photoFile, categorySelected[0], coordinates, price, units, new ArrayList<String>(), sellBy, deliveryOptionSelected[0]);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkForErrors(String description, int price, int units, Date sellBy, LatLng coordinates) {
        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Description can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.length() > 500) {
            Toast.makeText(getContext(), "Description can't exceed 500 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price == 0) {
            Toast.makeText(getContext(), "Price can't be empty or $0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (units == 0) {
            Toast.makeText(getContext(), "Units available can't be empty of 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sellBy.before(new Date())) {
            Toast.makeText(getContext(), "Sell by date can't be before today", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (coordinates == null) {
            Toast.makeText(getContext(), "There was an issue retrieving your location", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private void saveListing(String description, ParseUser currentUser, File photoFile, String category, LatLng coordinates, int price, int units, ArrayList<String> colors, Date sellBy, String delivery) {
        // Create new listing
        Log.i("CATEGORY TYPE", category.getClass().toString());
        Log.i("CATEGORY ACTUAL", category.toString());
        Listing listing = new Listing();
        listing.setAuthor(ParseUser.getCurrentUser());
        listing.setDescription(description);
        listing.setLatitude(coordinates.latitude);
        listing.setLongitude(coordinates.longitude);
        listing.setPrice(price);
        listing.setUnits(units);
        listing.put("category", category);
        listing.setColors(colors);
        listing.setSellBy(sellBy);
        if (delivery.equals("Available")) {
            listing.setDelivery(true);
        } else {
            listing.setDelivery(false);
        }

        Log.i("HERE YE", listing.getCategory());
        Bitmap selectedImage = ((BitmapDrawable) ivListingPic.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
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
                etPrice.setText("");
                etUnits.setText("");
                etSellBy.setText("");
                spCategories.setSelection(0);
                spDelivery.setSelection(0);
                ivListingPic.setImageResource(0);
                ivRedClicked.setImageResource(0);
                ivOrangeClicked.setImageResource(0);
                ivYellowClicked.setImageResource(0);
                ivGreenClicked.setImageResource(0);
                ivBlueClicked.setImageResource(0);
                ivPurpleClicked.setImageResource(0);
                ivWhiteClicked.setImageResource(0);
                ivBrownClicked.setImageResource(0);
                ivBlackClicked.setImageResource(0);
            }
        });
    }
}