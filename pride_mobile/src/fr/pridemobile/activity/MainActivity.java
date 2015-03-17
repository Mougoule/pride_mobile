package fr.pridemobile.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences.Editor;
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
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ConnexionResponse;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.utils.Constants;

public class MainActivity extends PrideAbstractActivity {

	private static final String TAG = "MAIN";

	/** Durée entre 2 clic sur bouton Back */
	private long backPressedTime = 0;

	/** Eléments de l'interface */
	private Button connectButton;
	private EditText loginEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Vérification connexion automatique
		// String token = prefs.getString(Constants.PREF_TOKEN, null);
		
		// Page de connexion
		setContentView(R.layout.activity_main);

		// Éléments
		connectButton = (Button) findViewById(R.id.btnConnect);
		loginEditText = (EditText) findViewById(R.id.login);
		passwordEditText = (EditText) findViewById(R.id.password);

		// Lance la connexion à la validation du champ de texte
		passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// Si les champs login et mot de passe sont saisies
					if ((loginEditText.getText().length() != 0) && (passwordEditText.getText().length() != 0)) {
						// Recuperation du codeLivreur et mot de passe
						String codeliv = loginEditText.getText().toString();
						String pass = passwordEditText.getText().toString();

						// Tentative de connexion
						connect(codeliv, pass);

					} else if ((loginEditText.getText().length() == 0) && (passwordEditText.getText().length() != 0)) {
						// Si le champs mot de passe est vide
						Toast.makeText(getApplicationContext(), "Login vide", Toast.LENGTH_SHORT).show();
					} else if ((passwordEditText.getText().length() == 0) && (loginEditText.getText().length() != 0)) {
						// Si le champs login est vide
						Toast.makeText(getApplicationContext(), "Mot de passe vide", Toast.LENGTH_SHORT).show();
					} else {
						// Si les champs login et mot de passe sont vide
						Toast.makeText(getApplicationContext(), "Mot de passe et login vides", Toast.LENGTH_SHORT)
								.show();
					}
					return true;
				}
				return false;
			}
		});

		// Bouton Connect
		connectButton.setOnClickListener(new ConnectOnClickListener());
	}

	private class ConnectOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// Si les champs login et mot de passe sont saisies
			if ((loginEditText.getText().length() != 0) && (passwordEditText.getText().length() != 0)) {
				// Recuperation du codeLivreur et mot de passe
				String codeliv = loginEditText.getText().toString();
				String pass = passwordEditText.getText().toString();

				// Tentative de connexion
				connect(codeliv, pass);

			} else if ((loginEditText.getText().length() == 0) && (passwordEditText.getText().length() != 0)) {
				// Si le champs mot de passe est vide
				Toast.makeText(getApplicationContext(), "Login vide", Toast.LENGTH_SHORT).show();
			} else if ((passwordEditText.getText().length() == 0) && (loginEditText.getText().length() != 0)) {
				// Si le champs login est vide
				Toast.makeText(getApplicationContext(), "Mot de passe vide", Toast.LENGTH_SHORT).show();
			} else {
				// Si les champs login et mot de passe sont vide
				Toast.makeText(getApplicationContext(), "Mot de passe et login vides", Toast.LENGTH_SHORT).show();
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

	/**
	 * Tentative de connexion
	 * 
	 * @param login
	 *            Code livreur
	 * @param password
	 *            Mot de passe
	 */
	private void connect(String login, String password) {
		Log.i(TAG, "Tentative de connexion");

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS,
				PrideConfiguration.WS_UTILISATEURS_CONNECT);

		// Ajout dans la map pour l'envoie
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		params.put("password", password);

		callWSPost(url, ConnexionResponse.class, params, new WSCallable<ConnexionResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				Editor editor = prefs.edit();
				if (response.isSuccess()) {
					// Connexion réussie

					// Sauvegarde local du codeLivreur et du token
					String token = response.getData().getToken().toString();
					editor.putString(Constants.PREF_LOGIN, response.getData().getUtilisateur().getLogin());
					editor.putString(Constants.PREF_TOKEN, token);
					editor.commit();

					showToastFromThread("Le token : " + token, MainActivity.this);
					//Toast.makeText(MainActivity.this, "Le token : " + token, Toast.LENGTH_LONG).show();

				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		}, false);
	}
}
