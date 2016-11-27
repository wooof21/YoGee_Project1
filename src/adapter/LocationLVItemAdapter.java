/**
 * 
 *@param
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
import android.widget.TextView;

/**
 * 
 *@param
 */
public class LocationLVItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	
	/**
	 * @param context
	 * @param list
	 */
	public LocationLVItemAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.location_lv_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.name = (TextView) convertView.findViewById(R.id.loc_item_name);
		vHolder.content = (TextView) convertView.findViewById(R.id.loc_item_detail);
		
		vHolder.name.setText(list.get(position).get("name"));
		vHolder.content.setText(list.get(position).get("city") + " " + list.get(position).get("address"));
		return convertView;
	}
	
	class ViewHolder{
		TextView name;
		TextView content;
	}
}
