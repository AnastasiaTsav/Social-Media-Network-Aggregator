package com.example.smnaggregator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.StrongBoxUnavailableException;
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
import androidx.fragment.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class MakePostActivity extends FragmentActivity {

    private EditText contentTxt;
    private ImageButton addImageBtn;
    private ImageView imageView;

    private Switch fbSwitchBtn;
    private Switch instSwitchBtn;
    private Switch twitterSwitchBtn;

    private CallbackManager fbManager;
    private ShareDialog shareDialog = new ShareDialog(this);

    private Button shareBtn;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    //system os is less than marshmallow
                    pickImageFromGallery();

                }
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.fullyInitialize();

                        if (fbSwitchBtn.isChecked()){
                            fbManager= CallbackManager.Factory.create();


                            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                            Bitmap bitmap = bitmapDrawable.getBitmap();



                            if (shareDialog.canShow(SharePhotoContent.class)){
                                SharePhoto sharePhoto = new  SharePhoto.Builder().setBitmap(bitmap).build();
                                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                                shareDialog.show(sharePhotoContent);

                            }

                            shareDialog.registerCallback(fbManager,new FacebookCallback<Sharer.Result>(){

                                @Override
                                public void onSuccess(Sharer.Result result) {
                                    Toast.makeText(MakePostActivity.this,"Share Successful",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(MakePostActivity.this,"Share canceled",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Toast.makeText(MakePostActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });

            }




    private void pickImageFromGallery() {
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
        }
    }

    //handle result of picked image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to imageview
            imageView.setImageURI(data.getData());
        }

    }
    //handle result of facebook callbackManager

}
