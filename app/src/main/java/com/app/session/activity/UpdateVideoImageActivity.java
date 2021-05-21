package com.app.session.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.utility.Constant;
import com.app.session.utility.Utility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.app.session.utility.Utility.log;

public class UpdateVideoImageActivity extends BaseActivity
{

    private Uri mCameraImageUri, mImageCaptureUri;

    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video_image);
        type=getIntent().getStringExtra(Constant.TYPE);
        if(type.equals(Constant.REQ_GALLERY))
        {
            Gallery();
        }
        else
        {
            TakePic();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CAMERA:


                    CropImage.activity(mCameraImageUri)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setAspectRatio(2, 1)
                            .start(this);


                    break;

                case Constant.REQUEST_CODE_ALBUM:
                    try {
//                        String PicturePath="";
                        mImageCaptureUri = data.getData();
//                        Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageCaptureUri);
//                        bm = ConvetBitmap.Mytransform(bm);
//                        bm = Utility.rotateImage(bm, new File(mImageCaptureUri.getPath()));
//                        imgBriefCV.setImageBitmap(bm);
                        CropImage.activity(mImageCaptureUri)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setAspectRatio(2, 1)
                                .start(this);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        Intent intent=new Intent();
                        intent.putExtra("URI",resultUri);
                        setResult(Constant.REQUEST_IMAGE,intent);
                        finish();
                    }
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                    log("getting erroor");
                    break;


                default:
                    finish();
                    break;


            }
        }
        else
        {
            finish();
        }

    }

    public void Gallery() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.REQUEST_CODE_ALBUM);

        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constant.REQUEST_CODE_ALBUM);
        }
    }
    public void TakePic() {

        try {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory() + "/consultlot.png");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mCameraImageUri = Uri.fromFile(f);
            } else {
                mCameraImageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraImageUri);
            cameraIntent.putExtra("return-data", true);
            startActivityForResult(cameraIntent, Constant.REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}