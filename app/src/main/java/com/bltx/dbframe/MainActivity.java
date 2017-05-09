package com.bltx.dbframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bltx.dbframe.sqlitecore.DaoManagerFactory;


public class MainActivity extends AppCompatActivity {

    public Userdao dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("12345", "初始化 创建数据库");
        dataHelper = DaoManagerFactory.getInstance().getDataHelper(Userdao.class, User.class);
    }

    public void insert(View view) {
        User user = new User();
        user.setName("张三");
        user.setPassword("123456");
        dataHelper.insert(user);

    }

    public void update(View view) {
        User user = new User();
        user.setName("lisi");
        user.setPassword("123456789");

        User where = new User();
        where.setName("张三");

        dataHelper.update(user,where);
    }
}
