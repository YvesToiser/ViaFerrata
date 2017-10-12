package fr.wcs.viaferrata;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wilderjm on 27/09/17.
 */

public class Tab3Photo extends Fragment {


    public static final String VIA_STORAGE_PATH = "image/";
    public static final String VIA_DATABASE_PATH = "image/";
    public static final int REQUEST_CODE = 1234;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 99;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 98;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int TAKE_IMAGE_REQUEST = 4;
    private static final int CAMERA_IMAGE_REQUEST = 101;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private Button mTakeImage;
    private Button mSelectImage;
    private Button mUploadImage;
    private ImageView mImageView;
    private Uri mFilePath;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private String mViaName = "";
    private String imageName;
    private Bitmap mThumbNail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tab3photo, container, false);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mTakeImage = (Button) rootview.findViewById(R.id.takeImage);
        mSelectImage = (Button) rootview.findViewById(R.id.selectImage);
        mUploadImage = (Button) rootview.findViewById(R.id.uploadImage);
        mImageView = (ImageView) rootview.findViewById(R.id.imageSelected);


        Intent intent = getActivity().getIntent();
        final ViaFerrataModel maviaferrata = (ViaFerrataModel) intent.getParcelableExtra("via");
        mViaName = maviaferrata.getNom();


        //take picture from camera
        mTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, TAKE_IMAGE_REQUEST);
            }
        });


        //choose picture from gallery
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selectionner une image"), PICK_IMAGE_REQUEST);
            }
        });


        //Upload image
        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            mFilePath = data.getData();
            uploadFromPath(mFilePath);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mFilePath);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == TAKE_IMAGE_REQUEST) {
            //PERMISSIONS WRITE EXTERNAL
            mThumbNail = (Bitmap) data.getExtras().get("data");
            checkPermission();


            mImageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
        }

    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    PERMISSIONS, PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }
        //si la personne arrive ici elle a les droits

        //on recupere image suite à acces des permission
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mThumbNail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File dest = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            dest.createNewFile();
            fo = new FileOutputStream(dest);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO get the file path!!!
        mFilePath = Uri.fromFile(dest);
        uploadFromPath(mFilePath);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //La personne a refusé les permissions, on re-demande en boucle
                    //TODO: Afficher toast à la place pour expliquer pourquoi ca ne marchera pas
                    //checkPermission();
                } else {
                    //La personne a accepté les permissions

                    checkPermission();

                }
            }
        }
    }

    public void uploadFromPath(final Uri path) {
        if (path != null) {

            final ProgressBar progressDialog = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleSmall);


            //
            StorageReference viaRef = mStorageReference.child("image/" + mViaName + "/" + path.getLastPathSegment());
            viaRef.putFile(path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), "File Uploaded "+path.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //progressDialog.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            System.out.println("Upload is " + progress + "% done");
                            int currentprogress = (int) progress;
                            //progressDialog.setVisibility(View.VISIBLE);
                        }
                    })
            ;

        } else {

            //progressDialog.show();
            mImageView.setDrawingCacheEnabled(true);
            mImageView.buildDrawingCache();
            Bitmap bitmap = mImageView.getDrawingCache();
            ByteArrayOutputStream baas = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baas);
            byte[] data = baas.toByteArray();
            StorageReference viaRef = mStorageReference.child("image/" + mViaName + "/");
            viaRef.putBytes(data)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getActivity().getApplicationContext(), "File Uploaded "+path.toString(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            System.out.println("Upload is " + progress + "% done");
                            int currentprogress = (int) progress;

                        }
                    })
            ;

            //Toast.makeText(getActivity().getApplicationContext(), "selectionnez une image", Toast.LENGTH_LONG).show();
        }
    }

}



    /*//Fonction appareil photo
    private static final int TAKE_PICTURE = 1;
    private void takePicture(){
        mTakeImage.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PICTURE);


        });
    }




    //Fonction Acces gallerie
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selectionner une image"), PICK_IMAGE_REQUEST);
    }
    //Fonction Upload
    private void uploadFile(){

        if(filePath != null){

            final ProgressBar progressDialog = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleSmall);


            //
            StorageReference viaRef = mStorageReference.child("image/" + mViaName +"/"+ filePath.getLastPathSegment());

            viaRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity().getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            System.out.println("Upload is " + progress + "% done");
                            int currentprogress = (int) progress;
                            progressDialog.setProgress(currentprogress);
                        }
                    })
            ;

        }else {
            //display error toast
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                &&data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {

        if (v == mSelectImage){
            //open file choser
            showFileChooser();
        }else if (v == mUploadImage){
            //upload file
            uploadFile();
        }*//**//*else if (v == mTakeImage){
            takePicture();*//**//*

        }
    }*/