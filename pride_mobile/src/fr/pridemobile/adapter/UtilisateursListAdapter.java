package fr.pridemobile.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.model.beans.Utilisateur;

/**
 * Adapteur de la liste des utilisateurs
 */
public class UtilisateursListAdapter extends ArrayAdapter<Utilisateur> {

	
	public UtilisateursListAdapter(final Context context, List<Utilisateur> utlisateurObject) {
		super(context, R.layout.row_utilisateur, utlisateurObject);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		final Utilisateur utilisateur = getItem(position);
		if (utilisateur != null) {
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
				convertView = inflater.inflate(R.layout.row_utilisateur, parent, false);

				holder = new ViewHolder();
				holder.txtLogin = (TextView) convertView.findViewById(R.id.login);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String login = utilisateur.getLogin();
			holder.txtLogin.setText(login);
			/*convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent activity = new Intent(getContext(), DetailProjetActivity.class);
					activity.putExtra("position", position);
					activity.putExtra("nomProjet", login);
					getContext().startActivity(activity);
				}
			});*/
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView txtLogin;
	}
}