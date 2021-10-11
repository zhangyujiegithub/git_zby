package com.biaozhunyuan.tianyi.common.attach;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.base.BasePopupWindowForListView;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.model.ImageFloder;

import java.util.List;

public class ListImageDirPopupWindow extends
		BasePopupWindowForListView<ImageFloder> {
	private ListView mListDir;

	public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView) {
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews() {
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		CommanAdapter<ImageFloder> adapter = new CommanAdapter<ImageFloder>(
				mDatas, context, R.layout.list_dir_item) {
			@Override
			public void convert(int position, ImageFloder item,
					BoeryunViewHolder viewHolder) {
				TextView tvName = viewHolder.getView(R.id.id_dir_item_name);
				TextView tvCount = viewHolder.getView(R.id.id_dir_item_count);
				viewHolder.setImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				String dirLastName = item.getDir();
				if (!TextUtils.isEmpty(dirLastName)) {
					dirLastName = dirLastName.substring(dirLastName
							.lastIndexOf("/") + 1);
				}
				tvName.setText("" + dirLastName);
				tvCount.setText(item.getCount() + "张");
			}
		};

		mListDir.setAdapter(adapter);
	}

	public interface OnImageDirSelected {
		void selected(ImageFloder floder);
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents() {
		mListDir.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

				if (mImageDirSelected != null) {
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params) {
		// TODO Auto-generated method stub
	}

}
