/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;

import model.GrabOrderModel;
import view.ListViewForGrab;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;

import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class MainGrabLvOtherAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<GrabOrderModel> list;
	private View parent;
	private LayoutInflater lInflater;
	private View view1;
	private Handler handler;

	private PopupWindow popup;
	private final int TYPE_ONE = 0, TYPE_TWO = 1;
	private MainGrabLvItemAdapter cmAdapter;

	/**
	 * @param context
	 * @param list
	 */
	public MainGrabLvOtherAdapter(Context context,
			ArrayList<GrabOrderModel> list, View parent, View view1, Handler handler) {
		super();
		this.context = context;
		this.list = list;
		this.parent = parent;
		this.view1 = view1;
		this.handler = handler;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return TYPE_ONE;
		} else {
			return TYPE_TWO;
		}

	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return view1;
		} else {
			return list;
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int type = getItemViewType(position);
		ViewHolder1 vHolder1 = new ViewHolder1();
		ViewHolder2 vHolder2 = new ViewHolder2();
		if (convertView == null) {
			if (type == TYPE_ONE) {
				convertView = view1;
				convertView.setTag(vHolder1);
			} else {
				convertView = lInflater
						.inflate(R.layout.grablv_mall_item, null);
				vHolder2.lv = (ListViewForGrab) convertView
						.findViewById(R.id.grabLv_mall_list);
				// cmAdapter = new MainGrabLvItemAdapter(context, list, parent);
				// vHolder2.lv.setAdapter(cmAdapter);
				convertView.setTag(vHolder2);
			}
		} else {
			if (type == TYPE_ONE) {
				vHolder1 = (ViewHolder1) convertView.getTag();
			} else {
				vHolder2 = (ViewHolder2) convertView.getTag();
			}

		}
		if (type == TYPE_ONE) {
			
		} else {
			cmAdapter = new MainGrabLvItemAdapter(context, list, parent);
			vHolder2.lv.setAdapter(cmAdapter);
		}
		return convertView;
	}

	// /**
	// *
	// * @param
	// */
	// @Override
	// public void notifyDataSetChanged() {
	// // TODO Auto-generated method stub
	// super.notifyDataSetChanged();
	// if (mAdapter != null) {
	// mAdapter.notifyDataSetChanged();
	// }
	//
	// }

	class ViewHolder2 {
		ListViewForGrab lv;
	}

	class ViewHolder1 {
		View view1;
	}

}