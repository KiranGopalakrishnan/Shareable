package com.example.shareable;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
 
    Context context;
    Typeface font;
    public CustomListViewAdapter(Context context, int resourceId,
            List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }
     
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        TextView Di;
    }
     
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        font = Typeface.createFromAsset(this.context.getAssets(), "lato_reg.ttf");
        RowItem rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.Di = (TextView) convertView.findViewById(R.id.defaultIcon);
            //TextView ListTitle = (TextView) convertView.findViewById(R.id.title); 
    		holder.txtTitle.setTypeface(font);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
                 
  //      holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
       
        if(rowItem.getDefault() == true)
        {
        	holder.Di.setBackgroundResource(R.drawable.notify_pairing);
        	holder.Di.setText("Paired");
        	holder.Di.setTypeface(font);
        }
        
        return convertView;
    }
}