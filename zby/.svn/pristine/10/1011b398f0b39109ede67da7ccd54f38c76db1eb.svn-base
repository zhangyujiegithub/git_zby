package com.biaozhunyuan.tianyi.models;


import android.text.TextUtils;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;
import com.biaozhunyuan.tianyi.newuihome.FunctionName;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class MenuFunction {
    List<FunctionName> listName = new ArrayList<>();
    /**
     * OA默认功能点
     */
    private EnumFunctionPoint[] defultOAPoints = new EnumFunctionPoint[]{
    };

    /**
     * CRM默认功能点
     */
    private final EnumFunctionPoint[]
            defultCRMPoints = new EnumFunctionPoint[]{
    };

    private ArrayList<MenuChildItem> mGridItems;


    public MenuFunction() {
        super();
        mGridItems = new ArrayList<MenuChildItem>();
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_attendance_new, "考勤",
                EnumFunctionPoint.ATTANCE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_apply_new, "申请",
                EnumFunctionPoint.APPLY));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_log, "日志",
                EnumFunctionPoint.LOG));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_task_new, "任务",
                EnumFunctionPoint.TASK));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_notice_new, "通知",
                EnumFunctionPoint.NOTICE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_costomer_new, "客户",
                EnumFunctionPoint.CLIENT));
        mGridItems.add(new MenuChildItem(R.drawable.icon_menu_business, "微信记录",
                EnumFunctionPoint.WE_CHAT_RECORD));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_addressbook_new,
                "内通讯录", EnumFunctionPoint.INSIDE_COMMUNICATION));
        mGridItems.add(new MenuChildItem(R.drawable.icon_new_chanpin, "产品",
                EnumFunctionPoint.PRODUCT));
        mGridItems.add(new MenuChildItem(R.drawable.icon_new_yuyue, "预约",
                EnumFunctionPoint.CHANGHUI_BESPOKE_LIST));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_plan_new, "单据",
                EnumFunctionPoint.INVOICES));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_space_new, "工单",
                EnumFunctionPoint.WORKORDER));
        mGridItems.add(new MenuChildItem(R.drawable.icon_menu_project, "项目",
                EnumFunctionPoint.CRMPROJECT));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_attendance_new, "项目打卡",
                EnumFunctionPoint.PROJECT_ATTENDANCE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_menu_business, "商机",
                EnumFunctionPoint.CRMBUSINESS));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_genjinjilu_new, "课程中心",
                EnumFunctionPoint.CURRICULUM));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_space_new, "考试中心",
                EnumFunctionPoint.EXAMINATION));
        mGridItems.add(new MenuChildItem(R.drawable.icon_menu_business, "市场活动",
                EnumFunctionPoint.ACTIVITY));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_genjinjilu_new, Global.CONTACT_TITLE,
                EnumFunctionPoint.CONTACTS));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_space_new, "空间",
                EnumFunctionPoint.SPACE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_space_new, "资讯",
                EnumFunctionPoint.INFORMATION));
        mGridItems.add(new MenuChildItem(R.drawable.icon_message_select,
                "聊天", EnumFunctionPoint.CHAT));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_costomer_new, "收文",
                EnumFunctionPoint.INCOMING));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_costomer_new, "发文",
                EnumFunctionPoint.DISPATCH));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_plan_new, "报销",
                EnumFunctionPoint.APPLYFOR_INVOICE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_plan_new, "线索",
                EnumFunctionPoint.CRMCLUE));
        mGridItems.add(new MenuChildItem(R.drawable.icon_home_genjinjilu_new, "BUG管理",
                EnumFunctionPoint.BUG));

    }


    /**
     * 首次默认排序
     */
    public ArrayList<MenuChildItem> getFunctions(List<EnumFunctionPoint> points, boolean isFirst) {

        ArrayList<MenuChildItem> menuChildItems = new ArrayList<MenuChildItem>();
        for (MenuChildItem item : mGridItems) {
            for (EnumFunctionPoint point : points) {
                if (item.ponit.getValue() == point.getValue()) {
                    menuChildItems.add(item);
                    break;
                }
            }
        }
        return menuChildItems;
    }

    /**
     * 首次默认排序
     */
    public List<FunctionBoard> getFunctions(List<FunctionBoard> points) {
        List<FunctionBoard> list = new ArrayList<>();
        for (MenuChildItem item : mGridItems) {
            for (FunctionBoard point : points) {
                if (item.ponit.getValue() == EnumFunctionPoint.TASK.getValue()) {
                    if (point.getFunction().equals("任务")) {
                        point.setIco(item.ico);
                        list.add(point);
                        break;
                    }
                } else if (item.ponit.getValue() == EnumFunctionPoint.NOTICE.getValue()) {
                    if (point.getFunction().equals("通知")) {
                        point.setIco(item.ico);
                        list.add(point);
                        break;
                    }
                } else if (item.ponit.getValue() == EnumFunctionPoint.APPLY.getValue()) {
                    if (point.getFunction().equals("申请")) {
                        point.setIco(item.ico);
                        list.add(point);
                        break;
                    }
                }
            }
        }
        for (FunctionBoard point : points) {
            if (!TextUtils.isEmpty(point.getH5Url())) {
                point.setIco(R.drawable.icon_home_space_new);
                list.add(point);
            }
        }
        return list;
    }

    /***/
    public ArrayList<MenuChildItem> getFunctionsItem(List<EnumFunctionPoint> points) {

        ArrayList<MenuChildItem> menuChildItems = new ArrayList<MenuChildItem>();
        for (MenuChildItem item : mGridItems) {
            for (EnumFunctionPoint point : points) {
                if (item.ponit.getValue() == point.getValue()) {
                    menuChildItems.add(item);
                    break;
                }
            }
        }
        return menuChildItems;
    }

    /**
     * 获得OA默认显示模块
     */
    public List<EnumFunctionPoint> getDefaultOAFunctions() {
        List<EnumFunctionPoint> list = new ArrayList<EnumFunctionPoint>();
        for (EnumFunctionPoint point : defultOAPoints) {
            list.add(point);
        }
        return list;
    }

    /**
     * 获得CRN默认显示模块
     */
    public List<EnumFunctionPoint> getDefaultCRMFunctions() {
        List<EnumFunctionPoint> list = new ArrayList<EnumFunctionPoint>();
        for (EnumFunctionPoint point : defultCRMPoints) {
            list.add(point);
        }
        return list;
    }


    /**
     * 根据功能点获取菜单
     *
     * @param point
     * @return
     */
    public MenuChildItem getMenuByPoint(EnumFunctionPoint point) {
        MenuChildItem childItem = null;
        for (MenuChildItem item : mGridItems) {
            if (item.ponit.getValue() == point.getValue()) {
                childItem = item;
                break;
            }
        }
        return childItem;
    }

}
