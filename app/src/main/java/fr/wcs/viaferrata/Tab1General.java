package fr.wcs.viaferrata;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "Tab1";



    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.tab1generalb, container, false);

        final TextView name = rootview.findViewById(R.id.viaName);
        final TextView ville = rootview.findViewById(R.id.viaVille);
        final TextView departement =  rootview.findViewById(R.id.viaDepartement);
        final TextView region =  rootview.findViewById(R.id.viaRegion);
        final TextView niveau =  rootview.findViewById(R.id.viaNiveau);
        final TextView passerelles =  rootview.findViewById(R.id.viaPasserelles);
        final TextView pont =  rootview.findViewById(R.id.viapontSinge);
        final TextView echelle =  rootview.findViewById(R.id.viaEchelle);
        final TextView tyrolienne =  rootview.findViewById(R.id.viaTyrol);

        //Fonts
        final TextView viaVilleStatic =  rootview.findViewById(R.id.viaVilleStat);
        final TextView viaDepartementStatic =  rootview.findViewById(R.id.viaDepartementStat);
        final TextView viaRegionStatic =  rootview.findViewById(R.id.viaRegionStat);
        final TextView viaNiveauStatic =  rootview.findViewById(R.id.viaNiveauStatic);

        Typeface myTitlesFont = Typeface.createFromAsset(getContext().getAssets(), "Fonts/Montserrat-Medium.ttf");
        viaVilleStatic.setTypeface(myTitlesFont);
        viaDepartementStatic.setTypeface(myTitlesFont);
        viaRegionStatic.setTypeface(myTitlesFont);
        viaNiveauStatic.setTypeface(myTitlesFont);

        Typeface myTextFont = Typeface.createFromAsset(getContext().getAssets(), "Fonts/Quicksand-Regular.ttf");
        name.setTypeface(myTextFont);
        ville.setTypeface(myTextFont);
        departement.setTypeface(myTextFont);
        region.setTypeface(myTextFont);
        niveau.setTypeface(myTextFont);
        passerelles.setTypeface(myTextFont);
        pont.setTypeface(myTextFont);
        echelle.setTypeface(myTextFont);
        tyrolienne.setTypeface(myTextFont);


        Intent intent = getActivity().getIntent();
        ViaFerrataModel maviaferrata = (ViaFerrataModel) intent.getParcelableExtra("via");
        Log.d(TAG, "Object Tagg " + maviaferrata);


        String nomVia = maviaferrata.getNom();
        name.setText(nomVia);

        String villeVia = maviaferrata.getVille();
        ville.setText(villeVia);

        String depVia = maviaferrata.getDptNom();
        departement.setText(depVia);

        String regionVia = maviaferrata.getRegion();
        region.setText(regionVia);

        String niveauvia = maviaferrata.getDifficulteInWords();
        niveau.setText(niveauvia);

        String nbPasserelles = maviaferrata.getNbPasserelle();
        passerelles.setText(nbPasserelles);

        String nbPont = maviaferrata.getNbPontSinge();
        pont.setText(nbPont);

        String nbEchellesFillets = maviaferrata.getNbEchelleFilet();
        echelle.setText(nbEchellesFillets);

        String nbTyroliennes = maviaferrata.getNbTyrolienne();
        tyrolienne.setText(nbTyroliennes);

        return rootview;



    }

}
