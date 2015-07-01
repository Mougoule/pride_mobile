package fr.pridemobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.utils.Constants;

public class MainActivity extends PrideAbstractActivity {

	private static final String TAG = "MAIN";

	/** Durée entre 2 clic sur bouton Back */
	private long backPressedTime = 0;

	/** Eléments de l'interface */
	private Button connectButton;
	private Button inscriptionButton;
	private EditText loginEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Page de connexion
		setContentView(R.layout.activity_main);

		// Éléments
		connectButton = (Button) findViewById(R.id.btnConnect);
		inscriptionButton = (Button) findViewById(R.id.btnInscription);
		loginEditText = (EditText) findViewById(R.id.login);
		passwordEditText = (EditText) findViewById(R.id.password);

		if(prefs.contains(Constants.PREF_LOGIN) && prefs.contains(Constants.PREF_MDP)){
			connect(prefs.getString(Constants.PREF_LOGIN, null), prefs.getString(Constants.PREF_MDP, null));
		}
		
		// Lance la connexion à la validation du champ de texte
		passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						// Si les champs login et mot de passe sont saisies
						if ((loginEditText.getText().length() != 0) && (passwordEditText.getText().length() != 0)) {
							// Recuperation du login et mot de passe
							String login = loginEditText.getText().toString();
							String pass = passwordEditText.getText().toString();
	
							// Tentative de connexion
							connect(login, pass);
	
						} else {
							Toast.makeText(getApplicationContext(), getString(R.string.pls_complete_field), Toast.LENGTH_LONG).show();
						}
						return true;
					}
				return false;
			}
		});

		// Bouton connexion
		connectButton.setOnClickListener(new ConnectOnClickListener());

		// Bouton inscription
		inscriptionButton.setOnClickListener(new InscriptionOnClickListener());
	}
	
	private class InscriptionOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
			startActivity(intent);
		}
	}

	private class ConnectOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// Si les champs login et mot de passe sont saisies
			if ((loginEditText.getText().length() != 0) && (passwordEditText.getText().length() != 0)) {
				// Recuperation du codeLivreur et mot de passe
				String login = loginEditText.getText().toString();
				String pass = passwordEditText.getText().toString();

				// Tentative de connexion
				connect(login, pass);

			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.pls_complete_field), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void connect(String login, String password){
		Log.i(TAG, "Tentative de connexion");
		super.connect(login, password, MainActivity.this, MesParticipationsActivity.class);
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
