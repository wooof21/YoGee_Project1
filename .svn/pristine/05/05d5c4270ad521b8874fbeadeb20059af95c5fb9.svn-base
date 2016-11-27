/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import model.Certification;
import tools.Exit;
import tools.MD5Util;
import tools.PictureUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class SkillCertificationUploadActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back, add;
	private TextView save;
	private LinearLayout container, container1, container2, container3,
			container4, container5;
	private EditText title1, title2, title3, title4, title5;
	private ImageView del1, del2, del3, del4, del5;
	private ImageView img1, img2, img3, img4, img5;

	private ArrayList<String> mSelectPath;
	private File pic;
	private List<File> listFile;// 文件集
	private List<String> listMD5;// MD5集

	private File file1, file2, file3, file4, file5;
	private String path1, path2, path3, path4, path5;

	private ArrayList<Certification> skillImgList;

	private int[] seen = new int[] { 1, 0, 0, 0, 0 };
	private String[] file = new String[] { "", "", "", "", "" };
	// private String[] pathStingsStrings = new String[] { path1, path2, path3,
	// path4, path5 };

	private EditText[] title;
	protected static final int SKILL_OK = 7;
	private LinearLayout[] ll;
	private InputMethodManager inputManager;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.skill_certification);

		initView();
		initData();
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
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		save = (TextView) findViewById(R.id.title_tv);
		add = (ImageView) findViewById(R.id.skill_certification_add);
		container = (LinearLayout) findViewById(R.id.skill_certification_container);

		container1 = (LinearLayout) findViewById(R.id.skill_certification_item_view1);
		container2 = (LinearLayout) findViewById(R.id.skill_certification_item_view2);
		container3 = (LinearLayout) findViewById(R.id.skill_certification_item_view3);
		container4 = (LinearLayout) findViewById(R.id.skill_certification_item_view4);
		container5 = (LinearLayout) findViewById(R.id.skill_certification_item_view5);

		title1 = (EditText) findViewById(R.id.skill_certification_item_title1);
		title2 = (EditText) findViewById(R.id.skill_certification_item_title2);
		title3 = (EditText) findViewById(R.id.skill_certification_item_title3);
		title4 = (EditText) findViewById(R.id.skill_certification_item_title4);
		title5 = (EditText) findViewById(R.id.skill_certification_item_title5);

		del1 = (ImageView) findViewById(R.id.skill_certification_item_delete1);
		del2 = (ImageView) findViewById(R.id.skill_certification_item_delete2);
		del3 = (ImageView) findViewById(R.id.skill_certification_item_delete3);
		del4 = (ImageView) findViewById(R.id.skill_certification_item_delete4);
		del5 = (ImageView) findViewById(R.id.skill_certification_item_delete5);

		img1 = (ImageView) findViewById(R.id.skill_certification_item_pic1);
		img2 = (ImageView) findViewById(R.id.skill_certification_item_pic2);
		img3 = (ImageView) findViewById(R.id.skill_certification_item_pic3);
		img4 = (ImageView) findViewById(R.id.skill_certification_item_pic4);
		img5 = (ImageView) findViewById(R.id.skill_certification_item_pic5);

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		add.setOnClickListener(this);

		del1.setOnClickListener(this);
		del2.setOnClickListener(this);
		del3.setOnClickListener(this);
		del4.setOnClickListener(this);
		del5.setOnClickListener(this);

		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);
		img5.setOnClickListener(this);

		listFile = new ArrayList<File>();
		listMD5 = new ArrayList<String>();//
		skillImgList = new ArrayList<Certification>();

		container2.setVisibility(View.GONE);
		container3.setVisibility(View.GONE);
		container4.setVisibility(View.GONE);
		container5.setVisibility(View.GONE);

		title = new EditText[] { title1, title2, title3, title4, title5 };
		ll = new LinearLayout[] { container1, container2, container3,
				container4, container5 };
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void initData() {
		skillImgList = (ArrayList<Certification>) getIntent()
				.getSerializableExtra("skillImgList");
		int count = skillImgList.size();
		if (count == 1) {
			setPicPath(skillImgList.get(0).getTitle(), getBitmap(skillImgList
					.get(0).getPath()), 1);
		} else if (count == 2) {
			for (int i = 1; i < 3; i++) {
				setPicPath(skillImgList.get(i - 1).getTitle(),
						getBitmap(skillImgList.get(i - 1).getPath()), i);
			}
		} else if (count == 3) {
			for (int i = 1; i < 4; i++) {
				setPicPath(skillImgList.get(i - 1).getTitle(),
						getBitmap(skillImgList.get(i - 1).getPath()), i);
			}
		} else if (count == 4) {
			for (int i = 1; i < 5; i++) {
				setPicPath(skillImgList.get(i - 1).getTitle(),
						getBitmap(skillImgList.get(i - 1).getPath()), i);
			}
		} else if (count == 5) {
			for (int i = 1; i < 6; i++) {
				setPicPath(skillImgList.get(i - 1).getTitle(),
						getBitmap(skillImgList.get(i - 1).getPath()), i);
			}
		}

	}

	public static String getPath(Bitmap bitmap) {
		try {
			File pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			return pic.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	private void setPicPath(String title, Bitmap btm, int i) {
		if (i == 1) {
			title1.setText(title);
		} else if (i == 2) {
			title2.setText(title);
			container2.setVisibility(View.VISIBLE);
		} else if (i == 3) {
			title3.setText(title);
			container3.setVisibility(View.VISIBLE);
		} else if (i == 4) {
			title4.setText(title);
			container4.setVisibility(View.VISIBLE);
		} else if (i == 5) {
			title5.setText(title);
			container5.setVisibility(View.VISIBLE);
		}
		setPicTwo(btm, i);
		seen[i - 1] = 1;
		file[i - 1] = "1";
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
				// file[which - 1] = new File(path);
				// pathStingsStrings[which - 1] = path;
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			mSelectPath = new ArrayList<String>();
			mSelectPath = data
					.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			StringBuilder sb = new StringBuilder();
			for (String p : mSelectPath) {
				sb.append(p);
				sb.append("\n");
				System.out.println("pic path ---> " + sb.toString());
				// save(mSelectPath);
			}
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.arg1 = requestCode;
			msg.obj = mSelectPath.get(0);
			msg.sendToTarget();
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

	private void setPic(String path, int which) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// 计算压缩尺寸
		options.inSampleSize = PictureUtil.calculateInSampleSize(options, 480,
				800);
		// 解码
		options.inJustDecodeBounds = false;

		Bitmap b = BitmapFactory.decodeFile(path, options);
		switch (which) {
		case 1:
			img1.setImageBitmap(b);
			seen[0] = 1;
			file[0] = "1";
			break;
		case 2:
			img2.setImageBitmap(b);
			seen[1] = 1;
			file[1] = "1";
			break;
		case 3:
			img3.setImageBitmap(b);
			seen[2] = 1;
			file[2] = "1";
			break;
		case 4:
			img4.setImageBitmap(b);
			seen[3] = 1;
			file[3] = "1";
			break;
		case 5:
			img5.setImageBitmap(b);
			seen[4] = 1;
			file[4] = "1";
			break;
		default:
			break;
		}

	}

	private void setPicTwo(Bitmap btm, int which) {
		switch (which) {
		case 1:
			img1.setImageBitmap(btm);
			break;
		case 2:
			img2.setImageBitmap(btm);
			break;
		case 3:
			img3.setImageBitmap(btm);
			break;
		case 4:
			img4.setImageBitmap(btm);
			break;
		case 5:
			img5.setImageBitmap(btm);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// if (inputManager.isAcceptingText()) {
		// inputManager.hideSoftInputFromWindow(getCurrentFocus()
		// .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_tv:
			skillImgList.clear();
			for (int i = 0, j = seen.length; i < j; i++) {
				if (seen[i] == 1 && "1".equals(file[i])) {
					Certification cert = new Certification();
					cert.setTitle(title[i].getText().toString());
					Bitmap image = null;
					if (i + 1 == 1) {
						image = ((BitmapDrawable) img1.getDrawable())
								.getBitmap();
					} else if (i + 1 == 2) {
						image = ((BitmapDrawable) img2.getDrawable())
								.getBitmap();
					} else if (i + 1 == 3) {
						image = ((BitmapDrawable) img3.getDrawable())
								.getBitmap();
					} else if (i + 1 == 4) {
						image = ((BitmapDrawable) img4.getDrawable())
								.getBitmap();
					} else if (i + 1 == 5) {
						image = ((BitmapDrawable) img4.getDrawable())
								.getBitmap();
					}

					cert.setPath(getPath(image));
					skillImgList.add(cert);
				}
			}
			Intent intent = getIntent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("skillImgList", skillImgList);
			intent.putExtras(bundle);
			setResult(SKILL_OK, intent);
			System.gc();
			finish();
			break;
		case R.id.skill_certification_add:
			if (getVisiableChild() == 6) {
				Toast.makeText(SkillCertificationUploadActivity.this,
						"最多可添加5个技能证书!", Toast.LENGTH_SHORT).show();
			} else {
				if (validate()) {
					for (int i = 0, j = seen.length; i < j; i++) {
						if (seen[i] == 0) {
							seen[i] = 1;
							ll[i].setVisibility(View.VISIBLE);
							break;
						}
					}
				} else {
					Toast.makeText(SkillCertificationUploadActivity.this,
							"技能证书需要名称及图片!", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.skill_certification_item_delete1:
			container1.setVisibility(View.GONE);
			title1.setText("");
			img1.setImageResource(R.drawable.camera_resume);
			seen[0] = 0;
			break;
		case R.id.skill_certification_item_delete2:
			container2.setVisibility(View.GONE);
			title2.setText("");
			img2.setImageResource(R.drawable.camera_resume);
			seen[1] = 0;
			break;
		case R.id.skill_certification_item_delete3:
			container3.setVisibility(View.GONE);
			title3.setText("");
			img3.setImageResource(R.drawable.camera_resume);
			seen[2] = 0;
			break;
		case R.id.skill_certification_item_delete4:
			container4.setVisibility(View.GONE);
			title4.setText("");
			img4.setImageResource(R.drawable.camera_resume);
			seen[3] = 0;
			break;
		case R.id.skill_certification_item_delete5:
			container5.setVisibility(View.GONE);
			title5.setText("");
			img5.setImageResource(R.drawable.camera_resume);
			seen[4] = 0;
			break;
		case R.id.skill_certification_item_pic1:
			selectPic(1);
			break;
		case R.id.skill_certification_item_pic2:
			selectPic(2);
			break;
		case R.id.skill_certification_item_pic3:
			selectPic(3);
			break;
		case R.id.skill_certification_item_pic4:
			selectPic(4);
			break;
		case R.id.skill_certification_item_pic5:
			selectPic(5);
			break;
		default:
			break;
		}
	}

	private int getVisiableChild() {
		int count = 0;
		for (int i = 0, j = container.getChildCount(); i < j; i++) {
			if (container.getChildAt(i).getVisibility() == View.VISIBLE) {
				count++;
			}
		}
		return count;
	}

	private boolean validate() {
		boolean bool = true;
		for (int i = 0, j = seen.length; i < j; i++) {
			if (seen[i] == 1) {
				if (title[i].getText().toString().length() != 0
						&& file[i] == "1") {
					continue;
				} else {
					bool = false;
					break;
				}
			} else {
				continue;
			}
		}
		return bool;
	}

}
