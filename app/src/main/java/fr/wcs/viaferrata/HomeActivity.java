package fr.wcs.viaferrata;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    final private boolean hasFavorite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button buttonRecherche=(Button)findViewById(R.id.buttonRecherche);
        Button buttonFavoris=(Button)findViewById(R.id.buttonFavoris);


        // Check if there is favorite

        if(!hasFavorite){buttonFavoris.setVisibility(View.GONE);}


        buttonRecherche.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intentSearch=new Intent(HomeActivity.this,MapsActivity.class);
                startActivity(intentSearch);
            }
        });


        buttonFavoris.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intentFavorite=new Intent(HomeActivity.this,MapsActivity.class);
                intentFavorite.putExtra("favori", true);
                startActivity(intentFavorite);
            }
        });
    }
}
