/**
 * 
 * @param
 */
package popup;

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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.ChangeUserInfoActivity;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;

import adapter.PopUpOccupationLvAdapter;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class OccupationPopUpWindow extends PopupWindow {

	private Context context;
	private String uid, sex, occupation, birthday, city, name, phone;
	private ArrayList<HashMap<String, String>> list;
	private String[] occu = { "教师", "公务员", "学生", "医生", "工程师", "IT人士", "白领",
			"服务人员", "技术工人", "自营", "其他" };
	private PopUpOccupationLvAdapter adapter;

	public OccupationPopUpWindow(Context context, View parent, String uid,
			String sex, String occupation, String birthday, String city,
			String name, String phone) {
		View view = View.inflate(context, R.layout.popup_occupation, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.context = context;
		this.uid = uid;
		this.sex = sex;
		this.occupation = occupation;
		this.birthday = birthday;
		this.city = city;
		this.name = name;
		this.phone = phone;

		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0, j = occu.length; i < j; i++) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			if (occupation.equals(occu[i])) {
				hashMap.put("title", occu[i]);
				hashMap.put("select", "1");
			} else {
				hashMap.put("title", occu[i]);
				hashMap.put("select", "0");
			}

			list.add(hashMap);
		}

		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_occupation_dismiss);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_occupation_commit);
		ListView lv = (ListView) view.findViewById(R.id.popup_occupation_lv);

		adapter = new PopUpOccupationLvAdapter(context, list);
		lv.setAdapter(adapter);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("position ---> " + position);
				for (int i = 0, j = list.size(); i < j; i++) {
					list.get(i).put("select", "0");
				}
				OccupationPopUpWindow.this.occupation = list.get(position).get(
						"title");
				list.get(position).put("select", "1");
				adapter.notifyDataSetChanged();
				updateHttpClient(OccupationPopUpWindow.this.occupation);
				Message msg = ChangeUserInfoActivity.instance.handler
						.obtainMessage();
				msg.what = 104;
				msg.obj = OccupationPopUpWindow.this.occupation;
				msg.sendToTarget();
				dismiss();

			}
		});
		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

	}

	/**
	 * 简历中职业类型
	 */
	public OccupationPopUpWindow(Context context, View parent,
			String occupationText) {
		View view = View.inflate(context, R.layout.popup_occupation, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.context = context;
		this.occupation = "";

		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0, j = occu.length; i < j; i++) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("title", occu[i]);
			if (occupationText.equals(occu[i])) {
				hashMap.put("select", "1");
			} else {
				hashMap.put("select", "0");
			}

			list.add(hashMap);
		}

		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_occupation_dismiss);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_occupation_commit);
		ListView lv = (ListView) view.findViewById(R.id.popup_occupation_lv);
		LinearLayout l = (LinearLayout) view.findViewById(R.id.l);
		// l.setVisibility(View.GONE);

		adapter = new PopUpOccupationLvAdapter(context, list);
		lv.setAdapter(adapter);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("position ---> " + position);
				for (int i = 0, j = list.size(); i < j; i++) {
					list.get(i).put("select", "0");
				}
				OccupationPopUpWindow.this.occupation = list.get(position).get(
						"title");
				list.get(position).put("select", "1");
				adapter.notifyDataSetChanged();
				Message msg = ChangeUserResumeActivity.instance.mHandler
						.obtainMessage();
				msg.what = 3;
				msg.obj = OccupationPopUpWindow.this.occupation;
				msg.sendToTarget();
				dismiss();

			}
		});
		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

	}

	private boolean bool = false;

	private boolean updateHttpClient(String occupation) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("name", name);
			job.put("sex", sex);
			job.put("occupation", OccupationPopUpWindow.this.occupation);
			job.put("birthday", birthday);
			job.put("city", city);
			job.put("phone", phone);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(context, Config.UPDATE_USER_INFO_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String str = new String(arg2);
							System.out.println("修改个人信息接口返回 ---> " + str);
							String message = "";
							try {
								JSONObject job = new JSONObject(str);
								String state = job.getString("state");
								message = job.getString("msg");
								if (state.equals("success")) {
									JSONObject data = job.getJSONObject("data");
									String result = data.getString("result");
									if (result.equals("1")) {
										bool = true;
									} else {
										bool = false;
										Toast.makeText(context, message,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(context, message,
											Toast.LENGTH_SHORT).show();
									bool = false;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(context, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
							bool = false;
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

		return bool;
	}
}
