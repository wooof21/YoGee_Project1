/**
 * 
 * @param
 */
package discover;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindow;
import popup.ContentPopUpWindowSingleButton;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.AddressMainActivity;

import find.IntegralExplainActivity;

/**
 * 
 * @param
 */
public class CreditMallGoodsDetailActivity extends BaseActivity implements
		OnClickListener{

	private ImageView back, pic;
	private TextView title, credit, detail, addrBtn, checkMore, how, exchange;
	private FrameLayout frame;
	private RelativeLayout parent;

	private String goodsID, from, addrId, point;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_mall_goods_detail);
		initView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		pic = (ImageView) findViewById(R.id.credit_mall_goods_detail_pic);
		title = (TextView) findViewById(R.id.credit_mall_goods_detail_title);
		credit = (TextView) findViewById(R.id.credit_mall_goods_detail_credit);
		detail = (TextView) findViewById(R.id.credit_mall_goods_detail_detail);
		addrBtn = (TextView) findViewById(R.id.credit_mall_goods_detail_addrbtn);
		checkMore = (TextView) findViewById(R.id.credit_mall_goods_detail_morebtn);
		how = (TextView) findViewById(R.id.credit_mall_goods_detail_how);
		exchange = (TextView) findViewById(R.id.credit_mall_goods_detail_exchange);
		frame = (FrameLayout) findViewById(R.id.credit_mall_goods_detail_frame);
		parent = (RelativeLayout) findViewById(R.id.credit_mall_goods_detail_parent);

		back.setOnClickListener(this);
		addrBtn.setOnClickListener(this);
		checkMore.setOnClickListener(this);
		how.setOnClickListener(this);
		exchange.setOnClickListener(this);

		goodsID = getIntent().getExtras().getString("id");
		from = getIntent().getExtras().getString("from", "");
		System.out.println("goodsId ---> " + goodsID);
		detailHttp(goodsID, new Tools().getUserId(this));
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					ImageLoader.getInstance().displayImage(
							hashMap.get("integralmallPicture"), pic);
					title.setText(hashMap.get("integralmallName"));
					detail.setText(hashMap.get("integralmallRemarks"));
					credit.setText(Html.fromHtml("<font color=\"red\">"
							+ hashMap.get("price")
							+ "</font><font color=\"grey\">积分</font>"));
					point = hashMap.get("price");
					if (from.equals("")){
						if (Double.valueOf(hashMap.get("integral")) >= Double
								.valueOf(hashMap.get("price"))){
							addrBtn.setVisibility(View.VISIBLE);
							exchange.setVisibility(View.VISIBLE);
						}else{
							addrBtn.setVisibility(View.GONE);
							frame.setVisibility(View.VISIBLE);
						}
					}else{
						frame.setVisibility(View.GONE);
						exchange.setVisibility(View.GONE);
						addrBtn.setVisibility(View.GONE);
						checkMore.setVisibility(View.VISIBLE);
					}
				break;
				case 2: // 兑换
					exchangeHttp(new Tools()
							.getUserId(CreditMallGoodsDetailActivity.this));
				break;
				case 3:
					new ContentPopUpWindowSingleButton(
							CreditMallGoodsDetailActivity.this, parent,
							"恭喜, 兑换成功!", "creditmall_exchange", goodsID);
				break;
				default:
				break;
			}
		}

	};

	private void exchangeHttp(String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		System.out.println("addrId ---> " + addrId);
		try{
			job.put("id", goodsID);
			job.put("addressid", addrId);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString());

			HttpClient.post(this, Config.CREDIT_MALL_EXCHANGE_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("积分商城商品兑换接口返回  ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										if(data.getString("result").equals("1")){
											Message msg = handler.obtainMessage();
											msg.what = 3;
											msg.sendToTarget();
										}else{
											Toast.makeText(
													CreditMallGoodsDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												CreditMallGoodsDetailActivity.this,
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
							Toast.makeText(CreditMallGoodsDetailActivity.this,
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

	private void detailHttp(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString());

			HttpClient.post(this, Config.CREDIT_MALL_GOODS_DETAIL_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("积分商城商品详情接口返回  ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										HashMap<String, String> hashMap = new HashMap<String, String>();
										JSONObject data = result
												.getJSONObject("data");
										hashMap.put("integral",
												data.getString("integral"));
										JSONObject job = data
												.getJSONObject("Details");
										hashMap.put("integralmallId",
												job.getString("integralmallId"));
										hashMap.put("integralmallName", job
												.getString("integralmallName"));
										hashMap.put(
												"integralmallPicture",
												job.getString("integralmallPicture"));
										hashMap.put(
												"integralmallRemarks",
												job.getString("integralmallRemarks"));
										hashMap.put("price",
												job.getString("price"));

										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();
									}else{
										Toast.makeText(
												CreditMallGoodsDetailActivity.this,
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
							Toast.makeText(CreditMallGoodsDetailActivity.this,
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 100:
				if (resultCode == RESULT_OK){
					addrId = data.getExtras().getString("addrId", null);
					System.out.println("地址id ---> " + addrId);
					addrBtn.setBackgroundResource(R.drawable.corner_green_stroke);
					addrBtn.setTextColor(Color.rgb(34, 181, 112));
					addrBtn.setText("地址设置成功");
				}else{
					Toast.makeText(this, "请选择地址!", Toast.LENGTH_SHORT).show();
				}
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
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back:
				finish();
			break;
			case R.id.credit_mall_goods_detail_addrbtn: // 选择地址
				Intent intent = new Intent(this, AddressMainActivity.class);
				intent.putExtra("from", "creditmall");
				startActivityForResult(intent, 100);
			break;
			case R.id.credit_mall_goods_detail_morebtn: // 查看更多好礼
				//Toast.makeText(CreditMallGoodsDetailActivity.this, "功能正在内侧中,稍后开放...", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(CreditMallGoodsDetailActivity.this,
						CreditMallActivity.class));
			break;
			case R.id.credit_mall_goods_detail_how: // 如何获得积分
				startActivity(new Intent(this, IntegralExplainActivity.class));
			break;
			case R.id.credit_mall_goods_detail_exchange: // 立即兑换
				if (addrId == null){
					Toast.makeText(this, "请先设置地址后再兑换!", Toast.LENGTH_SHORT)
							.show();
				}else{
					new ContentPopUpWindow(this, parent, "",
							"creditmall_exchange", point, handler);
				}
			break;
			default:
			break;
		}
	}
}
