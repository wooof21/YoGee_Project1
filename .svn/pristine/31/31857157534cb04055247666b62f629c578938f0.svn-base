/**
 * 
 * @param
 */
package fragment;

import java.io.UnsupportedEncodingException;

import login.Login;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.OrderDetailActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class OrderDetailToGrabFragment extends Fragment{

	public static OrderDetailToGrabFragment instance;
	private String id, uid, rid;

	/**
	 * 
	 * @param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_to_grab, null);

		TextView grab = (TextView) view
				.findViewById(R.id.fragment_to_grab_grab);

		instance = this;

		id = getArguments().getString("id");
		uid = getArguments().getString("uid");
		rid = getArguments().getString("rid");
		grab.setOnClickListener(new OnClickListener(){//

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (new Tools().getUserId(getActivity()).equals(rid)){
						Toast.makeText(getActivity(), "不能抢自己发的单!", Toast.LENGTH_SHORT).show();
				}else{
					new ContentPopUpWindow(getActivity(),
							OrderDetailActivity.instance.parent, "确认要抢此订单么?~",
							"grab");
				}
			}
		});
		return view;
	}
	
	/**
	 * 包含Activity、Fragment或View的应用 1. MobclickAgent.onResume()
	 * 和MobclickAgent.onPause() 方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能) 2.
	 * MobclickAgent.onPageStart() 和 MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
	 * 
	 * 在仅有Activity的程序中，SDK 自动帮助开发者调用了 2. 中的方法，并把Activity
	 * 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
	 * 首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)
	 * 禁止默认的页面统计方式，这样将不会再自动统计Activity。
	 * 
	 * @param
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("OrderDetailToGrabFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause(){
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("OrderDetailToGrabFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		super.onPause();
	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			grabCommitHttpClient(id, uid);
		}

	};

	private void grabCommitHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		JSONObject job = new JSONObject();
		try{
			job.put("id", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString(), "utf-8");

			HttpClient.post(getActivity(), Config.USER_GRAB_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("抢单提交结果  ---> " + str);
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										if (result.equals("1")){
											Toast.makeText(getActivity(),
													"抢单成功!", Toast.LENGTH_SHORT)
													.show();
											getActivity().finish();
										}else{
											Toast.makeText(getActivity(),
													message,
													Toast.LENGTH_SHORT).show();
										}
									}else{
										//
										Toast.makeText(getActivity(), message,
												Toast.LENGTH_SHORT).show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(getActivity(), message,
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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
