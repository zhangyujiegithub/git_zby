package com.biaozhunyuan.tianyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;


/**
 * Android pull to refresh ListView.
 */
public class PullToRefreshListView extends ListView {

    /**
     * Avoid pull all the screen height will make header view too large, so make
     * a scale ratio.
     */
    private static final int RATIO = 3;

    /**
     * 头布局
     */
    private View mHeader;
    private ImageView iv_arrow;
    private ProgressBar pb_refresh;
    private TextView tv_title;
    private TextView tv_time;

    /**
     * 脚布局
     */
    private View mFooter;
    private ImageView iv_foot_arrow;
    private ProgressBar pb_foot_refresh;
    private TextView tv_foot_title;
    private TextView tv_foot_time;

    /**
     * Height of the HeaderView
     */
    private int mHeaderHeight;
    private int mFooterHeight;

    /**
     * The Y axis position when finger onTouch down.
     */
    private int downPositionY;

    /**
     * The Y axis position when finger onTouch moving
     */
    private int currentPositionY;

    /**
     * The pull distance.
     */
    private int pullDistance;

    /**
     * Current State
     */
    private State mState;

    /**
     * Animation for the arrow when is moving clockwise.
     */
    private Animation animation;

    /**
     * Animation for the arrow when is moving anticlockwise.
     */
    private Animation reverseAnimation;

    /**
     * If can pull now
     */
    private boolean isCanPullToRefresh;


    /**
     * 是否可以加载更多
     */
    private boolean isCanLoadMore;

    private OnRefreshListener mOnRefreshListener;

    private int mTotalItemCount;
    private int mLastVisibleItem;

    /**
     * If you pull down but not release, your finger move up, this time we should play a reverse Animation.
     */
    private boolean isBack;

    private OnScrollListener mOnScrollListener;

    private boolean isAllowItemClick;

    public PullToRefreshListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mHeader = inflater.inflate(
                R.layout.control_listview_pull_to_refresh_header, null);
        mFooter = inflater.inflate(
                R.layout.control_listview_pull_to_refresh_header, null);
        iv_arrow = (ImageView) mHeader.findViewById(R.id.pull_to_refresh_image);
        pb_refresh = (ProgressBar) mHeader
                .findViewById(R.id.pull_to_refresh_progress);
        tv_title = (TextView) mHeader.findViewById(R.id.pull_to_refresh_text);
        tv_time = (TextView) mHeader
                .findViewById(R.id.pull_to_refresh_updated_at);

        iv_foot_arrow = (ImageView) mFooter.findViewById(R.id.pull_to_refresh_image);
        pb_foot_refresh = (ProgressBar) mFooter
                .findViewById(R.id.pull_to_refresh_progress);
        tv_foot_title = (TextView) mFooter.findViewById(R.id.pull_to_refresh_text);
        tv_foot_time = (TextView) mFooter
                .findViewById(R.id.pull_to_refresh_updated_at);
        measureHeaderView(mHeader);
        measureHeaderView(mFooter);

        mHeaderHeight = mHeader.getMeasuredHeight();
        mFooterHeight = mFooter.getMeasuredHeight();
        mHeader.setPadding(0, -mHeaderHeight, 0, 0);
        mFooter.setPadding(0, mFooterHeight, 0, 0);

        mHeader.invalidate();
        mFooter.invalidate();

        addHeaderView(mHeader);
        addFooterView(mFooter);

        mState = State.ORIGNAL;

