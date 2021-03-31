package com.example.playgroundkt.util;

import android.app.Activity;

import com.example.playgroundkt.myinterface.butterknife.IBindFieldViewService;


/**
 * <br> ClassName:   ${className}
 * <br> Description:
 * <br>
 * <br> @author:      谢文良
 * <br> Date:        2018/8/9 17:44
 */
public class BindFieldViewUtil {
    private IBindFieldViewService mBindFieldViewService;

    private static BindFieldViewUtil mBindFieldViewUtil;

    public static BindFieldViewUtil getInstance() {
        if (mBindFieldViewUtil == null) {
            mBindFieldViewUtil = new BindFieldViewUtil();
        }
        return mBindFieldViewUtil;
    }

    private BindFieldViewUtil() {
        try {
            mBindFieldViewService = (IBindFieldViewService) Class.forName("com.example.playgroundkt.myinterface.butterknife.BindFieldViewService").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void bind(Activity activity) {
        if(mBindFieldViewService != null){
            mBindFieldViewService.bind(activity).init();
        }
    }
}
