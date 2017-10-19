package fr.wcs.viaferrata;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by wilderjm on 27/09/17.
 */

public class Tab3Photo extends Fragment {

    private final String TAG = "TEST";

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
    private Button mCancel;
    private ImageView mImageView;
    private ImageView mImageViewTest;
    private Uri mFilePath;
    private StorageReference mStorageReference;
    public FirebaseStorage mStorage;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private String mViaName = "";
    private String imageName;
    private Bitmap mThumbNail;
    private FloatingActionButton mFloatingActionButton;
    private AlertDialog dialog;
    private ProgressBar mProgressBar;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.tab3photo, container, false);

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mUploadImage = (Button) rootview.findViewById(R.id.cancelAction);
        mFloatingActionButton = (FloatingActionButton) rootview.findViewById(R.id.floatingActionButton);


        recyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        Intent intent = getActivity().getIntent();
        final ViaFerrataModel maviaferrata = (ViaFerrataModel) intent.getParcelableExtra("via");
        mViaName = maviaferrata.getNom();

        //RECUPERATION DES PHOTOS
        final ArrayList<String> photoList = new ArrayList<>();
        DatabaseReference galleryPhotoRef = mDatabase.getReference("photos");
        galleryPhotoRef.orderByChild("viaName").equalTo(mViaName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot photoSnapshot : dataSnapshot.getChildren()){
                    PhotoModel myPhotoModel = photoSnapshot.getValue(PhotoModel.class);

                    photoList.add(myPhotoModel.getPhotoUri());

           /*         String uri = myPhotoModel.getPhotoUri();
                    Log.d("test", "photolist image trouvee en bdd "+ uri);
                    StorageReference gsReference = mStorage.getReferenceFromUrl(uri);
                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(gsReference)
                            .into(mImageViewTest);*/

                }
                //creating adapter
                adapter = new GalleryAdapter(getActivity(), photoList);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Alert dialog on floating action button

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo,container,false);
                mTakeImage = (Button) mView.findViewById(R.id.takeImage);
                mSelectImage = (Button) mView.findViewById(R.id.selectImage);
                mImageView = (ImageView) mView.findViewById(R.id.imageSelected);
                mCancel = (Button) mView.findViewById(R.id.cancelAction);
                mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar2);
                mProgressBar.setVisibility(View.GONE);

                //take picture from camera
                mTakeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dispatchTakePictureIntent();
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

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();

                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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
            checkPermission();
        }

    }


    String mCurrentPhotoPath;
    Uri mCurrentPhotoUri;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCurrentPhotoUri = FileProvider.getUriForFile(getActivity(),
                        "fr.wcs.viaferrata.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, TAKE_IMAGE_REQUEST);
            }
        }
    }

    /*private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(),matrix, true);
    }*/

    /*private Bitmap setReducedImageSize(){
        int targetImageViewWidht = mImageView.getWidth();
        int targetImageViewHeight = mImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);

        int cameraImageWidth =bmOptions.outWidth;
        int cameraImageHeight =bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewHeight, cameraImageHeight/targetImageViewHeight);
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
    }*/


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


//        mThumbNail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        uploadFromPath(mCurrentPhotoUri);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),mCurrentPhotoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mImageView.setImageBitmap(bitmap);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //La personne a accepté les permissions
                    checkPermission();
                } else {

                    //La personne a refusé les permissions, on re-demande en boucle
                    //TODO: Afficher toast à la place pour expliquer pourquoi ca ne marchera pas



                }
            }
        }
    }

    public void uploadFromPath(final Uri path) {
        if (path != null) {



            StorageReference viaRef = mStorageReference.child("image/" + mViaName.replace(" ", "_") + "/" + path.getLastPathSegment());
            viaRef.putFile(path)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.GONE);

                            Toast.makeText(getActivity().getApplicationContext(), "Image envoyée ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
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

                            float progress = 100f * ((float)taskSnapshot.getBytesTransferred() / (float)taskSnapshot.getTotalByteCount());
                            System.out.println("Upload is " + progress + "% done");
                            int currentProgress = (int) progress;
                            mProgressBar.setProgress(currentProgress);
                            mProgressBar.setVisibility(View.VISIBLE);



                        }
                    });


            DatabaseReference imageRef = mDatabase.getReference("photos");
            PhotoModel newPhoto = new PhotoModel(mViaName,viaRef.toString());
            imageRef.push().setValue(newPhoto);

        }
    }
}