package app.teeramet.money.moneydiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import app.teeramet.moneydiary.R;

/**
 * Created by barbie on 17/8/2559.
 */
public class ImageManage {
    private  final String TAG ="File :";
    public static final int CAMERA_CODE = 100;
    public static final int GALLERY_CODE = 101;

    Bitmap selectedImage;
    private Context context;
    private String pathString;
    private Activity activity;

    public ImageManage() {
    }

    public ImageManage(Context context) {
        this.context = context;
    }

    public ImageManage(Activity activity) {
        this.activity = activity;
    }

    public ImageManage(Context context, Activity activity) {
        this.context=context;
        this.activity=activity;
    }


    public void selectImage() {
        final String[] LIST = {"Take a Camera", "Choose in Gallery"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Select");
        alert.setItems(LIST, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        activity.startActivityForResult(Intent.createChooser(intent, "Take a Pic"), CAMERA_CODE);
                    }

                } else if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    activity.startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_CODE);
                }
            }
        });
        alert.create().show();
    }
    public String Gallery(Intent data) {
        String pathPackage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"
                + Environment.getDataDirectory() + "/" + context.getPackageName();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "IMG" + time + ".jpg";


        InputStream imageStream;
        Uri imageUri;

        File dir = new File(pathPackage + "/IMAGE");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File createFile = new File(dir.getAbsolutePath() + "/" + filename);

        try {
            createFile.createNewFile();
            FileOutputStream fileOutputStream=new FileOutputStream(pathPackage+"/IMAGE/"+filename);
            imageUri = data.getData();  //receive Uri
            imageStream = context.getContentResolver().openInputStream(imageUri); //Uri > Input Stream
            selectedImage = BitmapFactory.decodeStream(imageStream);  //Input Stream > Bitmap
            ByteArrayOutputStream stream=new ByteArrayOutputStream();  //Bitmap > Byte array
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,stream);  //

            byte[] bytes=stream.toByteArray();
            fileOutputStream.write(bytes);  //Byte array > file;
            fileOutputStream.close();

            pathString = createFile.getAbsolutePath();

        } catch (FileNotFoundException e) {
            Log.i(TAG, "File not Found!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathString;
    }

    public void Camera(Intent data) {

        Bundle extras = data.getExtras();
        Bitmap selectedImage = (Bitmap) extras.get("data");
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String Filename = "IMG" + time + "camera.jpg";

        try {
            ByteArrayOutputStream fileoutput = new ByteArrayOutputStream();
            File FileCreate = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + Filename);
            FileCreate.createNewFile();

            FileOutputStream streamOutput = new FileOutputStream(FileCreate);// FileOutputStream streamOutput;
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, streamOutput);
            streamOutput.write(fileoutput.toByteArray());
            streamOutput.close();
        } catch (Exception e) {
            Log.e("OnCamera", e.getMessage());
        }
    }



    public String saveImageToStorage(Intent data) {
        String pathPackage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"
                + Environment.getDataDirectory() + "/" + context.getPackageName();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "IMG" + time + ".jpg";

        Bundle extras = data.getExtras();
        selectedImage = (Bitmap) extras.get("data");
        File dir = new File(pathPackage + "/IMAGE");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            ByteArrayOutputStream fileoutput = new ByteArrayOutputStream();


            File createFile = new File(dir.getAbsolutePath() + "/" + filename);
            createFile.createNewFile();

            FileOutputStream streamOutput = new FileOutputStream(createFile);// FileOutputStream streamOutput;
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, streamOutput);
            streamOutput.write(fileoutput.toByteArray());
            streamOutput.close();
            pathString = createFile.getAbsolutePath();

        } catch (Exception e) {
            e.getMessage();
        }

        return pathString;


    }
    public Transformation tranform() {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(50)
                .oval(true)
                .build();
        return transformation;
    }

    public int randomColorBackground() {
        int[] resId = {R.drawable.oval, R.drawable.oval1, R.drawable.oval2, R.drawable.oval3
                , R.drawable.oval4, R.drawable.oval5};
        Random rand=new Random();
        int random=rand.nextInt(5);
        return resId[random];
    }

}
