package fr.wcs.viaferrata;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button buttonRecherche=(Button)findViewById(R.id.buttonRecherche);
        Button buttonFavoris=(Button)findViewById(R.id.buttonFavoris);
        buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent toSearchActivity=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(toSearchActivity);
            }
        });
        buttonFavoris.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent toFavoritesActivity=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(toFavoritesActivity);
            }
        });
    }
}
