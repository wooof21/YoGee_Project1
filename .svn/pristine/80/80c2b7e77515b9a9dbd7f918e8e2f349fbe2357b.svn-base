/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;

import model.Experience;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.AddAddressActivity;
import com.youge.jobfinder.activity.AddExperienceActivity;
import com.youge.jobfinder.activity.AddressMainActivity;
import com.youge.jobfinder.activity.ExperienceMainActivity;

/**
 * 
 * @param
 */
public class ExperienceLVItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Experience> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public ExperienceLVItemAdapter(Context context, ArrayList<Experience> list) {
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
		// return 10;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.experience_lv_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.startDate = (TextView) convertView
				.findViewById(R.id.addr_lv_item_startDate);
		vHolder.endDate = (TextView) convertView
				.findViewById(R.id.addr_lv_item_endDate);
		vHolder.Employer = (TextView) convertView
				.findViewById(R.id.addr_lv_item_Employer);
		vHolder.obligation = (TextView) convertView
				.findViewById(R.id.addr_lv_item_obligation);
		vHolder.count = (TextView) convertView
				.findViewById(R.id.addr_lv_item_month);
		vHolder.fix = (LinearLayout) convertView
				.findViewById(R.id.addr_lv_item_fix);

		vHolder.startDate.setText(list.get(position).getStartDate());
		vHolder.endDate.setText(list.get(position).getEndDate());
		vHolder.Employer.setText(list.get(position).getEmployer());
		vHolder.obligation.setText(list.get(position).getObligation());
		vHolder.count.setText("("
				+ mouthcount(list.get(position).getStartDate(),
						list.get(position).getEndDate()) + ")");

		vHolder.fix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, AddExperienceActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("experienceList", list);
				intent.putExtras(mBundle);
				intent.putExtra("isFromExp", false);
				intent.putExtra("position", position + "");
				((ExperienceMainActivity) context).startActivityForResult(
						intent, 200);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView startDate;
		TextView endDate;
		TextView Employer;
		TextView obligation;
		TextView count;
		LinearLayout fix;
	}

	private String mouthcount(String startDate, String endDate) {
		String count = "";
		int sYear = Integer.parseInt(startDate.split("\\.")[0]);
		int sMonth = Integer.parseInt(startDate.split("\\.")[1]);
		int eYear = Integer.parseInt(endDate.split("\\.")[0]);
		int eMonth = Integer.parseInt(endDate.split("\\.")[1]);
		if (eYear - sYear != 0) {
			count = eYear - sYear + "年";
		}
		if (eMonth - sMonth != 0) {
			if (eMonth - sMonth < 0) {
				if (eYear - sYear - 1 == 0) {
					count = 12 + eMonth - sMonth + "个月";
				} else {
					count = eYear - sYear - 1 + "年"
							+ (12 + eMonth - sMonth + "个月");
				}
			} else {
				count = count + (eMonth - sMonth + "个月");
			}
		}

		return count;
	}
}
