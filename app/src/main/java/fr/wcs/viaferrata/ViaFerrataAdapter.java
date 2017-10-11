package fr.wcs.viaferrata;

import android.content.Context;
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
        ViewHolder viewHolder;

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

        viewHolder.imgViewFav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
                    // Your code that you want to execute on this button click
                    Toast.makeText(context, "Mazette! ça marche!", Toast.LENGTH_SHORT).show();
                }

            });
        viewHolder.imgViewDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                // Your code that you want to execute on this button click
                Toast.makeText(context, "Le bouton Done aussi!", Toast.LENGTH_SHORT).show();
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