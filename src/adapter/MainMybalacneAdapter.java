package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import model.OrderRecords;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.BalacneExplainActivity;
import com.youge.jobfinder.vip.WithdrawDepositActivity;

public class MainMybalacneAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<OrderRecords> list;

	/**
	 * @param context
	 * @param list
	 */
	public MainMybalacneAdapter(Context context, ArrayList<OrderRecords> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
		// return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder2 viewHolder2 = new ViewHolder2();
		if (convertView == null) {

			convertView = View.inflate(context, R.layout.main_mybalacne, null);
			viewHolder2.integral_text = (TextView) convertView
					.findViewById(R.id.integral_text);
			viewHolder2.integral_time = (TextView) convertView
					.findViewById(R.id.integral_time);
			viewHolder2.integral = (TextView) convertView
					.findViewById(R.id.integral);
			convertView.setTag(viewHolder2);
		} else {
			viewHolder2 = (ViewHolder2) convertView.getTag();
		}
		viewHolder2.integral_text.setText(list.get(position).getTitle());
		viewHolder2.integral_time.setText(list.get(position).getCreateDate());
		if ("1".equals(list.get(position).getType())) {
			viewHolder2.integral.setText("-" + list.get(position).getMoney());
			viewHolder2.integral.setTextColor(context.getResources().getColor(
					R.color.sex_text_color));
		} else if ("2".equals(list.get(position).getType())) {
			viewHolder2.integral.setText("+" + list.get(position).getMoney());
			viewHolder2.integral.setTextColor(context.getResources().getColor(
					R.color.binding_text_color));
		}
		return convertView;
	}

	public class ViewHolder2 {
		private TextView integral_text, integral_time, integral;
	}

}
