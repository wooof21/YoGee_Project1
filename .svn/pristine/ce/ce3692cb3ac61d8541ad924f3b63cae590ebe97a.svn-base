/**
 * 
 * @param
 */
package adapter;

import ivzoom.ImagePagerActivity;

import java.util.ArrayList;

import popup.GalleyPopUpWindow;
import tools.Tools;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.ImageZoomActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @param
 */
public class MainGrabGvInLvAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<String> list;
	private View parent1;
	private LayoutInflater lInflater;
	private int p;
	/**
	 * @param context
	 * @param list
	 */
	public MainGrabGvInLvAdapter(Context context, ArrayList<String> list, View parent){
		super();
		this.context = context;
		this.list = list;
		this.parent1 = parent;
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
		return position;//
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.my_order_main_gv_item,
					null);
		}
		ImageView iv = (ImageView) convertView
				.findViewById(R.id.my_grab_order_gv_item_iv);

		ImageLoader.getInstance().displayImage(list.get(position), iv);
		
		p = position;
		
		iv.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				imageBrower(p, list);
			}
		});
		
//		iv.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				System.out.println("1111111");
////				Intent intent = new Intent(context, ImageZoomActivity.class);
////				intent.putExtra("list", list);
////				context.startActivity(intent);
//				new GalleyPopUpWindow(context, parent1, list);
//			}
//		});

		return convertView;
	}
	
	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2){
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

}
