/**
 * 
 * @param
 */
package popup;

import java.io.UnsupportedEncodingException;

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

import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class NamePopUpWindow extends PopupWindow {

	private Context context;
	private String uid, sex, occupation, birthday, city, sname, sPhone;

	public NamePopUpWindow(final Context context, View parent, String uid,
			String sex, String occupation, String birthday, String city,
			String sname, String sPhone) {
		View view = View.inflate(context, R.layout.popup_name, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.context = context;
		this.uid = uid;
		this.sex = sex;
		this.occupation = occupation;
		this.birthday = birthday;
		this.city = city;
		this.sname = sname;
		this.sPhone = sPhone;

		final EditText name = (EditText) view
				.findViewById(R.id.popup_name_name);
		name.setText(sname);
		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_name_dismiss);
		TextView commit = (TextView) view.findViewById(R.id.popup_name_commit);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().length() != 0) {
					updateHttpClient(name.getText().toString());
				} else {
					Toast.makeText(context, "请起个好听的名字吧~!", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

	}

	private void updateHttpClient(final String name) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("name", name);
			job.put("sex", sex);
			job.put("occupation", occupation);
			job.put("birthday", birthday);
			job.put("city", city);
			job.put("phone", sPhone);

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
										Message msg = ChangeUserInfoActivity.instance.handler
												.obtainMessage();
										msg.what = 101;
										msg.obj = name;
										msg.sendToTarget();
										dismiss();
									} else {
										Toast.makeText(context, message,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(context, message,
											Toast.LENGTH_SHORT).show();
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

}
