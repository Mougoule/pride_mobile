package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.adapter.UtilisateursListAdapter;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ListeUtilisateursResponse;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.beans.Projet;
import fr.pridemobile.model.beans.Utilisateur;
import fr.pridemobile.service.WSCallable;

public class AjoutCollaborateurActivity extends PrideAbstractActivity {

	private static final String TAG = "AJOUT_COLLABORATEUR";

	/** Eléments de l'interface */
	private Button btnAjoutCollaborateur;
	private EditText txtLoginCollaborateur;
	private ListView collaborateurListView;
	private Projet projet = PrideApplication.INSTANCE.getCurrentProjet();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ajout_collaborateur);
		List<NavigationDrawerElement> drawerData = new ArrayList<NavigationDrawerElement>();
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COLLABORATIONS));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_GERES));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COMMUNAUTE));
		nitView(new NavigationListAdapter(this, drawerData));
		if (toolbar != null) {
			toolbar.setTitle(projet.getNomProjet()+" - Les participants");
			setSupportActionBar(toolbar);
		}
		initDrawer();
		getCollaborateurProjet();
		// Éléments
		btnAjoutCollaborateur = (Button) findViewById(R.id.btn_ajouter_collaborateur);
		txtLoginCollaborateur = (EditText) findViewById(R.id.login_collaborateur);
		collaborateurListView = (ListView) findViewById(R.id.liste_collaborateurs);

		btnAjoutCollaborateur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	private void getCollaborateurProjet() {
		Log.i(TAG, "Tentative de récupération des participants à un projet");

		

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS)
				+ PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS_COLLABORATEUR);

		url += "/"+projet.getNomProjet();
		
		callWSGet(url, ListeUtilisateursResponse.class, new WSCallable<ListeUtilisateursResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					List<Utilisateur> utilisateurs = response.getData();
					TextView txtListeVide = (TextView) findViewById(R.id.listeCollaborateursVide);
					ArrayAdapter<Utilisateur> adapter = new UtilisateursListAdapter(AjoutCollaborateurActivity.this, utilisateurs);
					setListViewFromThread(collaborateurListView, adapter, txtListeVide);
				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		});
	}

	/*private void creerProjet(String nomProjet, String description) {
		Log.i(TAG, "Tentative de création d'un projet");

		String login = prefs.getString(Constants.PREF_LOGIN, null);

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS);
		// Création de la map pour l'envoie des paramètres
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		params.put("nomProjet", nomProjet);
		params.put("description", description);

		callWSPost(url, NullResponse.class, params, new WSCallable<NullResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					Intent intent = new Intent(AjoutCollaborateurActivity.this, DetailProjetActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
					startActivity(intent);
				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		});
	}*/
}
