/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.hms.hbm.sample.base;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.huawei.hms.hbm.sample.common.HbmCodeName;
import com.huawei.hms.hbm.sample.R;
import com.huawei.hms.hbm.api.bean.HbmCode;
import com.huawei.hms.hbm.sdk.HbmSdkService;
import com.huawei.hms.hbm.sdk.INotify;
import com.huawei.hms.hbm.sdk.extend.PictureLoader;
import com.huawei.hms.hbm.sdk.extend.SrvUiComponent;

/**
 * Sample Application
 *
 * @author l00477806
 * @since 2020/5/27.
 */
public class HbmApplication extends Application implements PictureLoader, Application.ActivityLifecycleCallbacks {
    private static final String TAG = "HbmApplication";
    private int activityCount;  //activity的count数
    public static boolean isForeground; //是否在前台

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置主题颜色
        HbmSdkService.getInstance().init(this)
                .extend(SrvUiComponent.create(this, null, R.color.lives_functional_yellow));

        // 注册消息
        // ACCOUNT_LOGIN 账号登录, ACCOUNT_QUIT账号退出
        // AGREEMENT_CHANGED协议状态变更, FOLLOWED_CHANGED关注关系变更
        // NEW_MSG新消息, MSG_SRV_CHANGED服务通知消息
        // TRACK_SRV_MSG打点, MSG_SRV_DISMISS服务消息忽略
        HbmSdkService.getInstance().addNotify((type, data) -> {
            Log.i(TAG, "notify " + type);
            if (data != null) {
                Log.i(TAG, "data: " + data.toString());
            }
        }, INotify.ACCOUNT_LOGIN | INotify.ACCOUNT_QUIT | INotify.AGREEMENT_CHANGED
                | INotify.FOLLOWED_CHANGED | INotify.NEW_MSG | INotify.MSG_SRV_DISMISS | INotify.MSG_SRV_CHANGED
                | INotify.TRACK_SRV_MSG);

        // 打开消息渠道
        HbmSdkService.getInstance().enableChannel(true, result -> {
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                Log.d(TAG, "Channel is open");
            } else {
                Log.d(TAG, "Fail to open channel\n"
                        + "HbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
            }
        });

        //全局管理Activity生命周期
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void loadImage(ImageView imageView, String url, Drawable defaultDrawable) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isForeground();
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityCount--;
        isForeground();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * 判断是否在前台
     */
    private void isForeground() {
        if (activityCount > 0) {
            isForeground = true;
        } else {
            isForeground = false;
        }
        Log.e(TAG + "activityCount=", +activityCount + "-------isForeground=" + isForeground);
    }
}

