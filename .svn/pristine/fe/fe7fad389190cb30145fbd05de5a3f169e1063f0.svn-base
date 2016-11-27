/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import baidu.BaiDuMapActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class ConvenienceFacilityLvAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public ConvenienceFacilityLvAdapter(Context context,
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
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if (convertView == null){
			convertView = lInflater.inflate(
					R.layout.convenience_facility_lv_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.pic = (ImageView) convertView
				.findViewById(R.id.convenience_facility_lv_item_pic);
		vHolder.name = (TextView) convertView
				.findViewById(R.id.convenience_facility_lv_item_name);
		vHolder.distance = (TextView) convertView
				.findViewById(R.id.convenience_facility_lv_item_distance);
		vHolder.address = (TextView) convertView
				.findViewById(R.id.convenience_facility_lv_item_address);
		vHolder.phoneClick = (LinearLayout) convertView
				.findViewById(R.id.convenience_facility_lv_item_phone_click);
		vHolder.phone = (TextView) convertView
				.findViewById(R.id.convenience_facility_lv_item_phone);
		vHolder.rb = (RatingBar) convertView
				.findViewById(R.id.convenience_facility_lv_item_rb);
		vHolder.checkMap = (ImageView) convertView
				.findViewById(R.id.convenience_facility_lv_item_checkmap);

		ImageLoader.getInstance().displayImage(list.get(position).get("img"),
				vHolder.pic);
		vHolder.address.setText(list.get(position).get("address"));
		vHolder.distance.setText(list.get(position).get("distance"));
		vHolder.name.setText(list.get(position).get("name"));
		if (list.get(position).get("phone").length() == 0){
			vHolder.phone.setText("暂无");
		}else{
			vHolder.phone
					.setText(list.get(position).get("phone").split(" ")[0]);
			final String phone = vHolder.phone.getText().toString();
			vHolder.phoneClick.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v){
					// TODO Auto-generated method stub
					 context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone)));  
				}
			});
		}
		
		float rate = (float) (Float.valueOf(list.get(position).get("grade")) / 10.0);
		vHolder.rb.setRating(rate);
		final double orderLat = Double.valueOf(list.get(position).get("lat"));
		final double orderLng = Double.valueOf(list.get(position).get("lng"));
		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, BaiDuMapActivity.class);
				intent.putExtra("lat", orderLat);
				intent.putExtra("lng", orderLng);
				intent.putExtra("title", list.get(position).get("name"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView pic;
		TextView name;
		TextView address;
		LinearLayout phoneClick;
		TextView phone;
		TextView distance;
		RatingBar rb;
		ImageView checkMap;
	}
}
