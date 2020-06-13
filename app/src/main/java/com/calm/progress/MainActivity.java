package com.calm.progress;

import android.Manifest;
import android.util.Log;
import android.view.View;

import com.calm.injectlay.annotations.ContentView;
import com.calm.permission.annotations.PermissionApply;
import com.calm.permission.annotations.PermissionFailed;
import com.calm.permission.annotations.PermissionForever;
import com.calm.progress.base.BaseActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @Override
    protected void init() {

    }

    @PermissionApply(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA},requestCode = 1001)
    public void permissionApply(View view) {
        Log.e("CALM","权限申请成功");
    }
    @PermissionFailed(requestCode = 1001)
    public void permissionFailed(){
        Log.e("CALM","权限申请失败");
    }
    @PermissionForever(requestCode = 1001)
    public void permissionForever(){
        Log.e("CALM","权限申请失败,被永久拒绝了");
    }
}
