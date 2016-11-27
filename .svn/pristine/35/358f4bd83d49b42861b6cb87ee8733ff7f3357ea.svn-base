/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;

import model.Address;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.youge.jobfinder.activity.AddressMainActivity;

/**
 * 
 * @param
 */
public class AddressLVItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Address> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public AddressLVItemAdapter(Context context, ArrayList<Address> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.address_lv_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.name = (TextView) convertView
				.findViewById(R.id.addr_lv_item_name);
		vHolder.phone = (TextView) convertView
				.findViewById(R.id.addr_lv_item_phone);
		vHolder.address = (TextView) convertView
				.findViewById(R.id.addr_lv_item_address);
		vHolder.fix = (LinearLayout) convertView
				.findViewById(R.id.addr_lv_item_fix);
		vHolder.sel = (ImageView) convertView
				.findViewById(R.id.addr_lv_item_sel);

		if (list.get(position).getIsSelected().equals("0")) {
			vHolder.sel.setImageResource(R.drawable.circle_uns);
		} else {
			vHolder.sel.setImageResource(R.drawable.circle_sel);
		}
		vHolder.name.setText(list.get(position).getName());
		vHolder.phone.setText(list.get(position).getPhone());
		vHolder.address.setText(list.get(position).getAddress());
		if ("1".equals(list.get(position).getIsSelected())) {
			SharedPreferences sharedPre = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putString("addressMainName", list.get(position).getName());
			editor.putString("addressMainSex", list.get(position).getSex());
			editor.putString("addressMainAddress", list.get(position)
					.getAddress());
			editor.putString("addressMainPhone", list.get(position).getPhone());
			editor.commit();
		}

		vHolder.fix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, AddAddressActivity.class);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("name", list.get(position).getName());
				intent.putExtra("phone", list.get(position).getPhone());
				intent.putExtra("address", list.get(position).getAddress());
				intent.putExtra("sex", list.get(position).getSex());
				intent.putExtra("isSelected", list.get(position)
						.getIsSelected());
				((AddressMainActivity) context).startActivityForResult(intent,
						200);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView phone;
		TextView address;
		LinearLayout fix;
		ImageView sel;
	}
}
