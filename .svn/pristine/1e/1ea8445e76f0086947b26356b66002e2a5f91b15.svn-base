package com.youge.jobfinder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;

import tools.SchoolPullParse;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SchoolSecletActivity extends Activity implements OnClickListener {

	private EditText tv_resume_school;
	private ListView listView;
	private MyAdapter mAdapter;
	private ArrayList<String> list;
	private Handler mHandler;
	/** 在搜索数据时，用于标记最后一次输入的时间 */
	private long putTime = 0;
	/** 在搜索数据时，用于标记上一次输入的时间 */
	private long endPutTime = 0;
	/** 在搜索数据时，用于标记是否是经过查询 默认为false */
	private boolean searchBoolean = false;
	/** mHandler 中执行 更新 adapter中数据 */
	private final int DO_SEARCH = 1;

	private Timer timer;
	private String keyword;

	private XmlPullParser schoolParser;
	private String fileName = "school.xml";
	private String school;
	private ImageView back;
	private TextView title_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_seclet);
		findView();
		initHandler();
		initView();

	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		tv_resume_school = (EditText) findViewById(R.id.tv_resume_school);
		listView = (ListView) findViewById(R.id.listview);
		back = (ImageView) findViewById(R.id.back);
		title_tv = (TextView) findViewById(R.id.title_tv);
		back.setOnClickListener(this);
		title_tv.setOnClickListener(this);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		list = new ArrayList<String>();
		school = getIntent().getStringExtra("school");
		if (school != null) {
			if(!school.equals("点击选择")){
				tv_resume_school.setText(school);
			}
		}
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) tv_resume_school
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(tv_resume_school, 0);
				// 启动循环监听线程
				timer = new Timer();
				timer.schedule(new MyTimer(), 0, 1000);
			}
		}, 100);
		mAdapter = new MyAdapter();
		listView.setAdapter(mAdapter);
		tv_resume_school.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				keyword = s + "";

				Date date = new Date();
				putTime = date.getTime();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tv_resume_school.setText(list.get(position));
			}
		});
	}

	/**
	 * 初始化消息处理器，处理消息
	 * 
	 */
	private void initHandler() {
		mHandler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DO_SEARCH:
					mAdapter.notifyDataSetChanged();

				default:
					break;
				}
				return false;
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(SchoolSecletActivity.this,
						R.layout.school_lv_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.addr_lv_item_address);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (!TextUtils.isEmpty(keyword)) {
				viewHolder.name.setText(putstr(keyword, list.get(position)));
			}
			return convertView;
		}

		class ViewHolder {
			TextView name;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_tv:
			Intent intent = getIntent();
			intent.putExtra("school", tv_resume_school.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 开启Timer 类用于监听在搜索时最后一次输入后多长时间没有进行再次输入执行查询操作（这里设置为1s）
	 */
	private class MyTimer extends TimerTask {
		@Override
		public void run() {
			/**
			 * 在循环中，如果最后一次输入记录的时间和上一次输入记录的时间相等，则认为此时用户没有进行输入操作
			 */
			if (endPutTime == putTime) {
				/**
				 * 用当前时间和最后一次输入的时间的差如果大于1s 同时 该时间段的输入未被查询过，执行 if 语句
				 */
				Date date = new Date();
				if (date.getTime() - endPutTime > 1000 && searchBoolean) {
					if ((!TextUtils.isEmpty(keyword)) && keyword.length() != 0) {
						try {
							list.clear();

							schoolParser = getXMLFromResXml(fileName);
							list = SchoolPullParse.ParseXml(schoolParser,
									keyword);

							// lists_search.clear();
							// lists_search.addAll(dao.getSearchListDatas(
							// view_name,
							// levelTwoFunction.getTable_column(),
							// levelTwoFunction.getParent_id(), parent_id,
							// keyword, fromEditOrNew));
						} catch (Exception e) {

						}
						// 发送更新Adapter 中数据消息
						Message message = mHandler.obtainMessage();
						message.what = DO_SEARCH;
						mHandler.sendMessage(message);
					} else {
						list.clear();
						// 发送更新Adapter 中数据消息
						Message message = mHandler.obtainMessage();
						message.what = DO_SEARCH;
						mHandler.sendMessage(message);
					}
					searchBoolean = false;
				}
			} else {
				/**
				 * 在循环中，如果最后一次输入记录的时间和上一次输入记录的时间不相等，
				 * 则认为此时用户正在进行输入操作，将最后一次的输入时间付给上一次输入时间的变量
				 */
				endPutTime = putTime;
				searchBoolean = true;
			}
		}
	}

	/**
	 * 读取XML文件，xml文件放到res/xml文件夹中，若XML为本地文件，则推荐该方法
	 * 
	 * @param fileName
	 * @return : 读取到res/xml文件夹下的xml文件，返回XmlResourceParser对象（XmlPullParser的子类）
	 */
	public XmlResourceParser getXMLFromResXml(String fileName) {
		XmlResourceParser xmlParser = null;
		try {
			// */
			xmlParser = this.getResources().getXml(R.xml.school);
			return xmlParser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlParser;
	}

	/**
	 * 关键字变色
	 * 
	 */
	public static SpannableStringBuilder putstr(String keyword, String strtext) {

		if (strtext == null || keyword == null) {
			return null;
		}
		String docInfo = strtext;
		int keywordIndex = strtext.toUpperCase().indexOf(keyword.toUpperCase());

		SpannableStringBuilder style = new SpannableStringBuilder(docInfo);
		while (keywordIndex != -1) {
			// 关键字颜色改变
			style.setSpan(new ForegroundColorSpan(Color.BLUE), keywordIndex,
					keywordIndex + keyword.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			int tempkeywordTempIndex = keywordIndex + keyword.length();
			strtext = docInfo.substring(tempkeywordTempIndex, docInfo.length());
			keywordIndex = strtext.indexOf(keyword);
			if (keywordIndex != -1) {
				keywordIndex = keywordIndex + tempkeywordTempIndex;
			}
		}

		return style;

	}

}
