package fr.wcs.viaferrata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        viewHolder.textVille.setText(viaFerrataModel.getVille() + " (" + viaFerrataModel.getDptNb() + ") ");
        viewHolder.textDiff.setText(viaFerrataModel.getDifficulteInLetters());

        return convertView;
    }

    private class ViewHolder {
        TextView textName;
        TextView textVille;
        TextView textDiff;

        public ViewHolder(View view) {
            textName = view.findViewById(R.id.textNom);
            textVille = view.findViewById(R.id.textVille);
            textDiff = view.findViewById(R.id.textDiff);
        }
    }
}