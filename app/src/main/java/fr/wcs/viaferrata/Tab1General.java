package fr.wcs.viaferrata;
import android.content.Intent;
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


        Intent intent = getActivity().getIntent();
        ViaFerrataModel maviaferrata = (ViaFerrataModel) intent.getParcelableExtra("via");


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

        int niveauvia = maviaferrata.getDifficulte();
        niveau.setText(String.valueOf(niveauvia));

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
