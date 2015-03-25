package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

	@SuppressWarnings("unused")
	private static final String TAG = "PROJET";

	/** Eléments de l'interface */
	private TextView textViewDescription;
	private ListView listViewIdees;
	private Projet projet;
	/* TODO */
	//private Button buttonCommentaires;
	//private Button buttonModifDescription;
	private Button buttonAjouterUtilisateur;
	
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
	    listViewIdees = (ListView) findViewById(R.id.liste_idees);
	    textViewDescription.setText(projet.getDescription());
	    /* TODO */
	    //buttonModifDescription = (Button) findViewById(R.id.btn_modif_description);
	    //buttonCommentaires = (Button) findViewById(R.id.btn_commentaire);
	    buttonAjouterUtilisateur = (Button) findViewById(R.id.btn_ajout_utilisateur);
	    buttonAjouterUtilisateur.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailProjetActivity.this, AjoutCollaborateurActivity.class);
				startActivity(intent);
			}
		});
		
	    TextView txtlisteVide = (TextView) findViewById(R.id.listeIdeesVide);
	    ArrayAdapter<Idee> adapter = new IdeeListAdapter(DetailProjetActivity.this, projet.getIdees());
		listViewIdees.setAdapter(adapter);
		listViewIdees.setEmptyView(txtlisteVide);
	}
}
