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
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.vip.ExamineUserResumeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class MyEmployeeSelectedAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String oid, need;
	private Handler handler;
	// 屏幕宽度,由于我们用的是HorizontalScrollView,所以按钮选项应该在屏幕外
	private int mScreentWidth, which;
	private LayoutInflater lInflater;
	private View view;

	/**
	 * @param context
	 * @param list
	 */
	public MyEmployeeSelectedAdapter(Context context,
			ArrayList<HashMap<String, String>> list, int screenWidth,
			String oid, Handler handler, String need) {
		super();
		this.context = context;
		this.list = list;
		this.oid = oid;
		this.handler = handler;
		this.need = need;
		this.mScreentWidth = screenWidth;
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
		ViewHolder vHolder;
		if (convertView == null) {
			convertView = lInflater.inflate(
					R.layout.order_choose_employee_selected_lv_item, null);
			vHolder = new ViewHolder();
			vHolder.hsv = (HorizontalScrollView) convertView
					.findViewById(R.id.order_choose_emploee_selected_hsv);
			vHolder.action = convertView.findViewById(R.id.ll_action);
			vHolder.content = (LinearLayout) convertView
					.findViewById(R.id.order_choose_emploee_selected_conent);
			vHolder.head = (RoundImageView) convertView
					.findViewById(R.id.order_choose_employee_select_head);
			vHolder.name = (TextView) convertView
					.findViewById(R.id.order_choose_employee_select_name);
			vHolder.resume = (TextView) convertView
					.findViewById(R.id.order_choose_employee_select_resume);
			vHolder.money = (TextView) convertView
					.findViewById(R.id.order_choose_employee_select_price);
			vHolder.choose = (TextView) convertView
					.findViewById(R.id.order_choose_employee_select_choose);
			vHolder.delete = (TextView) convertView
					.findViewById(R.id.order_choose_employee_select_delete);

			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

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
			vHolder.resume.setVisibility(View.GONE);
		}
		vHolder.name.setText(list.get(position).get("username"));
		ImageLoader.getInstance().displayImage(list.get(position).get("img"),
				vHolder.head);
		vHolder.money.setText(list.get(position).get("price"));

		// 把位置放到view中,这样点击事件就可以知道点击的是哪一条item
		vHolder.delete.setTag(position);

		LayoutParams lp = vHolder.content.getLayoutParams();
		lp.width = mScreentWidth;

		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (view != null) {
						ViewHolder viewHolder1 = (ViewHolder) view.getTag();
						viewHolder1.hsv.smoothScrollTo(0, 0);
					}
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
					if (scrollX < actionW / 2) {
						viewHolder.hsv.smoothScrollTo(0, 0);
					} else// 否则的话显示操作区域
					{
						viewHolder.hsv.smoothScrollTo(actionW, 0);
					}
					return true;
				}
				return false;
			}
		});

		// 这里防止删除一条item后,ListView处于操作状态,直接还原
		if (vHolder.hsv.getScrollX() != 0) {
			vHolder.hsv.scrollTo(0, 0);
		}

		// 设置监听事件
		vHolder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("dddddddddddddddddddddddddd");
				deleteHttpClient(oid, list.get(position).get("id"),
						list.get(position).get("userid"));
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
		return convertView;
	}

	private void deleteHttpClient(String oid, final String sid, final String uid) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("id", oid);
			job.put("quotationid", sid);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.DELETE_BIDER_URL, se,
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
//										Toast.makeText(context, "删除报价人成功!",
//												Toast.LENGTH_SHORT).show();
										Message msg = handler.obtainMessage();
										msg.what = 2;
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

	class ViewHolder {
		HorizontalScrollView hsv;
		View action;
		LinearLayout content;
		RoundImageView head;
		TextView name;
		TextView resume;
		TextView money;
		TextView choose;
		TextView delete;
	}

}
