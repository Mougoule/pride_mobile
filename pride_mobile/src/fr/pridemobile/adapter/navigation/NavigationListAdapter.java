package fr.pridemobile.adapter.navigation;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import fr.pridemobile.R;
import fr.pridemobile.model.NavigationDrawerElement;

/**
 * Adapteur de la liste des éléments du navigation drawer
 */
public abstract class NavigationListAdapter extends ArrayAdapter<NavigationDrawerElement> {

	
	public NavigationListAdapter(final Context context, List<NavigationDrawerElement> drawerElementObject) {
		super(context, R.layout.row_drawer_element, drawerElementObject);
	}
}