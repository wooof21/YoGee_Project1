/**
 * 
 *@param
 */
package fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import view.RoundImageView;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BindingAccountActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.ServiceCentreActivity;
import com.youge.jobfinder.activity.MainMybalacneActivity;
import com.youge.jobfinder.vip.ChangeUserInfoActivity;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;
import com.youge.jobfinder.vip.ExamineUserResumeActivity;
import com.youge.jobfinder.vip.MoreSettingMainActivity;
import com.youge.jobfinder.vip.MyMessageMainActivity;
import com.youge.jobfinder.vip.RealNameIdentify;
import com.youge.jobfinder.vip.VIPUpdateActivity;

/**
 * 
 * @param
 */
public class VipCenterMainFragment extends Fragment implements OnClickListener {

	private TextView sign, name, job, credit, balance;
	private RoundImageView head;
	private FrameLayout myBalance, realName, myResume, bindAccount, msgNotify,
			shareToFriend, serviceCenter, moreSetting, credit_fl;//
	private String identity;

	/**
	 * 
	 * @param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.vip_center_main, null);
		initView(view);

		return view;
	}

	/**
	 * 包含Activity、Fragment或View的应用 1. MobclickAgent.onResume()
	 * 和MobclickAgent.onPause() 方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能) 2.
	 * MobclickAgent.onPageStart() 和 MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
	 * 
	 * 在仅有Activity的程序中，SDK 自动帮助开发者调用了 2. 中的方法，并把Activity
	 * 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
	 * 首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)
	 * 禁止默认的页面统计方式，这样将不会再自动统计Activity。
	 * 
	 * @param
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("VipCenterMainFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		userInfoHttpClient(new Tools().getUserId(getActivity()));
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("VipCenterMainFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		super.onPause();
	}

	private void initView(View root) {//
		// // 透明状态栏
		// getActivity().getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getActivity().getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		sign = (TextView) root.findViewById(R.id.title_tv);
		name = (TextView) root.findViewById(R.id.vip_main_name);
		job = (TextView) root.findViewById(R.id.vip_main_job);
		credit = (TextView) root.findViewById(R.id.vip_main_credit);
		balance = (TextView) root.findViewById(R.id.vip_main_balance);
		head = (RoundImageView) root.findViewById(R.id.vip_main_head);
		myBalance = (FrameLayout) root.findViewById(R.id.vip_main_mybalacne);
		realName = (FrameLayout) root.findViewById(R.id.vip_main_realname);
		myResume = (FrameLayout) root.findViewById(R.id.vip_main_myresume);
		credit_fl = (FrameLayout) root.findViewById(R.id.vip_main_credit_fl);
		bindAccount = (FrameLayout) root
				.findViewById(R.id.vip_main_bindaccount);
		msgNotify = (FrameLayout) root
				.findViewById(R.id.vip_main_msgnotification);
		shareToFriend = (FrameLayout) root
				.findViewById(R.id.vip_main_sharetofriend);
		serviceCenter = (FrameLayout) root
				.findViewById(R.id.vip_main_servicecenter);
		moreSetting = (FrameLayout) root
				.findViewById(R.id.vip_main_moresetting);

		sign.setOnClickListener(this);
		head.setOnClickListener(this);
		myBalance.setOnClickListener(this);
		realName.setOnClickListener(this);
		myResume.setOnClickListener(this);
		bindAccount.setOnClickListener(this);
		msgNotify.setOnClickListener(this);
		shareToFriend.setOnClickListener(this);
		serviceCenter.setOnClickListener(this);
		moreSetting.setOnClickListener(this);
		credit_fl.setOnClickListener(this);

		userInfoHttpClient(new Tools().getUserId(getActivity()));

		getImageFromAssetsFile(getActivity(), "logo.png");
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_tv:
			startActivity(new Intent(getActivity(), VIPUpdateActivity.class));
			break;
		case R.id.vip_main_head: // 进入修改资料
			startActivity(new Intent(getActivity(),
					ChangeUserInfoActivity.class));
			break;
		case R.id.vip_main_moresetting: // 更多设置
			startActivity(new Intent(getActivity(),
					MoreSettingMainActivity.class));
			break;
		case R.id.vip_main_sharetofriend: // 分享
			share();
			break;
		case R.id.vip_main_myresume:// 进入简历
			UserResume(new Tools().getUserId(getActivity()));
			break;
		case R.id.vip_main_servicecenter:// 进去服务中心
			startActivity(new Intent(getActivity(), ServiceCentreActivity.class));
			break;
		case R.id.vip_main_msgnotification: // 消息中心
			startActivity(new Intent(getActivity(), MyMessageMainActivity.class));
			break;
		case R.id.vip_main_mybalacne: // 我的余额
			startActivity(new Intent(getActivity(), MainMybalacneActivity.class));
			break;
		case R.id.vip_main_realname: // 个人认证
			startActivity(new Intent(getActivity(), RealNameIdentify.class));
			break;
		case R.id.vip_main_bindaccount: // 绑定账户
			startActivity(new Intent(getActivity(),
					BindingAccountActivity.class));
			break;
		case R.id.vip_main_credit_fl: // 芝麻信用
			startActivity(new Intent(getActivity(), VIPUpdateActivity.class));
			break;
		default:

			break;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 信息展示
				HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
				ImageLoader.getInstance()
						.displayImage(hashMap.get("img"), head);
				name.setText(hashMap.get("username"));
				if (hashMap.get("occupation").equals("")
						|| hashMap.get("occupation").equals(" ")) {
					job.setVisibility(View.GONE);
				} else {
					job.setText(hashMap.get("occupation"));
				}
				if (hashMap.get("money") == null) {
					balance.setText("0元");
				} else {
					balance.setText(hashMap.get("money") + "元");
				}
				if ("0".equals(hashMap.get("point"))) {
					credit.setText("未绑定");
				} else {
					credit.setText(hashMap.get("point") + "分");
				}
				break;
			case 2: // 进入简历主页判断
				if (!"0".equals(identity)) {
					String resumeResult = (String) msg.obj;
					if ("1".equals(resumeResult)) { // 1编辑简历
						Intent intent = new Intent(getActivity(),
								ExamineUserResumeActivity.class);
						intent.putExtra("isFromVipCenter", true);
						startActivity(intent);
					} else if ("0".equals(resumeResult)) { // 0没有简历
						Intent intent = new Intent(getActivity(),
								ChangeUserResumeActivity.class);
						intent.putExtra("isFromExamine", "0");
						startActivity(intent);
					}
				} else {
					Toast.makeText(getActivity(), "您还没有实名认证哦，快去认证吧",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}

	};

	private void userInfoHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(getActivity(), Config.GET_USER_INFO_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("个人信息接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONObject user = data
												.getJSONObject("user");
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("birthday",
												user.getString("birthday"));
										hashMap.put("money",
												user.getString("money"));
										hashMap.put("phone",
												user.getString("phone"));
										hashMap.put("sex",
												user.getString("sex"));
										hashMap.put("userid",
												user.getString("userid"));
										hashMap.put("username",
												user.getString("username"));
										hashMap.put("img",
												user.getString("img"));
										hashMap.put("city",
												user.getString("city"));
										hashMap.put("point",
												user.getString("point"));
										hashMap.put("occupation",
												user.getString("occupation"));
										identity = user.getString("identity");

										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();

									} else {
										Toast.makeText(getActivity(), message,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
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

	private void share() {
		statistics();
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("找事吧");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://123.57.15.173:8089/static/download/dowzs.html");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("有事就找它,没事找事吧");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(getActivity().getFilesDir().getPath()
				+ "/data/logo.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://123.57.15.173:8089/static/download/dowzs.html");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://123.57.15.173:8089/static/download/dowzs.html");

		// 启动分享GUI
		oks.show(getActivity());
	}

	/**
	 * 统计点击分享总数
	 * 
	 * @param
	 */
	private void statistics() {
		MobclickAgent.onEvent(getActivity(), "share_total");
	}

	private Bitmap getImageFromAssetsFile(Context context, String fileName) {
		// 获取应用的包名
		String packageName = context.getPackageName();
		// 定义存放这些图片的内存路径
		String path = getActivity().getFilesDir().getPath() + "/data/";
		// 如果这个路径不存在则新建
		File file = new File(path);
		Bitmap image = null;
		boolean isExist = file.exists();
		if (!isExist) {
			file.mkdirs();
		}
		// 获取assets下的资源
		AssetManager am = context.getAssets();
		try {
			// 图片放在img文件夹下
			InputStream is = am.open("img/" + fileName);
			image = BitmapFactory.decodeStream(is);
			FileOutputStream out = new FileOutputStream(path + fileName);
			// 这个方法非常赞
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 获取个人简历
	 */
	private void UserResume(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(getActivity(), Config.FIND_USER_RESUME_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("查看简历接口返回 ---> " + str);
								String message = "";
								try {
									Message msg = handler.obtainMessage();
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										Log.v("个人简历", data.toString());
										String resumeResult = data
												.getString("result");

										msg.what = 2;
										msg.obj = resumeResult;
										msg.sendToTarget();

									} else {
										Toast.makeText(getActivity(), message,
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			pd.dismiss();
		}
	}

}
