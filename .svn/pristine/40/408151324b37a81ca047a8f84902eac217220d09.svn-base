/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class CommentActivity extends BaseActivity implements OnClickListener{

	private ImageView back, good, medium, bad;
	private TextView post;
	private RatingBar rb1, rb2;
	private EditText content;

	private String grade, oid, type, comment, userid;
	private double total;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
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
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		good = (ImageView) findViewById(R.id.comment_good);
		medium = (ImageView) findViewById(R.id.comment_medium);
		bad = (ImageView) findViewById(R.id.comment_bad);
		post = (TextView) findViewById(R.id.title_tv);
		rb1 = (RatingBar) findViewById(R.id.comment_rb1);
		rb2 = (RatingBar) findViewById(R.id.comment_rb2);
		content = (EditText) findViewById(R.id.comment_content);

		back.setOnClickListener(this);
		good.setOnClickListener(this);
		medium.setOnClickListener(this);
		bad.setOnClickListener(this);
		post.setOnClickListener(this);

		grade = "0";
		total = 0;
		oid = getIntent().getExtras().getString("oid");
		type = getIntent().getExtras().getString("type");
		userid = getIntent().getExtras().getString("userid", "");
		System.out.println("oid ---> " + oid);
		System.out.println("type ---> " + type);

		rb1.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser){
				// TODO Auto-generated method stub
				if (fromUser){
					System.out.println("rating1 ---> " + rating);
					total = rb1.getRating() + rb2.getRating();
					if (total > 0 && total <= 3){
						setIvBg(R.id.comment_bad);
					}else if (total > 3 && total <= 6){
						setIvBg(R.id.comment_medium);
					}else if (total > 6 && total <= 10){
						setIvBg(R.id.comment_good);//
					}
				}
			}
		});

		rb2.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser){
				// TODO Auto-generated method stub
				if (fromUser){
					System.out.println("rating2 ---> " + rating);
					total = rb1.getRating() + rb2.getRating();
					if (total > 0 && total <= 3){
						setIvBg(R.id.comment_bad);
					}else if (total > 3 && total <= 6){
						setIvBg(R.id.comment_medium);
					}else if (total > 6 && total <= 10){
						setIvBg(R.id.comment_good);//
					}
				}
			}
		});
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
				setResult(RESULT_CANCELED);
				finish();
			break;
			case R.id.comment_good:
				setIvBg(R.id.comment_good);//
				rb1.setRating(5);
				rb2.setRating(5);
			break;
			case R.id.comment_medium:
				setIvBg(R.id.comment_medium);
				rb1.setRating(3);
				rb2.setRating(3);
			break;
			case R.id.comment_bad:
				setIvBg(R.id.comment_bad);
				rb1.setRating(1);
				rb2.setRating(1);
			break;
			case R.id.title_tv:
				if (validate()){
					commentPost(oid, new Tools().getUserId(this), type);
				}
			break;
			default:
			break;
		}
	}

	private void setIvBg(int id){
		good.setImageResource(R.drawable.comment_good_grey);
		medium.setImageResource(R.drawable.comment_medium_grey);
		bad.setImageResource(R.drawable.comment_bad_grey);
		switch(id){
			case R.id.comment_good:
				good.setImageResource(R.drawable.comment_good);
				grade = "1";
			break;
			case R.id.comment_medium:
				medium.setImageResource(R.drawable.comment_medium);
				grade = "2";
			break;
			case R.id.comment_bad:
				bad.setImageResource(R.drawable.comment_bad);
				grade = "3";
			break;
			default:
			break;
		}
		System.out.println("grade ---> " + grade);//
	}

	private boolean validate(){
		// if(content.getText().toString().length() == 0){
		// comment = "";
		// }else{
		// comment = content.getText().toString();
		// }
		if (grade.equals("0")){
			Toast.makeText(this, "请选中评价等级!", Toast.LENGTH_SHORT).show();
			return false;
		}else if (rb1.getRating() == 0){
			Toast.makeText(this, "请给完成速度打分!", Toast.LENGTH_SHORT).show();
			return false;
		}else if (rb2.getRating() == 0){
			Toast.makeText(this, "请给完成质量打分!", Toast.LENGTH_SHORT).show();
			return false;
		}else{
			return true;
		}
	}

	private void commentPost(String oid, String uid, String type){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		System.out.println("speed ---> " + rb1.getRating());
		System.out.println("quality ---> " + rb2.getRating());
		try{
			job.put("id", oid);
			job.put("userid", uid);
			job.put("type", type);
			job.put("grade", grade);
			job.put("speed", ((int) rb1.getRating()) + "");
			job.put("quality", ((int) rb2.getRating()) + "");
			job.put("content", content.getText().toString());

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.COMMENT_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("评价接口返回  ---> " + str);
								String message = "";
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										Toast.makeText(
												CommentActivity.this,
												"评价已提交!",
												Toast.LENGTH_SHORT).show();
										setResult(RESULT_OK);
										CommentActivity.this.finish();
//										if (result.equals("1")){
//											Toast.makeText(
//													CommentActivity.this,
//													"评价已提交!",
//													Toast.LENGTH_SHORT).show();
//											setResult(RESULT_OK);
//											CommentActivity.this.finish();
//										}else{
//											Toast.makeText(
//													CommentActivity.this,
//													message, Toast.LENGTH_SHORT)
//													.show();
//										}
									}else{
										Toast.makeText(CommentActivity.this,
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
							Toast.makeText(CommentActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}
}
