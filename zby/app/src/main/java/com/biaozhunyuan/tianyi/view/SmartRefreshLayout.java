package com.biaozhunyuan.tianyi.view;

/**
 * SmartRefreshLayout 方法汇总
 */

//public class SmartRefreshLayout {
    //下面示例中的值等于默认值
//    RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
//        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
//        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
//        refreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
//
//        refreshLayout.setHeaderHeight(100);//Header标准高度（显示下拉高度>=标准高度 触发刷新）
//        refreshLayout.setHeaderHeightPx(100);//同上-像素为单位 （V1.1.0删除）
//        refreshLayout.setFooterHeight(100);//Footer标准高度（显示上拉高度>=标准高度 触发加载）
//        refreshLayout.setFooterHeightPx(100);//同上-像素为单位 （V1.1.0删除）
//
//        refreshLayout.setFooterHeaderInsetStart(0);//设置 Header 起始位置偏移量 1.0.5
//        refreshLayout.setFooterHeaderInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）
//        refreshLayout.setFooterFooterInsetStart(0);//设置 Footer 起始位置偏移量 1.0.5
//        refreshLayout.setFooterFooterInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）
//
//        refreshLayout.setHeaderMaxDragRate(2);//最大显示下拉高度/Header标准高度
//        refreshLayout.setFooterMaxDragRate(2);//最大显示下拉高度/Footer标准高度
//        refreshLayout.setHeaderTriggerRate(1);//触发刷新距离 与 HeaderHeight 的比率1.0.4
//        refreshLayout.setFooterTriggerRate(1);//触发加载距离 与 FooterHeight 的比率1.0.4
//
//        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
//        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
//        refreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
//        refreshLayout.setEnablePureScrollMode(false);//是否启用纯滚动模式
//        refreshLayout.setEnableNestedScroll(false);//是否启用嵌套滚动
//        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
//        refreshLayout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
//        refreshLayout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
//        refreshLayout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
//        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);//是否在全部加载结束之后Footer跟随内容1.0.4
//        refreshLayout.setEnableOverScrollDrag(false);//是否启用越界拖动（仿苹果效果）1.0.4
//
//        refreshLayout.setEnableScrollContentWhenRefreshed(true);//是否在刷新完成时滚动列表显示新的内容 1.0.5
//        refreshLayout.srlEnableClipHeaderWhenFixedBehind(true);//是否剪裁Header当时样式为FixedBehind时1.0.5
//        refreshLayout.srlEnableClipFooterWhenFixedBehind(true);//是否剪裁Footer当时样式为FixedBehind时1.0.5
//
//        refreshLayout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
//        refreshLayout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//
//        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener());//设置多功能监听器
//        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider());//设置滚动边界判断
//        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter());//自定义滚动边界
//
//        refreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
//        refreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
//        refreshLayout.setRefreshContent(new View(context));//设置刷新Content（用于非xml布局代替addView）1.0.4
//
//        refreshLayout.autoRefresh();//自动刷新
//        refreshLayout.autoLoadMore();//自动加载
//        refreshLayout.autoRefresh(400);//延迟400毫秒后自动刷新
//        refreshLayout.autoLoadMore(400);//延迟400毫秒后自动加载
//        refreshLayout.finishRefresh();//结束刷新
//        refreshLayout.finishLoadMore();//结束加载
//        refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
//        refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束加载
//        refreshLayout.finishRefresh(false);//结束刷新（刷新失败）
//        refreshLayout.finishLoadMore(false);//结束加载（加载失败）
//        refreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
//        refreshLayout.closeHeaderOrFooter();//关闭正在打开状态的 Header 或者 Footer（1.1.0）
//        refreshLayout.resetNoMoreData();//恢复没有更多数据的原始状态 1.0.4（1.1.0删除）
//        refreshLayout.setNoMoreData(false);//恢复没有更多数据的原始状态 1.0.5

//}
//全局一次性设置默认属性和默认Header
//public class App extends Application {
//    static {//使用static代码段可以防止内存泄漏
//
//        //设置全局默认配置（优先级最低，会被其他设置覆盖）
//        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
//            @Override
//            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
//                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
//                layout.setReboundDuration(1000);
//                layout.setReboundInterpolator(new DropBounceInterpolator());
//                layout.setFooterHeight(100);
//                layout.setDisableContentWhenLoading(false);
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
//            }
//        });
//
//        //全局设置默认的 Header
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
//                layout.setEnableHeaderTranslationContent(false);
//                return new MaterialHeader(context).setColorSchemeResources(R.color.colorRed,R.color.colorGreen,R.color.colorBlue);
//            }
//        });
//    }
//}

//
//xml代码设置
//
//<!-- 下面示例中的值等于默认值 -->
//<com.scwang.smartrefresh.layout.SmartRefreshLayout
//        android:id="@+id/refreshLayout"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:srlAccentColor="@android:color/white"
//        app:srlPrimaryColor="@color/colorPrimary"
//        app:srlReboundDuration="300"
//        app:srlDragRate="0.5"
//
//        app:srlHeaderMaxDragRate="2"
//        app:srlFooterMaxDragRate="2"
//        app:srlHeaderTriggerRate="1"
//        app:srlFooterTriggerRate="1"
//
//        app:srlHeaderHeight="100dp"
//        app:srlFooterHeight="100dp"
//        app:srlHeaderInsetStart="0dp"
//        app:srlFooterInsetStart="0dp"
//
//        app:srlEnableRefresh="true"
//        app:srlEnableLoadMore="true"
//        app:srlEnableAutoLoadMore="true"
//        app:srlEnablePureScrollMode="false"
//        app:srlEnableNestedScrolling="false"
//        app:srlEnableOverScrollDrag="true"
//        app:srlEnableOverScrollBounce="true"
//        app:srlEnablePreviewInEditMode="true"
//        app:srlEnableScrollContentWhenLoaded="true"
//        app:srlEnableScrollContentWhenRefreshed="true"
//        app:srlEnableHeaderTranslationContent="true"
//        app:srlEnableFooterTranslationContent="true"
//        app:srlEnableLoadMoreWhenContentNotFull="false"
//        app:srlEnableFooterFollowWhenLoadFinished="false"
//
//        app:srlEnableClipHeaderWhenFixedBehind="true"
//        app:srlEnableClipFooterWhenFixedBehind="true"
//
//        app:srlDisableContentWhenRefresh="false"
//        app:srlDisableContentWhenLoading="false"
//
//        app:srlFixedFooterViewId="@+id/header_fixed"
//        app:srlFixedHeaderViewId="@+id/footer_fixed"
//        app:srlHeaderTranslationViewId="@+id/header_translation"
//        app:srlFooterTranslationViewId="@+id/footer_translation"
//        />
//<!--srlAccentColor:强调颜色-->
//<!--srlPrimaryColor:主题颜色-->
//<!--srlEnablePreviewInEditMode:是否启用Android Studio编辑xml时预览效果-->
//<!--srlFixedFooterViewId:指定一个View在内容列表滚动时固定-->
//<!--srlFixedHeaderViewId:指定一个View在内容列表滚动时固定-->
//<!--srlHeaderTranslationViewId:指定下拉Header时偏移的视图Id-->
//<!--srlFooterTranslationViewId:指定上拉Footer时偏移的视图Id-->
