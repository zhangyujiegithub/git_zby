package com.biaozhunyuan.tianyi.common.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.model.dict.LatestSelectedDict;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMDataHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "zlphone.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 3;
    private static ORMDataHelper helper;
    private SQLiteDatabase db = null;
    private Dao<User, Integer> userDao;// User的Dao
    private Dao<Latest, Integer> latestDao;// latest的dao
    private Dao<Organize, Integer> deptDao;// latest的dao

    private ORMDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getConnection();
        // onUpgrade(getWritableDatabase(), getConnectionSource(), 1, 1);
    }

    public static synchronized ORMDataHelper getInstance(Context context) {
        if (helper == null) {
            helper = new ORMDataHelper(context);
        }
        return helper;
    }

    private void getConnection() {
        // ReentrantLock lock = new ReentrantLock();
        // lock.lock();
        db = this.getReadableDatabase();
        // lock.unlock();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Organize.class);
            TableUtils.createTable(connectionSource, Latest.class);
//            TableUtils.createTable(connectionSource, 客户.class);
            // TableUtils.createTable(connectionSource, 通知.class);
            // TableUtils.createTable(connectionSource, 邮件.class);
            // TableUtils.createTable(connectionSource, 任务.class);
            // TableUtils.createTable(connectionSource, 日志.class);
            // TableUtils.createTable(connectionSource, 流程.class);
            // TableUtils.createTable(connectionSource, 销售机会.class);
            // TableUtils.createTable(connectionSource, 任务分类.class);
            // TableUtils.createTable(connectionSource, 已提醒.class);
            // TableUtils.createTable(connectionSource, 考勤信息.class);
            // TableUtils.createTable(connectionSource, 日程计划.class);
            // TableUtils.createTable(connectionSource, 联系拜访记录.class);
            // TableUtils.createTable(connectionSource, 联系拜访记录明细.class);
            // TableUtils.createTable(connectionSource, 订单.class);
//            TableUtils.createTable(connectionSource, PhotoInfo.class);
            TableUtils.createTable(connectionSource, LatestSelectedDict.class);
            // TableUtils.createTable(connectionSource, 请假.class);
            // TableUtils.createTable(connectionSource, 客户联系记录.class);
            // TableUtils.createTable(connectionSource, 问题反馈.class);
            // TableUtils.createTable(connectionSource, 联系人.class);
            // TableUtils.createTable(connectionSource, 帖子.class);
            // TableUtils.createTable(connectionSource, 客户投诉建议.class);
            // TableUtils.createTable(connectionSource, 论坛回帖.class);
            // TableUtils.createTable(connectionSource, 动态.class);
            // TableUtils.createTable(connectionSource, 动态已提醒.class);
        } catch (SQLException e) {
            Logger.e(ORMDataHelper.class.getName() + "创建数据库失败" + e);
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            Logger.e(ORMDataHelper.class.getName() + "创建数据库失败" + e);
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.e(ORMDataHelper.class.getName() + "失败" + ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int arg2, int arg3) {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Organize.class, true);
            TableUtils.dropTable(connectionSource, Latest.class, true);
//            // TableUtils.dropTable(connectionSource, 通知.class, true);
//            TableUtils.dropTable(connectionSource, Client.class, true);
//            TableUtils.dropTable(connectionSource, 客户.class, true);
            // TableUtils.dropTable(connectionSource, 邮件.class, true);
            // TableUtils.dropTable(connectionSource, 任务.class, true);
            // TableUtils.dropTable(connectionSource, 日志.class, true);
            // TableUtils.dropTable(connectionSource, 流程.class, true);
            // TableUtils.dropTable(connectionSource, 销售机会.class, true);
            // TableUtils.dropTable(connectionSource, 任务分类.class, true);
            // TableUtils.dropTable(connectionSource, 已提醒.class, true);
            // TableUtils.dropTable(connectionSource, 考勤信息.class, true);
//            TableUtils.dropTable(connectionSource, 部门.class, true);
            // TableUtils.dropTable(connectionSource, 日程计划.class, true);
            // TableUtils.dropTable(connectionSource, 联系拜访记录.class, true);
            // TableUtils.dropTable(connectionSource, 联系拜访记录明细.class, true);
            // TableUtils.dropTable(connectionSource, 订单.class, true);
//            TableUtils.dropTable(connectionSource, PhotoInfo.class, true);
            // TableUtils.dropTable(connectionSource, 请假.class, true);
            // TableUtils.dropTable(connectionSource, 客户联系记录.class, true);
//            TableUtils.dropTable(connectionSource, 测量信息.class, true);
//            TableUtils.dropTable(connectionSource, 流程分类表.class, true);
            // TableUtils.dropTable(connectionSource, 问题反馈.class, true);
            // TableUtils.dropTable(connectionSource, 联系人.class, true);
            // TableUtils.dropTable(connectionSource, 帖子.class, true);
            // TableUtils.dropTable(connectionSource, 客户投诉建议.class, true);
            // TableUtils.dropTable(connectionSource, 论坛回帖.class, true);
            // TableUtils.dropTable(connectionSource, 动态.class, true);
            // TableUtils.dropTable(connectionSource, 动态已提醒.class, true);
            // SQLiteDatabase.deleteDatabase(file);
            TableUtils.dropTable(connectionSource, LatestSelectedDict.class,
                    true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Logger.e(ORMDataHelper.class.getName() + "更新数据库失败" + e);
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
        Log.i("ormlite", "close()");
        // helloDao = null;
        if (db != null) {
            db.close();
        }
    }

    /**
     * 关闭
     */
    public void closeDatabase() {
        Log.i("ormlite", "closeDatabase()");
        // helloDao = null;
        if (db != null) {
            db.close();
        }
    }

    /**
     * 生成User的Dao
     */
    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return userDao;

    }

    /**
     * 生成部门的Dao
     */
    public Dao<Organize, Integer> getDeptDao() {
        if (deptDao == null) {
            try {
                deptDao = getDao(Organize.class);
            } catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return deptDao;

    }

    /**
     * 生成Latest的Dao
     */
    public Dao<Latest, Integer> getLatestDao() {
        if (latestDao == null) {
            try {
                latestDao = getDao(Latest.class);
            } catch (java.sql.SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return latestDao;

    }

    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }

    public boolean updateData(String sql, String[] bindArgs) {
        boolean flag = false;
        try {
            db.execSQL(sql, bindArgs);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 清空旧版数据库
     */
    public void deleteOldDb() {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Organize.class, true);
            TableUtils.dropTable(connectionSource, Latest.class, true);
            // TableUtils.dropTable(connectionSource, 通知.class, true);
//            TableUtils.dropTable(connectionSource, Client.class, true);
//            TableUtils.dropTable(connectionSource, 客户.class, true);
            // TableUtils.dropTable(connectionSource, 邮件.class, true);
            // TableUtils.dropTable(connectionSource, 任务.class, true);
            // TableUtils.dropTable(connectionSource, 日志.class, true);
            // TableUtils.dropTable(connectionSource, 流程.class, true);
            // TableUtils.dropTable(connectionSource, 销售机会.class, true);
            // TableUtils.dropTable(connectionSource, 任务分类.class, true);
            // TableUtils.dropTable(connectionSource, 已提醒.class, true);
            // TableUtils.dropTable(connectionSource, 考勤信息.class, true);
//            TableUtils.dropTable(connectionSource, 部门.class, true);
            // TableUtils.dropTable(connectionSource, 日程计划.class, true);
            // TableUtils.dropTable(connectionSource, 联系拜访记录.class, true);
            // TableUtils.dropTable(connectionSource, 联系拜访记录明细.class, true);
            // TableUtils.dropTable(connectionSource, 订单.class, true);
//            TableUtils.dropTable(connectionSource, PhotoInfo.class, true);
            // TableUtils.dropTable(connectionSource, 请假.class, true);
            // TableUtils.dropTable(connectionSource, 客户联系记录.class, true);
//            TableUtils.dropTable(connectionSource, 测量信息.class, true);
//            TableUtils.dropTable(connectionSource, 流程分类表.class, true);
            // TableUtils.dropTable(connectionSource, 帖子.class, true);
            // TableUtils.dropTable(connectionSource, 问题反馈.class, true);
            // TableUtils.dropTable(connectionSource, 联系人.class, true);
            // TableUtils.dropTable(connectionSource, 客户投诉建议.class, true);
            // TableUtils.dropTable(connectionSource, 论坛回帖.class, true);
            // TableUtils.dropTable(connectionSource, 动态.class, true);
            // TableUtils.dropTable(connectionSource, 动态已提醒.class, true);
            TableUtils.dropTable(connectionSource, LatestSelectedDict.class,
                    true);
//            TableUtils.dropTable(connectionSource, Rad产品字典.class, true);
//            TableUtils.dropTable(connectionSource, Slt产品字典.class, true);
//            TableUtils.dropTable(connectionSource, AlarmTask.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Logger.e(ORMDataHelper.class.getName() + "更新数据库失败" + e);
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
