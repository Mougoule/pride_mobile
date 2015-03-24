package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.adapter.IdeeListAdapter;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.beans.Idee;
import fr.pridemobile.model.beans.Projet;

public class DetailProjetActivity extends PrideAbstractActivity {

	private static final String TAG = "PROJET";

	/** Eléments de l'interface */
	private ImageView imageViewProjet;
	private TextView textViewDescription;
	private Button buttonModifDescription;
	private ListView listViewIdees;
	private Button buttonCommentaires;
	private Projet projet;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_projet);
		projet = PrideApplication.INSTANCE.getCurrentProjet();
		
		List<NavigationDrawerElement> drawerData = new ArrayList<NavigationDrawerElement>();
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COLLABORATIONS));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_GERES));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COMMUNAUTE));
		nitView(new NavigationListAdapter(this, drawerData));
		if (toolbar != null) {
            toolbar.setTitle(projet.getNomProjet());
            setSupportActionBar(toolbar);
        }
	    initDrawer();
	    textViewDescription = (TextView) findViewById(R.id.titre_rub_description_projet);
	    buttonModifDescription = (Button) findViewById(R.id.btn_modif_description);
	    listViewIdees = (ListView) findViewById(R.id.liste_idees);
	    buttonCommentaires = (Button) findViewById(R.id.btn_commentaire);
	    textViewDescription.setText(projet.getDescription());
		
	    Log.i(TAG, "projet.getIdees()"+projet.getIdees());
	    TextView txtlisteVide = (TextView) findViewById(R.id.listeIdeesVide);
	    ArrayAdapter<Idee> adapter = new IdeeListAdapter(DetailProjetActivity.this, projet.getIdees());
		listViewIdees.setAdapter(adapter);
		listViewIdees.setEmptyView(txtlisteVide);
	}
}
