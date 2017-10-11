package fr.wcs.viaferrata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static fr.wcs.viaferrata.HomeActivity.mySharedPref;

public class ViaFerrataAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ViaFerrataModel> item;

    public ViaFerrataAdapter(Context context, ArrayList<ViaFerrataModel> item){
        this.context = context;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_via, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ViaFerrataModel viaFerrataModel = (ViaFerrataModel) getItem(position);

        viewHolder.textName.setText(viaFerrataModel.getNom());
        viewHolder.textDpt.setText(viaFerrataModel.getDptNom());
        viewHolder.textDiff.setText(viaFerrataModel.getDifficulteInLetters());
        mySharedPref = context.getSharedPreferences("SP",MODE_PRIVATE);
        final String favId = "Fav" + viaFerrataModel.getNom();
        final String doneId = "Done" + viaFerrataModel.getNom();
        final String viaName = viaFerrataModel.getNom();
        boolean isFavorite = mySharedPref.getBoolean(favId, false);
        if(isFavorite){
            viewHolder.imgViewFav.setImageResource(R.drawable.etoilechecked);
        }else{
            viewHolder.imgViewFav.setImageResource(R.drawable.etoileunchecked);
        }
        boolean isDone = mySharedPref.getBoolean(doneId, false);
        if(isDone){
            viewHolder.imgViewDone.setImageResource(R.drawable.etoilechecked);
        }else{
            viewHolder.imgViewDone.setImageResource(R.drawable.ic_done);
        }


        viewHolder.imgViewFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean isFavorite = mySharedPref.getBoolean(favId, false);
                boolean isFavNewValue = !isFavorite;
                String toastMessage;
                if(isFavorite){
                    toastMessage = viaName+" "+"ne fait plus partie de vos favoris.";
                }else{
                    toastMessage = viaName+" "+"a été ajoutée à vos favoris.";
                }
                Toast toastFavorite = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
                toastFavorite.show();
                mySharedPref.edit().putBoolean(favId, isFavNewValue).apply();
                final boolean isFavoriteNow = mySharedPref.getBoolean(favId, false);
                if(isFavoriteNow){
                    viewHolder.imgViewFav.setImageResource(R.drawable.etoilechecked);
                }else{
                    viewHolder.imgViewFav.setImageResource(R.drawable.etoileunchecked);
                }
            }
        });
        viewHolder.imgViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean isDone = mySharedPref.getBoolean(doneId, false);
                boolean isDoneNewValue= !isDone;
                String toastMessage;
                if(isDone){
                    toastMessage = "Vous n'avez pas fait la via Ferrata"+" : "+viaName+".";
                }else{
                    toastMessage = "Vous avez fait la via Ferrata"+" : "+viaName+".";
                }
                Toast toastDone = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
                toastDone.show();
                mySharedPref.edit().putBoolean(doneId, isDoneNewValue).apply();
                final boolean isDoneNow = mySharedPref.getBoolean(doneId, false);

                if(isDoneNow){
                    viewHolder.imgViewDone.setImageResource(R.drawable.etoilechecked);
                }else{
                    viewHolder.imgViewDone.setImageResource(R.drawable.ic_done);
                }
            }
        });
        return convertView;
    }



    private class ViewHolder {
        TextView textName;
        TextView textDpt;
        TextView textDiff;
        ImageView imgViewFav;
        ImageView imgViewDone;


        public ViewHolder(View view) {
            textName = view.findViewById(R.id.textNom);
            textDpt = view.findViewById(R.id.textDpt);
            textDiff = view.findViewById(R.id.textDiff);
            imgViewFav = view.findViewById(R.id.imageViewFavori);
            imgViewDone = view.findViewById(R.id.imageViewDone);

        }
    }
}