package com.youge.jobfinder.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.DatePickerPopUpwindow;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.LabelSelectActivity;

/**
 * 发布订单, 填写订单主页面
 * 
 * @param
 */
public class FillOrderMainActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;
	private TextView done, online, offline, pay, priceTv, beginDate,
			finishDate, countTitle, countUnit, labelTV, fill_address_tv;
	private EditText titleEt, request, count, phoneEt;
	private LinearLayout container, camera, parent, label, phoneLL;
	private FrameLayout beginTime, finishTime, labelF, fill_address;
	private CheckBox cb1, cb2, cb3;

	private String phone, address, category, method, price, number, startDate,
			endDate, userid, onlinePay, need, timeout, lat = "43.803211",
			lng = "125.271649", labelType;

	private ArrayList<String> mSelectPath, labelList;
	private File pic;
	private List<File> listFile;// 文件集
	private List<String> listMD5;// MD5集
	private static final int REQUEST_IMAGE = 300;

	public static FillOrderMainActivity instance;
	private registerReceiver receiver;
	private InputMethodManager inputManager;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_order_main);
		initView();
	}

	private void initView() {
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		done = (TextView) findViewById(R.id.title_tv);
		online = (TextView) findViewById(R.id.fill_order_online);
		offline = (TextView) findViewById(R.id.fill_order_offline);
		pay = (TextView) findViewById(R.id.fill_order_pay);
		priceTv = (TextView) findViewById(R.id.fill_order_price);
		beginDate = (TextView) findViewById(R.id.fill_order_start_date);
		finishDate = (TextView) findViewById(R.id.fill_order_finish_date);
		countTitle = (TextView) findViewById(R.id.fill_order_count_title);
		countUnit = (TextView) findViewById(R.id.fill_order_count_unit);
		fill_address_tv = (TextView) findViewById(R.id.fill_address_tv);
		titleEt = (EditText) findViewById(R.id.fill_order_title);
		request = (EditText) findViewById(R.id.fill_order_request);
		count = (EditText) findViewById(R.id.fill_order_count);
		container = (LinearLayout) findViewById(R.id.fill_order_container);
		camera = (LinearLayout) findViewById(R.id.fill_order_camera);
		parent = (LinearLayout) findViewById(R.id.fill_order_parent);
		beginTime = (FrameLayout) findViewById(R.id.fill_order_start_time);
		finishTime = (FrameLayout) findViewById(R.id.fill_order_finish_time);
		fill_address = (FrameLayout) findViewById(R.id.fill_address);
		fill_address.setVisibility(View.GONE);
		cb1 = (CheckBox) findViewById(R.id.fill_order_cb1);
		cb2 = (CheckBox) findViewById(R.id.fill_order_cb2);
		cb3 = (CheckBox) findViewById(R.id.fill_order_cb3);
		phoneLL = (LinearLayout) findViewById(R.id.fill_order_phone_ll);
		phoneEt = (EditText) findViewById(R.id.fill_order_phone);
		cb3.setClickable(false);
		label = (LinearLayout) findViewById(R.id.fill_order_label);
		labelF = (FrameLayout) findViewById(R.id.fill_order_labelf);
		labelTV = (TextView) findViewById(R.id.fill_order_choose_label);

		back.setOnClickListener(this);
		done.setOnClickListener(this);
		camera.setOnClickListener(this);
		online.setOnClickListener(this);
		offline.setOnClickListener(this);
		pay.setOnClickListener(this);
		priceTv.setOnClickListener(this);
		beginTime.setOnClickListener(this);
		finishTime.setOnClickListener(this);
		labelF.setOnClickListener(this);
		fill_address.setOnClickListener(this);

		instance = this;
		listFile = new ArrayList<File>();
		listMD5 = new ArrayList<String>();//

		// 初始默认值, method='0'(线上), need=0(否需要简历), timeout=0(否超时赔付),
		// onlinePay="0"(否在线支付)
		method = "0";
		need = "0";
		timeout = "0";
		category = "0";
		onlinePay = "1";
		price = "0";
		number = "0";
		startDate = "0";
		endDate = "0";
		address = "";
		phone = "";
		userid = new Tools().getUserId(this);
		// SharedPreferences sp = this.getSharedPreferences("user",
		// Context.MODE_PRIVATE);
		// address = sp.getString("address", "");
		// phone = sp.getString("phone", "");
		// userid = sp.getString("id", "");
		// System.out.println("地址 ---> " + address);
		// ;
		// System.out.println("电话 ---> " + phone);

		labelList = new ArrayList<String>();
		labelType = "";

		lat = getIntent().getExtras().getString("lat");
		lng = getIntent().getExtras().getString("lon");
		setAddress();
		System.out.println("lat ---> " + lat);
		System.out.println("lng ---> " + lng);

		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					timeout = "1";
				} else {
					timeout = "0";
				}
			}
		});
		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					need = "1";
				} else {
					need = "0";
				}
			}
		});
		cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					onlinePay = "1";
				} else {
					onlinePay = "1";
				}
			}
		});

	}

	/**
	 * 处理日期选择结果handler
	 */
	public Handler dHandler = new Handler() {//

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 开始日期选择正确
				startDate = (String) msg.obj;
				beginDate.setText(startDate);
				endDate = new Tools().getTenMinLater(startDate);
				finishDate.setText(endDate);
				break;
			case 2: // 开始日期选择错误
				Toast.makeText(FillOrderMainActivity.this, "不能选择之前的日期, 请重新选择!",
						Toast.LENGTH_SHORT).show();
				break;
			case 3: // 结束日期选择正确
				endDate = (String) msg.obj;
				finishDate.setText(endDate);
				break;
			case 4: // 结束日期选择错误
				Toast.makeText(FillOrderMainActivity.this,
						"结束日期不能在开始日期之前, 请重新选择!", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		if (inputManager.isAcceptingText()) {
			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_tv:
			if (validate()) {
				System.out.println("listFile size ---> " + listFile.size());
				for (int i = 0, j = listMD5.size(); i < j; i++) {
					System.out.println("listMd5 " + i + " ---> "
							+ listMD5.get(i));
				}
				System.out.println("money ---> " + price);
				postOrderHttpClient(listFile, listMD5);
			}
			break;
		case R.id.fill_order_online:
			setOnOffBg(1);
			break;
		case R.id.fill_order_offline:
			setOnOffBg(2);
			break;
		case R.id.fill_order_pay:
			setOrderTypeBg(1);
			break;
		case R.id.fill_order_price:
			setOrderTypeBg(2);
			break;
		case R.id.fill_order_start_time:
			new DatePickerPopUpwindow(this, parent, new Tools().Today(), "fs",
					true);
			break;
		case R.id.fill_order_finish_time:
			if (startDate.equals("0")) {
				Toast.makeText(FillOrderMainActivity.this, "请先选择开始日期!",
						Toast.LENGTH_SHORT).show();
			} else {
				new DatePickerPopUpwindow(this, parent, endDate, "fe", true);
			}
			break;
		case R.id.fill_order_camera:
			selectPic();
			break;
		case R.id.fill_order_labelf:
			Intent intent = new Intent(FillOrderMainActivity.this,
					LabelSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("labelData", labelList);
			intent.putExtras(bundle);
			intent.putExtra("isFromFillOrderBoolean", true);
			startActivityForResult(intent, 100);
			break;
		case R.id.fill_address:
			Intent intents = new Intent(FillOrderMainActivity.this,
					AddressMainActivity.class);
			startActivityForResult(intents, 101);
			break;
		default:
			break;
		}
	}

	/**
	 * 统计发单点击次数, 线上线下单, 报价单抢单, 人数或价钱, 需要简历, 超时赔付
	 * 
	 * @param
	 */
	private void statistics(boolean success, String type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("onoff", method.equals("0") ? "线上" : "线下");
		map.put("bidGrab", category.equals("0") ? "抢单" : "报价单");
		map.put("count", price.equals("0") ? number : price);
		map.put("resume", need.equals("0") ? "不要" : "需要");
		map.put("timeOut", timeout.equals("0") ? "不要" : "需要");
		map.put("success", success ? "成功" : "失败");
		map.put("success", type);
		MobclickAgent.onEvent(this, "post_order", map);
	}

	private void postOrderHttpClient(List<File> listFile, List<String> listMD5) {
		RequestParams params = new RequestParams();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(FillOrderMainActivity.this);
		System.out.println("label tijiao ---> "
				+ labelType.substring(0, labelType.length() - 1));
		try {
			JSONObject job = new JSONObject();
			job.put("title", titleEt.getText().toString());
			job.put("details", request.getText().toString());
			job.put("phone", phone);
			job.put("address", address);
			job.put("category", category);
			job.put("method", method);
			job.put("price", price);
			job.put("number", number);
			job.put("startDate", startDate);
			job.put("endDate", endDate);
			job.put("onlinePay", onlinePay);
			job.put("need", need);
			job.put("timeout", timeout);
			job.put("lat", lat);
			job.put("lng", lng);
			job.put("userid", userid);
			job.put("type", labelType.substring(0, labelType.length() - 1));

			params.put("data", job.toString());

			if (listMD5.size() != 0) {
				for (int i = 0; i < listFile.size(); i++) {
					params.put(listMD5.get(i), listFile.get(i));
				}
			}

			HttpClient.post(Config.POST_ORDER_URL, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200) { // 成功
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									pd.dismiss();
									if (state.equals("success")) {
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										if (result.equals("1")) {
											Toast.makeText(
													FillOrderMainActivity.this,
													"提交成功!", Toast.LENGTH_SHORT)
													.show();
											Intent intent = new Intent(
													FillOrderMainActivity.this,
													OrderListDetailActivity.class);
											intent.putExtra("where",
													"FillOrder");//
											intent.putExtra("category",
													category);
											intent.putExtra("oid",
													data.getString("id"));
											startActivity(intent);
											FillOrderMainActivity.this.finish();
											statistics(true, "成功");
										} else {
											Toast.makeText(
													FillOrderMainActivity.this,
													msg, Toast.LENGTH_SHORT)
													.show();
											statistics(false, msg);
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								Toast.makeText(FillOrderMainActivity.this, msg,
										Toast.LENGTH_SHORT).show();
								statistics(false, msg);
								pd.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							statistics(false, "网络连接失败，请检测网络！");
							Toast.makeText(FillOrderMainActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 显示图片
				ArrayList<String> list = (ArrayList<String>) msg.obj;
				if (container.getChildCount() + list.size() > 4) {
					for (int i = 0, j = 4 - container.getChildCount(); i < j; i++) {
						showPic(list.get(i));
					}
				} else {
					for (int i = 0, j = list.size(); i < j; i++) {
						showPic(list.get(i));
					}
				}
				if (container.getChildCount() > 3) {
					camera.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		}

	};

	private void showPic(String path) {
		// 1.获取原始图片的长和宽,
		// 以下代码是对图片进行解码，inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小，这正好可以满足我们第一步的需要。
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int height = options.outHeight;
		int width = options.outWidth;

		// 2.计算压缩比例, inSampleSize就是缩放值。
		// inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2
		int reqHeight = 100;
		int reqWidth = 100;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			options.inSampleSize = heightRatio < widthRatio ? heightRatio
					: widthRatio;
		}

		// 3.缩放并压缩图片
		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(path, options);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		/**
		 * 前3行的代码其实已经得到了一个缩放的bitmap对象，如果你在应用中显示图片，就可以使用这个bitmap对象了。由于考虑到网络流量的问题。
		 * 我们好需要牺牲图片的质量来换取一部分空间
		 * ，这里调用bm.compress()方法进行压缩，这个方法的第二个参数，如果是100，表示不压缩，我这里设置的是60
		 * ，你也可以更加你的需要进行设置，在实验的过程中我设置为30，图片都不会失真。
		 */

		android.view.ViewGroup.LayoutParams cameraLp = camera.getLayoutParams();

		ImageView iv = new ImageView(this);
		LayoutParams lp = new LayoutParams(cameraLp.width, cameraLp.height);
		lp.setMargins(10, 0, 0, 0);
		iv.setLayoutParams(lp);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setPadding(5, 5, 5, 5);

		iv.setImageBitmap(bm);
		// if(container.getChildCount() < 3){
		container.addView(iv);
		// }else{
		// }

	}

	private void selectPic() {
		int selectedMode = MultiImageSelectorActivity.MODE_MULTI; // 照片单选,多选
		boolean showCamera = true; // 开启照相机
		int maxNum = 3; // 最多可选几张照片

		Intent intent = new Intent(FillOrderMainActivity.this,
				MultiImageSelectorActivity.class);
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
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				mSelectPath = new ArrayList<String>();
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
				msg.what = 1;
				msg.obj = mSelectPath;
				msg.sendToTarget();
			}
		} else if (requestCode == 100) { // 选择标签回调
			if (resultCode == 5) {
				labelType = "";
				labelList = (ArrayList<String>) data.getExtras()
						.getSerializable("labelData");
				label.removeAllViews();
				for (int i = 0, j = labelList.size(); i < j; i++) {
					ImageView iv = new ImageView(FillOrderMainActivity.this);
					LayoutParams lp = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					iv.setLayoutParams(lp);
					if (labelList.get(i).equals("1")) {
						iv.setImageResource(R.drawable.zhu);
					} else if (labelList.get(i).equals("2")) {
						iv.setImageResource(R.drawable.xiu);
					} else if (labelList.get(i).equals("3")) {
						iv.setImageResource(R.drawable.zhi);
					} else if (labelList.get(i).equals("4")) {
						iv.setImageResource(R.drawable.che);
					} else if (labelList.get(i).equals("5")) {
						iv.setImageResource(R.drawable.song);
					} else if (labelList.get(i).equals("6")) {
						iv.setImageResource(R.drawable.jia);
					}

					label.addView(iv);

					labelType += labelList.get(i) + ",";
				}
				System.out.println("labelType ---> " + labelType);
			}
		} else if (requestCode == 101) {
			if (resultCode == RESULT_OK) {
				setAddress();
			}
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

					if (!listMD5.contains(MD5Util.getMD5String(b))) {
						listFile.add(pic);
						// listMD5.add(MD5Util.getMD5String(PictureUtil.bitmapToString(f.getPath())));
						listMD5.add(MD5Util.getMD5String(b));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
			}
		}

		// getByAsyncHttpClientPost(listFile, listMD5);

	}

	private void setOnOffBg(int type) {
		if (type == 1) { // 线上
			online.setBackgroundResource(R.drawable.square_green_one);
			online.setTextColor(Color.rgb(255, 255, 255));
			offline.setBackgroundResource(R.drawable.square_green_stroke_two);
			offline.setTextColor(Color.rgb(34, 181, 112));
			method = "0";
			fill_address.setVisibility(View.GONE);
			phoneLL.setVisibility(View.VISIBLE);
		} else {
			offline.setBackgroundResource(R.drawable.square_green_stroke_one);
			offline.setTextColor(Color.rgb(255, 255, 255));
			online.setBackgroundResource(R.drawable.square_green_two);
			online.setTextColor(Color.rgb(34, 181, 112));
			method = "1";
			fill_address.setVisibility(View.VISIBLE);
			phoneLL.setVisibility(View.GONE);
		}
	}

	private void setOrderTypeBg(int type) {
		if (type == 1) { // 出价
			pay.setBackgroundResource(R.drawable.square_green_one);
			pay.setTextColor(Color.rgb(255, 255, 255));
			priceTv.setBackgroundResource(R.drawable.square_green_stroke_two);
			priceTv.setTextColor(Color.rgb(34, 181, 112));
			countTitle.setText("请输入您的出价金额");
			countUnit.setText("元");
			category = "0";
		} else {
			priceTv.setBackgroundResource(R.drawable.square_green_stroke_one);
			priceTv.setTextColor(Color.rgb(255, 255, 255));
			pay.setBackgroundResource(R.drawable.square_green_two);
			pay.setTextColor(Color.rgb(34, 181, 112));
			countTitle.setText("请选择订单需要人数");
			countUnit.setText("人");
			category = "1";
		}
	}

	private boolean validate() {
		if (labelType.equals("")) {
			Toast.makeText(this, "请至少选择一个标签!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (titleEt.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写标题!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (titleEt.getText().toString().length() < 3) {
			Toast.makeText(this, "标题请至少填写3个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (request.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写详细要求!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (method.equals("1") && address.equals("")) {
			Toast.makeText(this, "请选择地址!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (method.equals("0")) {
			if (phoneEt.getText().toString().length() == 0
					|| phoneEt.getText().toString().length() != 11) {
				Toast.makeText(this, "请提供正确的联系电话!", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				phone = phoneEt.getText().toString();
			}
		}
		if (request.getText().toString().length() < 5) {
			Toast.makeText(this, "详细要求请至少填写5个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (category.equals("0")) { // 出价状态, 价钱不可以为0, number为0
			if (count.getText().toString().length() == 0
					|| Integer.valueOf(count.getText().toString()) == 0) {
				Toast.makeText(this, "请填写价钱!", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				price = count.getText().toString();
			}
		}
		if (category.equals("1")) { // 报价状态, 价钱可以为0, number不可为0
			if (count.getText().toString().length() == 0
					|| Integer.valueOf(count.getText().toString()) == 0) {
				Toast.makeText(this, "请填写人数!", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				number = count.getText().toString();
			}
		}
		if (startDate.equals("0")) {
			Toast.makeText(this, "请选择开始时间!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (endDate.equals("0")) {
			Toast.makeText(this, "请选择结束时间!", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
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
				pp = new PushPopUpWindow(FillOrderMainActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"FillOrderMainActivity");
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

	private void setAddress() {
		SharedPreferences sp = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String addressMainAddress = sp.getString("addressMainAddress", "");
		String addressMainPhone = sp.getString("addressMainPhone", "");
		String addressMainName = sp.getString("addressMainName", "");
		String addressMainSex = "";
		if (!"".equals(sp.getString("addressMainSex", ""))) {
			if ("1".equals(sp.getString("addressMainSex", ""))) {
				addressMainSex = "先生";
			} else {
				addressMainSex = "女士";
			}
		}
		if (!"".equals(addressMainAddress) && !"".equals(addressMainPhone)
				&& !"".equals(addressMainName)) {
			fill_address_tv.setText(addressMainName + "  " + addressMainSex
					+ "   " + addressMainPhone + "\n" + addressMainAddress);
			address = addressMainAddress;
			phone = addressMainPhone;
		} else {
			fill_address_tv.setText("");
			fill_address_tv.setHint("添加常用地址");
			address = "";
			phone = "";
		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(FillOrderMainActivity.this);
		String notificationText = new Tools()
				.getNotificationText(FillOrderMainActivity.this);
		Boolean isFirstStart = new Tools()
				.getisFirstStart(FillOrderMainActivity.this);
		unBindAlias(new Tools().getUserId(FillOrderMainActivity.this),
				new Tools().getCID(FillOrderMainActivity.this),
				FillOrderMainActivity.this);
		SharedPreferences sharedPre = FillOrderMainActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(FillOrderMainActivity.this, parent,
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

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}

}
