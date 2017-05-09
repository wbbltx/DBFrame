package com.bltx.dbframe;


import android.util.Log;

import com.bltx.dbframe.sqlitecore.BaseDao;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class Userdao extends BaseDao {
    @Override
    public String createDataBase() {
        Log.i("12345", "执行创建数据库的语句  ");
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }
}
