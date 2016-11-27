package adapter;

import java.util.ArrayList;
import java.util.HashMap;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.MoreSettingMainActivity;

import find.IntegralExplainActivity;

public class MyIntegralAdapter extends BaseAdapter {

	private HashMap<String, String> headMap;
	private ArrayList<ArrayList<String>> bodyList;
	private Context context;
	private final int TYPE_0 = 0;
	private final int TYPE_1 = 1;

	public MyIntegralAdapter(Context context, HashMap<String, String> headMap,
			ArrayList<ArrayList<String>> bodyList) {
		this.headMap = headMap;
		this.bodyList = bodyList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bodyList.size() + 1;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return TYPE_0;
		} else {
			return TYPE_1;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position == TYPE_0) {
			return null;
		} else {
			return bodyList.get(position - 1);
		}

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		ViewHolder1 vh1 = new ViewHolder1();
		ViewHolder2 vh2 = new ViewHolder2();
		if (convertView == null) {

			if (type == TYPE_0) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.my_integral_head, null);
				LinearLayout integralExplain = (LinearLayout) convertView
						.findViewById(R.id.integralExplain);
				integralExplain.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						context.startActivity(new Intent(context,
								IntegralExplainActivity.class));
					}
				});
				vh1.integral = (TextView) convertView
						.findViewById(R.id.integral);
				convertView.setTag(vh1);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.my_integral_body, null);
				vh2.integral = (TextView) convertView
						.findViewById(R.id.integral);
				vh2.state = (TextView) convertView
						.findViewById(R.id.integral_text);
				vh2.time = (TextView) convertView
						.findViewById(R.id.integral_time);
				convertView.setTag(vh2);
			}
		} else {
			if (type == TYPE_0) {
				vh1 = (ViewHolder1) convertView.getTag();
			} else {
				vh2 = (ViewHolder2) convertView.getTag();
			}
		}

		if (type == TYPE_0) {
			vh1.integral.setText(headMap.get(position + ""));
		} else {
			vh2.state.setText(bodyList.get(position - 1).get(0));
			vh2.time.setText(bodyList.get(position - 1).get(1));
			vh2.integral.setText(bodyList.get(position - 1).get(2));
		}

		return convertView;
	}

	public class ViewHolder1 {
		TextView integral;
	}

	public class ViewHolder2 {
		TextView state;
		TextView time;
		TextView integral;
	}

}
