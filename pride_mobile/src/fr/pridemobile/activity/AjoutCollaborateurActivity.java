package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.adapter.AutocompleteCustomArrayAdapter;
import fr.pridemobile.adapter.UtilisateursListAdapter;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.customgraphic.CustomAutoCompleteTextView;
import fr.pridemobile.customgraphic.listener.CustomAutoCompleteTextViewChangedListener;
import fr.pridemobile.model.ListeUtilisateursResponse;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.UtilisateurResponse;
import fr.pridemobile.model.beans.Projet;
import fr.pridemobile.model.beans.Utilisateur;
import fr.pridemobile.service.WSCallable;

public class AjoutCollaborateurActivity extends PrideAbstractActivity {

	private static final String TAG = "AJOUT_COLLABORATEUR";

	/** Eléments de l'interface */
	private Button btnAjoutCollaborateur;
	public CustomAutoCompleteTextView txtLoginCollaborateur;
	// Adapteur pour l'auto compl�tion
	//public ArrayAdapter<String> adapterAutoComplete;
	public AutocompleteCustomArrayAdapter adapterAutoComplete;
	// Données de l'auto complétion (les nom des collaborateurs)
	public List<String> itemsAutoComplete = new ArrayList<String>();
	private ListView collaborateurListView;
	private Projet projet = PrideApplication.INSTANCE.getCurrentProjet();
	private List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();

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
			toolbar.setTitle(projet.getNomProjet() + " - Les participants");
			setSupportActionBar(toolbar);
		}
		initDrawer();
		getCollaborateurProjet();
		getAllCollaborateursNotInProjet();
		btnAjoutCollaborateur = (Button) findViewById(R.id.btn_ajouter_collaborateur);
		txtLoginCollaborateur = (CustomAutoCompleteTextView) findViewById(R.id.login_collaborateur);
		collaborateurListView = (ListView) findViewById(R.id.liste_collaborateurs);

		txtLoginCollaborateur.addTextChangedListener(new CustomAutoCompleteTextViewChangedListener(this));
		if (itemsAutoComplete != null) {
			if (itemsAutoComplete.size() > 0 && checkItems(itemsAutoComplete)) {
				txtLoginCollaborateur.setAdapter(adapterAutoComplete);
			}
		}
		btnAjoutCollaborateur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addCollaborateur();
			}
		});
	}

	private boolean checkItems(List<String> items) {
		for (String login : items) {
			if (login == null) {
				return false;
			}
		}
		return true;
	}

	private void getCollaborateurProjet() {
		Log.i(TAG, "Tentative de récupération des participants à un projet");

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS)
				+ PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS_COLLABORATEUR);

		url = addParametersToUrl(url, projet.getNomProjet());

		callWSGet(url, ListeUtilisateursResponse.class, new WSCallable<ListeUtilisateursResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					utilisateurs = response.getData();
					TextView txtListeVide = (TextView) findViewById(R.id.listeCollaborateursVide);
					ArrayAdapter<Utilisateur> adapter = new UtilisateursListAdapter(AjoutCollaborateurActivity.this,
							utilisateurs);
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

	// Récupère les collaborateurs qui ne participent pas au projet
	private void getAllCollaborateursNotInProjet() {
		Log.i(TAG, "Tentative de création d'un projet");

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS)
				+ PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS_NOTIN_PROJET);

		url = addParametersToUrl(url, projet.getNomProjet());
		callWSGet(url, ListeUtilisateursResponse.class, new WSCallable<ListeUtilisateursResponse>() {

			@Override
			public Void call() throws Exception {

				String errorCode = response.getCode();
				if (response.isSuccess()) {
					for (Utilisateur uti : response.getData()) {
						itemsAutoComplete.add(uti.getLogin());
					}
					if (itemsAutoComplete != null) {
						if (itemsAutoComplete.size() > 0) {
							adapterAutoComplete = new AutocompleteCustomArrayAdapter(AjoutCollaborateurActivity.this, itemsAutoComplete);
						}
					}
				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		});
	}
	
		// Récupère les collaborateurs qui ne participent pas au projet
		private void addCollaborateur() {
			Log.i(TAG, "Tentative d'ajout d'un collaborateur au projet");

			// Construction de l'URL
			String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS)
					+ PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS_COLLABORATEUR);

			// Création de la map pour l'envoie des paramètres
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("login", txtLoginCollaborateur.getText().toString());
			params.put("nomProjet", projet.getNomProjet());
			callWSPost(url, UtilisateurResponse.class, params, new WSCallable<UtilisateurResponse>() {

				@Override
				public Void call() throws Exception {

					String errorCode = response.getCode();
					if (response.isSuccess()) {
						utilisateurs.add(response.getData());
						TextView txtListeVide = (TextView) findViewById(R.id.listeCollaborateursVide);
						ArrayAdapter<Utilisateur> adapter = new UtilisateursListAdapter(AjoutCollaborateurActivity.this,
								utilisateurs);
						adapter.notifyDataSetChanged();
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
}
