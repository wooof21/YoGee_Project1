package com.youge.jobfinder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.vip.RealNameIdentify;

public class RealNameDetailActivity extends BaseActivity implements
		OnClickListener {

	private String realNameId, states;
	private EditText real_name, identity_card_text;
	private TextView count_order_text, title_tv;
	private ImageView identity_card_all, back, identity_card_all2;
	private ArrayList<String> mSelectPath;
	private String isIDCard1 = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
	private HashMap<String, String> realNameMap;
	private LinearLayout parent, title_tv_parent;
	private CustomProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_name_detail);
		findView();
		initView();
		if (!TextUtils.isEmpty(realNameId)) {
			authenticationHttpClient(new Tools().getUserId(this));
		}
	}

	private void findView() {
		real_name = (EditText) findViewById(R.id.real_name);
		identity_card_text = (EditText) findViewById(R.id.identity_card_text);
		count_order_text = (TextView) findViewById(R.id.count_order_text);
		title_tv = (TextView) findViewById(R.id.title_tv);
		identity_card_all = (ImageView) findViewById(R.id.identity_card_all);
		parent = (LinearLayout) findViewById(R.id.parent);
		title_tv_parent = (LinearLayout) findViewById(R.id.title_tv_parent);
		back = (ImageView) findViewById(R.id.back);
		identity_card_all2 = (ImageView) findViewById(R.id.identity_card_all2);
	};

	private void initView() {
		realNameId = getIntent().getStringExtra("id");
		realNameMap = new HashMap<String, String>();
		identity_card_all.setOnClickListener(this);
		identity_card_all2.setOnClickListener(this);
		back.setOnClickListener(this);
		identity_card_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				count_order_text.setText(s.length() + "");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_tv:
			if (validate()) {
				updateAuthenticationHttpClient();
			}
			break;
		case R.id.identity_card_all:
			selectPic(1);
			break;
		case R.id.identity_card_all2:
			selectPic(100);
			break;
		case R.id.back:
			Intent intent = new Intent(RealNameDetailActivity.this,
					RealNameIdentify.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	private boolean validate() {
		if (TextUtils.isEmpty(real_name.getText())) {
			Toast.makeText(RealNameDetailActivity.this, "姓名不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(identity_card_text.getText())) {
			Toast.makeText(RealNameDetailActivity.this, "身份证不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!identity_card_text.getText().toString().matches(isIDCard1)) {
			// || !identity_card_text.getText().toString().matches(isIDCard2)
			Toast.makeText(RealNameDetailActivity.this, "身份证格式不对！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (realNameMap.size() != 2) {
			Toast.makeText(RealNameDetailActivity.this, "请上传照片!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String path = (String) msg.obj;
				int which = msg.arg1;
				System.out.println("which ---> " + which);
				setPic(path, which);
				break;
			case 1000:
				String path1 = (String) msg.obj;
				int which1 = msg.arg1;
				System.out.println("which ---> " + which1);
				setPic(path1, which1);
				break;
			case 100:
				pd.dismiss();
				double sim = (Double) msg.obj;
				System.out.println("相似度:  " + sim);
				if (sim >= 40) { // 自动审核通过
					title_tv.setBackgroundResource(R.drawable.fill_order_orange);
					title_tv.setText("提交认证");
					title_tv.setOnClickListener(RealNameDetailActivity.this);
				} else {
					title_tv.setBackgroundResource(R.drawable.corner_grey);
					title_tv.setText("自动认证失败");
					title_tv.setClickable(false);
					Toast.makeText(RealNameDetailActivity.this,
							"自动审核未通过, 请确保照片符合上传样板规则, 确保照片足够清晰...",
							Toast.LENGTH_LONG).show();
				}
				break;
			case 200:
				pd.dismiss();
				Toast.makeText(RealNameDetailActivity.this,
						"自动审核未通过, 请确保照片中只有本人及本人身份证, 确保照片足够清晰...",
						Toast.LENGTH_LONG).show();
				title_tv.setBackgroundResource(R.drawable.corner_grey);
				title_tv.setClickable(false);
				title_tv.setText("自动认证失败");
				break;
			case 300:
				title_tv.setEnabled(true);
				break;
			case 400:
				title_tv.setEnabled(true);
				break;
			default:
				break;
			}
		}

	};
	private Bitmap idPic;

	private void setPic(String path, int which) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// 计算压缩尺寸
		options.inSampleSize = PictureUtil.calculateInSampleSize(options, 480,
				800);
		// 解码
		options.inJustDecodeBounds = false;

		idPic = BitmapFactory.decodeFile(path, options);
		switch (which) {
		case 1:
			identity_card_all.setImageBitmap(idPic);
			realNameMap.put("1", path);
			checkFace();
			break;
		case 100:
			identity_card_all2.setImageBitmap(idPic);
			realNameMap.put("100", path);
			break;
		default:
			break;
		}

	}

	private void selectPic(int requestCode) {
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
		// if (mSelectPath != null && mSelectPath.size() > 0) {
		// intent.putExtra(
		// MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
		// mSelectPath);
		// }
		startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				mSelectPath = new ArrayList<String>();
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.arg1 = requestCode;
				msg.obj = mSelectPath.get(0);
				msg.sendToTarget();
			}
			break;
		case 100:
			if (resultCode == RESULT_OK) {
				mSelectPath = new ArrayList<String>();
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				Message msg = handler.obtainMessage();
				msg.what = 1000;
				msg.arg1 = requestCode;
				msg.obj = mSelectPath.get(0);
				msg.sendToTarget();
			}
			break;
		default:
			break;
		}

	}

	private void authenticationHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("id", realNameId);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.AUTH_ENTICATION, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
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
										JSONObject examine = data
												.getJSONObject("examine");
										real_name.setText(examine
												.getString("name"));
										identity_card_text.setText(examine
												.getString("nubmer"));
										count_order_text.setText(examine
												.getString("nubmer").length()
												+ "");
										ImageLoader.getInstance().displayImage(
												examine.getString("img"),
												identity_card_all);
										ImageLoader.getInstance().displayImage(
												examine.getString("idimg"),
												identity_card_all2);
										realNameMap.put("2",
												examine.getString("img"));
										realNameMap.put("1",
												examine.getString("idimg"));
										states = examine.getString("state");
										if ("0".equals(states)
												|| "3".equals(states)) {
											identity_card_all
													.setClickable(true);
											identity_card_all2
													.setClickable(true);
											title_tv_parent
													.setVisibility(View.VISIBLE);
										} else if ("1".equals(states)
												|| "2".equals(states)) {
											identity_card_all
													.setClickable(false);
											identity_card_all2
													.setClickable(false);
											identity_card_text
													.setEnabled(false);
											real_name.setEnabled(false);
											title_tv_parent
													.setVisibility(View.GONE);
										}
									} else {
										Toast.makeText(
												RealNameDetailActivity.this,
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
							Toast.makeText(RealNameDetailActivity.this,
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
	 * 提交身份验证
	 * 
	 * @param List
	 *            <File> listMD5
	 * @param List
	 *            <File> listFile
	 */
	private void updateAuthenticationHttpClient() {
		title_tv.setEnabled(false);
		RequestParams params = new RequestParams();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(RealNameDetailActivity.this);
		pd.setMessage("正在提交...");
		pd.setCancelable(false);
		try {
			params = getParamsOther(params);

			JSONObject job = new JSONObject();

			job.put("userid", new Tools().getUserId(getApplicationContext())
					.toString());
			job.put("name", real_name.getText().toString());
			if (TextUtils.isEmpty(realNameId)) {
				job.put("id", "0");
			} else {
				job.put("id", realNameId);
			}
			job.put("number", identity_card_text.getText().toString());

			params.put("data", job.toString());
			HttpClient.post(Config.UPDATE_AUTH_ENTICATION, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200) {
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									pd.dismiss();
									if (state.equals("success")) {
										Toast.makeText(
												RealNameDetailActivity.this,
												"提交成功!", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(
												RealNameDetailActivity.this,
												RealNameIdentify.class);
										startActivity(intent);
										finish();
									} else {
										Message msg300 = handler
												.obtainMessage();
										msg300.what = 300;
										msg300.sendToTarget();
										Toast.makeText(
												RealNameDetailActivity.this,
												msg, Toast.LENGTH_SHORT).show();
										pd.dismiss();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								Message msg400 = handler.obtainMessage();
								msg400.what = 400;
								msg400.sendToTarget();
								Toast.makeText(RealNameDetailActivity.this,
										msg, Toast.LENGTH_SHORT).show();
								pd.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(RealNameDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private RequestParams getParamsOther(RequestParams params) {
		try {
			Bitmap image = null;
			image = ((BitmapDrawable) identity_card_all.getDrawable())
					.getBitmap();

			File pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			image.compress(Bitmap.CompressFormat.JPEG, 80, fos);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			byte[] b = baos.toByteArray();
			String md5 = MD5Util.getMD5String(b);
			params.put(md5 + "2", pic);
			System.out.println("md5 2 ---> " + md5);

			Bitmap image1 = null;
			image1 = ((BitmapDrawable) identity_card_all2.getDrawable())
					.getBitmap();

			File pic1 = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos1 = new FileOutputStream(pic1);

			image1.compress(Bitmap.CompressFormat.JPEG, 100, fos1);

			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			image1.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
			byte[] b1 = baos1.toByteArray();
			String md5_1 = MD5Util.getMD5String(b1);
			params.put(md5_1 + "1", pic1);
			System.out.println("md5 1 ---> " + md5_1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	private void checkFace() {
		pd = CustomProgressDialog.createDialog(this);
		pd.setMessage("正在自动审核中...");
		title_tv.setBackgroundResource(R.drawable.corner_grey);
		title_tv.setText("自动审核中...");
		FaceppDetect faceppDetect = new FaceppDetect();
		faceppDetect.setDetectCallback(new DetectCallback() {

			@Override
			public void detectResult(JSONObject rst) {
				// TODO Auto-generated method stub

			}
		});
		faceppDetect.detect(idPic);
	}

	private class FaceppDetect {
		DetectCallback callback = null;

		public void setDetectCallback(DetectCallback detectCallback) {
			callback = detectCallback;
		}

		public void detect(final Bitmap image) {

			new Thread(new Runnable() {

				public void run() {
					HttpRequests httpRequests = new HttpRequests(
							"17cdb6aadffd3a6451bc605d6ac9e8cf",
							"O-zG5caUzkGLzkAlKv_upKC8iBdB5hK3", true, false);
					// Log.v(TAG, "image size : " + img.getWidth() + " " +
					// img.getHeight());

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					float scale = Math.min(
							1,
							Math.min(600f / idPic.getWidth(),
									600f / idPic.getHeight()));
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);

					Bitmap imgSmall = Bitmap.createBitmap(idPic, 0, 0,
							idPic.getWidth(), idPic.getHeight(), matrix, false);
					// Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " "
					// + imgSmall.getHeight());

					imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] array = stream.toByteArray();

					try {
						// detect
						JSONObject result = httpRequests
								.detectionDetect(new PostParameters()
										.setImg(array));

						System.out.println("result" + result.toString());

						JSONArray face = result.getJSONArray("face");
						Message msg = handler.obtainMessage();
						if (face.length() == 2) { // 只能有俩张脸
							String face_id1 = result.getJSONArray("face")
									.getJSONObject(0).getString("face_id");
							String face_id2 = result.getJSONArray("face")
									.getJSONObject(1).getString("face_id");

							// 对比两张人脸的相似程度
							JSONObject Compare = httpRequests
									.recognitionCompare(new PostParameters()
											.setFaceId1(face_id1).setFaceId2(
													face_id2));
							final double smilar = Double.valueOf(Compare
									.getString("similarity"));
							msg.what = 100;
							msg.obj = smilar;
						} else {
							msg.what = 200;
						}
						msg.sendToTarget();

						// for(int i=0,j=face.length();i<j;i++){
						// JSONObject job = face.optJSONObject(i);
						// }
						// finished , then call the callback function
						if (callback != null) {
							callback.detectResult(result);
						}
					} catch (FaceppParseException e) {
						e.printStackTrace();
						RealNameDetailActivity.this
								.runOnUiThread(new Runnable() {
									public void run() {
										// textView.setText("Network error.");
									}
								});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}
	}

	interface DetectCallback {
		void detectResult(JSONObject rst);
	}
}
