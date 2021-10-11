package com.biaozhunyuan.tianyi.common.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典查询帮助类
 *
 * @author bohr
 */
@SuppressLint("NewApi")
public class DictionaryHelper {
    private Context context;

    public DictionaryHelper(Context context) {
        this.context = context;
    }

    /**
     * 根据id查询员工姓名
     *
     * @param id
     * @return
     */
    public String getUserNameById(String id) {
        String name = "";
        if (id != null) {
            ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
            Dao<User, Integer> dao = ormDataHelper.getUserDao();
            User user = new User();
            user.setUuid(id);
            try {
                List<User> result = dao.queryForMatching(user);
                if (result.size() > 0) {
                    name = result.get(0).getName();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // ormDataHelper.close();
            }
        }
        return name;
    }

    /**
     * 根据id查询员工对象
     *
     * @param id
     * @return
     */
    public User getUser(String id) {
        User user = new User();
        user.setUuid(id);
        if (id != null) {
            ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
            Dao<User, Integer> dao = ormDataHelper.getUserDao();
            try {
                List<User> result = dao.queryForMatching(user);
                if (result.size() > 0) {
                    user = result.get(0);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // ormDataHelper.close();
            }
        }
        return user;
    }


    /**
     * 更换用户头像
     *
     * @param userId
     * @param avastarId
     */
    public void changeUserAvastar(String userId, String avastarId) {
        User user = new User();
        user.setUuid(userId);
        if (userId != null) {
            ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
            Dao<User, Integer> dao = ormDataHelper.getUserDao();
            try {
                List<User> result = dao.queryForMatching(user);
                if (result.size() > 0) {
                    user = result.get(0);
                    user.setAvatar(avastarId);
                    dao.update(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // ormDataHelper.close();
            }
        }
    }

    /**
     * 根据id查询员工姓名
     *
     * @param
     * @return
     */
    public String getUserNameById(int Id) {
        String id = String.valueOf(Id);
        return getUserNameById(id);
    }

    /**
     * 根据id查询多个员工姓名
     *
     * @param ids 例如：‘12’;‘14’;‘13’;
     * @return 员工姓名 例如：张三,李四,王五
     */
    public String getUserNamesById(String ids) {
        String names = "";
        if (ids == null || ids.equals("")) {
            return names;
        }
        if (ids.contains("'")) {
            ids = ids.replace("'", "");
        }
        if (ids.contains(";")) {
            if (ids.startsWith(";")) { // 针对数据表 ;12;13;
                ids = ids.substring(1, ids.length());
            }
            if (ids.endsWith(";")) { // 针对数据表 ;12;13;
                ids = ids.substring(0, ids.length() - 1);
            }
            names = queryNames(ids, ";");
        }

        if (ids.contains("|")) {
            if (ids.startsWith("|")) { // 针对数据表 |12|13|
                ids = ids.substring(1, ids.length());
            }
            if (ids.endsWith("|")) { // 针对数据表 ;12;13;
                ids = ids.substring(0, ids.length() - 1);
            }
            names = queryNames(ids, "|");
        }
        if (ids.contains(",")) {
            if (ids.startsWith(",")) { // 针对数据表 |12|13|
                ids = ids.substring(1, ids.length());
            }
            if (ids.endsWith(",")) { // 针对数据表 ;12;13;
                ids = ids.substring(0, ids.length() - 1);
            }
            names = queryNames(ids, ",");
        }

        if (!ids.contains(",")) {
            try { // 单个ID
//                int id = Integer.parseInt(ids);
                names = getUserNameById(ids);
            } catch (Exception e) {
            }
        }
        return names;
    }

    /**
     * 将ids转为id数组
     *
     * @param ids 例如：‘12’;‘14’;‘13’;
     * @return
     */
    public String[] getUserIdArray(String ids) {
        if (ids == null || ids.equals("")) {
            return new String[0];
        }
        if (ids.contains("'")) {
            ids = ids.replace("'", "");
        }
        if (ids.contains("|")) {
            ids = ids.replace("|", ";");
        }
        if (ids.contains(",")) {
            ids = ids.replace(",", ";");
        }
        if (ids.contains(";")) {
            if (ids.startsWith(";")) { // 针对数据表 ;12;13;
                ids = ids.substring(1, ids.length());
            }
            if (ids.endsWith(";")) { // 针对数据表 ;12;13;
                ids = ids.substring(0, ids.length() - 1);
            }

            if (ids.contains(";")) {
                String[] idArray = ids.split(";");
                return idArray;
            }
        }

        if (ids.length() <= 2) {
            try { // 单个ID
                String[] idArray = new String[]{ids};
                return idArray;
            } catch (Exception e) {
            }
        }
        return new String[0];
    }

    /**
     * 根据id查询客户名称
     *
     * @param id
     * @return
     */
//    public String getClientNameById(int id) {
//        return getClientById(id).getCustomerName();
//    }

    /**
     * 根据id查询客户
     *
     * @param
     * @return
     */
//    public Client getClientById(int id) {
//        Client client = new Client();
//        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
//        try {
//            Dao<Client, Integer> dao = ormDataHelper.getDao(Client.class);
//            Client user = new Client();
//            user.setId(id);
//            // user.setCorpId(Global.mUser.CorpId);
//            List<Client> result = dao.queryForMatching(user);
//            if (result.size() > 0) {
//                client = result.get(0);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // ormDataHelper.close();
//        }
//        return client;
//    }
    private String queryNames(String ids, String reg) {
        String names = "";
        String[] idArr = ids.split(reg);
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
        Dao<User, Integer> dao = ormDataHelper.getUserDao();
        User user = new User();
        for (int i = 0; i < idArr.length; i++) {
            user.setUuid(idArr[i]);
            try {
                List<User> result = dao.queryForMatching(user);
                if (result.size() > 0) {
                    names += result.get(0).getName() + ",";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // ormDataHelper.close();
            }
        }
        if (names != null && names.length() > 0) {
            names = names.substring(0, names.length() - 1);
        }
        return names;
    }

    /**
     * 根据id查询员工头像
     *
     * @param id
     * @return 员工头像url
     */
    public String getUserPhoto(String id) {
        if (id != null) {
            ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
            Dao<User, Integer> dao = ormDataHelper.getUserDao();
            User user = new User();
            user.setUuid(id);
            try {
                List<User> result = dao.queryForMatching(user);
                if (result.size() > 0) {
                    String photo = result.get(0).getAvatar(); // 获得头像路径
                    Logger.i("userInfo" + result.get(0).toString());
                    return photo == null ? "" : photo;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // finally {
            // //ormDataHelper.close();
            // }
        }
        return "";
    }

    /**
     * 获得任务状态名
     *
     * @param stateId 状态Id
     * @return
     */
//    public String getStateName(int stateId) {
//        String[] arrs = context.getResources()
//                .getStringArray(R.array.statelist);
//        String stateName = "";
//        if (stateId >= 1 && stateId <= 6) {
//            stateName = arrs[stateId - 1];
//        } else {
//            stateName = "状态异常";
//        }
//        return stateName;
//    }

    /**
     * 根据id查询客户联系人
     *
     * @param id
     * @return
     * @author py 2014.8.7
     */
//    public String getContactsById(int id) {
//        String contacts = "";
//        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
//        try {
//            Dao<Client, Integer> dao = ormDataHelper.getDao(Client.class);
//            Client user = new Client();
//            user.setId(id);
//            // user.setCorpId(Global.mUser.CorpId);
//            List<Client> result = dao.queryForMatching(user);
//            if (result.size() > 0) {
//                contacts = result.get(0).getContacts();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // ormDataHelper.close();
//        }
//        return contacts;
//    }

    /**
     * 根据id查询部门名称姓名
     *
     * @param id
     * @return
     */
    public String getDepartNameById(String id) {
        String name = "";
        if (id != null) {
            ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
            try {
                Dao<Organize, Integer> dao = ormDataHelper.getDao(Organize.class);
                List<Organize> result = dao.queryForEq("uuid", id);
                if (result.size() > 0) {
                    name = result.get(0).getName();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            }
        }
        return name;
    }

    /**
     * 获取所有部门列表
     *
     * @param
     * @return
     */
    public List<Organize> getAllDepart() {
        List<Organize> result = new ArrayList<Organize>();
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
        try {
            Dao<Organize, Integer> dao = ormDataHelper.getDao(Organize.class);
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }


    /**
     * 根据部门id获取部门下所有员工
     *
     * @param deptId 部门id
     * @return 部门下所有员工
     */
    public List<User> getStaffByDeptId(String deptId) {
        List<User> list = new ArrayList<>();
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);

        try {
            Dao<User, Integer> dao = ormDataHelper.getDao(User.class);
            list = dao.queryBuilder()
                    .where().eq("departmentId", deptId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取所有员工
     *
     * @return
     */
    public List<User> getAllStaff() {
        List<User> list = new ArrayList<>();
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);

        try {
            Dao<User, Integer> dao = ormDataHelper.getDao(User.class);
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }





    /**
     * 向数据库中插入一条最近联系人
     *
     * @param object 要插入的数据
     */
    public void insertLatest(Object object) {
        Latest latest = null;
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
        if (object instanceof User) {
            latest = new Latest();
            User user = (User) object;
            latest.setUuid(user.getUuid());
            latest.setName(user.getName());
            latest.setAvatar(user.getAvatar());
            latest.setMobile(user.getMobile());
            latest.setTelephone(user.getTelephone());
            latest.setEnterpriseMailbox(user.getEnterpriseMailbox());
            latest.setPhoneExt(user.getPhoneExt());
            latest.setPost(user.getPost());
        } else if (object instanceof Latest) {
            latest = (Latest) object;
        }
        try {
            Dao<Latest, Integer> latestDao = ormDataHelper.getLatestDao();
            List<Latest> removeLatests = latestDao.queryBuilder().where()
                    .eq("uuid", latest.getUuid()).query();
            latestDao.delete(removeLatests);
            latestDao.create(latest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算listview的高度
     *
     * @param listView
     */
    public static int getListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        return params.height;
    }
}