        super.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Avoid override when use setOnScrollListener
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                //这里是上拉加载实现的关键，firstVisibleItem,为ListView可见的第一个Item的序号，visibleItemCount为可见Item的数量，
                //二者相加就是可见最后一个Item的序号，而totalItemCount是ListView所有的Item的数量。
                mLastVisibleItem = firstVisibleItem + visibleItemCount;
                mTotalItemCount = totalItemCount;

                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem,
                            visibleItemCount, totalItemCount);
                }

                if (firstVisibleItem == 0) {
                    isCanPullToRefresh = true;
                } else {
                    isCanPullToRefresh = false;
                }

                /**
                 * 判断最后一个可见项是否等于总的个数
                 * 如果等于证明是在最后一个位置，可以上拉加载更多。
                 * 否则不能加载更多
                 */
                if (mLastVisibleItem == mTotalItemCount) {
                    isCanLoadMore = true;
                } else {
                    isCanLoadMore = false;
                }
            }
        });

        animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(250);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());

        reverseAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);
        reverseAnimation.setInterpolator(new LinearInterpolator());
    }

    /**
     * Measure the height of the view will be when showing.
     *
     * @param view View to measure.
     */
    private void measureHeaderView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int childMeasureHeight;
        if (lp.height > 0) {
            childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height,
                    MeasureSpec.EXACTLY);
        } else {
            // Measure specification mode: The parent has not imposed any
            // constraint on the child. It can be whatever size it wants.
            childMeasureHeight = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        view.measure(childMeasureWidth, childMeasureHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y = (int) ev.getRawY();
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downPositionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                isAllowItemClick = false;
                if (!isCanPullToRefresh && !isCanLoadMore) {  //不能刷新也不能加载更多
                    break;
                }
                currentPositionY = y;

                pullDistance = (currentPositionY - downPositionY) / RATIO;

                if ((mState == State.REFRESHING) || (mState == State.LOADING_MORE)) {  //状态等于正在刷新或者正在加载更多
                    break;
                } else if (mState == State.ORIGNAL && pullDistance > 0) {
                    mState = State.PULL_TO_REFRESH;
                    changeState();
                } else if (mState == State.PULL_TO_REFRESH
                        && pullDistance > mHeaderHeight) {
                    mState = State.RELEASE_TO_REFRESH;
                    changeState();
                } else if (mState == State.RELEASE_TO_REFRESH) {
                    if (pullDistance < 0) {
                        // invisible
                        mState = State.ORIGNAL;
                        changeState();
                    } else if (pullDistance < mHeaderHeight) {
                        mState = State.PULL_TO_REFRESH;
                        isBack = true;
                        changeState();
                    }

                }

                if (mState != State.REFRESHING && pullDistance > 0) {
                    mHeader.setPadding(0, (int) (pullDistance - mHeaderHeight), 0,
                            0);
                }

                if (mState == State.ORIGNAL && pullDistance < 0) { //状态等于闲置状态并且距离小于0
                    mState = State.PULL_TO_LOADMORE;
                    changeFootState();
                } else if (mState == State.PULL_TO_LOADMORE
                        && pullDistance < -mFooterHeight) {  //状态等于上拉加载并且距离小于脚布局的高度的相反数
                    mState = State.RELEASE_TO_LOADINGMORE;
                    changeFootState();
                } else if (mState == State.RELEASE_TO_LOADINGMORE) {
                    if (pullDistance > 0) {
                        // invisible
                        mState = State.ORIGNAL;
                        changeFootState();
                    } else if (pullDistance > mFooterHeight) {
                        mState = State.RELEASE_TO_LOADINGMORE;
                        isBack = true;
                        changeFootState();
                    }

                }

                if (mState != State.LOADING_MORE && pullDistance < 0) {
                    mHeader.setPadding(0, (int) (pullDistance + mHeaderHeight), 0,
                            0);
                }

                break;
            case MotionEvent.ACTION_UP:
                isAllowItemClick = true;
                if (mState == State.REFRESHING || mState == State.LOADING_MORE) {
                    break;
                } else if (mState == State.PULL_TO_REFRESH) {
                    mState = State.ORIGNAL;
                } else if (mState == State.RELEASE_TO_REFRESH) {
                    mState = State.REFRESHING;
                } else if (mState == State.PULL_TO_LOADMORE) {
                    mState = State.ORIGNAL;
                } else if (mState == State.RELEASE_TO_LOADINGMORE) {
                    mState = State.LOADING_MORE;
                } else {
                    break;
                }
                changeState();
                changeFootState();
                break;

            default:
                break;
        }

        return super.onTouchEvent(ev);
    }


    /**
     * @param isRefresh 是否可以下拉刷新
     */
    public void setIsRefresh(boolean isRefresh) {
        if (!isRefresh) {
            removeHeaderView(mHeader);
        }

    }

    /**
     * Change the state of header view when ListView in different state.
     */
    private void changeState() {
        if (mState == State.ORIGNAL) {
            iv_arrow.setVisibility(View.VISIBLE);
            pb_refresh.setVisibility(View.GONE);
            tv_time.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);
            iv_arrow.clearAnimation();
            mHeader.setPadding(0, -mHeaderHeight, 0, 0);
        } else if (mState == State.PULL_TO_REFRESH) {
            iv_arrow.setVisibility(View.VISIBLE);
            pb_refresh.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);

            iv_arrow.clearAnimation();
            // tv_title.setText(getResources().getString(R.string.pull_refresh));
            tv_title.setText("松开刷新...");

            if (isBack) {
                // Come from release to refresh to pull to refresh
                iv_arrow.startAnimation(animation);
                isBack = false;
            }
        } else if (mState == State.RELEASE_TO_REFRESH) {
            iv_arrow.setVisibility(View.VISIBLE);
            pb_refresh.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);

            iv_arrow.clearAnimation();
            // tv_time.setText(getResources().getString(
            // R.string.release_to_refresh));

            iv_arrow.startAnimation(reverseAnimation);
        } else if (mState == State.REFRESHING) {
            iv_arrow.setVisibility(View.GONE);
            pb_refresh.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);

            iv_arrow.clearAnimation();
            tv_title.setText("正在刷新...");

            mHeader.setPadding(0, 0, 0, 0);

            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh();
            }
        }

    }


    /**
     * 改变脚布局的样式
     */
    private void changeFootState() {
        if (mState == State.ORIGNAL) {
            iv_foot_arrow.setVisibility(View.VISIBLE);
            pb_foot_refresh.setVisibility(View.GONE);
            tv_foot_time.setVisibility(View.VISIBLE);
            tv_foot_title.setVisibility(View.VISIBLE);
            iv_foot_arrow.clearAnimation();
            mFooter.setPadding(0, mHeaderHeight, 0, 0);
        } else if (mState == State.PULL_TO_LOADMORE) {
            iv_foot_arrow.setVisibility(View.VISIBLE);
            pb_foot_refresh.setVisibility(View.GONE);
            tv_foot_title.setVisibility(View.VISIBLE);
            tv_foot_time.setVisibility(View.VISIBLE);

            iv_foot_arrow.clearAnimation();
            // tv_title.setText(getResources().getString(R.string.pull_refresh));
            tv_foot_title.setText("松开加载...");

            if (isBack) {
                // Come from release to refresh to pull to refresh
                iv_foot_arrow.startAnimation(animation);
                isBack = false;
            }
        } else if (mState == State.RELEASE_TO_LOADINGMORE) {
            iv_foot_arrow.setVisibility(View.VISIBLE);
            pb_foot_refresh.setVisibility(View.GONE);
            tv_foot_title.setVisibility(View.VISIBLE);
            tv_foot_time.setVisibility(View.VISIBLE);

            iv_foot_arrow.clearAnimation();
            // tv_time.setText(getResources().getString(
            // R.string.release_to_refresh));

            iv_foot_arrow.startAnimation(reverseAnimation);
        } else if (mState == State.LOADING_MORE) {
            iv_foot_arrow.setVisibility(View.GONE);
            pb_foot_refresh.setVisibility(View.VISIBLE);
            tv_foot_title.setVisibility(View.VISIBLE);
            tv_foot_time.setVisibility(View.VISIBLE);

            iv_foot_arrow.clearAnimation();
            tv_foot_title.setText("正在加载...");

            mFooter.setPadding(0, 0, 0, 0);
        }

    }

    /**
     * When complete refresh data, you must use this method to hide the header
     * view, if not the header view will be shown all the time.
     */
    @SuppressWarnings("deprecation")
    public void onRefreshComplete() {
        mState = State.ORIGNAL;
        changeState();
        // tv_time.setText(getResources().getString(R.string.update_time)
        // + new Date().toLocaleString());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setAdapter(ListAdapter adapter) {
        // tv_time.setText(getResources().getString(R.string.update_time)
        // + new Date().toLocaleString());
        super.setAdapter(adapter);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    /**
     * Set refresh data listener.
     *
     * @param listener OnRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    /**
     * All the state of ListView
     */
    public enum State {
        ORIGNAL, PULL_TO_REFRESH, REFRESHING, RELEASE_TO_REFRESH, //下拉刷新状态
        PULL_TO_LOADMORE, LOADING_MORE, RELEASE_TO_LOADINGMORE;     //上拉加载状态
    }

    /**
     * Interface when refresh data.
     */
    public interface OnRefreshListener {
        abstract void onRefresh();
    }

    /**
     * 当上拉加载更多时的接口
     */
    public interface OnLoadMoreListener {
        abstract void onLoad();
    }

    /**
     * 是否允许Item点击
     *
     * @return
     */
    public boolean isAllowItemClick() {
        return isAllowItemClick;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {// 事件拦截
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isAllowItemClick = true;

                break;

            case MotionEvent.ACTION_MOVE:

                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

}