package com.biaozhunyuan.tianyi.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.biaozhunyuan.tianyi.common.model.MyContacts;

import java.util.ArrayList;

/**
 * 获取通讯录工具类
 * 返回的数据：
 * [{
 * "name": "xxx",
 * "note": "呵呵呵呵",
 * "phone": "13333333332"
 * },
 * {
 * "name": "yyy",
 * "phone": "13333333333"
 * },
 * {
 * "name": "zzz",
 * "phone": "13333333334"
 * },
 * ......
 * ]
 */
public class ContactUtils {
    public static ArrayList<MyContacts> getAllContacts(Context context) {
        ArrayList<MyContacts> contacts = new ArrayList<MyContacts>();

        // 获取联系人数据
        ContentResolver cr = context.getContentResolver();
//获取所有电话信息（而不是联系人信息），这样方便展示
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 姓名
                ContactsContract.CommonDataKinds.Phone.NUMBER,// 电话号码
        };
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext())  {
            String name = cursor.getString(0);
            String number = cursor.getString(1);
            //保存到对象里
            MyContacts info = new MyContacts();
            info.setName(name);
            info.setPhone(number);
            //保存到集合里
            contacts.add(info);
        }
        cursor.close();
        return contacts;
    }
}
