package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.NullResponse;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.utils.Constants;

public class CreerProjetActivity extends PrideAbstractActivity {
	
	private static final String TAG = "CREER_PROJET";

	/** Eléments de l'interface */
	private Button btnCreerProjet;
	private EditText txtNomProjet;
	private EditText txtDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_creer_projet);
		List<NavigationDrawerElement> drawerData = new ArrayList<NavigationDrawerElement>();
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COLLABORATIONS));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_GERES));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COMMUNAUTE));
		nitView(new NavigationListAdapter(this, drawerData));
		if (toolbar != null) {
            toolbar.setTitle(getString(R.string.lib_creer_projet));
            setSupportActionBar(toolbar);
        }
	    initDrawer();

		// Éléments
		btnCreerProjet = (Button) findViewById(R.id.btn_creer_projet);
		txtNomProjet = (EditText) findViewById(R.id.titre_projet);
		txtDescription = (EditText) findViewById(R.id.description_projet);
		
		btnCreerProjet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAndCreateProjet();
			}
		});
	}
	
	private void checkAndCreateProjet(){
		if ((txtNomProjet.getText().length() != 0) && (txtDescription.getText().length() != 0)) {
			String nomProjet = txtNomProjet.getText().toString();
			String description = txtDescription.getText().toString();
			creerProjet(nomProjet, description);

		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.pls_complete_field), Toast.LENGTH_LONG).show();
		}
	}
	
	private void creerProjet(String nomProjet, String description){
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
					/* On redirige vers la suite */
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
