package com.lyl.study.cloud.wechat.api.facade;

import com.lyl.study.cloud.wechat.api.dto.WxJsapiSign;
import com.lyl.study.cloud.wechat.api.dto.WxUser;

public interface WxOAuth2Facade {
    /**
     * 获取OpenID
     *
     * @param appId AppID
     * @param code  code
     * @return OpenID
     */
    String oauth2getOpenId(String appId, String code);

    /**
     * 获取微信用户信息
     *
     * @param appId  appID
     * @param openId OpenID
     * @return 微信用户信息
     */
    WxUser oauth2getUserInfo(String appId, String openId);

    /**
     * 创建Jsapi签名
     *
     * @param appId appID
     * @param url   url
     * @return Jsapi签名
     */
    WxJsapiSign createJsapiSignature(String appId, String url);
}