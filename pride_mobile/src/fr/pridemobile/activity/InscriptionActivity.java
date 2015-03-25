package fr.pridemobile.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ConnexionResponse;
import fr.pridemobile.model.UtilisateurResponse;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.utils.Constants;

public class InscriptionActivity extends PrideAbstractActivity {

	private static final String TAG = "INSCRIPTION";

	/** Eléments de l'interface */
	private Button inscriptionButton;
	private EditText loginEditText;
	private EditText passwordEditText;
	private EditText emailEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_inscription);

		// Éléments
		inscriptionButton = (Button) findViewById(R.id.btnInscription);
		loginEditText = (EditText) findViewById(R.id.login);
		passwordEditText = (EditText) findViewById(R.id.password);
		emailEditText = (EditText) findViewById(R.id.email);
		
		// Fait apparaitre le clavier adapté pour les adresses email
		emailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		// Bouton inscription
		inscriptionButton.setOnClickListener(new InscriptionOnClickListener());
	}
	
	private class InscriptionOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(verificationChamps()){
				String login = loginEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String email = emailEditText.getText().toString();
				// Tentative d'inscription
				insciption(login, password, email);
			}else{
				Toast.makeText(getApplicationContext(), getString(R.string.pls_complete_field), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private boolean verificationChamps(){
		if ((loginEditText.getText().length() != 0) && (passwordEditText.getText().length() != 0) && (emailEditText.getText().length() != 0)) {
			return true;
		} else {
			return false;
			
		}
	}

	/**
	 * Tentative d'inscription d'un utilisateur
	 * 
	 * @param login de l'utilisateur
	 * @param password password de l'utilisateur
	 * @param email email de l'utilisateur
	 * @param pseudo pseudo de l'utilisateur
	 */
	private void insciption(final String login, final String password, final String email) {
		Log.i(TAG, "Tentative d'inscription");

		// Construction de l'URL
		String url = PrideApplication.INSTANCE.getProperties(PrideConfiguration.WS_UTILISATEURS);

		// Ajout dans la map pour l'envoie
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login);
		params.put("password", password);
		params.put("email", email);

		callWSPost(url, UtilisateurResponse.class, params, new WSCallable<UtilisateurResponse>() {

			@Override
			public Void call() throws Exception {
				String errorCode = response.getCode();
				if (response.isSuccess()) {
					connect(login, password);
				} else {
					// Erreur inconnue
					logError(TAG, response);
					showErrorFromCode(errorCode);
				}
				return null;
			}
		});
	}
	
	/**
	 * Tentative de connexion
	 * 
	 * @param login
	 * @param password
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

					Intent intent = new Intent(InscriptionActivity.this, MesParticipationsActivity.class);
					// On enlève les précédentes activity comme ça l'écran résultat est le premier écran
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

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
