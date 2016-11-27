/**
 * 
 * @param
 */
package adapter;

import ivzoom.ImagePagerActivity;

import java.util.ArrayList;
import java.util.HashMap;

import popup.GalleyPopUpWindow;
import tools.Tools;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.ImageZoomActivity;

import discover.ConvenienceFacilitiyActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @param
 */
public class ConvenienceFacilityGVAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public ConvenienceFacilityGVAdapter(Context context,
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
		return position;//
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.convenience_facility_gv_item,
					null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.iv = (ImageView) convertView
				.findViewById(R.id.my_grab_order_gv_item_iv);
		vHolder.tv = (TextView) convertView
				.findViewById(R.id.my_grab_order_gv_item_tv);

		ImageLoader.getInstance().displayImage(list.get(position).get("img"),
				vHolder.iv);
		vHolder.tv.setText(list.get(position).get("name"));

		return convertView;
	}

	class ViewHolder{
		ImageView iv;
		TextView tv;
	}
}
