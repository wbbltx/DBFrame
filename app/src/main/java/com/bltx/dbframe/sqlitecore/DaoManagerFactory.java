package com.bltx.dbframe.sqlitecore;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class DaoManagerFactory {
    private String path;
    private SQLiteDatabase sqLiteDatabase;
    private static DaoManagerFactory daoManagerFactory = new DaoManagerFactory(new File(Environment.getExternalStorageDirectory(), "logic.db"));


    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> clazz, Class<M> entity) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entity,sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

    public DaoManagerFactory(File file) {
        path = file.getAbsolutePath();
        openDataBase();

    }

    private void openDataBase() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    public static DaoManagerFactory getInstance(){
        return daoManagerFactory;
    }
}
