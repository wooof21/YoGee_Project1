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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BindingAccountActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class AlipayPopUpWindow extends PopupWindow {

	private Context context;
	private String uid;

	public AlipayPopUpWindow(final Context context, View parent, String uid) {
		View view = View.inflate(context, R.layout.popup_alipay, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.context = context;
		this.uid = uid;

		final EditText name = (EditText) view
				.findViewById(R.id.popup_name_name);
		final EditText name_too = (EditText) view
				.findViewById(R.id.popup_name_name_too);

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
				if (name.getText().equals(name_too.getText())) {
					if (name.getText().toString().length() != 0) {
						bindingHttpClient(name.getText().toString());
					} else {
						Toast.makeText(context, "取消绑定", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(context, "两次输入账号不一致！", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	private boolean bool = false;

	private boolean bindingHttpClient(String alipay) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("type", "2");
			job.put("openid", alipay);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(context, Config.BINDING, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String str = new String(arg2);
							System.out.println("绑定支付宝接口返回 ---> " + str);
							String message = "";
							try {
								JSONObject job = new JSONObject(str);
								String state = job.getString("state");
								message = job.getString("msg");
								if (state.equals("success")) {
									JSONObject data = job.getJSONObject("data");
									String result = data.getString("result");
									if (result.equals("1")) {
										Message msg = BindingAccountActivity.instance.handler
												.obtainMessage();
										msg.what = 1;
										msg.sendToTarget();
										dismiss();
									} else {
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
