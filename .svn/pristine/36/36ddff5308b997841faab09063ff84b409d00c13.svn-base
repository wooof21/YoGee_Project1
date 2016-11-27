package find;

import view.GridViewForIntegralItem;
import adapter.GridViewForIntegralItemAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class IntegralItemActivity extends BaseActivity implements
		OnClickListener {

	private GridViewForIntegralItem gridView;
	private GridViewForIntegralItemAdapter mAdapter;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_item);
		findView();
		initView();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		gridView = (GridViewForIntegralItem) findViewById(R.id.gridview);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		mAdapter = new GridViewForIntegralItemAdapter(getApplicationContext(),
				null);
		gridView.setAdapter(mAdapter);
		sv = (ScrollView) findViewById(R.id.scrollview);
		sv.smoothScrollTo(0, 0);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(IntegralItemActivity.this,
						IntegralDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
