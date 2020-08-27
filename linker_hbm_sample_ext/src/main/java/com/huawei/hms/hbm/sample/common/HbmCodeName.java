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
package com.huawei.hms.hbm.sample.common;

import com.huawei.hms.hbm.api.bean.HbmCode;

import java.util.HashMap;
import java.util.Map;

/**
 * java类作用描述
 *
 * @author l00568035
 * @since 2020/6/2 9:47
 */
public class HbmCodeName extends HbmCode {
    public static String getHbmCodeName(HbmCode hbmCode) {
        Map<Integer, String> map = new HashMap<>();
        map.put(HbmCode.CODE_ACCOUNT_ERROR, "账号错误\nCODE_ACCOUNT_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_AGREEMENT_DISAGREE, "协议未同意\nCODE_AGREEMENT_DISAGREE"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_ACCOUNT_CHILD, "儿童账号\nCODE_DETAIL_ACCOUNT_CHILD"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_ACCOUNT_INVALID, "账号失效，需要重新拉账号登录\nCODE_DETAIL_ACCOUNT_INVALID"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_ACCOUNT_OVERSEAS, "海外账号\nCODE_DETAIL_ACCOUNT_OVERSEAS"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_HMS_KIT_NEED_UPGRADE, "kit需要升级\nCODE_DETAIL_HMS_KIT_NEED_UPGRADE"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_HMS_NEED_UPGRADE, "HMSCore需要升级\nCODE_DETAIL_HMS_NEED_UPGRADE"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_HMS_UPGRADING, "框架正在升级中\nCODE_DETAIL_HMS_UPGRADING"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_HMS_CORE_ERROR, "HMS框架错误\nCODE_HMS_CORE_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_NET_ERROR, "网络错误\nCODE_NET_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_SERVER_ERROR, "服务器接口错误\nCODE_SERVER_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_SUCCESS, "成功\nCODE_SUCCESS \ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_ACCOUNT_LOGOFF, "账号未登录\nCODE_DETAIL_ACCOUNT_LOGOFF"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_SNS_ERROR, "关注关系错误\nCODE_SNS_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_HBM_CLICK_TO_FAST, "Activity点击过快\nCODE_HBM_CLICK_TO_FAST"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_HBM_ERROR, "HBM程序内部错误\nCODE_HBM_ERROR"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        map.put(HbmCode.CODE_DETAIL_HMS_SERVICE_UPGRADE, "Kit接口不存在，需要升级Kit"
                + "\ndetailCode: " + hbmCode.getDetailCode());
        return map.get(hbmCode.getCode());
    }
}
