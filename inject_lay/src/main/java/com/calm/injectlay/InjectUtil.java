package com.calm.injectlay;

import com.calm.injectlay.annotations.ContentView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Create by Calm
 * 2020/6/12 21:00
 * 注入工具
 */
public class InjectUtil {
    private InjectUtil(){}
    public static void inject(Object context){
        if(context == null){
            return;
        }
        injectLayout(context);
    }
    /**
     * 注入布局文件
     * @param context
     */
    private static void injectLayout(Object context) {
        int layoutId = 0;
        //拿到类
        Class<?> clazz = context.getClass();
        //拿到ContentView注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView == null){
            return;
        }
        layoutId = contentView.value();
        try {
            Method setContentViewMethod = context.getClass().getMethod("setContentView", int.class);
            setContentViewMethod.invoke(context,layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
