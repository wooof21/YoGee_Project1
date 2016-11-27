/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.youge.jobfinder.R;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 *@param
 */
public class MyBalanceItemAdapter extends BaseAdapter{

	private Context context;
	private HashMap<String, String> map;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	private final int TYPE_ONE = 1, TYPE_TWO = 2;
	/**
	 * 
	 *@param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size() + 1;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public int getViewTypeCount(){
		// TODO Auto-generated method stub
		return 2;
	}
	
	/**
	 * 
	 *@param
	 */
	@Override
	public int getItemViewType(int position){
		// TODO Auto-generated method stub
		if(position == 0){
			return TYPE_ONE;
		}else{
			return TYPE_TWO;
		}
		
	}
	/**
	 * 
	 *@param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		if(position == 0){
			return null;
		}else{
			return list.get(position-1);
		}
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		ViewHolder1 vh1 = new ViewHolder1();
		ViewHolder2 vh2 = new ViewHolder2();
		int type = getItemViewType(position);
		if(type == TYPE_ONE){
			convertView = lInflater.inflate(R.layout.balance_lv_item_view_type1, null);
			vh1.hint = (LinearLayout) convertView.findViewById(R.id.balance_type1_hint);
			vh1.balance = (TextView) convertView.findViewById(R.id.balance_type1_balance);
			vh1.withdraw = (TextView) convertView.findViewById(R.id.balance_type1_withdraw);
		}else{
			convertView = lInflater.inflate(R.layout.balance_lv_item_view_type2, null);
			vh2.method = (TextView) convertView.findViewById(R.id.balance_type2_method);
			vh2.date = (TextView) convertView.findViewById(R.id.balance_type2_date);
			vh2.balance = (TextView) convertView.findViewById(R.id.balance_type2_balance);
			vh2.inOut = (TextView) convertView.findViewById(R.id.balance_type2_out);
		}
		
		
		return convertView;
	}
	
	class ViewHolder1{
		LinearLayout hint;
		TextView balance;
		TextView withdraw;
	}
	class ViewHolder2{
		TextView method;
		TextView date;
		TextView balance;
		TextView inOut;
	}

}
