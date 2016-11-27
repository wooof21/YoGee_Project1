/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

import model.ActivityModel;

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
public class ActivityLVItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ActivityModel> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public ActivityLVItemAdapter(Context context, ArrayList<ActivityModel> list){
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
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.activity_lv_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.iv = (ImageView) convertView
				.findViewById(R.id.activity_lv_item_iv);
		vHolder.tv = (TextView) convertView
				.findViewById(R.id.activity_lv_item_tv);

		ImageLoader.getInstance().displayImage(
				list.get(position).getActivityImg(), vHolder.iv);
		vHolder.tv.setText(list.get(position).getActivityTitle());
		return convertView;
	}

	class ViewHolder{
		ImageView iv;
		TextView tv;
	}
}
