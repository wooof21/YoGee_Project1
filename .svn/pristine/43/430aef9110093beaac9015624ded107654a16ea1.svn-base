package adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youge.jobfinder.R;

public class ListViewForExchangeAdapter extends BaseAdapter {

	private Context context;
	public static List<List<Map<String, String>>> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public ListViewForExchangeAdapter(Context context,
			List<List<Map<String, String>>> list) {
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
		// return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.integral_exchange,
					null);
			viewHolder = new ViewHolder();
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
	}

}
