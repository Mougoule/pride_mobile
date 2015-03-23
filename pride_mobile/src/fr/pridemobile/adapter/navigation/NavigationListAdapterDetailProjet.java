package fr.pridemobile.adapter.navigation;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.model.NavigationDrawerElement;

public class NavigationListAdapterDetailProjet extends NavigationListAdapter{

	public NavigationListAdapterDetailProjet(Context context, List<NavigationDrawerElement> drawerElementObject) {
		super(context, drawerElementObject);
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
					//Intent activity = new Intent(getContext(), DetailLivraisonActivity.class);
					//activity.putExtra("position", position);
					//getContext().startActivity(activity);
					Toast.makeText(getContext(), "Titre label : "+titre, Toast.LENGTH_SHORT).show();
				}
			});
		}
		return convertView;
	}
	
	private static class ViewHolder {
		private TextView txtLabelElement;
	}

}
