/**
 * 
 * @param
 */
package popup;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.ChangeUserInfoActivity;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;

import adapter.PopUpOccupationLvAdapter;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class EducationPopUpWindow extends PopupWindow {

	private Context context;
	private String uid, sex, occupation, birthday, city, name;
	private ArrayList<HashMap<String, String>> list;
	private String[] occu = { "博士", "硕士", "本科", "大专", "高中", "中专", "初中", "小学" };
	private PopUpOccupationLvAdapter adapter;

	/**
	 * 简历中学历类型
	 */
	public EducationPopUpWindow(Context context, View parent,String educationText) {
		View view = View.inflate(context, R.layout.popup_occupation, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.context = context;
		this.occupation = "";

		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0, j = occu.length; i < j; i++) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("title", occu[i]);
			if (educationText.equals(occu[i])) {
				hashMap.put("select", "1");
			} else {
				hashMap.put("select", "0");
			}

			list.add(hashMap);
		}

		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_occupation_dismiss);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_occupation_commit);
		ListView lv = (ListView) view.findViewById(R.id.popup_occupation_lv);
		LinearLayout l = (LinearLayout) view.findViewById(R.id.l);
		//l.setVisibility(View.GONE);

		adapter = new PopUpOccupationLvAdapter(context, list);
		lv.setAdapter(adapter);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("position ---> " + position);
				for (int i = 0, j = list.size(); i < j; i++) {
					list.get(i).put("select", "0");
				}
				EducationPopUpWindow.this.occupation = list.get(position).get(
						"title");
				list.get(position).put("select", "1");
				adapter.notifyDataSetChanged();
				Message msg = ChangeUserResumeActivity.instance.mHandler
						.obtainMessage();
				msg.what = 4;
				msg.obj = EducationPopUpWindow.this.occupation;
				msg.sendToTarget();
				dismiss();

			}
		});
		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}
}
