package com.bltx.dbframe.sqlitecore;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public interface IBaseDao<T> {

    long insert(T entity);

    int update(T entity, T where);

}
