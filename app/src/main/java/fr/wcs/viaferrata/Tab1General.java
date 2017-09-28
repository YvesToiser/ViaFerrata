package fr.wcs.viaferrata;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by wilderjm on 27/09/17.
 */

public class Tab1General extends Fragment{
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.tab1general, container, false);
        return rootview;
    }
}
