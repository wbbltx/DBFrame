package com.bltx.dbframe;


import com.bltx.dbframe.sqlitecore.DBField;
import com.bltx.dbframe.sqlitecore.DBTable;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

@DBTable("tb_user")
public class User {
    @DBField("name")
    public String name;

    public String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
