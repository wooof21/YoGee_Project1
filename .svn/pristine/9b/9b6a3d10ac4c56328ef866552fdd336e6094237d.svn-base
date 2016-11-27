package find;

import java.util.ArrayList;
import java.util.HashMap;

import tools.RefreshableView;
import adapter.MyIntegralAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class MyIntegralActivity extends BaseActivity implements OnClickListener {
	private ListView listView;
	private MyIntegralAdapter adapter;
	private HashMap<String, String> headMap;
	private ArrayList<ArrayList<String>> bodyList;
	private RefreshableView refresh;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_integral);

		findView();
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
	 * 根据ID查找控件
	 */
	private void findView() {
		listView = (ListView) findViewById(R.id.listview);
		refresh = (RefreshableView) findViewById(R.id.refresh);
		back = (ImageView) findViewById(R.id.back);
	}

	/**
	 * 初始化数据 设置监听事件
	 */
	private void initView() {
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		headMap = new HashMap<String, String>();
		bodyList = new ArrayList<ArrayList<String>>();
		headMap.put("0", "345");
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("取消订单");
		list1.add("2015-09-16");
		list1.add("-32");
		for (int i = 0; i < 7; i++) {
			bodyList.add(list1);
		}
		adapter = new MyIntegralAdapter(MyIntegralActivity.this, headMap,
				bodyList);
		listView.setAdapter(adapter);
		back.setOnClickListener(this);
		refresh.setOnRefreshListener(
				new RefreshableView.PullToRefreshListener() {
					@Override
					public void onRefresh() {
						refresh.finishRefreshing();
					}

				}, 0);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}

	}
}
