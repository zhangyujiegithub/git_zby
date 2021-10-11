package com.biaozhunyuan.tianyi.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.helper.Logger;

/**
 * Created by k on 2016/12/9.
 */

public abstract class BaseFragment extends Fragment {
    /***/
    public String TAG = getClass().getSimpleName();
    public final String INFO_ERRO_SERVER = "网络不给力，请稍后再试";

    private Toast toast;

    /**
     * 标记页面是否已经启动
     */
    private boolean mHasStart;

    /**
     * 标记页面是否初次加载
     */
    private boolean mHasFirstLoad = true;

    @Override
    public void onStart() {
        super.onStart();
        mHasStart = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("setUserVisibleHint_OnResume" + "onResume =" + mHasStart + "----" + getUserVisibleHint() + "--" + mHasFirstLoad);
        if (mHasStart) {
            if (mHasFirstLoad && getUserVisibleHint()) {
                mHasFirstLoad = false;
                //当初次对用户可见时加载布局
                loadUserVisibleData();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.i("setUserVisibleHint" + TAG + "----" + isVisibleToUser + mHasStart + "----" + getUserVisibleHint() + "--" + mHasFirstLoad);
        if (mHasStart) {
            if (mHasFirstLoad && isVisibleToUser) {
                mHasFirstLoad = false;
                //当初次对用户可见时加载布局
                loadUserVisibleData();
            }
        }
    }

    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(info);
            toast.show();
        }
    }

    /**
     * 填充数据到布局中。预加载，当Fragment可见时调用该方法
     */
    public abstract void loadUserVisibleData();
}
