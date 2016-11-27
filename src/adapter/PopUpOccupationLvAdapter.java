/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.youge.jobfinder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class PopUpOccupationLvAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	
	/**
	 * @param context
	 * @param list
	 */
	public PopUpOccupationLvAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}
	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.popup_occupation_lv_item, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.popup_occupation_lv_item_title);
		ImageView sel = (ImageView) convertView.findViewById(R.id.popup_occupation_lv_item_iv);
		
		title.setText(list.get(position).get("title"));
		if(list.get(position).get("select").equals("0")){
			sel.setVisibility(View.GONE);
		}else{
			sel.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

}
