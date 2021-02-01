package com.example.smnaggregator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Objects;

import static com.facebook.appevents.AppEventsLogger.getUserData;

public class MakePostActivity  extends AppCompatActivity {

    private static final String TAG = "MakePostActivity";

    private EditText contentTxt;
    private ImageButton addImageBtn;
    private ImageView imageView;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch fbSwitchBtn;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch instSwitchBtn;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch twitterSwitchBtn;

    private CallbackManager fbManager;
    private ShareDialog shareDialog = new ShareDialog(this);

    private Button shareBtn;

    private Uri imageUri;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_CODE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        contentTxt = findViewById(R.id.makePostTxt);
        addImageBtn = findViewById(R.id.addImageButton);
        imageView = findViewById(R.id.imageView);
        fbSwitchBtn = findViewById(R.id.fbPostSwitch);
        instSwitchBtn = findViewById(R.id.instagramPostSwitch);
        twitterSwitchBtn = findViewById(R.id.twitterPostSwitch);
        shareBtn = findViewById(R.id.publishPostButton);


        //handle addImageBtn button click//
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
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
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission())
                if (fbSwitchBtn.isChecked()) {
                    FacebookSdk.fullyInitialize();
                    if (imageView.getDrawable() != null) {
                        fbManager = CallbackManager.Factory.create();

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ClipboardManager clipboardManager= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData data = ClipData.newPlainText("FacebookText",contentTxt.getText().toString());
                        clipboardManager.setPrimaryClip(data);

                        Toast.makeText(MakePostActivity.this,"Copied to Clipboard",Toast.LENGTH_SHORT).show();


                        if (shareDialog.canShow(SharePhotoContent.class)){
                            //anoigoyme parathyro me eikona alla to keimeno
                            //tha grafei kateytheian sto fb//
                            SharePhoto sharePhoto = new  SharePhoto.Builder().setBitmap(bitmap).build();
                            SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                            shareDialog.show(sharePhotoContent);

                        }

                        shareDialog.registerCallback(fbManager, new FacebookCallback<Sharer.Result>() {

                            @Override
                            public void onSuccess(Sharer.Result result) {
                                Toast.makeText(MakePostActivity.this, "Share Successful", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(MakePostActivity.this, "Share canceled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Toast.makeText(MakePostActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        //to setContentDescription einai deprecated opote de mporoyme
                        //na anebasoyme keimeno apo tin efarmogi para mono na anoiksoyme
                        //parathyro gia na grapsoyme kateytheian sto fb
                        //anoigoyme parathyro xwris eikona
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(""))
                                .build();
                        shareDialog.show(content);

                    }
                }

                if (twitterSwitchBtn.isChecked()) {
                    if (imageView.getDrawable() != null) {
                        TweetComposer.Builder builder = new TweetComposer.Builder(MakePostActivity.this)
                                .text(contentTxt.getText().toString())
                                .image(imageUri);
                        builder.show();
                        getUserData();
                    }else{
                        TweetComposer.Builder builder = new TweetComposer.Builder(MakePostActivity.this)
                                .text(contentTxt.getText().toString());
                        builder.show();
                        getUserData();
                    }
                }

                if(instSwitchBtn.isChecked()){
                    if(imageView.getDrawable() != null ){
                        createInstagramIntent();
                    }
                    else{
                        Toast.makeText(MakePostActivity.this, "Instagram needs photo!!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Instagram feed needs photo not text only");
                    }
                }

            }
        });


    }

    private void createInstagramIntent(){
        //create the new intent using the'send action
        Intent instagram = new Intent(Intent.ACTION_SEND);

        //set the MIME type
        instagram.setType("image/*");

        //Add the URI to the Intent
        instagram.putExtra(Intent.EXTRA_STREAM, imageUri);
        instagram.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        instagram.setPackage("com.instagram.android");

        //Broadcast the Intent
        startActivity(Intent.createChooser(instagram, "Share to"));
    }


    private void pickImageFromGallery(){
        //intent to pick image
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
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
                Toast.makeText(MakePostActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }

        }
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to imageview
            Uri selectedImage = data.getData();
            imageUri = selectedImage;
         //   imageView.setImageURI(data.getData());
            // solution with picasso library makes fatal exception error because large bitmap
            //so we use the Gridle library which supports and larger files
            Glide.with(this).load(selectedImage).into(imageView);
        }

    }


    protected boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

}
