package com.bltx.dbframe.sqlitecore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.sax.EndElementListener;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    private SQLiteDatabase sqLiteDatabase;
    private boolean isInit = false;
    private String tableName;
    private Class<T> entityClass;
    private Map<String, Field> map;

    //                                            User                    sqlitedatabase
    public synchronized void init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.sqLiteDatabase = sqLiteDatabase;
            this.tableName = entity.getAnnotation(DBTable.class).value();
            this.entityClass = entity;
            isInit = true;
            sqLiteDatabase.execSQL(createDataBase());
            map = new HashMap<>();
            initCacheMap();
        }

    }

    private void initCacheMap() {
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = null;
        try {
            cursor = this.sqLiteDatabase.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            Field[] columnFields = entityClass.getFields();
            for (Field columnField : columnFields) {
                columnField.setAccessible(true);
            }

            for (String columnName : columnNames) {
                for (Field columnField : columnFields) {
                    String fieldName;
                    if (columnField.getAnnotation(DBField.class) != null) {
                        fieldName = columnField.getAnnotation(DBField.class).value();
                    } else {
                        fieldName = columnField.getName();
                    }
                    if (columnName.equals(fieldName)) {
                        map.put(columnName, columnField);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    public abstract String createDataBase();


    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> fieldIterator = map.values().iterator();
        while (fieldIterator.hasNext()) {
            Field columnToFields = fieldIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (columnToFields.getAnnotation(DBField.class) != null) {
                cacheKey = columnToFields.getAnnotation(DBField.class).value();
            } else {
                cacheKey = columnToFields.getName();
            }
            try {
                if (columnToFields.get(entity) == null) {
                    continue;
                }
                cacheValue = columnToFields.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }
        return result;
    }

    private ContentValues getContentValue(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    @Override
    public long insert(T entity) {
        Map<String, String> map = getValues(entity);
        Log.i("12345", "map--" + map.keySet().size());
        ContentValues contentValue = getContentValue(map);
        long result = sqLiteDatabase.insert(tableName, null, contentValue);
        Log.i("12345", "tableName--" + tableName + " contentValue " + contentValue.keySet().size());
        return result;
    }

    @Override
    public int update(T entity, T where) {
        int result ;
        Map<String, String> values = getValues(entity);
        Condition condition = new Condition(getValues(where));
        result = sqLiteDatabase.update(tableName,getContentValue(values),condition.whereClause,condition.whereArgs);
        return result;
    }

    class Condition{
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String,String> map){
            ArrayList list = new ArrayList();
            StringBuilder builder = new StringBuilder();
            builder.append(" 1=1 ");
            Set keys = map.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = map.get(key);
                if (value != null){
                    builder.append(" and "+key+" =? ");
                    list.add(value);
                }
            }
            this.whereClause = builder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }
    }
}
