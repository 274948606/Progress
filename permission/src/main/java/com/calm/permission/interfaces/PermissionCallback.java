package com.calm.permission.interfaces;

/**
 * Create by Calm
 * 2020/6/12 15:57
 * 权限申请回调
 */
public interface PermissionCallback {
    /**
     * 权限申请成功
     */
    void permissionApplySuccess();

    /**
     * 权限申请失败,但非永久拒绝
     */
    void permissionApplyFailed();

    /**
     * 权限申请被永久拒绝
     */
    void permissionApplyForever();
}
