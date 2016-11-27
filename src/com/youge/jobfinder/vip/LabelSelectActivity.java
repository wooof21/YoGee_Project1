package com.youge.jobfinder.vip;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class LabelSelectActivity extends BaseActivity implements
		OnClickListener {
	private ArrayList<String> labelData;

	private FrameLayout fl_labelOne, fl_labelTwo, fl_labelThree, fl_labelFour,
			fl_labelFive, fl_labelSix;
	private ImageView labelOne, labelTwo, labelThree, labelFour, labelFive,
			labelSix, back;

	private TextView title_tv, remind_text;

	/** 消息标识标签选择 */
	protected static final int LABEL_OK = 5;
	private Boolean isFromFillOrderBoolean = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_select);
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
		fl_labelOne = (FrameLayout) findViewById(R.id.fl_labelOne);
		fl_labelTwo = (FrameLayout) findViewById(R.id.fl_labelTwo);
		fl_labelThree = (FrameLayout) findViewById(R.id.fl_labelThree);
		fl_labelFour = (FrameLayout) findViewById(R.id.fl_labelFour);
		fl_labelFive = (FrameLayout) findViewById(R.id.fl_labelFive);
		fl_labelSix = (FrameLayout) findViewById(R.id.fl_labelSix);
		labelOne = (ImageView) findViewById(R.id.labelOne);
		labelTwo = (ImageView) findViewById(R.id.labelTwo);
		labelThree = (ImageView) findViewById(R.id.labelThree);
		labelFour = (ImageView) findViewById(R.id.labelFour);
		labelFive = (ImageView) findViewById(R.id.labelFive);
		labelSix = (ImageView) findViewById(R.id.labelSix);
		title_tv = (TextView) findViewById(R.id.title_tv);
		back = (ImageView) findViewById(R.id.back);
		remind_text = (TextView) findViewById(R.id.remind_text);
	}

	/**
	 * 初始化控件 添加点击事件
	 */
	private void initView() {
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		labelData = new ArrayList<String>();
		isFromFillOrderBoolean = getIntent().getBooleanExtra(
				"isFromFillOrderBoolean", false);
		if (isFromFillOrderBoolean) {
			remind_text.setText("请选择您的订单所属类型，这样会增加您的被抢单几率哦~");
		}

		labelData = (ArrayList<String>) getIntent().getSerializableExtra(
				"labelData");
		if (labelData != null) {
			for (int i = 0, j = labelData.size(); i < j; i++) {
				labelShow(labelData.get(i));
				System.out.println("labels ---> " + labelData.get(i));
			}
		}
		fl_labelOne.setOnClickListener(this);
		fl_labelTwo.setOnClickListener(this);
		fl_labelThree.setOnClickListener(this);
		fl_labelFour.setOnClickListener(this);
		fl_labelFive.setOnClickListener(this);
		fl_labelSix.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_labelOne:
			changeLabel("1");
			break;
		case R.id.fl_labelTwo:
			changeLabel("2");
			break;
		case R.id.fl_labelThree:
			changeLabel("3");
			break;
		case R.id.fl_labelFour:
			changeLabel("4");
			break;
		case R.id.fl_labelFive:
			changeLabel("5");
			break;
		case R.id.fl_labelSix:
			changeLabel("6");
			break;
		case R.id.title_tv:
			Intent intent = getIntent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("labelData", labelData);
			intent.putExtras(bundle);
			setResult(LABEL_OK, intent);
			finish();
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 更改标签选择
	 */
	private void changeLabel(String count) {
		if (isFromFillOrderBoolean) {
			if (labelData.size() != 0) {
				String countSel = labelData.get(0);
				if ("1".equals(countSel)) {
					labelOne.setVisibility(View.INVISIBLE);
				} else if ("2".equals(countSel)) {
					labelTwo.setVisibility(View.INVISIBLE);
				} else if ("3".equals(countSel)) {
					labelThree.setVisibility(View.INVISIBLE);
				} else if ("4".equals(countSel)) {
					labelFour.setVisibility(View.INVISIBLE);
				} else if ("5".equals(countSel)) {
					labelFive.setVisibility(View.INVISIBLE);
				} else if ("6".equals(countSel)) {
					labelSix.setVisibility(View.INVISIBLE);
				}
				labelData.remove(0);
			}
			labelData.add(count);
			if ("1".equals(count)) {
				labelOne.setVisibility(View.VISIBLE);
			} else if ("2".equals(count)) {
				labelTwo.setVisibility(View.VISIBLE);
			} else if ("3".equals(count)) {
				labelThree.setVisibility(View.VISIBLE);
			} else if ("4".equals(count)) {
				labelFour.setVisibility(View.VISIBLE);
			} else if ("5".equals(count)) {
				labelFive.setVisibility(View.VISIBLE);
			} else if ("6".equals(count)) {
				labelSix.setVisibility(View.VISIBLE);
			}
		} else {
			if (labelData.contains(count)) {
				labelData.remove(count);
				if ("1".equals(count)) {
					labelOne.setVisibility(View.INVISIBLE);
				} else if ("2".equals(count)) {
					labelTwo.setVisibility(View.INVISIBLE);
				} else if ("3".equals(count)) {
					labelThree.setVisibility(View.INVISIBLE);
				} else if ("4".equals(count)) {
					labelFour.setVisibility(View.INVISIBLE);
				} else if ("5".equals(count)) {
					labelFive.setVisibility(View.INVISIBLE);
				} else if ("6".equals(count)) {
					labelSix.setVisibility(View.INVISIBLE);
				}

			} else {
				int num = 3;
				if (labelData.size() < num) {
					labelData.add(count);
					if ("1".equals(count)) {
						labelOne.setVisibility(View.VISIBLE);
					} else if ("2".equals(count)) {
						labelTwo.setVisibility(View.VISIBLE);
					} else if ("3".equals(count)) {
						labelThree.setVisibility(View.VISIBLE);
					} else if ("4".equals(count)) {
						labelFour.setVisibility(View.VISIBLE);
					} else if ("5".equals(count)) {
						labelFive.setVisibility(View.VISIBLE);
					} else if ("6".equals(count)) {
						labelSix.setVisibility(View.VISIBLE);
					}
				}
			}
		}

	}

	/**
	 * 初始化label页面选项
	 */
	private void labelShow(String count) {
		if ("1".equals(count)) {
			labelOne.setVisibility(View.VISIBLE);
		} else if ("2".equals(count)) {
			labelTwo.setVisibility(View.VISIBLE);
		} else if ("3".equals(count)) {
			labelThree.setVisibility(View.VISIBLE);
		} else if ("4".equals(count)) {
			labelFour.setVisibility(View.VISIBLE);
		} else if ("5".equals(count)) {
			labelFive.setVisibility(View.VISIBLE);
		} else if ("6".equals(count)) {
			labelSix.setVisibility(View.VISIBLE);
		}
	}
}
