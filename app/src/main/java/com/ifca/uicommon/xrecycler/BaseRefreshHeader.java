package com.ifca.uicommon.xrecycler;

/**
 * Created by rex on 2017/4/21.
 * 功能：刷新接口
 */

interface BaseRefreshHeader {
    int STATE_NORMAL=0;
    int STATE_RELEASE_TO_REFRESH=1;
    int STATE_REFRESHING=2;
    int STATE_DONE=3;
    void onMove(float delta);
    boolean releaseAction();
    void refreshComplete();
}
