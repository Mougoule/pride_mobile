package fr.pridemobile.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.model.beans.Idee;

/**
 * Adapteur de la liste des projets
 */
public class IdeeListAdapter extends ArrayAdapter<Idee> {

	
	public IdeeListAdapter(final Context context, List<Idee> ideeObject) {
		super(context, R.layout.row_idee, ideeObject);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		Idee idee = null;

		idee = getItem(position);
		if (idee != null) {
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
				convertView = inflater.inflate(R.layout.row_idee, parent, false);

				holder = new ViewHolder();
				holder.txtIdee = (TextView) convertView.findViewById(R.id.txt_idee);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// Récuperation de la description des livraisons
			final String txtIdee = idee.getIdee();
			holder.txtIdee.setText(txtIdee);
			// Gestion du click sur la liste des livraisons
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					//Intent activity = new Intent(getContext(), DetailProjetActivity.class);
					//activity.putExtra("position", position);
					//activity.putExtra("idee", txtIdee);
					//getContext().startActivity(activity);
				}
			});
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView txtIdee;
	}
}