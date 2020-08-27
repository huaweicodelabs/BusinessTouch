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

package com.huawei.hms.hbm.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.huawei.hms.hbm.sample.common.HbmCodeName;
import com.huawei.hms.hbm.sample.listview.SdkListAdapter;
import com.huawei.hms.hbm.sample.common.SampleToast;
import com.huawei.hms.hbm.api.bean.HbmCode;
import com.huawei.hms.hbm.api.bean.req.HbmIntent;
import com.huawei.hms.hbm.api.bean.rsp.SrvMsgData;
import com.huawei.hms.hbm.sdk.HbmSdkService;
import com.huawei.hms.hbm.sdk.INotify;
import com.huawei.hms.hbm.utils.HbmLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author l00568035
 * @since 2020/6/10
 */
public class SampleEnterActivity extends Activity {
    public String TAG = "SampleEnterActivity";
    private Handler handler;

    private static final int ITEMID = 10;       //用于显示当前注册事件的item的ia号

    //ListView title 成员 SDK接口名称
    private static String[] title = {"init",
            "isHbmAvailable",
            "upgrade",
            "getHbmAgreementStateV2",
            "enableHbmAgreement",
            "enableChannlel On",
            "enableChannlel Off",
            "getChannelStatus",
            "startHbmActivity",
            "getUnReadMsg",
            "当前已注册事件",
            "addNotify",
            "removeNotify",
            "getSrvNotifyList",
            "getVisitedPubList",
            "getSrvMsgList"};
    //ListView explanation 成员 SDK接口注解
    private static String[] explanation = {"在Application onCreate加载初始化",
            "判断当前Kit环境是否可用(HMSCore不存在或者Kit不存在)",
            "当接入App调用接口收到HMSCore版本不匹配（包括没有安装）或者Kit版本不匹配，根据业务需要可以触发升级" +
                    "\n说明：本接口只会单独触发HMSCore升级或者Kit升级",
            "获取HBM Kit协议状态是否打开。",
            "同意HBM协议",
            "测试打开渠道开关，需要在账号登录情况下使用",
            "测试关闭渠道开关，需要在账号登录情况下使用",
            "获取渠道开关是否打开",
            "打开HBM页面具体进入的页面参考HbmIntent定义",
            "获取用户未读消息数量",
            "",
            "添加事件回调接口",
            "移除事件回调",
            "获取需要提醒的服务卡片信息",
            "获取访问的服务号列表",
            "获取某个时间之前的服务卡片消息"};

    private HbmSdkService hbmSDKService;
    //ListView
    private ListView listView;

