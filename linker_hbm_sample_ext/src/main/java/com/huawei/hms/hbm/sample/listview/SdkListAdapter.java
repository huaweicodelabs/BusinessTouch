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

package com.huawei.hms.hbm.sample.listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.hbm.sample.R;
import com.huawei.hms.hbm.sample.common.HbmCodeName;
import com.huawei.hms.hbm.sample.common.SampleToast;
import com.huawei.hms.hbm.api.bean.HbmCode;
import com.huawei.hms.hbm.api.bean.req.HbmIntent;
import com.huawei.hms.hbm.sdk.HbmSdkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: l00568035
 * @since: 2020/6/11 10:28
 */
public class SdkListAdapter extends BaseAdapter {
    private Context context;
    List<Map<String, Object>> list;
    List<RadioButton> radioButtons;

    public SdkListAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private String[] intentTitle = {"打开HBM网页",
            "打开HBM设置页面",
            "打开HBM商家消息列表",
            "打开我的关注商家列表页面",
            "打开全部服务动态页面",
            "进入用户协议页面",
            "进入隐私协议页面",
            "打开服务号中心页面"};

    // radioButton 点击事件注册
    private void radioEvent() {
        radioButtons = new ArrayList<>(intentTitle.length);
        for (int i = 0; i < intentTitle.length; i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setFocusable(false);
            radioButton.setPadding(80, 0, 0, 0);                // 设置文字距离按钮四周的距离
            radioButton.setText(intentTitle[i]);
            int finalI = i;
            radioButton.setOnClickListener(v -> {
                HbmSdkService hbmSDKService = HbmSdkService.getInstance().init(context);
                String[] hbmIntentList = {HbmIntent.ACTION_TO_WEB, HbmIntent.ACTION_TO_SETTING,
                        HbmIntent.ACTION_TO_THREAD, HbmIntent.ACTION_TO_FOLLOWED,
                        HbmIntent.ACTION_TO_SRV, HbmIntent.ACTION_TO_USER_AGREEMENT,
                        HbmIntent.ACTION_TO_PRIVACY_AGREEMENT, HbmIntent.ACTION_TO_SQUARE};
                String hbmIntent = hbmIntentList[finalI];
                String url = hbmIntent.equals(HbmIntent.ACTION_TO_WEB) ? "https://www.baidu.com" : "";
                hbmSDKService.startHbmActivity((Activity) context, HbmIntent.create(context, hbmIntent)
                        .putExtra(HbmIntent.KEY_URL, url), result -> {
                    if (result.getHbmCode().getCode() != HbmCode.CODE_SUCCESS) {
                        SampleToast.showToast(context, "无法跳转\n" + HbmCodeName.getHbmCodeName(result.getHbmCode()), Toast.LENGTH_SHORT);
                    }
                });
            });
            radioButtons.add(radioButton);
        }
    }

    private void initViewHold(ViewHolder vh, int position) {
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(position);
            String title = (String) map.get("title");
            vh.title.setText(title);
            vh.explanation.setText((String) map.get("explanation"));
            vh.time.setVisibility(View.VISIBLE);
            vh.result.setVisibility(View.VISIBLE);
            vh.time.setText((String) map.get("time"));
            vh.result.setText((String) map.get("result"));

            if (title.equals("addNotify")
                    || title.equals("removeNotify")) {
                vh.checkgroup.removeAllViews();
                vh.checkgroup.setVisibility(View.VISIBLE);
                vh.radioGroup.setVisibility(View.GONE);
                String[] notifyTitle = {"协议状态变更", "账号退出", "账号登录", "关注关系变更", "服务动态变更", "新消息", "打点事件"};
                boolean[] flag = (boolean[]) map.get("flag");
                for (int i = 0; i < notifyTitle.length; i++) {
                    CheckBox checkBox = new CheckBox(context);
                    checkBox.setFocusable(false);
                    if (flag != null)
                        checkBox.setChecked(title.equals("addNotify") & flag[i]);
                    checkBox.setPadding(80, 0, 0, 0);                // 设置文字距离按钮四周的距离
                    checkBox.setText(notifyTitle[i]);
                    vh.checkgroup.addView(checkBox, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else if (title.equals("startHbmActivity")) {
                vh.radioGroup.removeAllViews();
                vh.radioGroup.setVisibility(View.VISIBLE);
                vh.time.setVisibility(View.GONE);
                vh.result.setVisibility(View.GONE);
                vh.checkgroup.setVisibility(View.GONE);
                for (int i = 0; i < intentTitle.length; i++) {
                    ViewGroup viewGroup = (ViewGroup) radioButtons.get(i).getParent();
                    if (viewGroup != null)
                        viewGroup.removeView(radioButtons.get(i));
                    vh.radioGroup.addView(radioButtons.get(i), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            } else if (title.equals("当前已注册事件")) {
                vh.checkgroup.setVisibility(View.GONE);
                vh.radioGroup.setVisibility(View.GONE);
                vh.time.setVisibility(View.GONE);
                vh.result.setVisibility(View.GONE);
            } else {
                vh.checkgroup.setVisibility(View.GONE);
                vh.radioGroup.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder vh = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sdkenter_list_item, null);
            vh = new ViewHolder();
            vh.title = view.findViewById(R.id.lv_title);
            vh.explanation = view.findViewById(R.id.lv_explanation);
            vh.result = view.findViewById(R.id.lv_result);
            vh.time = view.findViewById(R.id.lv_time);
            vh.checkgroup = view.findViewById(R.id.lv_checkgroup);
            vh.radioGroup = view.findViewById(R.id.lv_radiogroup);
            if (radioButtons == null) {
                radioEvent();
            }
            view.setTag(vh);
        }
        initViewHold((ViewHolder) view.getTag(), position);
        return view;
    }

    class ViewHolder {
        TextView title;
        TextView explanation;
        TextView result;
        TextView time;
        LinearLayout checkgroup;
        RadioGroup radioGroup;
    }
}
