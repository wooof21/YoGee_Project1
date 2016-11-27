/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class SkillCertificationLvAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	private ArrayList<String> mSelectPath;
	
	private Message tMsg;
	private String text;

	/**
	 * @param context
	 * @param list
	 */
	public SkillCertificationLvAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.skill_certification_item,
					null);
		}
		final ViewHolder vHolder = new ViewHolder();
		vHolder.title = (EditText) convertView
				.findViewById(R.id.skill_certification_item_title);
		vHolder.delete = (ImageView) convertView
				.findViewById(R.id.skill_certification_item_delete);
		vHolder.pic = (ImageView) convertView
				.findViewById(R.id.skill_certification_item_pic);

		if (!list.get(position).get("title").equals("")){
			vHolder.title.setText(list.get(position).get("title"));
		}
//		if (!list.get(position).get("path").equals("")){
//			vHolder.pic
//					.setImageBitmap(compress(list.get(position).get("path")));
//		}
		
//		vHolder.pic.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				selectPic();
//				Message msg = SkillCertificationUploadActivity.instance.handler.obtainMessage();
//				msg.what = 100;
//				msg.arg1 = position;
//				msg.obj = vHolder.title.getText().toString();
//				msg.sendToTarget();
//			}
//		});
//		
//		vHolder.delete.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		vHolder.title.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				System.out.println("et clicke postion ---> " + position);
//				Message msg = SkillCertificationUploadActivity.instance.handler.obtainMessage();
//				msg.what = 100;
//				msg.obj = position;
//				msg.sendToTarget();
//			}
//		});
//		
//		tMsg = SkillCertificationUploadActivity.instance.handler.obtainMessage();
//		tMsg.what = 2;
//		vHolder.title.addTextChangedListener(new TextWatcher(){
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count){
//				// TODO Auto-generated method stub
//				text = s.toString();
//				System.out.println("text ---> " + text);
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after){
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s){
//				// TODO Auto-generated method stub
//			}
//		});
//		

		return convertView;
	}

	public String getTitle(){
		return text;
	}

	class ViewHolder{
		EditText title;
		ImageView delete;
		ImageView pic;
	}



}
