package fr.pridemobile.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import fr.pridemobile.R;
import fr.pridemobile.model.beans.Projet;

/**
 * Adapteur de la liste des projets
 */
public class ProjetsListAdapter extends ArrayAdapter<Projet> {

	
	public ProjetsListAdapter(final Context context, List<Projet> projetObject) {
		super(context, R.layout.row_projet, projetObject);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		Projet projet = null;

		projet = getItem(position);
		if (projet != null) {
			if (convertView == null) {
				LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
				convertView = inflater.inflate(R.layout.row_projet, parent, false);

				holder = new ViewHolder();
				holder.txtNomProjet = (TextView) convertView.findViewById(R.id.nom_projet);
				holder.txtDescription = (TextView) convertView.findViewById(R.id.description_projet);
				holder.txtNote = (TextView) convertView.findViewById(R.id.note_projet);
				holder.imgProjet = (ImageView) convertView.findViewById(R.id.image_projet);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// Récuperation de la description des livraisons
			holder.txtNomProjet.setText(projet.getNomProjet());
			updateTextAndVisibility(holder.txtDescription, projet.getDescription());
			updateTextAndVisibility(holder.txtNote, projet.getNoteProjet());
			updateImageAndVisibility(holder.imgProjet, projet.getImage());
			final String nom = holder.txtNomProjet.getText().toString();
			// Gestion du click sur la liste des livraisons
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					//Intent activity = new Intent(getContext(), DetailLivraisonActivity.class);
					//activity.putExtra("position", position);
					//getContext().startActivity(activity);
					Toast.makeText(getContext(), "Nom projet : "+nom, Toast.LENGTH_SHORT).show();
				}
			});
		}
		return convertView;
	}

	/**
	 * Vérifier si les champs texte sont null. Permet aussi de cacher les champs
	 * vides.
	 * 
	 * @param textview
	 * @param txt
	 *            champs du WS
	 */
	private void updateTextAndVisibility(TextView textview, String txt) {
		if (txt == null || txt.length() == 0) {
			textview.setVisibility(View.GONE);
		} else {
			textview.setText(txt);
		}
	}
	
	/**
	 * Vérifier si les champs texte sont null. Permet aussi de cacher les champs
	 * vides.
	 * 
	 * @param textview
	 * @param nb
	 *            champs du WS
	 */
	private void updateTextAndVisibility(TextView textview, int nb) {
		textview.setText(Integer.toString(nb));
	}
	
	/**
	 * Vérifier si les champs texte sont null. Permet aussi de cacher les champs
	 * vides.
	 * 
	 * @param imageview
	 * @param txt
	 *            champs du WS
	 */
	private void updateImageAndVisibility(ImageView imageview, byte[] img) {
		if (img == null || img.length == 0) {
			imageview.setVisibility(View.GONE);
		} else {
	        Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);

	        imageview.setMinimumHeight(bm.getHeight());
	        imageview.setMinimumWidth(bm.getWidth());
	        imageview.setImageBitmap(bm);
		}
	}

	private static class ViewHolder {
		private TextView txtNomProjet;
		private TextView txtDescription;
		private TextView txtNote;
		private ImageView imgProjet;
	}
}