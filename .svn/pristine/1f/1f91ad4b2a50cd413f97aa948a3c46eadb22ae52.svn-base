/**
 * 
 * @param
 */
package login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import popup.DatePickerPopUpwindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import view.RoundImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class RegisterEditInfo extends BaseActivity implements OnClickListener {

	private ImageView back;
	private LinearLayout headLL, parent;
	private RoundImageView head;
	private EditText name, psw, repsw;
	private RadioGroup rg;
	private RadioButton rb1, rb2;
	private RelativeLayout birthday;
	private TextView date, save, commit;

	private String sex, dateSel, phone;
	private ArrayList<String> mSelectPath;
	private File pic;
	private List<File> listFile;// 文件集
	private List<String> listMD5;// MD5集
	private static final int REQUEST_IMAGE = 200;

	public static RegisterEditInfo instance;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_edit_info);
		initView();
		instance = this;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		headLL = (LinearLayout) findViewById(R.id.register_edit_info_headll);
		parent = (LinearLayout) findViewById(R.id.register_edit_info_parent);
		head = (RoundImageView) findViewById(R.id.register_edit_info_headiv);
		name = (EditText) findViewById(R.id.register_edit_info_name);
		psw = (EditText) findViewById(R.id.register_edit_info_psw);
		repsw = (EditText) findViewById(R.id.register_edit_info_repsw);
		rg = (RadioGroup) findViewById(R.id.register_edit_info_radiogroup);
		rb1 = (RadioButton) findViewById(R.id.register_edit_info_rb1);
		rb2 = (RadioButton) findViewById(R.id.register_edit_info_rb2);
		birthday = (RelativeLayout) findViewById(R.id.register_edit_info_birthday);
		date = (TextView) findViewById(R.id.register_edit_info_date);
		save = (TextView) findViewById(R.id.title_tv);
		commit = (TextView) findViewById(R.id.register_edit_info_commit);

		sex = "1";
		dateSel = "";
		phone = getIntent().getExtras().getString("phone");
		System.out.println("phone ---> " + phone);
		listFile = new ArrayList<File>();
		listMD5 = new ArrayList<String>();//

		back.setOnClickListener(this);
		headLL.setOnClickListener(this);
		birthday.setOnClickListener(this);
		save.setOnClickListener(this);
		commit.setOnClickListener(this);

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == rb1.getId()) {
					sex = "1";
				} else {
					sex = "2";
				}
			}
		});
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
		case R.id.register_edit_info_headll:
			selectPic();
			break;
		case R.id.register_edit_info_birthday:
			new DatePickerPopUpwindow(this, parent, new Tools().Today(), "r",
					false);
			break;
		case R.id.title_tv://

			break;
		case R.id.register_edit_info_commit://
			if (validate()) {
				updateInfoHttpClient(listFile, listMD5);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 保存信息
	 * 
	 * @param List
	 *            <File> listMD5
	 * @param List
	 *            <File> listFile
	 */
	private void updateInfoHttpClient(List<File> listFile, List<String> listMD5) {
		RequestParams params = new RequestParams();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(RegisterEditInfo.this);
		try {
			for (int i = 0; i < listFile.size(); i++) {
				params.put(listMD5.get(i), listFile.get(i));
			}
			JSONObject job = new JSONObject();
			job.put("passwd", psw.getText().toString());
			job.put("phone", phone);
			job.put("name", name.getText().toString());
			job.put("sex", sex);
			job.put("birthday", dateSel);

			params.put("data", job.toString());

			HttpClient.post(Config.SAVE_PERSONAL_INFO_URL, params,
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
										JSONObject data = job
												.getJSONObject("data");
										JSONObject user = data
												.getJSONObject("user");

										Toast.makeText(RegisterEditInfo.this,
												"注册成功!", Toast.LENGTH_SHORT)
												.show();
										RegisterEditInfo.this.finish();
									} else {
										Toast.makeText(RegisterEditInfo.this,
												msg, Toast.LENGTH_SHORT).show();
										pd.dismiss();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								Toast.makeText(RegisterEditInfo.this, msg,
										Toast.LENGTH_SHORT).show();
								pd.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(RegisterEditInfo.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void selectPic() {
		int selectedMode = MultiImageSelectorActivity.MODE_SINGLE; // 照片单选,多选
		boolean showCamera = true; // 开启照相机
		int maxNum = 1; // 最多可选几张照片

		Intent intent = new Intent(RegisterEditInfo.this,
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

	private boolean validate() {
		if (psw.getText().toString().length() == 0) {
			Toast.makeText(this, "请设置密码!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (repsw.getText().toString().length() == 0) {
			Toast.makeText(this, "请确认密码!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!psw.getText().toString().equals(repsw.getText().toString())) {
			Toast.makeText(this, "俩次输入密码不一致!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (name.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写姓名!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (dateSel.equals("")) {
			Toast.makeText(this, "请选择生日!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (pic == null) {
			Toast.makeText(this, "请上传头像!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 处理日期选择返回结果
				dateSel = (String) msg.obj;
				date.setText(dateSel);
				break;
			case 2: // 处理头像回调
				String path = (String) msg.obj;
				headLL.setVisibility(View.GONE);
				head.setVisibility(View.VISIBLE);
				setHead(path);
				break;
			case 3:// 处理日期选择返回结果, 日期选择不正确
				Toast.makeText(RegisterEditInfo.this, "您还没出生!",
						Toast.LENGTH_SHORT).show();
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
	}
}
