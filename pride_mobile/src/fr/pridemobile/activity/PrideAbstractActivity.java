package fr.pridemobile.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.adapter.navigation.NavigationListAdapter;
import fr.pridemobile.application.PrideApplication;
import fr.pridemobile.application.PrideConfiguration;
import fr.pridemobile.model.ConnexionResponse;
import fr.pridemobile.model.NavigationDrawerElement;
import fr.pridemobile.model.WebappResponse;
import fr.pridemobile.service.WSCallable;
import fr.pridemobile.service.WSError;
import fr.pridemobile.service.WSService;
import fr.pridemobile.service.common.ServiceException;
import fr.pridemobile.service.common.WSFile;
import fr.pridemobile.service.common.WSMethod;
import fr.pridemobile.utils.Constants;
import fr.pridemobile.utils.Messages;

public abstract class PrideAbstractActivity extends ActionBarActivity implements OnSharedPreferenceChangeListener {

	private static final String TAG = "PRIDE_ACTIVITY";

	/** Prï¿½fï¿½rences partagï¿½es */
	protected SharedPreferences prefs;

	/** Loader WS */
	private ProgressDialog httpProgressDialog;

	private boolean reload;

	/** Les attributs pour le drawer */
	protected DrawerLayout drawerLayout;
	protected ActionBarDrawerToggle drawerToggle;
	protected ListView leftDrawerList;
	protected ArrayAdapter<NavigationDrawerElement> navigationDrawerAdapter;
	protected Toolbar toolbar;
	protected List<NavigationDrawerElement> leftSliderData = new ArrayList<NavigationDrawerElement>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Ajout du menu option
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// Quand on modifie les prefs, on a rien ï¿½ faire
	}

	/**
	 * Appel de web service
	 * 
	 * @param method
	 *            Mï¿½thode Http
	 * @param url
	 *            Url (hors base)
	 * @param clazz
	 *            Classe attendue en rï¿½ponse
	 * @param params
	 *            Paramï¿½tres de l'appel (POST)
	 * @param callback
	 *            Foncion ï¿½ exï¿½cuter au retour du WS
	 * @param checkDeviceErrors
	 *            Indique si le comportement en cas de device inconnu est
	 *            spï¿½cifique
	 */
	private <T extends WebappResponse<?>> void callWS(final WSMethod method, final String url, final Class<T> clazz,
			final Map<String, Object> params, final Map<String, WSFile> files, final WSCallable<T> callback) {

		// vï¿½rification de la connexion rï¿½seau
		if (isNetworkAvailable()) {
			// Affichage du loader
			if (httpProgressDialog == null) {
				httpProgressDialog = new ProgressDialog(PrideAbstractActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_DARK);
				httpProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				httpProgressDialog.setCancelable(false);
				httpProgressDialog.setMessage(getString(R.string.progress_msg));
			}
			httpProgressDialog.show();

			// Constrcution URL
			StringBuilder sb = new StringBuilder(PrideApplication.INSTANCE.getProperty(PrideConfiguration.WS_BASE))
					.append(url);
			final String urlComplete = sb.toString();

			// Lancement de l'appel HTTP
			Thread thread = new Thread(new WSRunnable<T>(urlComplete, method, clazz, params, files, callback));
			thread.start();
		} else {
			// Non connectï¿½
			showError("Erreur : network not availible");
		}
	}


	/**
	 * Appel de WS post
	 * 
	 * @param url
	 *            URL ï¿½ appeler
	 * @param clazz
	 *            Classe pour le mapping de la rï¿½ponse
	 * @param params
	 *            Paramï¿½tres de Web service
	 * @param callback
	 *            Focntion ï¿½ exï¿½cuter lors de la rï¿½ponse
	 */
	public <T extends WebappResponse<?>> void callWSPost(final String url, final Class<T> clazz,
			final Map<String, Object> params, final WSCallable<T> callback) {
		callWS(WSMethod.POST, url, clazz, params, null, callback);
	}

	/**
	 * Appel de WS put
	 * 
	 * @param url
	 *            URL ï¿½ appeler
	 * @param clazz
	 *            Classe pour le mapping de la rï¿½ponse
	 * @param params
	 *            Paramï¿½tres de Web service
	 * @param callback
	 *            Focntion ï¿½ exï¿½cuter lors de la rï¿½ponse
	 * @param checkLicenceErrors
	 *            Indique qu'il faut vï¿½rifier les erreurs de licences
	 */
	public <T extends WebappResponse<?>> void callWSPut(final String url, final Class<T> clazz,
			final Map<String, Object> params, final Map<String, WSFile> files, final WSCallable<T> callback) {
		callWS(WSMethod.PUT, url, clazz, params, files, callback);
	}

	/**
	 * Appel de WS get
	 * 
	 * @param url
	 *            URL ï¿½ appeler
	 * @param clazz
	 *            Classe pour le mapping de la rï¿½ponse
	 * @param params
	 *            Paramï¿½tres de Web service
	 * @param callback
	 *            Fonction ï¿½ exï¿½cuter lors de la rï¿½ponse
	 */
	public <T extends WebappResponse<?>> void callWSGet(final String url, final Class<T> clazz,
			final WSCallable<T> callback) {
		callWS(WSMethod.GET, url, clazz, null, null, callback);
	}

	/**
	 * Log une erreur WS
	 * 
	 * @param tag
	 *            Tag
	 * @param response
	 *            Webapp response
	 */
	public void logError(String tag, WebappResponse<?> response) {
		PrideApplication.INSTANCE.logError(tag, response);
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param code
	 *            code d'erreur
	 * @param message
	 *            message d'erreur
	 */
	public void showError(final String message) {
		showError(message, null);
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param message
	 *            message d'erreur
	 * @param intent
	 *            A faire sur OK
	 * 
	 */
	public void showError(final String message, final Intent intent) {
		PrideAbstractActivity.this.runOnUiThread(new Runnable() {

			public void run() {
				final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(
						PrideAbstractActivity.this, android.R.style.Theme_Holo_Light_Dialog)).create();
				alertDialog.setTitle("Erreur");
				alertDialog.setMessage(message);
				alertDialog.setCancelable(true);
				alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Ferme le dialog
								alertDialog.dismiss();
								if (intent != null) {
									startActivity(intent);
								}
							}
						});
				alertDialog.show();
			}
		});
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param code
	 *            code d'erreur
	 * @param intent
	 *            A faire sur OK
	 */
	public void showErrorFromCode(final String code) {
		showError(Messages.findWSMessage(code, PrideAbstractActivity.this));
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param code
	 *            code d'erreur
	 */
	public void showErrorFromCode(final String code, final Intent intent) {
		showError(Messages.findWSMessage(code, PrideAbstractActivity.this), intent);
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param errorMessage
	 *            message d'erreur
	 */
	public void showErrorStandard(final String errorMessage) {
		String msg = "Erreur gen \n" + errorMessage;
		showError(msg);
	}

	/**
	 * Affichage des dialogs pour la gestion d'erreurs
	 * 
	 * @param errorMessage
	 *            message d'erreur
	 */
	public void showErrorStandard(Exception e) {
		String msg = "Erreur gen";
		if (e != null && e.getLocalizedMessage() != null) {
			msg += "\n" + e.getLocalizedMessage();
		}
		showError(msg);
	}

	/**
	 * Deconnexion de l'application et redirection ï¿½ la page de login
	 */
	public void logout() {
		Log.i(TAG, "Dï¿½connexion");

		// Vidage de la session
		// PrideApplication.INSTANCE.clear();

		// Supprime tout sauf la langue
		Editor editor = prefs.edit();
		editor.remove(Constants.PREF_LOGIN);
		editor.remove(Constants.PREF_TOKEN);
		editor.remove(Constants.PREF_AUTOLOGIN);
		editor.commit();
	}

	/**
	 * Deconnexion de l'application et redirection ï¿½ la page de login
	 */
	public void logoutAndRedirect() {
		Log.i(TAG, "Dï¿½connexion et redirection");
		logout();
		Intent intent = new Intent(PrideAbstractActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * int id = item.getItemId(); if (id == R.id.logout) { if
		 * (PrideApplication.INSTANCE.getCurrentChargement() == null) { 
		 * logoutAndRedirect(); } else { 
		 * d'abord showCurrentMissionAlert(); }
		 * 
		 * } if (id == R.id.conf) { startActivity(new Intent(this,
		 * ConfigurationActivity.class)); return true; }
		 * 
		 * if (id == R.id.acceuil) { startActivity(new Intent(this,
		 * RechercheMissionActivity.class)); return true; } if (id ==
		 * R.id.retour) { startActivity(new Intent(this,
		 * RetourSupportActivity.class)); return true; } if (id == R.id.propos)
		 * { startActivity(new Intent(this, ProposActivity.class)); return true;
		 * } return (super.onOptionsItemSelected(item));
		 */
		
        return false;
	}

	/**
	 * Vï¿½rifier si les champs texte sont null. Permet aussi de cacher les champs
	 * vides.
	 * 
	 * @param textview
	 * @param txt
	 *            champs du WS
	 */
	protected void updateTextAndVisibility(TextView textview, String txt) {
		if (txt == null || txt.length() == 0) {
			textview.setVisibility(View.GONE);
		} else {
			textview.setText(txt);
		}
	}

	/**
	 * Runnable d'appel WS
	 * 
	 * @param <T>
	 *            Classe attendue de rï¿½ponse du WS
	 */
	private class WSRunnable<T extends WebappResponse<?>> implements Runnable {

		/** URL complï¿½te ï¿½ appeler */
		private String url;

		/** Mï¿½thode HTTP */
		private WSMethod method;

		/** Classe attendue */
		private Class<T> clazz;

		/** Paramï¿½tres */
		private Map<String, Object> params;

		/** Donï¿½nes fichier */
		private Map<String, WSFile> files;

		/** Fonction retour WS */
		private WSCallable<T> callback;

		public WSRunnable(String url, WSMethod method, Class<T> clazz, Map<String, Object> params,
				Map<String, WSFile> files, WSCallable<T> callback) {
			super();
			this.url = url;
			this.method = method;
			this.clazz = clazz;
			this.params = params;
			this.callback = callback;
			this.files = files;
		}

		@Override
		public void run() {
			try {

				// On fait notre appel de WS
				Log.i("WS", "Calling " + method + " web service : " + url);

				// Rï¿½cupï¿½ration token
				String token = prefs.getString(Constants.PREF_TOKEN, "");

				// Rï¿½cupï¿½ration androidId
				String androidId = Secure.getString(PrideAbstractActivity.this.getContentResolver(), Secure.ANDROID_ID);

				T wsResponse;
				switch (method) {
				case GET:
					wsResponse = WSService.get(url, clazz, token, androidId);
					break;
				case POST:
					wsResponse = WSService.post(url, clazz, params, token, androidId);
					break;
				case PUT:
					wsResponse = WSService.put(url, clazz, params, files, token, androidId);
					break;
				default:
					throw new IllegalArgumentException("This HTTP method " + method + " is not implemented yet");
				}

				// On cache le loader
				httpProgressDialog.dismiss();
				Log.i("WS", "Calling web service after dismiss : " + url);

				// Gestion erreur technique
				final String errorCode = wsResponse.getCode();
				if (!wsResponse.isSuccess() && WSError.TECH.checkCode(errorCode)) {
					// Log de l'erreur
					logError("WS", wsResponse);

					// Affichage popup erreur en focntion du code reï¿½u
					showErrorFromCode(errorCode);

				} else if (!wsResponse.isSuccess() && WSError.SEC_00.checkCode(errorCode)) {

					// Token invalide

					// On supprime le token en cours

					// On retire la mission et les retours en cours
					/*
					 * PrideApplication.INSTANCE.setCurrentChargement(null);
					 * PrideApplication.INSTANCE.setCurrentMissionId(-1);
					 * PrideApplication.INSTANCE.setCurrentSupportRetours(null);
					 * PrideApplication.INSTANCE.setCurrentRetourId(-1);
					 */
					// Dï¿½connexion
					logout();

					// Affichage de l'erreur et redirection
					showErrorFromCode(errorCode, new Intent(PrideAbstractActivity.this, MainActivity.class));

				} else {
					// Pas d'erreur technique, token OK

					// Appel du callback
					try {
						callback.call(wsResponse);
					} catch (Exception e) {
						// Erreur lors de l'appel du callback
						Log.e("WS", "Une erreur s'est produite lors de l'appel du callback", e);
						showErrorStandard(e);
					}
				}
			} catch (ServiceException e) {
				httpProgressDialog.dismiss();
				Log.e("WS", "Une erreur s'est produite lors de l'appel au WS", e);
				showErrorStandard(e);
			}
		}
	}

	/**
	 * Vï¿½rifie l'accï¿½s au rï¿½seau
	 * 
	 * @return Vrai si connectï¿½ au rï¿½seau
	 */
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onPostResume() {
		super.onResume();
		if (reload) {
			reload = false;
			recreate();
		}
	}

	/**
	 * Mï¿½thode pour la crï¿½ation de toast au sein d'un thread (on ne peut pas
	 * crï¿½er un toast dans un thread, il faut un runOnUiThread
	 * 
	 * @param msg
	 *            Le message ï¿½ afficher dans le toast
	 * @param context
	 *            Le context dans lequel afficher le toast
	 */
	public void showToastFromThread(final String msg, final Context context) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
			}
		});

	}
	
	/**
	 * Mï¿½thode permettant de setter une liste view depuis un thread. Cela devant ï¿½tre fait depuis un UIThread
	 * 
	 * @param listView la liste view ï¿½ setter
	 * @param adapter l'adapter pour setter la liste view
	 * @param txtListeVide le texte en cas de liste vide
	 */
	public void setListViewFromThread(final ListView listView, final ArrayAdapter<?> adapter, final TextView txtListeVide) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listView.setAdapter(adapter);
				listView.setEmptyView(txtListeVide);
			}
		});
	}
	
	/**
	 * Méthode permettant de setter une liste view depuis un thread. Cela devant ï¿½tre fait depuis un UIThread
	 * 
	 * @param listView la liste view ï¿½ setter
	 * @param adapter l'adapter pour setter la liste view
	 * @param txtListeVide le texte en cas de liste vide
	 */
	public String addParametersToUrl(String url, String... params) {
		for(String param : params){
			param = param.replace(" ", "%20");
			url += "/"+param;
		}
		
		return url;
	}

	protected void nitView(NavigationListAdapter navigationListAdapter) {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		leftDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		navigationDrawerAdapter = navigationListAdapter;
		leftDrawerList.setAdapter(navigationDrawerAdapter);
	}

	protected void initDrawer() {

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
	}
	
	/**
	 * Tentative de connexion
	 * 
	 * @param login
	 *            Code livreur
	 * @param password
	 *            Mot de passe
	 */
	protected void connect(String login, final String password, final Context context, final Class<? extends PrideAbstractActivity> clazz) {
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
					editor.putString(Constants.PREF_MDP, password);
					editor.putString(Constants.PREF_TOKEN, token);
					editor.commit();

					Intent intent = new Intent(context, clazz);
					// On enlève les précédentes activity comme ça l'écran résultat est le premier écran
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
