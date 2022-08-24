package com.mypath.instagramapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
//import com.parse.SaveCallback;

import java.io.File;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    public static final String TAG = "PostActivity";

    private Button btTakepicture;
    private Button btPost;
    private ImageView ivPostimage;
    private EditText etCapture;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public ImageButton ibUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menutop);
            // Sets the Toolbar to act as the ActionBar for this Activity window.
            // Make sure the toolbar exists in the activity and is not null
            setSupportActionBar(toolbar);

            Objects.requireNonNull(getSupportActionBar()).setTitle("");

            btTakepicture = findViewById(R.id.btTakepicture);
            btPost = findViewById(R.id.btPost);
            etCapture = findViewById(R.id.etCapture);
            ivPostimage = findViewById(R.id.ivPostimage);

            ibUser = findViewById(R.id.ibUser);
            ImageButton ibHome = findViewById(R.id.ibHome);

            ibUser.setOnClickListener(view -> {
                Intent i = new Intent(PostActivity.this,LogoutActivity.class);
                startActivity(i);
//                finish();
            });

            ibHome.setOnClickListener(view -> {
                Intent i = new Intent(PostActivity.this,MainActivity.class);
                startActivity(i);
//                finish();
            });

            btTakepicture.setOnClickListener(view -> onLaunchCamera());

            btPost.setOnClickListener(view -> {
                String description = etCapture.getText().toString().trim();

                if(description.isEmpty()){
                    Toast.makeText(PostActivity.this, "Caption Cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(photoFile==null || ivPostimage.getDrawable()==null  ){
                    Toast.makeText(PostActivity.this, "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser user = ParseUser.getCurrentUser();
                savePost(description,user,photoFile);
            });
        } else {
            // show the signup or login screen
            Intent i = new Intent(PostActivity.this,LoginActivity.class);
            startActivity(i);
        }


    }

    private void savePost(String description, ParseUser user, File photoFile) {
        Post post = new Post();

        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(user);

        ProgressBar pb = findViewById(R.id.pbLoading);
        pb.setVisibility(ProgressBar.VISIBLE);

        post.saveInBackground(e -> {
            if(e!=null){
                Log.e(TAG, "Error while saving", e);
                Toast.makeText(PostActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "Post save was successful!!! ");
            etCapture.setText("");
            ivPostimage.setImageResource(0);


            btPost.setVisibility(View.INVISIBLE);
            etCapture.setVisibility(View.INVISIBLE);
            btTakepicture.setVisibility(View.VISIBLE);

            Toast.makeText(PostActivity.this, "Save", Toast.LENGTH_SHORT).show();
            pb.setVisibility(ProgressBar.INVISIBLE);
        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.mypath.instagramapp", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            cameraResultLauncher.launch(intent);
        }
    }

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from EditActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // by this point we have the camera photo on disk
                        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        // RESIZE BITMAP, see section below
                        // Load the taken image into a preview
                        ImageView ivPreview = ivPostimage;
                        ivPreview.setImageBitmap(takenImage);
                        
                        etCapture.setText("");
                        btPost.setVisibility(View.VISIBLE);
                        etCapture.setVisibility(View.VISIBLE);
                        btTakepicture.setVisibility(View.INVISIBLE);
                    } else { // Result was a failure
                        Toast.makeText(PostActivity.this, "Error taking picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}