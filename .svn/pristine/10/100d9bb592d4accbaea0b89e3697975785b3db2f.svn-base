/**
 * 
 * @param
 */
package popup;

import java.io.UnsupportedEncodingException;

import login.RegisterEditInfo;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.FillOrderMainActivity;
import com.youge.jobfinder.activity.FillOrderMaintActivity;
import com.youge.jobfinder.vip.ChangeUserInfoActivity;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;

/**
 * 
 * @param
 */
public class DatePickerPopUpwindow extends PopupWindow {

	private String date, time, dateTime;
	private long compare;
	private String uid, sex, occupation, birthday, city, name, phone;
	private Context context;
	private boolean bool = false;
	private String curDate;

	public DatePickerPopUpwindow(Context context, View parent,
			String curDateTime, final String from, boolean showTime) {
		curDate = curDateTime;
		View view = View.inflate(context, R.layout.popup_datepicker, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView dismiss = (TextView) view.findViewById(R.id.dp_popup_dismiss);
		TextView choose = (TextView) view.findViewById(R.id.dp_popup_login);
		DatePicker dp = (DatePicker) view.findViewById(R.id.dp_popup_dp);
		TimePicker tp = (TimePicker) view.findViewById(R.id.dp_popup_tp);
		tp.setIs24HourView(true);
		if (showTime) {
			tp.setVisibility(View.VISIBLE);
		} else {
			tp.setVisibility(View.GONE);
		}

		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (from.equals("r")) {
					compare = new Tools().compareDateNoTime(curDate, date);
					System.out.println("compare ---> " + compare);
					Message msg = RegisterEditInfo.instance.handler
							.obtainMessage();
					if (compare < 0) {
						msg.what = 3;
					} else {
						msg.what = 1;
						msg.obj = date;
						dismiss();
					}
					msg.sendToTarget();
				} else if (from.equals("resume")) {

					compare = new Tools().compareDateNoTime(date, curDate);
					System.out.println("compare ---> " + compare);
					Message msg = ChangeUserResumeActivity.instance.mHandler
							.obtainMessage();
					if (compare > 0) {
						msg.what = 2;
					} else {
						msg.what = 1;
						msg.obj = date;
						dismiss();
					}
					msg.sendToTarget();
				} else if (from.equals("fs")) {
					compare = new Tools()
							.compareDateWithTime(curDate, dateTime);
					System.out.println("compare ---> " + compare);
					Message msg = FillOrderMaintActivity.instance.dHandler
							.obtainMessage();
					if (compare > 0) {
						msg.what = 2;
					} else {
						msg.what = 1;
						msg.obj = dateTime;
						dismiss();
					}
					msg.sendToTarget();
				} else if (from.equals("fe")) {
					compare = new Tools()
							.compareDateWithTime(curDate, dateTime);
					System.out.println("compare ---> " + compare);
					Message msg = FillOrderMainActivity.instance.dHandler
							.obtainMessage();
					if (compare > 0) {
						msg.what = 4;
					} else {
						msg.what = 3;
						msg.obj = dateTime;
						dismiss();
					}
					msg.sendToTarget();
				}
			}
		});

		String[] dt = curDate.split("\\s");
		System.out.println("dt 0 " + dt[0]);
		System.out.println("dt 1 " + dt[1]);
		String[] dt_date = dt[0].split("-");
		String[] dt_time = dt[1].split(":");

		if (!showTime) {
			curDate = dt[0];
		}

		for (int i = 0, j = dt_date.length; i < j; i++) {
			System.out.println("date " + i + " ---> " + dt_date[i]);
		}
		for (int i = 0, j = dt_time.length; i < j; i++) {
			System.out.println("time " + i + " ---> " + dt_time[i]);
		}
		int year = Integer.valueOf(dt_date[0]);
		int monthOfYear = Integer.valueOf(dt_date[1]) - 1;
		int dayOfMonth = Integer.valueOf(dt_date[2]);
		int hr = Integer.valueOf(dt_time[0]);
		int min = Integer.valueOf(dt_time[1]);
		date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		time = hr + ":" + min + ":00";
		dateTime = date + " " + time;
		dp.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
				dateTime = date + " " + time;
			}
		});

		tp.setCurrentHour(hr);
		tp.setCurrentMinute(min);
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				time = hourOfDay + ":" + minute + ":00";
				dateTime = date + " " + time;
			}
		});

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.TOP, 0, 0);
		update();
	}

	public DatePickerPopUpwindow(final Context context, View parent,
			String uid, String sex, String occupation, String birthday,
			String city, String name, String phone) {
		View view = View.inflate(context, R.layout.popup_datepicker, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView dismiss = (TextView) view.findViewById(R.id.dp_popup_dismiss);
		TextView choose = (TextView) view.findViewById(R.id.dp_popup_login);
		DatePicker dp = (DatePicker) view.findViewById(R.id.dp_popup_dp);
		TimePicker tp = (TimePicker) view.findViewById(R.id.dp_popup_tp);
		tp.setIs24HourView(true);
		tp.setVisibility(View.GONE);

		this.context = context;
		this.uid = uid;
		this.sex = sex;
		this.occupation = occupation;
		this.birthday = birthday;
		this.city = city;
		this.name = name;
		this.phone = phone;
		date = birthday;
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		String[] d = birthday.split("-");
		int year = Integer.valueOf(d[0]);
		int monthOfYear = Integer.valueOf(d[1]) - 1;
		int dayOfMonth = Integer.valueOf(d[2]);

		dp.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

			}
		});

		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				compare = new Tools().compareDateNoTime(
						new Tools().TodayNoTime(), date);
				System.out.println("compare ---> " + compare);
				Message msg = ChangeUserInfoActivity.instance.handler
						.obtainMessage();
				if (compare < 0) {
					msg.what = 1030;
					msg.sendToTarget();
				} else {
					updateHttpClient(date);
					msg.what = 103;
					msg.obj = date;
					msg.sendToTarget();
					dismiss();
				}
			}
		});

	}

	private boolean updateHttpClient(String date) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(context);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("name", name);
			job.put("sex", sex);
			job.put("occupation", occupation);
			job.put("birthday", date);
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
									System.out.println("result ---> " + result);
									if (result.equals("1")) {
										System.out.println("result = 1");
										DatePickerPopUpwindow.this.bool = true;
									} else {
										DatePickerPopUpwindow.this.bool = false;
										Toast.makeText(context, message,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(context, message,
											Toast.LENGTH_SHORT).show();
									DatePickerPopUpwindow.this.bool = false;
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
							DatePickerPopUpwindow.this.bool = false;
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

		System.out.println("bool ---> " + DatePickerPopUpwindow.this.bool);
		return DatePickerPopUpwindow.this.bool;
	}
}
