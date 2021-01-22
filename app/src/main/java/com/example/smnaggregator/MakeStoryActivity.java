package com.example.smnaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Share;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareStoryContent;
import com.facebook.share.widget.ShareDialog;

public class MakeStoryActivity extends AppCompatActivity {

    private static final String TAG = "MakeStoryActivity";
    private ImageView storyImage;
    private VideoView storyVideo;
    private ImageButton addImageStoryBtn;
    private Switch fbStory;
    private Switch instagramStory;
    private Switch twitterStory;
    private Button shareStory;

    private CallbackManager fbManager;
    private ShareDialog shareDialog = new ShareDialog(this);

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_CODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_story);

        storyImage = findViewById(R.id.storyImageView);
        storyVideo = findViewById(R.id.videoView);
        addImageStoryBtn = findViewById(R.id.addStoryImageButton);
        fbStory = findViewById(R.id.fbStoryButton);
        instagramStory = findViewById(R.id.instaStoryButton);
        twitterStory = findViewById(R.id.twitterStoryButton);
        shareStory = findViewById(R.id.shareStoryButton);

        //handle addImageStoryBtn//
        addImageStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os is less than marshmallow
                    pickImageFromGallery();

                }
            }

        });

        shareStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    if(fbStory.isChecked()){
                        FacebookSdk.fullyInitialize();
                        fbManager = CallbackManager.Factory.create();
                        //model an image background asset
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) storyImage.getDrawable();
                        Bitmap image = bitmapDrawable.getBitmap();

                        if(shareDialog.canShow(ShareStoryContent.class)){
                            SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
                            //add to ShareStoryContent
                            ShareStoryContent shareStoryContent = new ShareStoryContent.Builder().setBackgroundAsset(photo).build();
                            shareDialog.show(shareStoryContent);
                        }

                        shareDialog.registerCallback(fbManager, new FacebookCallback<Sharer.Result>() {

                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(MakeStoryActivity.this, "Share FB Story Successful", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Share FB story successful");
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(MakeStoryActivity.this, "Share FB  story cancelled", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Share FB Story cancelled");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.e(TAG, error.getMessage() + "error in facebook story sharing..");
                            }
                        });


                    }
                }
            }
        });





    }



    private void pickImageFromGallery(){
        //intent to pick image
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to imageview
            Uri selectedImage = data.getData();
            storyImage.setImageURI(selectedImage);
        }
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                pickImageFromGallery();
            } else {
                //permission was denied
                Toast.makeText(this, "Permission denied..!", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode==WRITE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
        else if(requestCode==READ_EXTERNAL_STORAGE_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.e("value","Permission already granted");
                //check here code is needed or not
            } else
            {
                Log.e("value", "Permission Denied, You cannot use local drive .");
                Toast.makeText(MakeStoryActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }
    }

    protected boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }

    //file
    protected void requestPermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(MakeStoryActivity.this, "Read External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }


}