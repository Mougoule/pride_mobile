package fr.pridemobile.adapter.navigation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.pridemobile.R;
import fr.pridemobile.activity.MesProjetsActivity;
import fr.pridemobile.model.NavigationDrawerElement;

/**
 * Adapteur de la liste des éléments du navigation drawer
 */
public class NavigationListAdapter extends ArrayAdapter<NavigationDrawerElement> {

	public NavigationListAdapter(final Context context, List<NavigationDrawerElement> drawerElementObject) {
		super(context, R.layout.row_drawer_element, drawerElementObject);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		NavigationDrawerElement element = null;

		element = getItem(position);
		if (element != null) {
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
				convertView = inflater.inflate(R.layout.row_drawer_element, parent, false);

				holder = new ViewHolder();
				holder.txtLabelElement = (TextView) convertView.findViewById(R.id.label_element);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// Récuperation de la description des livraisons
			final String titre = element.getTitre();
			holder.txtLabelElement.setText(titre);
			// Gestion du click sur la liste des livraisons
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					switch (titre) {

					case ConstantLienNavigation.PROJETS_COLLABORATIONS:
						
					case ConstantLienNavigation.PROJETS_GERES:
						Intent activity = new Intent(getContext(), MesProjetsActivity.class);
						//activity.putExtra("position", position);
						getContext().startActivity(activity);
					case ConstantLienNavigation.PROJETS_COMMUNAUTE:

					}
				}
			});
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView txtLabelElement;
	}
}