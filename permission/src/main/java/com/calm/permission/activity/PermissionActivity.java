package com.calm.permission.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.calm.permission.interfaces.PermissionCallback;
import com.calm.permission.utils.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Create by Calm
 * 2020/6/12 15:52
 * 一个透明的Activity，在申请的时候由这个Activity来接管申请的逻辑
 */
public class PermissionActivity extends AppCompatActivity {
    private static PermissionCallback permissionCallback;
    private static final String REQUEST_PERMISSIONS = "request.permissions";
    private static final String REQUEST_CODE = "request.code";
    private static final int REQUEST_CODE_DEFAULT = -1;
    public static void launchActivity(Context context,String[] permissions,
                                      int requestCode,PermissionCallback callback){
        permissionCallback = callback;
        Bundle bundle = new Bundle();
        bundle.putStringArray(REQUEST_PERMISSIONS,permissions);
        bundle.putInt(REQUEST_CODE,requestCode);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context,PermissionActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //参数错误,直接返回
        if(intent == null){
            finish();
            return;
        }
        String[] permissions = intent.getStringArrayExtra(REQUEST_PERMISSIONS);
        int requestCode = intent.getIntExtra(REQUEST_CODE,REQUEST_CODE_DEFAULT);
        if(permissions == null || permissions.length == 0 ||
            requestCode == REQUEST_CODE_DEFAULT || permissionCallback == null){
            finish();
            return;
        }
        //所有权限均已被申请，不需要在申请权限
        if(PermissionUtil.hasPermissionRequest(this,permissions)){
            permissionCallback.permissionApplySuccess();
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this,permissions,requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限与回调长度不一致，有问题
        if(permissions.length != grantResults.length){
            permissionCallback.permissionApplyFailed();
            finish();
            return;
        }
        //所有权限都申请成功了
        if(PermissionUtil.hasPermissionSuccess(grantResults)){
            permissionCallback.permissionApplySuccess();
            finish();
            return;
        }
        /**
         * 有权限被永久拒绝了
         */
        if(PermissionUtil.shouldShowRequestPermissionRationale(this,permissions,grantResults)){
            permissionCallback.permissionApplyForever();
            finish();
            return;
        }
        //前面都不符合,表示被拒绝但非永久拒绝
        permissionCallback.permissionApplyFailed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        permissionCallback = null;
        overridePendingTransition(0,0);
    }
}
