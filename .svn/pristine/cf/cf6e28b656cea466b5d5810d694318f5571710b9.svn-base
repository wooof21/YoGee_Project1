/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import model.Address;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.DatePickerPopUpwindow;
import popup.NamePopUpWindow;
import popup.OccupationPopUpWindow;
import popup.PhonePopUpWindow;
import popup.PushPopUpWindow;
import popup.SexPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import view.RoundImageView;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.AddressMainActivity;

/**
 * 
 * @param
 */
public class ChangeUserInfoActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back, phone_right;
	private RoundImageView head;
	private LinearLayout change;
	private View parent;

	private FrameLayout name, sex, birthday, job, city, address,
			vip_change_phone;
	private TextView nameTv, sexTv, birthdayTv, jobTv, cityTv, addrTv,
			vip_change_phonetv;

	private ArrayList<String> mSelectPath;
	private File pic;
	private List<File> listFile;// 文件集
	private List<String> listMD5;// MD5集
	private static final int REQUEST_IMAGE = 200;

	public static ChangeUserInfoActivity instance;

	private String sName, sSex, sBirthday, sOccupation, sCity, sAddress,
			sPhone;

	private ArrayList<Address> addrList;
	private registerReceiver receiver;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_change_info);
		initView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver();
		userInfoHttpClient(new Tools().getUserId(this));
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void initView() {
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		head = (RoundImageView) findViewById(R.id.vip_change_head);
		change = (LinearLayout) findViewById(R.id.vip_change_change);
		parent = findViewById(R.id.vip_change_parent);

		name = (FrameLayout) findViewById(R.id.vip_change_name);
		sex = (FrameLayout) findViewById(R.id.vip_change_sex);
		birthday = (FrameLayout) findViewById(R.id.vip_change_birthday);
		job = (FrameLayout) findViewById(R.id.vip_change_job);
		city = (FrameLayout) findViewById(R.id.vip_change_city);
		address = (FrameLayout) findViewById(R.id.vip_change_address);
		vip_change_phone = (FrameLayout) findViewById(R.id.vip_change_phone);
		nameTv = (TextView) findViewById(R.id.vip_change_nametv);
		sexTv = (TextView) findViewById(R.id.vip_change_sextv);
		birthdayTv = (TextView) findViewById(R.id.vip_change_birthdaytv);
		jobTv = (TextView) findViewById(R.id.vip_change_jobtv);
		cityTv = (TextView) findViewById(R.id.vip_change_citytv);
		addrTv = (TextView) findViewById(R.id.vip_change_addresstv);
		vip_change_phonetv = (TextView) findViewById(R.id.vip_change_phonetv);
		phone_right = (ImageView) findViewById(R.id.phone_right);

		back.setOnClickListener(this);
		change.setOnClickListener(this);

		name.setOnClickListener(this);
		sex.setOnClickListener(this);
		birthday.setOnClickListener(this);
		job.setOnClickListener(this);
		city.setOnClickListener(this);
		address.setOnClickListener(this);
		vip_change_phone.setOnClickListener(this);

		listFile = new ArrayList<File>();
		listMD5 = new ArrayList<String>();//

		instance = this;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.vip_change_change:
			selectPic();
			break;
		case R.id.vip_change_name:
			new NamePopUpWindow(ChangeUserInfoActivity.this, parent,
					new Tools().getUserId(ChangeUserInfoActivity.this), sSex,
					sOccupation, sBirthday, sCity, sName, sPhone);
			break;
		case R.id.vip_change_sex:
			new SexPopUpWindow(ChangeUserInfoActivity.this, parent,
					new Tools().getUserId(ChangeUserInfoActivity.this), sSex,
					sOccupation, sBirthday, sCity, sName, sPhone);
			break;
		case R.id.vip_change_birthday:
			new DatePickerPopUpwindow(ChangeUserInfoActivity.this, parent,
					new Tools().getUserId(ChangeUserInfoActivity.this), sSex,
					sOccupation, sBirthday, sCity, sName, sPhone);
			break;
		case R.id.vip_change_job:
			// new PostOrderPopUpWindow(ChangeUserInfoActivity.this, parent,
			// new Tools().getUserId(ChangeUserInfoActivity.this));
			new OccupationPopUpWindow(ChangeUserInfoActivity.this, parent,
					new Tools().getUserId(ChangeUserInfoActivity.this), sSex,
					sOccupation, sBirthday, sCity, sName, sPhone);
			break;
		case R.id.vip_change_city:
			Intent intent1 = new Intent(ChangeUserInfoActivity.this,
					CitySelectActivity.class);
			intent1.putExtra("sex", sSex);
			intent1.putExtra("occupation", sOccupation);
			intent1.putExtra("birthday", sBirthday);
			intent1.putExtra("city", sCity);
			intent1.putExtra("name", sName);
			intent1.putExtra("phone", sPhone);

			startActivityForResult(intent1, 1000);
			break;
		case R.id.vip_change_address:
			Intent intent = new Intent(ChangeUserInfoActivity.this,
					AddressMainActivity.class);
			// intent.putExtra("addrList", addrList);
			startActivityForResult(intent, 101);
			break;
		case R.id.vip_change_phone:
			new PhonePopUpWindow(ChangeUserInfoActivity.this, parent,
					new Tools().getUserId(ChangeUserInfoActivity.this), sSex,
					sOccupation, sBirthday, sCity, sName, sPhone);
			break;
		default:
			break;
		}
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 信息填写
				HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
				if (!hashMap.get("username").equals("")
						&& !hashMap.get("username").equals(" ")) {
					nameTv.setText(hashMap.get("username"));
				}
				if (!hashMap.get("img").equals("")
						&& !hashMap.get("img").equals(" ")) {
					ImageLoader.getInstance().displayImage(hashMap.get("img"),
							head);
				}
				if (!hashMap.get("occupation").equals("")
						&& !hashMap.get("occupation").equals(" ")) {
					jobTv.setText(hashMap.get("occupation"));
				}
				if (hashMap.get("sex").equals("1")) {
					sexTv.setText("男");
				} else if (hashMap.get("sex").equals("2")) {
					sexTv.setText("女");
				} else {
					sexTv.setText("请选择");
				}
				if (!hashMap.get("birthday").equals("")
						&& !hashMap.get("birthday").equals(" ")) {
					birthdayTv.setText(hashMap.get("birthday"));
				}
				if (!hashMap.get("city").equals("")
						&& !hashMap.get("city").equals(" ")) {
					cityTv.setText(hashMap.get("city"));
				}
				if (!hashMap.get("phone").equals("")
						&& !hashMap.get("phone").equals(" ")) {
					vip_change_phonetv.setText(hashMap.get("phone"));
				}
				break;
			case 2: // 设置头像
				String path = (String) msg.obj;
				setHead(path);
				break;
			case 3: // 设置地址
				sAddress = (String) msg.obj;
				if (!sAddress.equals("")) {
					addrTv.setText(sAddress.split("\\ ")[3]);
					SharedPreferences sharedPre = ChangeUserInfoActivity.this
							.getSharedPreferences("user", Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putString("address", sAddress);
					editor.commit();
				}
				break;
			case 101: // 更改姓名
				sName = (String) msg.obj;
				nameTv.setText(sName);
				break;
			case 102: // 性别
				sSex = (String) msg.obj;
				if (sSex.equals("1")) {
					sexTv.setText("男");
				} else {
					sexTv.setText("女");
				}
				break;
			case 103:
				sBirthday = (String) msg.obj;
				birthdayTv.setText(sBirthday);
				break;
			case 1030:
				Toast.makeText(ChangeUserInfoActivity.this, "您还没出生呢...",
						Toast.LENGTH_SHORT).show();
				break;
			case 104:
				sOccupation = (String) msg.obj;
				jobTv.setText(sOccupation);
				break;
			case 105:
				sPhone = (String) msg.obj;
				vip_change_phonetv.setText(sPhone);
				break;
			default:
				break;
			}
		}

	};

	private void setHead(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// 计算压缩尺寸
		options.inSampleSize = PictureUtil.calculateInSampleSize(options, 480,
				800);
		// 解码
		options.inJustDecodeBounds = false;

		Bitmap b = BitmapFactory.decodeFile(path, options);
		head.setImageBitmap(b);
		head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectPic();
			}
		});

		updateHeadHttpClient(new Tools().getUserId(this), listFile, listMD5);
	}

	private void selectPic() {
		int selectedMode = MultiImageSelectorActivity.MODE_SINGLE; // 照片单选,多选
		boolean showCamera = true; // 开启照相机
		int maxNum = 1; // 最多可选几张照片

		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
				showCamera);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				selectedMode);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_IMAGE:
			if (resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				for (String p : mSelectPath) {
					sb.append(p);
					sb.append("\n");
					System.out.println("pic path ---> " + sb.toString());
					save(mSelectPath);
				}
				Message msg = handler.obtainMessage();
				msg.what = 2;
				msg.obj = mSelectPath.get(0);
				msg.sendToTarget();
			}
			break;
		case 101: // 更新个人信息
			if (resultCode == RESULT_OK) {
				userInfoHttpClient(new Tools().getUserId(this));
			}
			break;
		case 1000: // 选择地址
			if (resultCode == RESULT_OK) {
				sCity = data.getExtras().getString("city");
				cityTv.setText(sCity);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 保存
	 * 
	 * @param mSelectPath
	 */
	private void save(ArrayList<String> mSelectPath) {

		for (int i = 0; i < this.mSelectPath.size(); i++) {

			if (this.mSelectPath.get(i) != null) {

				try {
					File f = new File(this.mSelectPath.get(i));

					Bitmap bm = PictureUtil.getSmallBitmap(this.mSelectPath
							.get(i));

					pic = new File(PictureUtil.getAlbumDir(),
							System.currentTimeMillis() + ".jpg");

					FileOutputStream fos = new FileOutputStream(pic);

					bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
					byte[] b = baos.toByteArray();

					listFile.add(pic);
					// listMD5.add(MD5Util.getMD5String(PictureUtil.bitmapToString(f.getPath())));
					listMD5.add(MD5Util.getMD5String(b));

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
			}
		}

		// getByAsyncHttpClientPost(listFile, listMD5);

	}

	private void updateHeadHttpClient(String uid, List<File> listFile,
			List<String> listMD5) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		RequestParams params = new RequestParams();

		try {
			for (int i = 0; i < listFile.size(); i++) {
				params.put(listMD5.get(i), listFile.get(i));
			}
			params.put("userid", uid);
			HttpClient.post(Config.UPDATE_USER_HEAD_URL, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("上传头像接口返回  ---> " + str);
								String message = "";
								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")) {
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										if (result.equals("1")) {
											Toast.makeText(
													ChangeUserInfoActivity.this,
													"头像更新成功!",
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(
													ChangeUserInfoActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									} else {
										Toast.makeText(
												ChangeUserInfoActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
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
							Toast.makeText(ChangeUserInfoActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	private void userInfoHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.GET_USER_INFO_URL, se,
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

										if (user.getString("phone").equals("")) {
											sPhone = " ";
										} else {
											sPhone = user.getString("phone");
											phone_right
													.setVisibility(View.INVISIBLE);
											vip_change_phone.setEnabled(false);
										}
										if (user.getString("birthday").equals(
												"")) {
											sBirthday = " ";
										} else {
											sBirthday = user
													.getString("birthday");
										}
										if (user.getString("username").equals(
												"")) {
											sName = " ";
										} else {
											sName = user.getString("username");
										}
										if (user.getString("sex").equals("")) {
											sSex = " ";
										} else {
											sSex = user.getString("sex");
										}
										if (user.getString("city").equals("")) {
											sCity = " ";
										} else {
											sCity = user.getString("city");
										}
										if (user.getString("occupation")
												.equals("")) {
											sOccupation = " ";
										} else {
											sOccupation = user
													.getString("occupation");
										}

										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();

										JSONArray aList = data
												.getJSONArray("list");
										addrList = new ArrayList<Address>();
										sAddress = "";
										if (aList.length() == 0) {
											sAddress = "";
											//addrTv.setText(sAddress.split("\\ ")[3]);
											addrTv.setHint("请选择");
										} else {
											for (int i = 0, j = aList.length(); i < j; i++) {
												JSONObject addr = aList
														.optJSONObject(i);
												Address addre = new Address();
												addre.setAddress(addr
														.getString("address"));
												addre.setId(addr
														.getString("id"));
												addre.setName(addr
														.getString("name"));
												addre.setPhone(addr
														.getString("phone"));
												addre.setSex(addr
														.getString("sex"));
												addre.setIsSelected(addr
														.getString("isSelected"));

												if (addr.getString("isSelected")
														.equals("1")) {
													sAddress = addr
															.getString("address");
												}

												addrList.add(addre);
											}
											Message msg1 = handler
													.obtainMessage();
											msg1.what = 3;
											msg1.obj = sAddress;
											msg1.sendToTarget();
										}

									} else {
										Toast.makeText(
												ChangeUserInfoActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
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
							Toast.makeText(ChangeUserInfoActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
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

	/**
	 * 注册广播
	 * 
	 */
	private void registerReceiver() {
		receiver = new registerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.service.fresh");
		registerReceiver(receiver, filter);
	}

	/**
	 * 接收广播，设置下拉刷新是否显示
	 * 
	 */
	private PushPopUpWindow pp;
	private PushPopUpWindow[] pps = new PushPopUpWindow[] { null, null };

	protected class registerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if ("1".equals(new Tools().getNotificationType(context))
					|| "2".equals(new Tools().getNotificationType

					(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(ChangeUserInfoActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"ChangeUserInfoActivity");
				pps[0] = pp;
			} else if ("3".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				mandatoryExit(context);
			} else {
				NotificationManager nManager = (NotificationManager)

				context.getSystemService

				(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
				notification.audioStreamType =

				android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification.setLatestEventInfo(context, "找事吧", new Tools

				().getNotificationText(context), pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(ChangeUserInfoActivity.this);
		String notificationText = new Tools()
				.getNotificationText(ChangeUserInfoActivity.this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(ChangeUserInfoActivity.this);
		unBindAlias(new Tools().getUserId(ChangeUserInfoActivity.this),
				new Tools().getCID(ChangeUserInfoActivity.this),
				ChangeUserInfoActivity.this);
		SharedPreferences sharedPre = ChangeUserInfoActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(ChangeUserInfoActivity.this, parent,
				new Tools().getNotificationText(context));
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "1");// 强制退出是0

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.UN_BIND_ALIAS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[]

						arg1, byte[] arg2) {
							if (arg0 == 200) {
								String str = new String

								(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result =

									new JSONObject(str);
									String state =

									result.getString("state");
									message =

									result.getString("msg");
									if (state.equals

									("success")) {
										JSONObject

										data = result

										.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String

										results = data

										.getString("result");
										if

										("0".equals(results)) {

											Log.v("别人资料", "提交失败");
										} else {

											Log.v("别人资料", "提交成功");
										}
									}
								} catch (JSONException e) {
									e.printStackTrace

									();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[]

						arg1, byte[] arg2, Throwable

						arg3) {
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// protected void onPause() {
	// super.onPause();
	// unregisterReceiver(receiver);
	// }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}
}