    private INotify iNotify;
    private SdkListAdapter sdkListAdapter;
    private List<Map<String, Object>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_enter);

        initData();
        initHandler();
        initEvent();
    }

    private void initData() {
        listView = findViewById(R.id.list_sdkenter);
        list = new ArrayList();
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", title[i]);
            map.put("explanation", explanation[i]);
            map.put("result", " ");
            map.put("time", " ");
            map.put("input", " ");
            list.add(map);
        }
        sdkListAdapter = new SdkListAdapter(this, list);
        //为Listview设置适配器
        listView.setAdapter(sdkListAdapter);
    }

    /**
     * 初始化适配器需要的数据格式
     */
    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1) {
                    case 1: {
                        //成功，更新界面
                        sdkListAdapter.notifyDataSetChanged();// 更新列表数据
                        break;
                    }
                    case 2: {
                        //失败，反馈
                        HbmCode hbmCode = (HbmCode) msg.obj;
                        sdkListAdapter.notifyDataSetChanged();// 更新列表数据
                        if (hbmCode != null) {
                            SampleToast.showToast(SampleEnterActivity.this, "Error code: " + HbmCodeName.getHbmCodeName(hbmCode), Toast.LENGTH_SHORT);
                            HbmLog.e(TAG, HbmCodeName.getHbmCodeName(hbmCode));
                        }
                        break;
                    }
                    default:
                        sdkListAdapter.notifyDataSetChanged();// 更新列表数据
                        break;
                }
            }
        };
    }

    private int[] notifyValue = {INotify.AGREEMENT_CHANGED, INotify.ACCOUNT_QUIT, INotify.ACCOUNT_LOGIN,
            INotify.FOLLOWED_CHANGED, INotify.MSG_SRV_CHANGED, INotify.NEW_MSG, INotify.TRACK_SRV_MSG};

    // 操作枚举类
    public enum CaseEnum {
        CASE0() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfInit(map, position);
            }
        },
        CASE1() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfIsHbmAvailable(map, position);
            }
        },
        CASE2() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfUpgrade(map, position);
            }
        },
        CASE3() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfGetHbmAgreementState(map, position);
            }
        },
        CASE4() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfEnableHbmAgreement(map, position);
            }
        },
        CASE5() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfEnableChannlel(map, position, true);
            }
        },
        CASE6() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfEnableChannlel(map, position, false);
            }
        },
        CASE7() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfGetChannelStatus(map, position);
            }
        },
        CASE8() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfStartHbmActivity(map, view, position);
            }
        },
        CASE9() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfGetUnReadMsg(map, position);
            }
        },
        CASE10() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.updateNotifyStatus(flag);
            }
        },
        CASE11() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfAddNotify(map, view, position, flag, notifyFlag);
            }
        },
        CASE12() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfRemoveNotify(map, view, position, flag, notifyFlag);
            }
        },
        CASE13() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfgetSrvNotifyList(map, position);
            }
        },
        CASE14() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfgetVisitedPubList(map, position);
            }
        },
        CASE15() {
            public void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag) {
                sample.itemOfgetSrvMsgList(map, position);
            }
        };

        public abstract void doItem(SampleEnterActivity sample, Map<String, Object> map, int position, View view, boolean[] flag, int[] notifyFlag);
    }

    //初始化点击
    private void initClickEvent(boolean[] flag, int[] notifyFlag) {
        listView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            Map<String, Object> map = list.get(arg2);
            Map<Integer, CaseEnum> caseEnumMap = new HashMap<>();
            caseEnumMap.put(0, CaseEnum.CASE0);
            caseEnumMap.put(1, CaseEnum.CASE1);
            caseEnumMap.put(2, CaseEnum.CASE2);
            caseEnumMap.put(3, CaseEnum.CASE3);
            caseEnumMap.put(4, CaseEnum.CASE4);
            caseEnumMap.put(5, CaseEnum.CASE5);
            caseEnumMap.put(6, CaseEnum.CASE6);
            caseEnumMap.put(7, CaseEnum.CASE7);
            caseEnumMap.put(8, CaseEnum.CASE8);
            caseEnumMap.put(9, CaseEnum.CASE9);
            caseEnumMap.put(10, CaseEnum.CASE10);
            caseEnumMap.put(11, CaseEnum.CASE11);
            caseEnumMap.put(12, CaseEnum.CASE12);
            caseEnumMap.put(13, CaseEnum.CASE13);
            caseEnumMap.put(14, CaseEnum.CASE14);
            caseEnumMap.put(15, CaseEnum.CASE15);

            caseEnumMap.get(arg2).doItem(this, map, arg2, arg1, flag, notifyFlag);
        });
    }

    // 初始化事件
    private void initEvent() {
        final String[][] notifyTitle = {{"协议状态变更", "账号退出", "账号登录", "关注关系变更", "服务动态变更", "新消息", "打点事件"}};
        final boolean[] flag = new boolean[notifyValue.length];        //全局事件注册标记
        final int[] notifyFlag = {0};           //全局事件注册
        iNotify = (type, data) -> {
            HbmLog.d(TAG, " get Notify type: " + type);
            if ((notifyFlag[0] | type) == notifyFlag[0] && type != 0) {
                String strOut = "";
                int typeTmp = type;
                for (int i = 0; typeTmp > 0; i++, typeTmp /= 2)
                    if (typeTmp % 2 != 0)
                        strOut += flag[i] ? (notifyTitle[0][i] + ",") : "";
                if (strOut.endsWith(","))
                    strOut = strOut.substring(0, strOut.length() - 1);
                SampleToast.showToast(this, "获得如下事件变更通知: " + strOut, Toast.LENGTH_SHORT);
            }
        };
        initClickEvent(flag, notifyFlag);
    }

    // item：初始化操作
    private void itemOfInit(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        hbmSDKService = HbmSdkService.getInstance().init(this);
        map.put("time", (System.currentTimeMillis() - startTime) + " ms");
        Message msg = new Message();
        if (hbmSDKService != null) {
            map.put("result", "初始化成功");
            msg.arg1 = 1;
        } else {
            map.put("result", "初始化失败");
            msg.arg1 = 2;
        }
        list.set(position, map);
        handler.sendMessage(msg);


    }

    // item：环境可用
    private void itemOfIsHbmAvailable(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().isHbmAvailable(result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                map.put("result", result.getResult() ? "当前kit环境可用" : ("当前kit环境不可用" + HbmCodeName.getHbmCodeName(result.getHbmCode())));
                msg.arg1 = 1;
            } else {
                map.put("result", "判断当前Kit环境失败\nHbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                list.set(position, map);
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：升级
    private void itemOfUpgrade(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();//获取开始时间 毫秒
        HbmSdkService.getInstance().upgrade(this);
        map.put("result", "升级");
        map.put("time", (System.currentTimeMillis() - startTime) + " ms");
        list.set(position, map);
        Message msg = new Message();
        msg.arg1 = 1;
        handler.sendMessage(msg);
    }

    // item：获取协议状体
    private void itemOfGetHbmAgreementState(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getHbmAgreementStateV2(result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS && result.getResult() != null) {
                if (result.getResult().getState() == result.getResult().STATE_AGREE)
                    map.put("result", "协议同意");
                else if (result.getResult().getState() == result.getResult().STATE_CHANGED)
                    map.put("result", "协议变更");
                else
                    map.put("result", "协议未同意");
                msg.arg1 = 1;
            } else if (result.getResult() == null) {
                map.put("result", "获得协议状态失败\n返回值为null");
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            } else {
                map.put("result", "获得协议状态失败\nHbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：同意协议
    private void itemOfEnableHbmAgreement(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().enableHbmAgreement(result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                map.put("result", "成功同意协议");
                msg.arg1 = 1;
            } else {
                map.put("result", "同意协议失败\nHbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：打开/关闭渠道
    private void itemOfEnableChannlel(Map<String, Object> map, int position, boolean flag) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmLog.e(TAG, "channel " + flag);
        HbmSdkService.getInstance().enableChannel(flag, result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                map.put("result", (flag ? "打开" : "关闭") + "通道成功");
                msg.arg1 = 1;
            } else {
                map.put("result", (flag ? "打开" : "关闭") + "通道失败\nHbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：获取渠道状态
    private void itemOfGetChannelStatus(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getChannelStatus(result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                map.put("result", "当前channel" + (result.getResult() ? "已打开" : "未打开"));
                msg.arg1 = 1;
            } else {
                map.put("result", "查询channel状态失败\nHbmCode: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：页秒跳转
    private void itemOfStartHbmActivity(Map<String, Object> map, View view, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        String[] hbmIntentList = {HbmIntent.ACTION_TO_WEB, HbmIntent.ACTION_TO_SETTING,
                HbmIntent.ACTION_TO_THREAD, HbmIntent.ACTION_TO_FOLLOWED,
                HbmIntent.ACTION_TO_SRV, HbmIntent.ACTION_TO_USER_AGREEMENT, HbmIntent.ACTION_TO_PRIVACY_AGREEMENT, HbmIntent.ACTION_TO_SQUARE};

        RadioGroup radioGroup = view.findViewById(R.id.lv_radiogroup);
        int checkedId = radioGroup.getCheckedRadioButtonId();

        if (checkedId != -1) {
            String hbmIntent = hbmIntentList[(checkedId - 1) % hbmIntentList.length];
            String url = "";
            if (hbmIntent.equals(HbmIntent.ACTION_TO_WEB)) {
                url = "https://www.baidu.com";
            }
            HbmSdkService.getInstance().startHbmActivity(this, HbmIntent.create(this, hbmIntent)
                    .putExtra(HbmIntent.KEY_URL, url), result -> {
                Message msg = new Message();
                if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                    map.put("result", "跳转页面成功");
                    msg.arg1 = 1;
                } else {
                    map.put("result", "跳转页面失败: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                    msg.arg1 = 2;
                    msg.obj = result.getHbmCode();
                }
                map.put("time", (System.currentTimeMillis() - startTime) + " ms");
                list.set(position, map);
                handler.sendMessage(msg);
            });
        } else {
            SampleToast.showToast(this, "请选择需要跳转的页面", Toast.LENGTH_SHORT);
            map.put("result", "请选择需要跳转的页面");
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }
    }

    // item：获取未读消息
    private void itemOfGetUnReadMsg(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getUnReadMsg(result -> {
            Message msg = new Message();
            if (result.getHbmCode().getCode() == HbmCode.CODE_SUCCESS) {
                map.put("result", "获得未读消息成功：" + result.getResult());
                msg.arg1 = 1;
            } else {
                map.put("result", "获得未读消息失败: " + HbmCodeName.getHbmCodeName(result.getHbmCode()));
                msg.arg1 = 2;
                msg.obj = result.getHbmCode();
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    /**
     * 注册通知
     *
     * @param map        用于listview的当前item显示数据
     * @param view       当前item的view
     * @param position   当前item的位置
     * @param flag       全局事件记录标记
     * @param notifyFlag 记录当前notify的注册标记。长度为1的数组对象。
     */
    private void itemOfAddNotify(Map<String, Object> map, View view, int position, boolean[] flag, int[] notifyFlag) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        LinearLayout linearLayout = view.findViewById(R.id.lv_checkgroup);
        int notifyFlagTmp = 0;          //选择的待注册的事件的标记
        boolean flagTmp = false;        //用于记录是否存在新的事件需要注册

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
            if (checkBox.isChecked()) {
                notifyFlagTmp |= notifyValue[i];    //记录当前选择的标记
                flagTmp |= !flag[i];          //判断该当前选择的标记是否已经注册过。若当前所选事件标记 flag[i] 之前未注册（为0），则存在新事件需要注册
                flag[i] = true;                //更新全局变量  当前选择的事件一定会被注册，所以可以提前置为true
            }
        }
        if (flagTmp) {
            //存在新的事件需要注册
            if (notifyFlag[0] != 0) {
                HbmSdkService.getInstance().removeNotify(iNotify);    //移除已注册事件
                HbmLog.d(TAG, "removeNotify");
            }
            notifyFlag[0] = notifyFlag[0] | notifyFlagTmp;
            HbmLog.e(TAG, "当前注册事件：" + notifyFlag[0]);

            HbmSdkService.getInstance().addNotify(iNotify, notifyFlag[0]);
            HbmLog.d(TAG, "addNotify");
            map.put("result", "注册事件成功");
            map.put("flag", flag);
        } else if (notifyFlagTmp != 0) {
            //虽然有选择，但是事件已经注册过
            map.put("result", "已注册过这些事件");
        } else {
            //没有任何选择
            map.put("result", "请先选择需要注册事件");
        }
        map.put("time", (System.currentTimeMillis() - startTime) + " ms");
        list.set(position, map);
        Message msg = new Message();
        msg.arg1 = 1;
        handler.sendMessage(msg);
        updateNotifyStatus(flag);       //更新已注册事件列表显示
    }

    // item：移除通知
    private void itemOfRemoveNotify(Map<String, Object> map, View view,
                                    int position, boolean[] flag, int[] notifyFlag) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        if (notifyFlag[0] == 0) {
            map.put("result", "请先注册事件");
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        } else {
            LinearLayout linearLayout = view.findViewById(R.id.lv_checkgroup);
            int notifyFlagTmp = notifyFlag[0];          //选择的待移除的事件的标记
            boolean[] flagTmp = flag.clone();           //用于记录已注册的事件
            boolean changeFlag = true;                  //用于记录是否存在已注册事件需要移除
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                if (checkBox.isChecked()) {
                    changeFlag &= flag[i];          //判断该事件是否已经注册过  事件注册过 则 与真得真 事件未注册过 与假得假 选择中存在未注册得事件
                    notifyFlagTmp ^= notifyValue[i];        //事件已注册 消除该事件。对于未注册的事件 ，由于changeFlag的判断不为真，因此不会进行修改，不会影响到全局
                    flagTmp[i] = false;             //临时记录事件标记，由于修改可能不成功，因此不能直接修改全局flag
                }
            }

            if (changeFlag && notifyFlagTmp != notifyFlag[0]) {
                //存在已经注册的事件需要移除
                HbmSdkService.getInstance().removeNotify(iNotify);
                HbmLog.d(TAG, "removeNotify");
                if (notifyFlagTmp != 0) {
                    //将剩余的事件注册回去
                    HbmSdkService.getInstance().addNotify(iNotify, notifyFlagTmp);
                    HbmLog.d(TAG, "addNotify");
                    HbmLog.e(TAG, "当前注册事件：" + notifyFlagTmp);
                }

                notifyFlag[0] = notifyFlagTmp;      //更新已注册事件
                System.arraycopy(flagTmp, 0, flag, 0, flag.length); //已注册事件标记变更
                map.put("result", "移除注册事件成功");
            } else if (!changeFlag) {
                //有选择，但是选择的事件未注册过
                map.put("result", "选择中存在未注册的事件");
            } else {
                //没有选择需要移除的事件
                map.put("result", "请选择需要移除的事件");
            }
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            Message msg = new Message();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }
        updateNotifyStatus(flag);
    }

    // item：更新注册消息通知列表
    private void updateNotifyStatus(boolean[] flag) {
        final String[] notifyTitle = {"协议状态变更", "账号退出", "账号登录", "关注关系变更", "服务动态变更", "新消息", "打点事件"};
        Map<String, Object> map = list.get(ITEMID);
        String notifyFlagOut = "";
        for (int i = 0; i < flag.length; i++) {
            if (flag[i])
                notifyFlagOut = notifyFlagOut + notifyTitle[i] + ",";
        }
        if (notifyFlagOut.lastIndexOf(',') == notifyFlagOut.length() - 1) {
            notifyFlagOut = notifyFlagOut.substring(0, Math.max(notifyFlagOut.length() - 1, 0));
        }
        map.put("explanation", notifyFlagOut);
        list.set(ITEMID, map);
        Message msg = new Message();
        msg.arg1 = 1;
        handler.sendMessage(msg);
    }

    // item：获取注册消息列表
    private void itemOfgetSrvNotifyList(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getSrvNotifyList(-1, result -> {
            Message msg = new Message();
            int sizeInAll = result.getResult().getSizeInAll();
            int sizeInProcessing = result.getResult().getSizeInProcessing();
            List<SrvMsgData> msgDataList = result.getResult().getSrvMsgDataList();
            map.put("result", "获取到" + sizeInAll + "条消息");
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：获取访问列表
    private void itemOfgetVisitedPubList(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getVisitedPubList(-1, result -> {
            Message msg = new Message();
            int publisesize = result.getResult().size();
            map.put("result", "获取到" + publisesize + "条最近访问记录");
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }

    // item：霍夫服务卡片列表
    private void itemOfgetSrvMsgList(Map<String, Object> map, int position) {
        long startTime = System.currentTimeMillis();   //获取开始时间 毫秒
        HbmSdkService.getInstance().getSrvMsgList(System.currentTimeMillis(), -1, false, result -> {
            Message msg = new Message();
            int publisesize = result.getResult().size();
            map.put("result", "获取到" + publisesize + "条服务卡片消息");
            map.put("time", (System.currentTimeMillis() - startTime) + " ms");
            list.set(position, map);
            handler.sendMessage(msg);
        });
    }
}
