/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

import discover.CreditMallExchangeListDetail;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class CreditMallExchangeListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public CreditMallExchangeListAdapter(Context context,
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
		ViewHolder vHolder;
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.credit_mall_list_item, null);
			vHolder = new ViewHolder();
			vHolder.pic = (ImageView) convertView.findViewById(R.id.credit_mall_exchange_list_item_pic);
			vHolder.name = (TextView) convertView.findViewById(R.id.credit_mall_exchange_list_item_name);
			vHolder.credit = (TextView) convertView.findViewById(R.id.credit_mall_exchange_list_item_credit);
			vHolder.status = (TextView) convertView.findViewById(R.id.credit_mall_exchange_list_item_status);
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(list.get(position).get("img"), vHolder.pic);
		vHolder.name.setText(list.get(position).get("name"));
		vHolder.credit.setText(Html.fromHtml("<font size=\"14\" color=\"red\">"
							+ list.get(position).get("point")
							+ "</font><font size=\"12\" color=\"grey\">积分</font>"));
		convertView.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CreditMallExchangeListDetail.class);
				intent.putExtra("id", list.get(position).get("id"));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder{
		ImageView pic;
		TextView name;
		TextView credit;
		TextView status;
	}

}
