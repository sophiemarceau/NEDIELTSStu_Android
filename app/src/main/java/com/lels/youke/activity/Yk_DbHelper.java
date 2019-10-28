package com.lels.youke.activity;


import android.content.Context;
import android.util.Log;

import com.lelts.tool.AppUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * 包名：com.example.DBAppDemo
 * 描述：数据库的操作
 * Date 2015/7/17 0017.
 * Time 上午 9:07.
 * 修改日期：
 * 修改内容：
 */
public class Yk_DbHelper implements DbUtils.DbUpgradeListener {
    private static Yk_DbHelper dbHelper;
    private final String dbname = "yktestdbzw";//数据库名称
    private int version;//数据库版本
    private DbUtils mDBClient;

    private Yk_DbHelper(Context context) {
        version = AppUtils.getVersionCode(context);
        mDBClient = DbUtils.create(context, dbname, version, this);
        mDBClient.configAllowTransaction(true);
        mDBClient.configDebug(true);
        //不同的写法
      
//      DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
//      config.setDbName(dbname); //db名
//      config.setDbVersion(version);  //db版本
//      mDBClient = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
//      mDBClient.configAllowTransaction(true);
//      mDBClient.configDebug(true);

    }

    public static Yk_DbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new Yk_DbHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onUpgrade(DbUtils dbUtils, int oldVer, int newVer) {
        try {
            //数据库的更新 
            if (oldVer < newVer) { //旧的版本小于新的版本
            	/*if (2 == newVer) {
            		List<ClockInfo> list = dbUtils.findAll(ClockInfo.class);
                    dbUtils.dropTable(ClockInfo.class);
                    for (int a = 0; a < list.size(); a++) {
                    	ClockInfo info = list.get(a);
                    	System.out.println("old version clock info : " + info.title);
                    }
            	}*/
            }
        } catch (Exception e) {
            Log.d("shibai", "失败了");
        }
    }

    /////////###########################################开始了数据库的 增、删、改、查###############################################/////////

    /**
     * 插入单个对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public synchronized boolean save(Object entity) {
        try {
            mDBClient.save(entity);
        } catch (DbException e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 插入全部对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public synchronized boolean saveAll(List<?> entity) {
        try { 
            mDBClient.saveAll(entity);
        } catch (DbException e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除这个表中的所有数据
     * @param entity 实体类的对象
     * @return true:成功 false:失败
     */
    public synchronized boolean delete(Object entity) {
        try {
            mDBClient.delete(entity);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据条件删除表
     * @param entity 表名称
     * @param colun  列名
     * @param value  值
     * @return true:成功  false:失败
     */
    public synchronized boolean deleteCriteria(Class<?> entity, String colun, String value) {
        try {
            mDBClient.delete(entity, WhereBuilder.b(colun, "=", value));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新这张表中的所有数据
     * @param entity 实体类的对象
     * @return true:更新成功 false:更新失败
     */
    public synchronized boolean update(Object entity) {
        try {
            mDBClient.saveOrUpdate(entity);//先去查这个条数据 根据id来判断是存储还是更新 如果存在更新
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据参数更新表中的数据
     * @param entity 实体类的对象
     * @param value  要更新的字段
     * @return true:更新成功 false:更新失败
     */
    public synchronized boolean update(Object entity, String... value) {
        try {
            mDBClient.update(entity, value);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查找 根据id
     * @param cla 要查询的类
     * @param id  要查询的id
     * @return 查询到的数据
     */
    public synchronized <T> Object searchOne(Class<T> cla, int id) {
        try {
            return mDBClient.findFirst(Selector.from(cla).where(WhereBuilder.b("id", "=", id)));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }

    /**
     * 正叙查找 没有条件的
     * @param entity 要查询的类
     * @param <T>    要查询的类
     * @return 查询到的数据
     */
    public synchronized <T> List<T> search(Class<T> entity) {
        try {
            return mDBClient.findAll(Selector.from(entity));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }

    /**
     * 倒叙查找所有数据 没有条件的
     * @param entityClass
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> List<T> searchDesc(Class<T> entityClass) {
        try {
            return mDBClient.findAll(Selector.from(entityClass).orderBy("id", true));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 倒叙查找所有数据 没有条件的
     * @param entityClass 实体类
     * @param column      定义的查询条件
     * @param value       定义的查询值
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> List<T> searchCriteria(Class<T> entityClass, String column, String value) {
        try {
            return mDBClient.findAll(Selector.from(entityClass).where(WhereBuilder.b(column, "=", value)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除表格
     * @param entityClass 实体类
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> boolean drop(Class<T> entityClass) {
        try {
            mDBClient.dropTable(entityClass);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
}
