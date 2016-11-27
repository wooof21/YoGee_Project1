/**
 * 
 * @param
 */
package discover;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.PullToRefreshLayout.OnRefreshListener;
import view.MGridView;
import view.PullableScrollView;
import adapter.CreditMallGVAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class CreditMallCategoryMainActivity extends BaseActivity implements
		OnRefreshListener{

	private ImageView back;
	private TextView title;
	private PullToRefreshLayout refresh;
	private PullableScrollView psv;
	private MGridView gv;

	private int count = 10, total = 0;
	private ArrayList<HashMap<String, String>> list;

	private String typeId;
	private CreditMallGVAdapter adapter;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_mall_category_detail_main);
		initView();
	}

	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title_title);
		refresh = (PullToRefreshLayout) findViewById(R.id.credit_mall_refresh_view);
		psv = (PullableScrollView) findViewById(R.id.credit_mall_psv);
		gv = (MGridView) findViewById(R.id.credit_mall_category_main_lv_item_gv);
		psv.smoothScrollTo(0, 0);
		refresh.setOnRefreshListener(this);

		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				finish();
			}
		});

		title.setText(getIntent().getExtras().getString("name", "分类专区"));
		typeId = getIntent().getExtras().getString("id");
		list = new ArrayList<HashMap<String, String>>();
		adapter = new CreditMallGVAdapter(this, list, "2");
		gv.setAdapter(adapter);

		getCategoryList(typeId, "f");
	}

	private void getCategoryList(String id, final String refresh){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("typeid", id);
			job.put("count", count + "");
			job.put("total", total + "");

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.CREDIT_MALL_CATEGORY_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("商品分类专区接口返回 ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										ArrayList<HashMap<String, String>> singleList = new ArrayList<HashMap<String, String>>();
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("typemall");
										for(int i = 0, j = jArray.length(); i < j; i++){
											JSONObject job = jArray
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put(
													"integralmallId",
													job.getString("integralmallId"));
											hashMap.put(
													"integralmallName",
													job.getString("integralmallName"));
											hashMap.put(
													"integralmallPicture",
													job.getString("integralmallPicture"));
											hashMap.put(
													"integralmallPoint",
													job.getString("integralmallPoint"));

											singleList.add(hashMap);
										}
										if (refresh.equals("f")){
											list.clear();
											list.addAll(singleList);
										}else{
											list.addAll(singleList);
										}
										adapter.notifyDataSetChanged();
									}else{
										Toast.makeText(
												CreditMallCategoryMainActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(CreditMallCategoryMainActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		// 下拉刷新操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total = 0;
				getCategoryList(typeId, "f");
				// 千万别忘了告诉控件刷新完毕了哦！
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		// 加载操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total += count;
				getCategoryList(typeId, "n");
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}
}
