package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.adapter.ProjetsListAdapter;
import fr.pridemobile.adapter.navigation.ConstantLienNavigation;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ListeProjetsResponse;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.beans.Projet;
import fr.pridemobile.model.beans.Role;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.service.WSError;
import fr.pridemobile.utils.Constants;

public class MesProjetsActivity extends PrideAbstractActivity {

	private static final String TAG = "MES_PROJETS";

	/** Eléments de l'interface */
	private ListView projetsListeView;
	private Button btnCreerProjet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mes_projets);
		List<NavigationDrawerElement> drawerData = new ArrayList<NavigationDrawerElement>();
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COLLABORATIONS));
		drawerData.add(new NavigationDrawerElement(ConstantLienNavigation.PROJETS_COMMUNAUTE));
		nitView(new NavigationListAdapter(this, drawerData));
		if (toolbar != null) {
            toolbar.setTitle(getString(R.string.lib_mes_projets));
            setSupportActionBar(toolbar);
        }
	    initDrawer();

		// Éléments
		projetsListeView = (ListView) findViewById(R.id.liste_projets);
		btnCreerProjet = (Button) findViewById(R.id.btn_creer_projet);
		getProjets();
		
		btnCreerProjet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MesProjetsActivity.this, CreerProjetActivity.class);
				startActivity(intent);
			}
		});
	}

	private void getProjets() {
		Log.i(TAG, "Tentative de récupération des projets");

		String login = prefs.getString(Constants.PREF_LOGIN, null);
		
		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_PROJETS);
		url += "/"+Role.CHEF+"/"+login;
		callWSGet(url, ListeProjetsResponse.class, new WSCallable<ListeProjetsResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					List<Projet> projets = response.getData();
					TextView txtListeVide = (TextView) findViewById(R.id.listeProjetsVide);
					ArrayAdapter<Projet> adapter = new ProjetsListAdapter(MesProjetsActivity.this, projets);
					setListViewFromThread(projetsListeView, adapter, txtListeVide);
				} else if(WSError.UTI_01.checkCode(errorCode)){
					showErrorFromCode(WSError.UTI_01.getCode());
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
