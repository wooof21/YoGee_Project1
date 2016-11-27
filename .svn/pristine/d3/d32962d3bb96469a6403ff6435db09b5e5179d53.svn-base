package find;

import view.ListViewForExchange;
import adapter.ListViewForExchangeAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class IntegralExchangeActivity extends BaseActivity implements
		OnClickListener {
	private ListViewForExchange listview;
	private ListViewForExchangeAdapter mAdapter;
	private ImageView back;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_exchange);
		findView();
		initView();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		listview = (ListViewForExchange) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		mAdapter = new ListViewForExchangeAdapter(
				IntegralExchangeActivity.this, null);
		listview.setAdapter(mAdapter);
		sv = (ScrollView) findViewById(R.id.scrollview);
		sv.smoothScrollTo(0, 0);
		back.setOnClickListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(IntegralExchangeActivity.this,
						IntegralExchangeDetailActivity.class);
				startActivity(intent);
			}
		});
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
