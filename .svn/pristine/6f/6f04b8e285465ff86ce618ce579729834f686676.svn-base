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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.OrderListDetailActivity;

import tools.Config;
import tools.HttpClient;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class MyMessageLvItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	// 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外
	private int mScreentWidth, which;
	private LayoutInflater lInflater;
	private View view;
	private Handler handler;

	/**
	 * @param context
	 * @param list
	 */
	public MyMessageLvItemAdapter(Context context,
			ArrayList<HashMap<String, String>> list, int screenWidth,
			Handler handler){
		super();
		this.context = context;
		this.list = list;
		this.mScreentWidth = screenWidth;
		this.handler = handler;
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
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.my_message_lv_item, null);

			vHolder = new ViewHolder();
			vHolder.pic = (ImageView) convertView
					.findViewById(R.id.my_meseage_item_pic);
			vHolder.dt = (TextView) convertView
					.findViewById(R.id.my_meseage_item_dt);
			vHolder.content = (TextView) convertView
					.findViewById(R.id.my_meseage_item_content);
			vHolder.detail = (TextView) convertView
					.findViewById(R.id.my_meseage_item_detail);
			vHolder.hsv = (HorizontalScrollView) convertView
					.findViewById(R.id.msg_hsv);
			vHolder.delete = (TextView) convertView
					.findViewById(R.id.msg_delete);
			vHolder.action = convertView.findViewById(R.id.ll_action);
			vHolder.container = (LinearLayout) convertView
					.findViewById(R.id.msg_conent);
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).get("type").equals("2")){ // 系统消息
			vHolder.pic.setImageResource(R.drawable.msg_notify_sys);
			convertView.setClickable(false);
		}else{
			vHolder.pic.setImageResource(R.drawable.msg_notify_order);
		}

		vHolder.dt.setText(list.get(position).get("orderTime"));
		vHolder.content.setText(list.get(position).get("title"));
		vHolder.detail.setText(list.get(position).get("content"));

		// 把位置放到view中,这样点击事件就可以知道点击的是哪一条item
		vHolder.delete.setTag(position);

		LayoutParams lp = vHolder.container.getLayoutParams();
		lp.width = mScreentWidth;

		convertView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event){
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						if (view != null){
							ViewHolder viewHolder1 = (ViewHolder) view.getTag();
							viewHolder1.hsv.smoothScrollTo(0, 0);
						}
					break;
					case MotionEvent.ACTION_UP:
						// 获得ViewHolder
						ViewHolder viewHolder = (ViewHolder) v.getTag();
						view = v;
						// 获得HorizontalScrollView滑动的水平方向值.
						int scrollX = viewHolder.hsv.getScrollX();

						// 获得操作区域的长度
						int actionW = viewHolder.action.getWidth();
						// 注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
						// 如果水平方向的移动值<操作区域的长度的一半,就复原
						if (scrollX < actionW / 2){
							viewHolder.hsv.smoothScrollTo(0, 0);
						}else// 否则的话显示操作区域
						{
							viewHolder.hsv.smoothScrollTo(actionW, 0);
						}
					// return true;
					break;
				}
				return false;
			}
		});

		// 这里防止删除一条item后,ListView处于操作状态,直接还原
		if (vHolder.hsv.getScrollX() != 0){
			vHolder.hsv.scrollTo(0, 0);
		}

		// 设置监听事件
		vHolder.delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				deleteMsg(list.get(position).get("id"));
			}
		});

		vHolder.container.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (!list.get(position).get("type").equals("2")){
					System.out.println("position ---> " + position);
					Intent intent = new Intent(context,
							OrderListDetailActivity.class);
					intent.putExtra("category", list.get(position).get("type"));
					intent.putExtra("oid", list.get(position).get("goodOrder"));
					context.startActivity(intent);
				}

				// if (!list.get(position).get("type").equals("2")){
				// if (list.get(position).get("style").equals("1")){ // g
				// if (list.get(position).get("orderState").equals("1")
				// || list.get(position).get("orderState")
				// .equals("2")){
				// Intent intent = new Intent(context,
				// OrderDetailActivity.class);
				// intent.putExtra("category",
				// list.get(position).get("type"));
				// intent.putExtra("id",
				// list.get(position).get("goodOrder"));
				// context.startActivity(intent);
				// }else{
				// Intent intent2 = new Intent(context,
				// OrderStatusDetailActivity.class);
				// intent2.putExtra("category", list.get(position)
				// .get("type"));
				// intent2.putExtra("oid",
				// list.get(position).get("goodOrder"));
				// intent2.putExtra("orderState", list.get(position)
				// .get("orderState"));
				// if (list.get(position).get("orderState")
				// .equals("3")){
				// intent2.putExtra("where", "inprocess_grab");
				// }else{
				// intent2.putExtra("where", "g");
				// }
				// context.startActivity(intent2);
				// }
				// }else{
				// Intent intent1 = new Intent(context,
				// OrderStatusDetailActivity.class);
				// intent1.putExtra("category",
				// list.get(position).get("type"));
				// intent1.putExtra("oid",
				// list.get(position).get("goodOrder"));
				// intent1.putExtra("orderState",
				// list.get(position).get("orderState"));
				// if (list.get(position).get("orderState").equals("3")){
				// intent1.putExtra("where", "inprocess_post");
				// }else{
				// intent1.putExtra("where", "P");
				// }
				// context.startActivity(intent1);
				// }
				// }
			}
		});

		return convertView;
	}

	class ViewHolder{
		HorizontalScrollView hsv;
		View action;
		LinearLayout container;
		ImageView pic;
		TextView dt;
		TextView content;
		TextView detail;
		TextView delete;
	}

	private void deleteMsg(String id){
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.NOTIFICATION_DELETE_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("删除消息通知接口返回  ---> " + str);
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										Toast.makeText(context, "删除消息通知成功!",
												Toast.LENGTH_SHORT).show();
										Message msg = handler.obtainMessage();
										msg.sendToTarget();
									}else{
										Toast.makeText(context, message,
												Toast.LENGTH_SHORT).show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(context, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}
}
