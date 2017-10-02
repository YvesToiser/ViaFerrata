package fr.wcs.viaferrata;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wilderjm on 27/09/17.
 */

public class Tab1General extends Fragment{

    private int idVia;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.tab1general, container, false);

        final TextView depart = (TextView) rootview.findViewById(R.id.altDepart);
        final TextView arrivee = (TextView) rootview.findViewById(R.id.altArrivee);
        final TextView temps = (TextView) rootview.findViewById(R.id.tempsVia);
        final TextView niveau = (TextView) rootview.findViewById(R.id.lvlVia);

        //get the id from intent
        //idVia = getIntent().getIntExtra("id", 0);
        idVia = 1;

        //
        final DatabaseReference maDatabase;
        maDatabase = FirebaseDatabase.getInstance().getReference();
        Query mQueryRef = maDatabase.child("viaFerrata").child(String.valueOf(idVia));

        mQueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ViaFerrataModel maviaferrata = dataSnapshot.getValue(ViaFerrataModel.class);
                String nom = maviaferrata.getNom();
                //setTitle(nom);
                String altitudeDepart = maviaferrata.getAltitudeDepart();
                depart.setText(altitudeDepart);
                String altitudeArrivee = maviaferrata.getAltitudeArrivee();
                arrivee.setText(altitudeArrivee);
                String tempsvia = maviaferrata.getHoraireDuree();
                temps.setText(tempsvia);
                int niveauvia = maviaferrata.getDifficulte();
                niveau.setText(String.valueOf(niveauvia));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return rootview;


    }
}
