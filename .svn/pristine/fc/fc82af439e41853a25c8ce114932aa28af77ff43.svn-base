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

/**
 * 
 *@param
 */
public class DiscoverMainNearbyItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	
	/**
	 * @param context
	 * @param list
	 */
	public DiscoverMainNearbyItemAdapter(Context context,
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
		return 20;
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
			convertView = lInflater.inflate(R.layout.discover_main_lv_item, null);
		}
		return convertView;
	}

}
