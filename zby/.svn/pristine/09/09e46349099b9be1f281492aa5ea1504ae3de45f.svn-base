package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;

import java.util.ArrayList;

/***
 * 长汇客户Tab分类信息 （基本信息,会员信息的一个Tab），动态生成
 *
 * @author new
 *
 */
public class ChClientTabFragment extends Fragment {
    private static final String EXTRA_FORM_LIST = "extra_form_list";
    private ArrayList<表单字段> mFormList;

    private ChClientBiz mClientBiz;

    private LinearLayout mRootLayout;
    private boolean isEnabled;
    private String tableName;
    private String id;
    private boolean isShowContactAndPhone = true;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFormList = (ArrayList<表单字段>) bundle
                    .getSerializable(EXTRA_FORM_LIST);
            isEnabled = bundle.getBoolean("Enabled");
            tableName = bundle.getString("tableName");
            id = bundle.getString("id");
        }

        View view = inflater.inflate(R.layout.fragment_tab_ch_client, null);
        mRootLayout = (LinearLayout) view
                .findViewById(R.id.ll_root_ch_client_tab);
        if (mFormList != null && mFormList.size() > 0) {
            //isEnabled: 可编辑为 true, 不可编辑只可查看为 false
            mClientBiz = new ChClientBiz(getActivity(), mFormList, mRootLayout , isEnabled,tableName,id);
            mClientBiz.setRelateFragment(this);
            mClientBiz.setShowContactAndPhone(isShowContactAndPhone);
            mClientBiz.generateViews();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("ONA" + "onActivityResult");
//        理财产品 product = ChProductBiz.onActivityGetClient(requestCode, data);
//
//        if (product != null) {
//            List<EditText> formList = mClientBiz.getEditList();
//            for (int i = 0; i < formList.size(); i++) {
//                EditText etValue = formList.get(i);
//                表单字段 form = (表单字段) etValue.getTag();
//                Logger.i("ChClient::" + form.Name);
//                if (mClientBiz.isProductType(form.Name)) {
//                    form.Value = product.编号 + "";
//                    form.DicText = product.产品名称;
//                    etValue.setText(product.产品名称);
//                }
//            }
//        }
    }

    public void  setShowContactAndPhone(boolean showContactAndPhone) {
        isShowContactAndPhone = showContactAndPhone;
    }

    public ArrayList<表单字段> getFormList() {
        return mClientBiz.getFormList();
    }

    public void setFormListAddress(String mLocation,String mProvince,String mCity) {
        mClientBiz.setFormListAddress(mLocation,mProvince,mCity);
    }

    public static ChClientTabFragment newInstance(ArrayList<表单字段> formList,boolean isEnabled, String tableName , String id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_FORM_LIST, formList);
        bundle.putSerializable("Enabled", isEnabled);
        bundle.putSerializable("tableName", tableName);
        bundle.putSerializable("id", id);
        ChClientTabFragment fragment = new ChClientTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
