package fr.wcs.viaferrata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

import static fr.wcs.viaferrata.Tab3Photo.mViaName;
import static fr.wcs.viaferrata.Tab3Photo.maviaferrata;

/**
 * Created by wilderjm on 19/10/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    private ImageView mImage;
    private Context context;
    public static List<String> uploads;
    public FirebaseStorage mStorage = FirebaseStorage.getInstance();



    public GalleryAdapter(Context context, List<String> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProgressBar progressBar = holder.loadingProgressBar;
        progressBar.setVisibility(View.VISIBLE);
        String upload = uploads.get(position);
        StorageReference gsReference = mStorage.getReferenceFromUrl(upload);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("tag", "fail loading Image");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .placeholder(R.drawable.placeholdervia)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;
        public ProgressBar loadingProgressBar;

        public ViewHolder(final View itemView) {
            super(itemView);
            loadingProgressBar = (ProgressBar)itemView.findViewById(R.id.progressImageLoad);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewOfViaG);
        //    imageView.setMaxHeight(imageView.getWidth());
       //     imageView.setMinimumHeight(imageView.getWidth());
            Log.e("itemview WIDTH", String.valueOf(itemView.getWidth()));
            Log.e("itemview HEIGTH", String.valueOf(itemView.getHeight()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullScreenViewActivity.class);
                    intent.putExtra("name", mViaName);
                    intent.putExtra("id", getAdapterPosition());
                    intent.putExtra("via", maviaferrata);
                    context.startActivity(intent);
                }
            });
        }
    }
}


