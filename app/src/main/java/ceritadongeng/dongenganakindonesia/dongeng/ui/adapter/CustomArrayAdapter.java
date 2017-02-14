package ceritadongeng.dongenganakindonesia.dongeng.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ceritadongeng.dongenganakindonesia.dongeng.R;


public class CustomArrayAdapter extends ArrayAdapter<String> {

	private LayoutInflater mInflater;
	private Typeface mFont;
	private String[] mData;
	
    public CustomArrayAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data); 
        
        mInflater 	= LayoutInflater.from(context);        
        mData		= data;
    }

    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {        
    	ViewHolder holder = null;
    	
    	if (convertView == null) {			
			convertView		=  mInflater.inflate(R.layout.list_item_adapter, null);
				
			holder 			= new ViewHolder();							
			holder.titleTv	= (TextView) convertView.findViewById(R.id.tv_location_name);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
    	
    	if (mFont != null) {
    		holder.titleTv.setTypeface(mFont);
    	}
    	
    	holder.titleTv.setText(mData[position]);
    	
        return convertView;
    }
    
    /**
     * View Displayed inside list of possible options
     */
     @SuppressLint("InflateParams")
	@Override
     public View getDropDownView(int position, View convertView, ViewGroup parent) {
    	 ViewHolder holder = null;
     	
	     	if (convertView == null) {			
	 			convertView		=  mInflater.inflate(R.layout.simple_spinner_dropdown_item, null);
	 				
	 			holder 			= new ViewHolder();							
	 			holder.titleTv	= (TextView) convertView.findViewById(R.id.tv_location_name);
	 			
	 			convertView.setTag(holder);
	 		} else {
	 			holder = (ViewHolder) convertView.getTag();
	 		}
	     	
	     	if (mFont != null) {
	     		holder.titleTv.setTypeface(mFont);
	     	}
	     	
	     	holder.titleTv.setText(mData[position]);
	     	
         return convertView;
     }
     
    static class ViewHolder
    {
        TextView titleTv;
    }
	
}
