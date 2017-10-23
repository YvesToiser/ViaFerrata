package fr.wcs.viaferrata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static fr.wcs.viaferrata.FullScreenViewActivity.myVia;
import static fr.wcs.viaferrata.Tab3Photo.mViaName;

/**
 * Created by wilderjm on 22/10/17.
 */

public class FullScreenAdapter extends PagerAdapter{
    private Activity _activity;
    private List<String> uploads;
    public FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private LayoutInflater inflater;
    private Context context;

    // constructor
    public FullScreenAdapter(Context context, Activity activity, List<String> uploads) {
        this.uploads = uploads;
        this._activity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.uploads.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        Button btnClose;

//        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
//                false);

        View viewLayout = LayoutInflater.from(context).inflate(R.layout.layout_fullscreen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position), options);
        imgDisplay.setImageBitmap();*/
        String upload = uploads.get(position);
        StorageReference gsReference = mStorage.getReferenceFromUrl(upload);
        Glide.with(context).using(new FirebaseImageLoader()).load(gsReference).into(imgDisplay);

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // _activity.finish();
                Intent intent = new Intent(context, ViaActivity.class);
                intent.putExtra("via", myVia);
                intent.putExtra("fragment", "photo");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}
