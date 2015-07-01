package fr.pridemobile.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.pridemobile.R;

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<String> {
	 
    final String TAG = "AutocompleteCustomArrayAdapter.java";
         
    Context mContext;
    int layoutResourceId = R.layout.list_view_row;
    List<String> data = null;
 
    public AutocompleteCustomArrayAdapter(Context mContext, List<String> data) {
 
        super(mContext, R.layout.list_view_row, data);
         
        this.mContext = mContext;
        this.data = data;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         
        try{
             
            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post 
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout. 
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }
             
            // object item based on the position
            String objectItem = data.get(position);
 
            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            textViewItem.setText(objectItem);
             
            // in case you want to add some style, you can do something like:
            textViewItem.setBackgroundColor(Color.WHITE);
            textViewItem.setTextColor(Color.BLACK);
             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return convertView;
         
    }
}