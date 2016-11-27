package tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import popup.SexPopUpWindow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Tools {

	public static String getURL(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("GET");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}

	public void scaleInAnimation(View v) {
		ScaleAnimation sa = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setRepeatCount(0);
		sa.setFillAfter(false);
		sa.setDuration(100);
		v.startAnimation(sa);
	}

	public void upDown(View v) {
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 5);
		ta.setDuration(500);
		ta.setInterpolator(new CycleInterpolator(5));
		v.startAnimation(ta);
	}

	public String doPostData(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);

			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("POST");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}

	public String doPostData(String urlStr, String data) throws IOException {
		String result = "";
		byte[] xmlbyte = data.getBytes("UTF-8");
		Log.e("post�ӿ��ϴ� ��ʽ������---utf8---", data);

		URL url = new URL(urlStr);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setDoOutput(true);// �������
		conn.setDoInput(true);
		conn.setUseCaches(false);// ��ʹ�û���
		conn.setRequestMethod("POST");
		conn.getOutputStream().write(xmlbyte);
		conn.getOutputStream().flush();
		conn.getOutputStream().close();

		Log.e("conn.getResponseCode()----", "" + conn.getResponseCode());

		if (conn.getResponseCode() != 200)
			throw new RuntimeException("����urlʧ��");
		int codeOrder = conn.getResponseCode();

		if (codeOrder == 200) {

			InputStream inStream = conn.getInputStream();// ��ȡ�������

			// ʹ�������������ַ�(��ѡ)
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inStream.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			result = out.toString("UTF-8");
			Log.e("post���ؽ��--------", "" + result);
			out.close();

		} else {

		}

		return result;
	}

	public boolean getInternet(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUserLogin(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Boolean isLogin = false;
		String loginType = sharedPre.getString("loginType", "");
		if ("otherRegister".equals(loginType)) {
			String openid = sharedPre.getString("openid", "");
			Log.e("openid", "A" + openid);
			if ("".equals(openid)) {
				isLogin = false;
			} else {
				isLogin = true;
			}
		} else {
			String username = sharedPre.getString("phone", "");
			String password = sharedPre.getString("password", "");
			Log.e("username", "A" + username);
			Log.e("password", "B" + password);
			if ("".equals(username) || "".equals(password)) {
				isLogin = false;
			} else {
				isLogin = true;
			}
		}
		return isLogin;
	}

	public void setStatusBar(Activity activity) {
		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(activity);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);

		// 设置一个颜色给系统栏
		tintManager.setTintColor(Color.parseColor("#22b570"));
	}

	public String getUserId(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("id", "");
		// System.out.println("id ---> " + id);//
		return id;
	}

	public String getUserimg(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String userimg = sharedPre.getString("userimg", "");
		System.out.println("userimg ---> " + userimg);//
		return userimg;
	}

	public String getPhone(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String phone = sharedPre.getString("phone", "");
		System.out.println("phone ---> " + phone);//
		return phone;
	}

	public String getLastLat(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("lat_last", "");
		System.out.println("lat_last ---> " + id);
		return id;
	}

	public String getLastLon(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("lon_last", "");
		System.out.println("lon_last ---> " + id);
		return id;
	}

	public String getUserName(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("username", "");
		System.out.println("username ---> " + id);
		return id;
	}

	public String getNotificationId(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationId = sharedPre.getString("notificationId", "");
		System.out.println("notificationId ---> " + notificationId);
		return notificationId;
	}

	public String getNotificationType(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationType = sharedPre.getString("notificationType", "");
		System.out.println("notificationType ---> " + notificationType);
		return notificationType;
	}

	public String getNotificationText(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationText = sharedPre.getString("notificationText", "");
		System.out.println("notificationText ---> " + notificationText);
		return notificationText;
	}

	public Boolean getisFirstStart(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Boolean isFirstStart = sharedPre.getBoolean("isFirstStart", false);
		System.out.println("isFirstStart ---> " + isFirstStart);
		return isFirstStart;
	}

	public String getOid(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationText = sharedPre.getString("oid", "");
		System.out.println("oid ---> " + notificationText);
		return notificationText;
	}

	public String getCatefory(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationText = sharedPre.getString("category", "");
		System.out.println("category ---> " + notificationText);
		return notificationText;
	}

	public String getWhere(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationText = sharedPre.getString("where", "");
		System.out.println("where ---> " + notificationText);
		return notificationText;
	}

	public String getOrderState(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String notificationText = sharedPre.getString("orderState", "");
		System.out.println("orderState ---> " + notificationText);
		return notificationText;
	}

	public String getCID(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("cid", "");
		System.out.println("cid ---> " + id);
		return id;
	}
	
	public String getEntityName(Context context) {
		String id = getImei(context);
		System.out.println("entityName ---> " + id);
		return id;
	}
	
	public String getCurrentLat(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("jobfinder",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("currentLat", "");
		System.out.println("currentLat tools ---> " + id);
		return id;
	}
	
	public String getCurrentLng(Context context) {
		SharedPreferences sharedPre = context.getSharedPreferences("jobfinder",
				Context.MODE_PRIVATE);
		String id = sharedPre.getString("currentLng", "");
		System.out.println("currentLng tools ---> " + id);
		return id;
	}
	/**
	 * 获取设备IMEI码
	 * 
	 * @param context
	 * @return
	 */
	protected static String getImei(Context context) {
		String mImei = "";
		try {
			mImei = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			System.out.println("获取IMEI码失败");
			mImei = "";
		}
		return mImei;
	}
	public String getDate() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return String.valueOf(c.get(Calendar.MONTH) + 1) + "."
				+ String.valueOf(c.get(Calendar.DAY_OF_MONTH));
	}

	public Boolean getNowTime() {
		boolean result = false;
		final long aDayInMillis = 1000 * 60 * 60 * 24;
		final long currentTimeMillis = System.currentTimeMillis();

		Time now = new Time();
		now.set(currentTimeMillis);

		Time startTime = new Time();
		startTime.set(currentTimeMillis);
		startTime.hour = 22;
		startTime.minute = 0;

		Time endTime = new Time();
		endTime.set(currentTimeMillis);
		endTime.hour = 8;
		endTime.minute = 0;

		if (!startTime.before(endTime)) {
			// 跨天的特殊情况（比如22:00-8:00）
			startTime.set(startTime.toMillis(true) - aDayInMillis);
			result = !now.before(startTime) && !now.after(endTime); // startTime
																	// <= now <=
																	// endTime
			Time startTimeInThisDay = new Time();
			startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
			if (!now.before(startTimeInThisDay)) {
				result = true;
			}
		} else {
			// 普通情况(比如 8:00 - 22:00)
			result = !now.before(startTime) && !now.after(endTime); // startTime
																	// <= now <=
																	// endTime
		}
		return result;
	}

	public String getWeek() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String day = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(day)) {
			day = "��";
		} else if ("2".equals(day)) {
			day = "һ";
		} else if ("3".equals(day)) {
			day = "��";
		} else if ("4".equals(day)) {
			day = "��";
		} else if ("5".equals(day)) {
			day = "��";
		} else if ("6".equals(day)) {
			day = "��";
		} else if ("7".equals(day)) {
			day = "��";
		}
		return day;
	}

	public String Today() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		String d = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DAY_OF_MONTH) + " "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
		System.out.println("d ---> " + d);
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("today", sdf.format(date));

		return sdf.format(date);
	}

	public String TodayNoTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String d = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1)
				+ "-" + c.get(Calendar.DAY_OF_MONTH);
		System.out.println("d ---> " + d);
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("today", sdf.format(date));

		return sdf.format(date);
	}

	public String nextDay1(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		Date d = java.sql.Date.valueOf(Today());
		Calendar calendar1 = getCalendarDate(d);
		calendar1.setTime(d);
		calendar1.add(calendar1.DATE, 1);
		d = calendar1.getTime();
		sdf.format(d);
		Log.e("nextDay ---", sdf.format(d));
		return sdf.format(d);
	}

	public String nextDay2(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		Date d = java.sql.Date.valueOf(Today());
		Calendar calendar1 = getCalendarDate(d);
		calendar1.setTime(d);
		calendar1.add(calendar1.DATE, 2);
		d = calendar1.getTime();
		sdf.format(d);
		Log.e("nextDay ---", sdf.format(d));
		return sdf.format(d);
	}

	public String nextDay3(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		Date d = java.sql.Date.valueOf(Today());
		Calendar calendar1 = getCalendarDate(d);
		calendar1.setTime(d);
		calendar1.add(calendar1.DATE, 3);
		d = calendar1.getTime();
		sdf.format(d);
		Log.e("nextDay ---", sdf.format(d));
		return sdf.format(d);
	}

	public Calendar getCalendarDate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	public String getTenMinLater(String date) {
		String[] dt = date.split("\\s");
		String[] tt = dt[1].split(":");
		int ten = Integer.valueOf(tt[1]) + 10;
		int hr = Integer.valueOf(tt[0]);
		if (ten >= 60) {
			ten -= 60;
			hr += 1;
		}
		String newDt = dt[0] + " " + hr + ":" + ten + ":" + tt[2];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(newDt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdf.format(d);
	}

	public long compareDateNoTime(String d1, String d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = java.sql.Date.valueOf(d1);
		Date date2 = java.sql.Date.valueOf(d2);

		String sdf_d1 = sdf.format(date1);
		String sdf_d2 = sdf.format(date2);
		System.out.println("sdf_d1 ---> " + sdf_d1);
		System.out.println("sdf_d2 ---> " + sdf_d2);

		String[] split_d1 = sdf_d1.split("-");
		String[] split_d2 = sdf_d2.split("-");

		String str_d1 = split_d1[0] + split_d1[1] + split_d1[2];
		String str_d2 = split_d2[0] + split_d2[1] + split_d2[2];

		return Long.valueOf(str_d1) - Long.valueOf(str_d2);
	}

	public long compareDateWithTime(String dt1, String dt2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null, date2 = null;
		try {
			date1 = sdf.parse(dt1);
			date2 = sdf.parse(dt2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sdf_dt1 = sdf.format(date1);
		String sdf_dt2 = sdf.format(date2);
		System.out.println("sdf_d1 ---> " + sdf_dt1);
		System.out.println("sdf_d2 ---> " + sdf_dt2);

		String[] split_dt1 = sdf_dt1.split(" ");
		String[] split_dt2 = sdf_dt2.split(" ");

		String[] split_d1 = split_dt1[0].split("-");
		String[] split_d2 = split_dt2[0].split("-");
		String[] split_t1 = split_dt1[1].split(":");
		String[] split_t2 = split_dt2[1].split(":");

		String str_dt1 = split_d1[0] + split_d1[1] + split_d1[2] + split_t1[0]
				+ split_t1[1] + split_t1[2];
		String str_dt2 = split_d2[0] + split_d2[1] + split_d2[2] + split_t2[0]
				+ split_t2[1] + split_t2[2];
		System.out.println("dt1 long ---> " + Long.valueOf(str_dt1));
		System.out.println("dt2 long ---> " + Long.valueOf(str_dt2));
		return Long.valueOf(str_dt1) - Long.valueOf(str_dt2);
	}

	public int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	public int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * ��double��ݽ���ȡ����.
	 * <p>
	 * For example: <br>
	 * double value = 100.345678; <br>
	 * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br>
	 * retΪ100.3457 <br>
	 * 
	 * @param value
	 *            double���.
	 * @param scale
	 *            ����λ��(������С��λ��).
	 * @param roundingMode
	 *            ����ȡֵ��ʽ.
	 * @return ���ȼ��������.
	 */
	// public static double round(double value, int scale, int roundingMode) {
	// BigDecimal bd = new BigDecimal(value);
	// bd = bd.setScale(scale, roundingMode);
	// double d = bd.doubleValue();
	// bd = null;
	// return d;
	// }
	//
	// public double getDistance(double lat1, double myLat, double lon1, double
	// myLon){
	// if(myLat == 0 && myLat == 0){
	// return -1;
	// }else{
	// LatLng ll1 = new LatLng(lat1, lon1);
	// LatLng ll2 = new LatLng(myLat, myLon);
	// double distance = DistanceUtil.getDistance(ll1, ll2);
	// return distance;
	// }
	//
	// }

	public boolean isFristRun(Context context) {
		Log.e("����ָ��ҳ�ж�-------", "-------");
		SharedPreferences sharedPre = context.getSharedPreferences("frist",
				Context.MODE_PRIVATE);
		String fristString = sharedPre.getString("frist", "1");
		Log.e("frist====", "" + fristString);
		if ("1".equals(fristString)) {
			return true;
		} else {
			return false;
		}

	}

	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return 距离：单位为米
	 */
	public static double DistanceOfTwoPoints(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	private final static int CHINESE = 1;
	private final static int NUMBER_OR_CHARACTER = 2;

	/**
	 * sp转成px
	 * 
	 * @param spValue
	 * @param type
	 * @return
	 */
	public static float sp2px(Context context, float spValue, int type) {
		final float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		switch (type) {
		case CHINESE:
			return spValue * scaledDensity;
		case NUMBER_OR_CHARACTER:
			return spValue * scaledDensity * 10.0f / 18.0f;
		default:
			return spValue * scaledDensity;
		}
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);

		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);

	}
	
	public String convertSec(int sec){
		String result = "";
		int secs = sec % 60;
		int min = sec / 60;
		int hr = 0;
		if(min < 60){
			hr = 0;
		}else if(min == 60){
			hr = 1;
			min = 0;
		}else{
			hr = min / 60;
			min = min % 60;
		}
		if(hr != 0){
			result = result + hr +"小时";
			if(min != 0){
				result = result + min + "分钟";
			}
			if(secs != 0){
				result = result + secs + "秒";
			}
		}else{
			if(min != 0){
				result = result + min + "分钟";
			}
			if(secs != 0){
				result = result + secs + "秒";
			}
		}
		return result.substring(0, result.length()-1);
	}
}
