/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

import view.RoundImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 
 *@param
 */
public class CommentListItemAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	
	/**
	 * @param context
	 * @param list
	 */
	public CommentListItemAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.comment_list_item, null);
		}
		ViewHolder vHolder = new ViewHolder();
		vHolder.head = (RoundImageView) convertView.findViewById(R.id.comment_list_item_head);
		vHolder.name = (TextView) convertView.findViewById(R.id.comment_list_item_name);
		vHolder.grade = (ImageView) convertView.findViewById(R.id.comment_list_item_grade);
		vHolder.text = (TextView) convertView.findViewById(R.id.comment_list_item_text);
		vHolder.rb1 = (RatingBar) convertView.findViewById(R.id.comment_list_item_rb1);
		vHolder.rb2 = (RatingBar) convertView.findViewById(R.id.comment_list_item_rb2);
		vHolder.content = (TextView) convertView.findViewById(R.id.comment_list_item_content);
		
		ImageLoader.getInstance().displayImage(list.get(position).get("img"), vHolder.head);
		vHolder.name.setText(list.get(position).get("username"));
		if(list.get(position).get("grade").equals("1")){
			vHolder.grade.setImageResource(R.drawable.comment_good);
			vHolder.text.setText("好评");
		}else if(list.get(position).get("grade").equals("2")){
			vHolder.grade.setImageResource(R.drawable.comment_medium);
			vHolder.text.setText("中评");
		}else{
			vHolder.grade.setImageResource(R.drawable.comment_bad);
			vHolder.text.setText("差评");
		}
		
		float rate1 = Float.valueOf(list.get(position).get("speed"));
		float rate2 = Float.valueOf(list.get(position).get("quality"));
		vHolder.rb1.setRating(rate1);
		vHolder.rb2.setRating(rate2);
		vHolder.content.setText(list.get(position).get("content"));
		
		return convertView;
	}

	class ViewHolder{
		RoundImageView head;
		TextView name;
		ImageView grade;
		TextView text;
		RatingBar rb1, rb2;
		TextView content;
	}
}
