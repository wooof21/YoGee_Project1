/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class AddExperienceActivity extends Activity implements OnClickListener {

	private ImageView back, delete;
	private EditText add_experience_Employer, add_experience_obligation;
	private TextView commit, add_experience_startDate, add_experience_endDate;

	private String id, startDate, endDate, Employer, obligation, position;
	private Experience aModel;
	private ArrayList<Experience> aList;
	private Dialog mDialog;
	private InputMethodManager inputManager;
	/** 时间选择器中的日期值 */
	private String selDate;
	private Boolean isFromExp;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_experience);
		initView();
	}

	private void initView() {
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateServer = new SimpleDateFormat("yyyy.MM");
		selDate = dateServer.format(date);
		Exit.getInstance().addActivity(this);
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		back = (ImageView) findViewById(R.id.back);
		delete = (ImageView) findViewById(R.id.title_iv);
		add_experience_startDate = (TextView) findViewById(R.id.add_experience_startDate);
		add_experience_endDate = (TextView) findViewById(R.id.add_experience_endDate);
		add_experience_Employer = (EditText) findViewById(R.id.add_experience_Employer);
		add_experience_obligation = (EditText) findViewById(R.id.add_experience_obligation);
		commit = (TextView) findViewById(R.id.add_experience_commit);

		back.setOnClickListener(this);
		delete.setOnClickListener(this);
		commit.setOnClickListener(this);
		add_experience_startDate.setOnClickListener(this);
		add_experience_endDate.setOnClickListener(this);

		isFromExp = getIntent().getBooleanExtra("isFromExp", true);
		aList = new ArrayList<Experience>();
		aList = (ArrayList<Experience>) getIntent().getSerializableExtra(
				"experienceList");
		position = getIntent().getStringExtra("position");

		if (isFromExp) {
			id = "0";
			delete.setVisibility(View.GONE);
		} else {
			id = aList.get(Integer.parseInt(position)).getId();
			startDate = aList.get(Integer.parseInt(position)).getStartDate();
			endDate = aList.get(Integer.parseInt(position)).getEndDate();
			Employer = aList.get(Integer.parseInt(position)).getEmployer();
			obligation = aList.get(Integer.parseInt(position)).getObligation();
			add_experience_startDate.setText(startDate);
			add_experience_endDate.setText(endDate);
			add_experience_Employer.setText(Employer);
			add_experience_obligation.setText(obligation);
			delete.setVisibility(View.VISIBLE);
		}

	}

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
		case R.id.title_iv:
			aList.remove(Integer.parseInt(position));
			Intent intents = getIntent();
			Bundle bundles = new Bundle();
			bundles.putSerializable("list", aList);
			intents.putExtras(bundles);
			setResult(RESULT_OK, intents);
			AddExperienceActivity.this.finish();
			break;
		case R.id.add_experience_commit:
			if (validate()) {
				if (isFromExp) {
					Experience model = new Experience();
					model.setId(id);
					model.setPosition(position);
					model.setStartDate(add_experience_startDate.getText()
							.toString());
					model.setEndDate(add_experience_endDate.getText()
							.toString());
					model.setEmployer(add_experience_Employer.getText()
							.toString());
					model.setObligation(add_experience_obligation.getText()
							.toString());
					aList.add(model);
					if (aList.size() > 1) {
						Collections.sort(aList, new Comparator<Experience>() {

							public int compare(Experience o1, Experience o2) {

								// 按照入职日期升序排列
								if (compareDate(o1.getStartDate(),
										o2.getStartDate())) {
									return 1;
								}
								if (o1.getStartDate() == o2.getStartDate()) {
									return 0;
								}
								return -1;
							}
						});
					}
				} else {
					aList.get(Integer.parseInt(position)).setStartDate(
							add_experience_startDate.getText().toString());
					aList.get(Integer.parseInt(position)).setEndDate(
							add_experience_endDate.getText().toString());
					aList.get(Integer.parseInt(position)).setEmployer(
							add_experience_Employer.getText().toString());
					aList.get(Integer.parseInt(position)).setObligation(
							add_experience_obligation.getText().toString());
				}
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", aList);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				AddExperienceActivity.this.finish();
			}
			break;
		case R.id.add_experience_startDate:
			if (TextUtils.isEmpty(startDate)) {
				startDate = selDate;
			}
			String sYearStr = startDate.split("\\.")[0];
			String sMonthStr = startDate.split("\\.")[1];
			int mYear = Integer.parseInt(sYearStr);
			int mMonth = Integer.parseInt(sMonthStr);
			OnDateSetListener mDateSetListener = new OnDateSetListener() {
				// 解决OnDateSetListene重复执行两次的Bug
				boolean dateFlag = true;

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					if (dateFlag) {
						String monthOfYearStr = (monthOfYear + 1) + "";
						if ((monthOfYear + 1) < 10) {
							monthOfYearStr = "0" + monthOfYearStr;
						}
						startDate = year + "." + monthOfYearStr;
						add_experience_startDate.setText(startDate);
						dateFlag = false;
					}
				}
			};
			mDialog = new CustomerDatePickerDialog(AddExperienceActivity.this,
					mDateSetListener, mYear, mMonth - 1, 1);
			mDialog.show();

			DatePicker dp = findDatePicker((ViewGroup) mDialog.getWindow()
					.getDecorView());
			if (dp != null) {
				((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
						.getChildAt(2).setVisibility(View.GONE);
			}
			break;
		case R.id.add_experience_endDate:
			if (TextUtils.isEmpty(endDate)) {
				endDate = selDate;
			}
			String eYearStr = endDate.split("\\.")[0];
			String eMonthStr = endDate.split("\\.")[1];
			int eYear = Integer.parseInt(eYearStr);
			int eMonth = Integer.parseInt(eMonthStr);
			OnDateSetListener eDateSetListener = new OnDateSetListener() {
				// 解决OnDateSetListene重复执行两次的Bug
				boolean dateFlag = true;

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					if (dateFlag) {
						String monthOfYearStr = (monthOfYear + 1) + "";
						if ((monthOfYear + 1) < 10) {
							monthOfYearStr = "0" + monthOfYearStr;
						}
						endDate = year + "." + monthOfYearStr;
						add_experience_endDate.setText(endDate);
						dateFlag = false;
					}
				}
			};
			mDialog = new CustomerDatePickerDialog(AddExperienceActivity.this,
					eDateSetListener, eYear, eMonth - 1, 1);
			mDialog.show();

			DatePicker dps = findDatePicker((ViewGroup) mDialog.getWindow()
					.getDecorView());
			if (dps != null) {
				((ViewGroup) ((ViewGroup) dps.getChildAt(0)).getChildAt(0))
						.getChildAt(2).setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	class CustomerDatePickerDialog extends DatePickerDialog {

		public CustomerDatePickerDialog(Context context,
				OnDateSetListener callBack, int year, int monthOfYear,
				int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
			this.setTitle(year + "年" + (monthOfYear + 1) + "月");
		}

		@Override
		public void onDateChanged(DatePicker view, int year, int month, int day) {
			super.onDateChanged(view, year, month, day);
			mDialog.setTitle(year + "年" + (month + 1) + "月");
		}
	}

	/**
	 * 从当前Dialog中查找DatePicker子控件
	 * 
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	private boolean validate() {
		if (add_experience_startDate.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写入职时间!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (compareDate(add_experience_startDate.getText().toString(),
				selDate)) {
			Toast.makeText(this, "入职时间不能大于当前时间!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (add_experience_endDate.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写离职时间!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (compareDate(add_experience_endDate.getText().toString(),
				selDate)) {
			Toast.makeText(this, "离职时间不能大于当前时间!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (add_experience_Employer.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写工作单位!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (add_experience_obligation.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写职位名称!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (compareDate(startDate, endDate)) {
			Toast.makeText(this, "离职时间不能早于入职时间!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private Boolean compareDate(String startDate, String endDate) {
		int sYearStr = Integer.parseInt(startDate.split("\\.")[0]);
		int sMonthStr = Integer.parseInt(startDate.split("\\.")[1]);
		int eYearStr = Integer.parseInt(endDate.split("\\.")[0]);
		int eMonthStr = Integer.parseInt(endDate.split("\\.")[1]);
		if (sYearStr > eYearStr) {
			return true;
		} else if (sYearStr == eYearStr) {
			if (sMonthStr > eMonthStr) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
