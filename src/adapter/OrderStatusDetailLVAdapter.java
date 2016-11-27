/**
 * 
 * @param
 */
package adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;

import tools.Config;
import tools.HttpClient;
import view.RoundImageView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.FillOrderMainActivity;
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.activity.OrderListDetailActivity;
import com.youge.jobfinder.vip.ExamineUserResumeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class OrderStatusDetailLVAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String oid;
	private String type;
	private Handler handler;
	private int which, total;
	private String need;
	private LayoutInflater lInflater;

	

	/**
	 * @param context
	 * @param list
	 */
	public OrderStatusDetailLVAdapter(Context context,
			ArrayList<HashMap<String, String>> list, String oid, Handler handler, String type, int total, String need) {
		super();
		this.context = context;
		this.list = list;
		this.oid = oid;
		this.handler = handler;
		this.type = type;
		this.total = total;
		this.need = need;
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
		if(type.equals("1")){
			if (convertView == null) {
				convertView = lInflater.inflate(
						R.layout.order_choose_employee_lv_item, null);
			}
			ViewHolder vHolder = new ViewHolder();
			vHolder.head = (RoundImageView) convertView
					.findViewById(R.id.order_choose_employee_lv_item_head);
			vHolder.name = (TextView) convertView
					.findViewById(R.id.order_choose_employee_lv_item_name);
			vHolder.resume = (TextView) convertView
					.findViewById(R.id.order_choose_employee_lv_item_resume);
			vHolder.price = (TextView) convertView
					.findViewById(R.id.order_choose_employee_lv_item_price);
			vHolder.choose = (TextView) convertView
					.findViewById(R.id.order_choose_employee_lv_item_choose);

			vHolder.name.setText(list.get(position).get("username"));
			ImageLoader.getInstance().displayImage(list.get(position).get("img"),
					vHolder.head);
			vHolder.price.setText(list.get(position).get("price"));
			Log.e("need OrderStatusDetailLVAdapter ", need);
			if(need.equals("1")){
				vHolder.resume.setVisibility(View.VISIBLE);
				vHolder.resume.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								ExamineUserResumeActivity.class);
						intent.putExtra("isFromOtherInfo", true);
						intent.putExtra("otherUserID", list.get(position).get("userid"));
						context.startActivity(intent);
					}
				});
			}else{
				vHolder.resume.setVisibility(View.INVISIBLE);
			}

			vHolder.choose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(((OrderListDetailActivity) context).getS() < total){
						which = position;
						selHttpClient(oid, list.get(position).get("id"),
								list.get(position).get("userid"));
					}else{
						Toast.makeText(context, "您已选择足够人数, 请先删除(左滑)1人再重新选择!", Toast.LENGTH_SHORT).show();
					}
					
				}
			});

			vHolder.head.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, GetOtherInfoActivity.class);
					intent.putExtra("otherUserId", list.get(position).get("userid"));
					intent.putExtra("need", need);
					context.startActivity(intent);
				}
			});
		}else{
			if(convertView == null){
				convertView = lInflater.inflate(R.layout.select_employee_commit_lv_item, null);
			}
			ViewHolder2 vh2 = new ViewHolder2();
			vh2.head = (RoundImageView) convertView.findViewById(R.id.select_employee_commit_item_head);
			vh2.name = (TextView) convertView.findViewById(R.id.select_employee_commit_item_name);
			vh2.price = (TextView) convertView.findViewById(R.id.select_employee_commit_item_price);
			
			ImageLoader.getInstance().displayImage(list.get(position).get("img"), vh2.head);
			vh2.name.setText(list.get(position).get("username"));
			vh2.price.setText("出价￥" + list.get(position).get("price"));
		}


		return convertView;
	}
	

	private void selHttpClient(String oid, final String sid, final String uid) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("id", oid);
			job.put("quotationid", sid);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.SELECT_BIDER_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("选择报价人接口返回  ---> " + str);
								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")) {
//										Toast.makeText(context, "选择报价人成功!",
//												Toast.LENGTH_SHORT).show();
										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = uid;
										msg.sendToTarget();
									} else {
										Toast.makeText(context, message,
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}
	
	class ViewHolder2{
		RoundImageView head;
		TextView name;
		TextView price;
	}

	class ViewHolder {
		RoundImageView head;
		TextView name;
		TextView resume;
		TextView price;
		TextView choose;
	}
}
