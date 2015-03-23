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
import fr.pridemobile.adapter.navigation.NavigationListAdapterDetailProjet;
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
		drawerData.add(new NavigationDrawerElement("Home"));
		drawerData.add(new NavigationDrawerElement("Autre"));
		drawerData.add(new NavigationDrawerElement("Les collaborateurs"));
		nitView(new NavigationListAdapterDetailProjet(this, drawerData));
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

	/*private void getIdees() {
		Log.i(TAG, "Tentative de récupération des projets");

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS,
				PrideConfiguration.WS_UTILISATEURS_PROJET);
		url += "/"+projet.getNomProjet();
		callWSGet(url, ProjetResponse.class, new WSCallable<ProjetResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					
					Projet projet = response.getData();
					
				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		}, false);
	}*/
}
