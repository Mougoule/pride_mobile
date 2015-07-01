package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.adapter.ProjetsListAdapter;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ListeProjetsResponse;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.beans.Projet;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.service.WSError;
import fr.pridemobile.utils.Constants;

public class MesParticipationsActivity extends PrideAbstractActivity {

	private static final String TAG = "ACCUEIL";

	/** Eléments de l'interface */
	private ListView projetsListeView;
	
	/** Durée entre 2 clic sur bouton Back */
	private long backPressedTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mes_participations);
		List<NavigationDrawerElement> drawerData = new ArrayList<NavigationDrawerElement>();
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_GERES));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COMMUNAUTE));
		nitView(new NavigationListAdapter(this, drawerData));
		if (toolbar != null) {
            toolbar.setTitle(getString(R.string.lib_mes_participations));
            setSupportActionBar(toolbar);
        }
	    initDrawer();

		// Éléments
		projetsListeView = (ListView) findViewById(R.id.liste_projets);
		getProjets();
	}

	private void getProjets() {
		Log.i(TAG, "Tentative de récupération des projets");

		String login = prefs.getString(Constants.PREF_LOGIN, null);
		
		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS,
				PrideConfiguration.WS_UTILISATEURS_PROJET);
		url = addParametersToUrl(url, login);
		callWSGet(url, ListeProjetsResponse.class, new WSCallable<ListeProjetsResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					List<Projet> projets = response.getData();
					TextView txtListeVide = (TextView) findViewById(R.id.listeProjetsVide);
					ArrayAdapter<Projet> adapter = new ProjetsListAdapter(MesParticipationsActivity.this, projets);
					setListViewFromThread(projetsListeView, adapter, txtListeVide);
				} else if(WSError.UTI_01.checkCode(errorCode)){
					showError(WSError.UTI_01.getCode());
				}else{
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		});
	}
	
	/**
	 * Gestion du bouton back sur la page d'accueil. On presse 2 fois pour
	 * fermer l'appli.
	 */
	@Override
	public void onBackPressed() {
		long t = System.currentTimeMillis();
		if (t - backPressedTime > Constants.BACK_QUIT_DELAY) {
			// Premier clic ou temps écoulé depuis la dernière fois
			backPressedTime = t;
			Toast.makeText(this, R.string.ma_quitter_appli, Toast.LENGTH_SHORT).show();
		} else {
			// 2ème clic dans le temps imparti, on quitte l'appli
			moveTaskToBack(true);
		}
	}
}
