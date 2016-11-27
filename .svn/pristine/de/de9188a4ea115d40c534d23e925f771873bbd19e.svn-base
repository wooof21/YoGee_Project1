package adapter;

import java.util.ArrayList;

import model.RealName;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

public class RealNameIdentifyAdapter extends BaseAdapter {

	private Context context;
	public static ArrayList<RealName> list;
	private LayoutInflater lInflater;

	/**
	 * @param context
	 * @param list
	 */
	public RealNameIdentifyAdapter(Context context, ArrayList<RealName> list) {
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.real_name_identify,
					null);
			viewHolder.certification_img = (ImageView) convertView
					.findViewById(R.id.certification_img);
			viewHolder.certification_imgs = (ImageView) convertView
					.findViewById(R.id.certification_imgs);
			viewHolder.certification_name = (TextView) convertView
					.findViewById(R.id.certification_name);
			viewHolder.certification_istrue = (TextView) convertView
					.findViewById(R.id.certification_istrue);
			viewHolder.certification_updateDate = (TextView) convertView
					.findViewById(R.id.certification_updateDate);
			viewHolder.certification_imgcount = (TextView) convertView
					.findViewById(R.id.certification_imgcount);
			viewHolder.parent = (LinearLayout) convertView
					.findViewById(R.id.parent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if ("1".equals(list.get(position).getType())) {
			if (!TextUtils.isEmpty(list.get(position).getImg())) {
				ImageLoader.getInstance().displayImage(
						list.get(position).getImg(),
						viewHolder.certification_img);
			} else {
				viewHolder.certification_img
						.setImageResource(R.drawable.certification_img);
			}
			viewHolder.certification_img
					.setScaleType(ImageView.ScaleType.CENTER_CROP);
			viewHolder.certification_name.setText("实名认证");
			String state = "";
			if ("0".equals(list.get(position).getState())) {
				state = "未认证";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.GONE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.binding_text_color));
			} else if ("1".equals(list.get(position).getState())) {
				state = "审核中";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.sex_text_color));
			} else if ("2".equals(list.get(position).getState())) {
				state = "审核通过";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources()
						.getDrawable(R.drawable.realname_success));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.sex_text_color));
			} else if ("3".equals(list.get(position).getState())) {
				state = "审核失败";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.binding_text_color));
			}
			viewHolder.certification_istrue.setText(state);
		} else if ("2".equals(list.get(position).getType())) {
			viewHolder.certification_img
					.setImageResource(R.drawable.sesame_credit);
			viewHolder.certification_img
					.setScaleType(ImageView.ScaleType.FIT_XY);
			viewHolder.certification_name.setText("芝麻信用认证");
			String state = "";
			if ("0".equals(list.get(position).getState())) {
				state = "未开通";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.GONE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.binding_text_color));
			} else if ("1".equals(list.get(position).getState())) {
				state = "审核中";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.sex_text_color));
			} else if ("2".equals(list.get(position).getState())) {
				state = "审核通过";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources()
						.getDrawable(R.drawable.realname_success));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.sex_text_color));
			} else if ("3".equals(list.get(position).getState())) {
				state = "审核失败";
				viewHolder.certification_imgs.setImageDrawable(context
						.getResources().getDrawable(R.drawable.realname_error));
				// viewHolder.certification_imgcount.setVisibility(View.VISIBLE);
				viewHolder.certification_istrue.setTextColor(context
						.getResources().getColor(R.color.binding_text_color));
			}
			viewHolder.certification_istrue.setText(state);
		}

		if (!TextUtils.isEmpty(list.get(position).getUpdateDate())) {
			viewHolder.certification_updateDate.setVisibility(View.VISIBLE);
			viewHolder.certification_updateDate.setText(list.get(position)
					.getUpdateDate());
		}
		return convertView;
	}

	class ViewHolder {
		private ImageView certification_img, certification_imgs;
		private TextView certification_name, certification_istrue,
				certification_updateDate, certification_imgcount;
		private LinearLayout parent;
	}

}
