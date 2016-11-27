package tools;

import android.widget.AbsListView;

public interface OnScrollListener {
	void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount);
}
