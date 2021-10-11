package com.biaozhunyuan.tianyi.supportAndComment;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.Indicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangAnMin on 2018/3/5.
 * 通用的点赞、评论列表
 *  dataType:员工日志,联系记录,任务计划,公告通知
 */

public class CommonFragment extends Fragment {

    private Indicator tabView;
    private AutoMaxHeightViewpager viewpager;
    private SupportListPost commentPost;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private int supportCount = 0;
    private int commentCount = 0;

    private FragmentManager fragmentManager;
    private SupportListFragment supportListFragment;
    private CommentListFragment commentListFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            commentPost = (SupportListPost) getArguments().getSerializable("SupportListPost");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_support, null);
        initViews(view);
        setCount(0, 0);
        return view;
    }


    public static CommonFragment newInstance(SupportListPost post) {
        CommonFragment fragment = new CommonFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("SupportListPost", post);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initViews(View view) {
        tabView = view.findViewById(R.id.incator_title_fragment);
        viewpager = view.findViewById(R.id.page_comment_info);

        supportListFragment = SupportListFragment.newInstance(commentPost);
        supportListFragment.setViewpager(viewpager);
        mFragments.add(supportListFragment);

        commentListFragment = CommentListFragment.newInstance(commentPost);
        commentListFragment.setViewpager(viewpager);
        mFragments.add(commentListFragment);

        pagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        viewpager.setAdapter(pagerAdapter);
        tabView.setRelateViewPager(viewpager);


        supportListFragment.setOnSupportCountListener(new SupportListFragment.SupportCountListener() {
            @Override
            public void getcount(int count) {
                supportCount = count;
                setCount(supportCount, commentCount);
            }
        });


        commentListFragment.setOnCommentCountListener(new CommentListFragment.CommentCountListener() {
            @Override
            public void getcount(int count) {
                commentCount = count;
                setCount(supportCount, commentCount);
            }
        });


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewpager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * 设置点赞和评论的个数
     *
     * @param supportCount 点赞的个数
     * @param commentCount 评论的个数
     */
    private void setCount(int supportCount, int commentCount) {
        List<String> list = new ArrayList<>();
        list.add(new String("点赞(" + supportCount + ")"));
        list.add(new String("评论(" + commentCount + ")"));

        tabView.setTabItemTitles(list);
    }


    /**
     * 重新加载点赞列表页面
     */
    public void reloadSupport() {
        supportListFragment.reload();
    }


    /**
     * 重新加载评论列表页面
     */
    public void reloadComment() {
        commentListFragment.reload();
    }
}
