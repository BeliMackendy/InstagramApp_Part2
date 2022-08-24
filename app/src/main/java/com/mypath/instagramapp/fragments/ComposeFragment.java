package com.mypath.instagramapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mypath.instagramapp.Post;
import com.mypath.instagramapp.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class ComposeFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "ComposeFragment";

    private Button btTakepicture;
    private Button btPost;
    private ImageView ivPostimage;
    private EditText etCapture;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private ProgressBar pb;
    private RelativeLayout rlTakepost;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btTakepicture = view.findViewById(R.id.btTakepicture);
        btPost = view.findViewById(R.id.btPost);
        etCapture = view.findViewById(R.id.etCapture);
        ivPostimage = view.findViewById(R.id.ivPostimage);
        rlTakepost = view.findViewById(R.id.rlTakepost);
        pb = view.findViewById(R.id.pbLoading);

        btTakepicture.setOnClickListener(view1 -> onLaunchCamera());

        btPost.setOnClickListener(view12 -> {
            String description = etCapture.getText().toString().trim();

            if(description.isEmpty()){
                Toast.makeText(getContext(), "Caption Cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if(photoFile==null || ivPostimage.getDrawable()==null  ){
                Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser user = ParseUser.getCurrentUser();
            savePost(description,user,photoFile);
        });

    }


    private void savePost(String description, ParseUser user, File photoFile) {
        Post post = new Post();

        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(user);


        pb.setVisibility(ProgressBar.VISIBLE);

        post.saveInBackground(e -> {
            if(e!=null){
                Log.e(TAG, "Error while saving", e);
                Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "Post save was successful!!! ");
            etCapture.setText("");
            ivPostimage.setImageResource(0);


            btPost.setVisibility(View.INVISIBLE);
            rlTakepost.setVisibility(View.INVISIBLE);
            btTakepicture.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), "Save", Toast.LENGTH_SHORT).show();
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
        Uri fileProvider = FileProvider.getUriForFile(requireContext(), "com.mypath.instagramapp", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
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
                        rlTakepost.setVisibility(View.VISIBLE);
                        btTakepicture.setVisibility(View.INVISIBLE);
                    } else { // Result was a failure
                        Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}