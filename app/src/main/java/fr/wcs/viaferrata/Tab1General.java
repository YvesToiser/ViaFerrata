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

        final TextView name = (TextView) rootview.findViewById(R.id.viaName);
        final TextView prix = (TextView) rootview.findViewById(R.id.viaPrix);
        final TextView ville = (TextView) rootview.findViewById(R.id.viaVille);
        final TextView departement = (TextView) rootview.findViewById(R.id.viaDepartement);
        final TextView region = (TextView) rootview.findViewById(R.id.viaRegion);
        final TextView description = (TextView) rootview.findViewById(R.id.viaDescription);
        final TextView depart = (TextView) rootview.findViewById(R.id.viaAltDep);
        final TextView arrivee = (TextView) rootview.findViewById(R.id.viaAltArr);
        final TextView temps = (TextView) rootview.findViewById(R.id.viaDuree);
        final TextView niveau = (TextView) rootview.findViewById(R.id.viaNiveau);
        final TextView passerelles = (TextView) rootview.findViewById(R.id.viaPasserelles);
        final TextView pont = (TextView) rootview.findViewById(R.id.viapontSinge);
        final TextView echelle = (TextView) rootview.findViewById(R.id.viaEchelle);
        final TextView tyrolienne = (TextView) rootview.findViewById(R.id.viaTyrol);
        final TextView acces = (TextView) rootview.findViewById(R.id.viaAcces);
        final TextView info = (TextView) rootview.findViewById(R.id.viaInfo);

        //Fonts
        TextView viaNameStatic = (TextView) rootview.findViewById(R.id.viaNameStatic);
        final TextView viaVilleStatic = (TextView) rootview.findViewById(R.id.viaVilleStat);
        final TextView viaDepartementStatic = (TextView) rootview.findViewById(R.id.viaDepartementStat);
        final TextView viaRegionStatic = (TextView) rootview.findViewById(R.id.viaRegionStat);
        final TextView viaDescriptionStatic = (TextView) rootview.findViewById(R.id.viaDescriptionStatic);
        final TextView viaAltDepStatic = (TextView) rootview.findViewById(R.id.viaAltDpStatic);
        final TextView viaAltArrStatic = (TextView) rootview.findViewById(R.id.viaAltArrStatic);
        final TextView viaTempsStatic = (TextView) rootview.findViewById(R.id.viaTempsStatic);
        final TextView viaNiveauStatic = (TextView) rootview.findViewById(R.id.viaNiveauStatic);
        final TextView viaPasserelleStatic = (TextView) rootview.findViewById(R.id.viaPasserelleStatic);
        final TextView viaPontSingeStatic = (TextView) rootview.findViewById(R.id.viaPontSingeStatic);
        final TextView viaEchelleStatic = (TextView) rootview.findViewById(R.id.viaEchelleStatic);
        final TextView viaTyrolienneStatic = (TextView) rootview.findViewById(R.id.viaTyrolStatic);
        final TextView viaAccesStatic = (TextView) rootview.findViewById(R.id.viaAccesStatic);
        final TextView viaInfoStatic = (TextView) rootview.findViewById(R.id.viaInfoStatic);
        final TextView viaPrixStatic = (TextView) rootview.findViewById(R.id.viaPrix);

        Typeface myTitlesFont = Typeface.createFromAsset(getContext().getAssets(), "Fonts/Montserrat-Medium.ttf");
        viaNameStatic.setTypeface(myTitlesFont);
        viaVilleStatic.setTypeface(myTitlesFont);
        viaDepartementStatic.setTypeface(myTitlesFont);
        viaRegionStatic.setTypeface(myTitlesFont);
        viaDescriptionStatic.setTypeface(myTitlesFont);
        viaAltDepStatic.setTypeface(myTitlesFont);
        viaAltArrStatic.setTypeface(myTitlesFont);
        viaTempsStatic.setTypeface(myTitlesFont);
        viaNiveauStatic.setTypeface(myTitlesFont);
        viaPasserelleStatic.setTypeface(myTitlesFont);
        viaPontSingeStatic.setTypeface(myTitlesFont);
        viaEchelleStatic.setTypeface(myTitlesFont);
        viaTyrolienneStatic.setTypeface(myTitlesFont);
        viaAccesStatic.setTypeface(myTitlesFont);
        viaInfoStatic.setTypeface(myTitlesFont);
        viaPrixStatic.setTypeface(myTitlesFont);

        Typeface myTextFont = Typeface.createFromAsset(getContext().getAssets(), "Fonts/Quicksand-Regular.ttf");
        name.setTypeface(myTextFont);
        prix.setTypeface(myTextFont);
        ville.setTypeface(myTextFont);
        departement.setTypeface(myTextFont);
        region.setTypeface(myTextFont);
        description.setTypeface(myTextFont);
        depart.setTypeface(myTextFont);
        arrivee.setTypeface(myTextFont);
        temps.setTypeface(myTextFont);
        niveau.setTypeface(myTextFont);
        passerelles.setTypeface(myTextFont);
        pont.setTypeface(myTextFont);
        echelle.setTypeface(myTextFont);
        tyrolienne.setTypeface(myTextFont);
        acces.setTypeface(myTextFont);
        info.setTypeface(myTextFont);


        Intent intent = getActivity().getIntent();
        ViaFerrataModel maviaferrata = (ViaFerrataModel) intent.getParcelableExtra("via");
        Log.d(TAG, "Object Tagg " + maviaferrata);


        String nomVia = maviaferrata.getNom();
        name.setText(nomVia);

        String prixVia = maviaferrata.getPrix();
        prix.setText(prixVia);

        String villeVia = maviaferrata.getVille();
        ville.setText(villeVia);

        String depVia = maviaferrata.getDptNom();
        departement.setText(depVia);

        String regionVia = maviaferrata.getRegion();
        region.setText(regionVia);

        String descriptionVia = maviaferrata.getDescription();
        description.setText(descriptionVia);

        String altitudeDepart = maviaferrata.getAltitudeDepart();
        depart.setText(altitudeDepart);

        String altitudeArrivee = maviaferrata.getAltitudeArrivee();
        arrivee.setText(altitudeArrivee);

        String tempsvia = maviaferrata.getHoraireDuree();
        temps.setText(tempsvia);

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

        String infoAcces = maviaferrata.getInfoAcces();
        acces.setText(infoAcces);

        String infoGen = maviaferrata.getInfo();
        info.setText(infoGen);

        return rootview;



    }

}
