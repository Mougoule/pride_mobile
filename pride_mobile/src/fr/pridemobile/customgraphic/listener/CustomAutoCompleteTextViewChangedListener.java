package fr.pridemobile.customgraphic.listener;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import fr.pridemobile.activity.AjoutCollaborateurActivity;

public class CustomAutoCompleteTextViewChangedListener implements TextWatcher {

	public static final String TAG = "AUTO_COMPLETE_LISTENER";
	Context context;

	public CustomAutoCompleteTextViewChangedListener(Context context) {
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence userInput, int start, int before, int count) {

		Log.i(TAG, "User input: " + userInput);
		if (userInput.length() > 0) {
			AjoutCollaborateurActivity ajoutCollaborateurActivity = ((AjoutCollaborateurActivity) context);
			Log.i(TAG, "yoyoyoyoyo : ");
			// query the database based on the user input
			List<String> itemsAutoCompleteTmp = new ArrayList<String>();

			for(String uti : triFromItems(userInput, ajoutCollaborateurActivity)){
				Log.i(TAG, "aze2 : "+uti);
				if(uti != null){
					itemsAutoCompleteTmp.add(uti);
				}
			}
			if(ajoutCollaborateurActivity.adapterAutoComplete != null){
				// update the adapater
				ajoutCollaborateurActivity.adapterAutoComplete.notifyDataSetChanged();
				if (itemsAutoCompleteTmp != null) {
					if (itemsAutoCompleteTmp.size() > 0 && checkItems(itemsAutoCompleteTmp)) {
	
						ajoutCollaborateurActivity.adapterAutoComplete = new ArrayAdapter<String>(
								ajoutCollaborateurActivity, android.R.layout.simple_dropdown_item_1line,
								itemsAutoCompleteTmp);
						ajoutCollaborateurActivity.txtLoginCollaborateur
								.setAdapter(ajoutCollaborateurActivity.adapterAutoComplete);
					} else {
						itemsAutoCompleteTmp.clear();
						ajoutCollaborateurActivity.adapterAutoComplete = new ArrayAdapter<String>(
								ajoutCollaborateurActivity, android.R.layout.simple_dropdown_item_1line,
								itemsAutoCompleteTmp);
						ajoutCollaborateurActivity.txtLoginCollaborateur
								.setAdapter(ajoutCollaborateurActivity.adapterAutoComplete);
					}
				}
			}
		}
	}

	private List<String> triFromItems(CharSequence userInput, AjoutCollaborateurActivity ajoutCollaborateurActivity) {
		List<String> listItems = new ArrayList<String>();
		for (String login : ajoutCollaborateurActivity.itemsAutoComplete) {
			Log.i(TAG, "utilisateurLogin : " + login + " userInput : " + userInput);
			if (login.length() >= userInput.length()) {
				if (login.toLowerCase().startsWith(userInput.toString().toLowerCase())) {
					listItems.add(login);
				}
			}
		}
		
		for(String uti : listItems){
			Log.i(TAG, "aze : "+uti);
		}

		return listItems;
	}

	private boolean checkItems(List<String> items) {
		for (String login : items) {
			if (login == null) {
				return false;
			}
		}
		return true;
	}

}
