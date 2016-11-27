package find;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class IntegralExchangeDetailActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_exchange_detail);
		findView();
		initView();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		back = (ImageView) findViewById(R.id.back);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		back.setOnClickListener(this);
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
