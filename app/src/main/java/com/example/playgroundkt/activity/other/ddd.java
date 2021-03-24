package com.example.playgroundkt.activity.other;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @Description: java类作用描述
 * @Author: chunxiong.gu
 * @CreateDate: 2021/3/24 16:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/24 16:42
 * @UpdateRemark: 更新说明
 */
public class ddd extends AppCompatActivity {

    Handler handler1 = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    Handler handler2 = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });


}
