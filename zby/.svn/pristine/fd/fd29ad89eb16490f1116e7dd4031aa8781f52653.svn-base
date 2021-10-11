package com.biaozhunyuan.tianyi.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;

import static android.util.Log.i;

/***
 * 下拉刷新,底部点击可查看更多
 *
 * @author K 2015-10-20
 */
public class PullToRefreshAndLoadMoreListView extends ListView implements
        OnScrollListener {
    private final static String TAG = PullToRefreshAndLoadMoreListView.class
            .getSimpleName();
    /**
     * 下拉刷新
     */
    private final static int PULL_TO_REFRESH = 0;
    /** header的状态 */
    /**
     * 释放刷新
     */
    private final static int RELEASE_TO_REFRESH = 1;
    /**
     * 刷新中..
     */
    private final static int REFRESHING = 2;
    /**
     * 刷新完成
     */
    private final static int DONE = 3;
    public OnRefreshListener refreshListener;
    public OnLastItemVisibleListener lastItemVisibleListener;
    /**
     * 上次更新时间
     */
    private long mLastUpdateTime;
    private LayoutInflater inflater;
    private LinearLayout mHeaderView;
    private TextView mTipsText;
    private TextView mUpdateTimeText;
    private ImageView mArrowView;
    private ImageView mIvLoading;
    private RotateAnimation animRotate;
    private RotateAnimation pbarRotate;
    private RotateAnimation animReverseRotate;
    /**
     * 底部控件
     */
    private View mFooterView;
    private View mLoadAllDataFootView;
    private ImageView ivPbar;
    /**
     * 底部布局的高度
     */
    private int footerViewHeight;
    /**
     * 底部加载更多，上拉刷新显示'加载中..'，进度条可见
     */
    private TextView tvInfo;
    private ProgressBar pBar;
    /**
     * 是否正在加载中
     */
    public boolean mIsLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isRecored;
    /**
     * 默认paddingTop为 header高度的负值，使header在屏幕外不可见
     **/
    private int mHeaderViewPaddingTop;
    /**
     * header布局xml文件原始定义的paddingTop
     */
    private int mHeaderOrgPaddingTop;
    /**
     * 搜索内容
     */
    private String mHintSearch;
    private GestureDetector gestureDetector;
    private int mPullState;
    private boolean lastItemVisible;
    private boolean isCanRefresh = true;
    private boolean isCanLoadMore = true;

    /**
     * 第一个Item是否可见
     */
    private boolean isFirstItemVisible;
    private OnTouchEventListener onTouched = new OnTouchEventListener() {
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                    // case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (isRecored) {
                        requestDisallowInterceptTouchEvent(false);
                        if (mPullState != REFRESHING) {
                            if (mPullState == PULL_TO_REFRESH) {
                                mPullState = DONE;
                                changeHeaderViewByState(mPullState);

                                // mSearchView.setVisibility(View.VISIBLE);
                            } else if (mPullState == RELEASE_TO_REFRESH) {
                                mPullState = REFRESHING;
                                changeHeaderViewByState(mPullState);
                                onRefresh();
                            }
                        }
                        isRecored = false;
                        return true;
                    }
                    break;
            }
            return gestureDetector.onTouchEvent(event);
        }
    };
    /**
     * 自定义手势探测器
     */
    private OnGestureListener gestureListener = new OnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (isCanRefresh) {
                int deltaY = (int) (e1.getY() - e2.getY());
                if (mPullState != REFRESHING) {
                    // 第一个可见，且手势下拉
                    if (!isRecored && isFirstItemVisible && deltaY < 0) {
                        isRecored = true;
                        requestDisallowInterceptTouchEvent(true);
                        changeHeaderViewByState(PULL_TO_REFRESH); // TODO kxj
                    }
                    if (isRecored) {
                        int paddingTop = mHeaderView.getPaddingTop();
                        // 释放刷新的过程
                        if (paddingTop < 0 && paddingTop > mHeaderViewPaddingTop) {
                            if (mPullState == RELEASE_TO_REFRESH) {
                                changeHeaderViewByState(PULL_TO_REFRESH);
                            }
                            mPullState = PULL_TO_REFRESH;
                        } else if (paddingTop >= 0) {
                            if (mPullState == PULL_TO_REFRESH) {
                                changeHeaderViewByState(RELEASE_TO_REFRESH);
                            }
                            mPullState = RELEASE_TO_REFRESH;
                        }

                        // 根据手指滑动状态动态改变header高度
                        int topPadding = (int) (mHeaderViewPaddingTop - deltaY / 2);
                        mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
                                topPadding, mHeaderView.getPaddingRight(),
                                mHeaderView.getPaddingBottom());
                        mHeaderView.invalidate();
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }
    };

    public PullToRefreshAndLoadMoreListView(Context context) {
        this(context, null);
    }

    public PullToRefreshAndLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
        setSelector(R.drawable.list_view_gary_selector);
    }

    private void init(Context context) {
        mLastUpdateTime = System.currentTimeMillis();

        initArrowAnimation();

        initProgressAnimation();
        initPullHeader(context);
        initFooterView(context);

        // 为自定义ListView控件绑定滚动监听事件
        setOnScrollListener(this);
        gestureDetector = new GestureDetector(context, gestureListener);
    }

    /***
     * 实例化下拉ListView的Header布局
     *
     * @param context
     */
    private void initPullHeader(Context context) {
        inflater = LayoutInflater.from(context);
        mHeaderView = (LinearLayout) inflater.inflate(
                R.layout.pull_to_refresh_head, null);
        mArrowView = (ImageView) mHeaderView
                .findViewById(R.id.head_arrowImageView);
        mIvLoading = (ImageView) mHeaderView
                .findViewById(R.id.head_progressBar);
        mTipsText = (TextView) mHeaderView.findViewById(R.id.head_tipsTextView);
        mUpdateTimeText = (TextView) mHeaderView
                .findViewById(R.id.head_updatetimeTextView);

        mHeaderOrgPaddingTop = mHeaderView.getPaddingTop();
        measureView(mHeaderView);
        mHeaderViewPaddingTop = -mHeaderView.getMeasuredHeight();
        setHeaderPaddingTop(mHeaderViewPaddingTop);
        mHeaderView.invalidate();
        addHeaderView(mHeaderView, null, false);
    }

    /***
     * 初始化控件，添加底部控件
     *
     * @param context
     */
    private void initFooterView(Context context) {

        mFooterView = View.inflate(getContext(), R.layout.foot_custom_listview, null);
        mLoadAllDataFootView = View.inflate(getContext(), R.layout.foot_load_all_data, null);
        ivPbar = (ImageView) mFooterView.findViewById(R.id.footer_progressBar);
        mFooterView.measure(0, 0);
        footerViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -footerViewHeight, 0, 0);
        addFooterView(mFooterView, null, false);
    }

    /**
     * 加载数据完毕
     */
    public void loadCompleted() {
        mIsLoading = false;
        ivPbar.clearAnimation();
        mFooterView.setPadding(0, -footerViewHeight, 0, 0);
    }


    /**
     * 已经加载了全部数据
     */
    public void loadAllData() {
//        Toast.makeText(getContext(), "已经加载了全部数据", Toast.LENGTH_SHORT).show();
        if (getFooterViewsCount() == 1) {
            addFooterView(mLoadAllDataFootView, null, false);
        }
    }

    private void setHeaderPaddingTop(int paddingTop) {
        mHeaderView.setPadding(mHeaderView.getPaddingLeft(), paddingTop,
                mHeaderView.getPaddingRight(), mHeaderView.getPaddingBottom());
    }

    /**
     * 实例化下拉箭头动画
     */
    private void initArrowAnimation() {
        // 定义一个旋转角度为0 到-180度的动画，时长100ms
        animRotate = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animRotate.setInterpolator(new LinearInterpolator());
        animRotate.setDuration(100);
        animRotate.setFillAfter(true);

        animReverseRotate = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animReverseRotate.setInterpolator(new LinearInterpolator());
        animReverseRotate.setDuration(100);
        animReverseRotate.setFillAfter(true);
    }


    /**
     * 是否可以下拉刷新
     *
     * @param isCanRefresh
     */
    public void setIsCanRefresh(boolean isCanRefresh) {
        this.isCanRefresh = isCanRefresh;
    }

    /**
     * 是否可以上拉加载
     *
     * @param isCanLoadMore
     */
    public void setIsCanLoadMore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;
    }

    /**
     * 加载等待进度条
     */
    private void initProgressAnimation() {
        pbarRotate = new RotateAnimation(0, 359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        pbarRotate.setInterpolator(new LinearInterpolator());
        pbarRotate.setDuration(400);
        pbarRotate.setRepeatCount(-1);
    }

    public void onScroll(AbsListView view, int firstVisiableItem,
                         int visibleItemCount, int totalItemCount) {
        if (isCanLoadMore) {
            isFirstItemVisible = firstVisiableItem == 0 ? true : false;

            boolean loadMore = firstVisiableItem + visibleItemCount >= totalItemCount;

            if (loadMore) {
                if (mPullState != REFRESHING && lastItemVisible == false
                        && lastItemVisibleListener != null) {
                    lastItemVisible = true;
                    // including Header View,here using totalItemCount - 2
                    lastItemVisibleListener.onLastItemVisible(totalItemCount - 2);
                }
            } else {
                lastItemVisible = false;
            }
        }

        // 减去header 和footer个数
//        if ((totalItemCount - 2) > visibleItemCount) {
//            // TODO 底部加载更多按钮
//            mFooterView.setVisibility(VISIBLE);
//        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // OnScrollListener.SCROLL_STATE_FLING :手指离开屏幕甩动中
        // OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:手指正在屏幕上滑动中
        // OnScrollListener.SCROLL_STATE_IDLE: 闲置的，未滑动
        i("onScroll", "onScrollStateChanged");
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1) && !mIsLoading) {
            mIsLoading = true;

            mFooterView.setPadding(0, 0, 0, 0);//显示出footerView
            setSelection(getCount());//让listview最后一条显示出来，在页面完全显示出底布局

            ivPbar.startAnimation(pbarRotate);
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (onTouched.onTouchEvent(event)) {
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 改变刷新状态时，调用该方法来改变headerView 显示的内容
     *
     * @param state 刷新状态
     */
    private void changeHeaderViewByState(int state) {
        switch (state) {
            case RELEASE_TO_REFRESH:
                mIvLoading.clearAnimation();
                mIvLoading.setVisibility(View.GONE);
                mTipsText.setVisibility(View.VISIBLE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.startAnimation(animRotate);
                mTipsText.setText("释放立即刷新");
                break;
            case PULL_TO_REFRESH:
                mIvLoading.clearAnimation();
                mIvLoading.setVisibility(View.GONE);

                mTipsText.setVisibility(View.VISIBLE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.startAnimation(animReverseRotate);
                mTipsText.setText("下拉刷新");

                mUpdateTimeText.setText("更新于："
                        + DateTimeUtil.convertTimeToFormat(mLastUpdateTime));
                break;
            case REFRESHING:
                // 设置paddingTop为原始paddingTop
                setHeaderPaddingTop(mHeaderOrgPaddingTop);
                // 设置header布局为不可点击，进度条转圈中..
                mHeaderView.invalidate();

                mArrowView.clearAnimation();
                mArrowView.setVisibility(View.GONE);

                mIvLoading.setVisibility(View.VISIBLE);
                mIvLoading.clearAnimation();
                mIvLoading.startAnimation(pbarRotate);
                mTipsText.setText("正在刷新");

                break;
            case DONE:
                // 设置header消失动画
                if (mHeaderViewPaddingTop - 1 < mHeaderView.getPaddingTop()) {
                    ResetAnimimation animation = new ResetAnimimation(mHeaderView,
                            mHeaderViewPaddingTop, false);
                    animation.setDuration(300);
                    mHeaderView.startAnimation(animation);
                }

                mIvLoading.clearAnimation();
                mIvLoading.setVisibility(View.GONE);
                mArrowView.setVisibility(View.VISIBLE);
                mArrowView.clearAnimation();
                mArrowView.setImageResource(R.drawable.ic_pulltorefresh_arrow);

                mTipsText.setText("下拉刷新");
                setSelection(0); // listview显示到第一个Item
                break;
        }
    }

    /**
     * 调用ListView下拉刷新
     */
    public void startRefresh() {
        setSelection(0);
        mPullState = REFRESHING;
        changeHeaderViewByState(mPullState);
//        onRefresh();
    }

    /**
     * 监听下拉刷新
     *
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /***
     * 滑动到底部监听
     *
     * @param listener
     */
    public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        this.lastItemVisibleListener = listener;
    }

    /***
     * 滑动到底部监听
     *
     * @param
     */
    public void setOnLoadMore(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 下拉刷新完成
     */
    public void onRefreshComplete() {
        mPullState = DONE;
        changeHeaderViewByState(mPullState);
        Logger.i("updateTime："
                + DateTimeUtil.convertTimeToFormat(mLastUpdateTime));
        // 记录刷新时间
        mLastUpdateTime = System.currentTimeMillis();
    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
        if (getFooterViewsCount() == 2) {
            removeFooterView(mLoadAllDataFootView);
        }
    }

    /***
     * 计算headView的width及height值
     *
     * @param child
     *            计算控件对象
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 当listview滑动到达底部时被回调
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public interface OnLastItemVisibleListener {
        public void onLastItemVisible(int lastIndex);
    }

    private interface OnTouchEventListener {
        public boolean onTouchEvent(MotionEvent ev);
    }

    /**
     * 消失动画
     */
    public class ResetAnimimation extends Animation {
        private int targetHeight;
        private int originalHeight;
        private int extraHeight;
        private View view;
        private boolean down;
        private int viewPaddingBottom;
        private int viewPaddingRight;
        private int viewPaddingLeft;

        protected ResetAnimimation(View view, int targetHeight, boolean down) {
            this.view = view;
            this.viewPaddingLeft = view.getPaddingLeft();
            this.viewPaddingRight = view.getPaddingRight();
            this.viewPaddingBottom = view.getPaddingBottom();
            this.targetHeight = targetHeight;
            this.down = down;
            originalHeight = view.getPaddingTop();
            extraHeight = this.targetHeight - originalHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {

            int newHeight;
            newHeight = (int) (targetHeight - extraHeight
                    * (1 - interpolatedTime));
            view.setPadding(viewPaddingLeft, newHeight, viewPaddingRight,
                    viewPaddingBottom);
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }
    }

}
