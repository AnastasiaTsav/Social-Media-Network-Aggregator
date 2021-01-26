package com.example.smnaggregator;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareStoryContent;
import com.facebook.share.widget.ShareDialog;

public class MakeStoryActivity extends AppCompatActivity {

    private static final String TAG = "MakeStoryActivity";
    private ImageView storyImage;
    private ImageButton addImageStoryBtn;
    private Switch fbStory;
    private Switch instagramStory;
    private Switch twitterStory;
    private Button shareStory;

    private Uri imageUri;

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
                if(instagramStory.isChecked()){
                    if(storyImage.getDrawable() != null){
                        createStoryInstagramIntent();
                    }
                    else{
                        Toast.makeText(MakeStoryActivity.this, "Instagram needs a photo  or a video", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Instagram story needs photo");
                    }
                }
                if(twitterStory.isChecked()){
                    //create the new Intent using the send's action
                    Intent twitterIntent = new Intent(Intent.ACTION_SEND);
                    //set the MIME type
                    twitterIntent.setType("image/*");
                    //Add the URI to intent
                    twitterIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    twitterIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(twitterIntent, "share to fleet"));
                }
            }
        });


    }

    private void createStoryInstagramIntent() {
        //Define background asset URI
        Uri backgroundAssetUri = Uri.parse(imageUri.toString());
        //Instatiate implicit intent with ADD_TO_STORY action and background asset
        Intent storyIntent = new Intent("com.instagram.share.ADD_TO_STORY");
        storyIntent.setDataAndType(backgroundAssetUri, "image/*");
        storyIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        storyIntent.setPackage("com.instagram.android");
        //Instantiate activity and verify it will resolve implicit intent
        if(this.getPackageManager().resolveActivity(storyIntent, 0) != null){
            this.startActivityForResult(storyIntent, 0);
        }
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
            imageUri = selectedImage;
           // solution with picasso library makes fatal exception error because large bitmap
            //so we use the Gridle library which supports and larger files
            //storyImage.setImageURI(selectedImage); //
           Glide.with(this).load(selectedImage).into(storyImage);
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




}