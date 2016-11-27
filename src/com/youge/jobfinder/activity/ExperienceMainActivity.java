/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.Experience;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import adapter.ExperienceLVItemAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class ExperienceMainActivity extends Activity implements OnClickListener {

	private ImageView back;
	private ListView lv;
	private FrameLayout add;
	private TextView title_tv;

	private ArrayList<Experience> list;
	private ExperienceLVItemAdapter adapter;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.experience_main_lv);
		initView();
	}

	private void initView() {
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.experience_main_lv_lv);
		add = (FrameLayout) findViewById(R.id.experience_main_lv_add);
		title_tv = (TextView) findViewById(R.id.title_tv);

		back.setOnClickListener(this);
		add.setOnClickListener(this);
		title_tv.setOnClickListener(this);

		list = new ArrayList<Experience>();
		list = (ArrayList<Experience>) getIntent().getSerializableExtra(
				"experienceList");
		adapter = new ExperienceLVItemAdapter(this, list);
		lv.setAdapter(adapter);
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
			Intent intentss = getIntent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("experienceList", list);
			intentss.putExtras(bundle);
			setResult(RESULT_OK, intentss);
			finish();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.experience_main_lv_add:
			Intent intent = new Intent(ExperienceMainActivity.this,
					AddExperienceActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("experienceList", list);
			intent.putExtras(mBundle);
			intent.putExtra("isFromExp", true);
			startActivityForResult(intent, 100);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				list.clear();
				list.addAll((ArrayList<Experience>) data.getExtras()
						.getSerializable("list"));
				adapter.notifyDataSetChanged();
			}
			break;
		case 200: // 地址修改回调
			if (resultCode == RESULT_OK) {
				list.clear();
				list.addAll((ArrayList<Experience>) data.getExtras()
						.getSerializable("list"));
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}
}
