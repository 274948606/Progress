package com.calm.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.calm.permission.annotations.PermissionFailed;
import com.calm.permission.annotations.PermissionForever;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Create by Calm
 * 2020/6/12 16:10
 */
public class PermissionUtil {
    private PermissionUtil(){ }

    /**
     * 是否所有权限均已被申请
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissionRequest(Context context,String[] permissions){
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 是否所有权限都申请成功了
     * @param result
     * @return
     */
    public static boolean hasPermissionSuccess(int... result){
        if(result == null || result.length == 0){
            return false;
        }
        for (int i : result) {
            if(i != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 是否存在被永久拒绝的权限
     * @param activity
     * @param permissions
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions,int[] grantResults){
        for (int i = 0; i< grantResults.length; i++){
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                //i位的权限是申请通过的权限  shouldShowRequestPermissionRationale会返回false
                continue;
            }
            //被拒绝了的权限 看下是被永久拒绝
            if(!ActivityCompat.shouldShowRequestPermissionRationale(activity,permissions[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 将结果回调到被annotationClass这个注解的requestCode这个方法
     * @param o
     * @param annotationClass
     * @param requestCode
     */
    public static void invokeMethod(Object o,Class annotationClass,int requestCode){
        Class<?> aClass = o.getClass();
        //获取到所有的方法
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            //方法是否被annotation所注解
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            if(annotationPresent){
                Annotation annotation = method.getAnnotation(annotationClass);
                int code = -1;
                if(annotationClass == PermissionFailed.class){
                    code = ((PermissionFailed)annotation).requestCode();
                }else if(annotationClass == PermissionForever.class){
                    code = ((PermissionForever)annotation).requestCode();
                }
                if(code == requestCode){
                    try {
                        method.invoke(o);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
