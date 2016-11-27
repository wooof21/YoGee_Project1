/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;

import model.NowEvent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class EventPopupAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NowEvent> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public EventPopupAdapter(Context context, ArrayList<NowEvent> list) {
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
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vHolder = new ViewHolder();
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.event_item, null);
			vHolder.event_item_img = (ImageView) convertView
					.findViewById(R.id.event_item_img);
			vHolder.content = (TextView) convertView
					.findViewById(R.id.event_item_detail);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(
				list.get(position).getActivityImg(), vHolder.event_item_img);
		vHolder.content.setText(list.get(position).getActivityTile());
		return convertView;
	}

	class ViewHolder {
		ImageView event_item_img;
		TextView content;
	}
}
