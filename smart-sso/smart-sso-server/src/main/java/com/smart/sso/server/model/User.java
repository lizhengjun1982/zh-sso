package com.smart.sso.server.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.smart.mvc.enums.TrueFalseEnum;
import com.smart.mvc.model.PersistentObject;
import com.smart.sso.server.weixin.WeiXinUserInfo;

/**
 * 用户
 *
 * @author Joe
 */
public class User extends PersistentObject {

    private static final long serialVersionUID = 1106412532325860697L;

    /**
     * 登录名
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 最后登录IP
     */
    private String lastLoginIp = "";
    /**
     * 登录总次数
     */
    private Integer loginCount = Integer.valueOf(0);
    /**
     * 最后登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 是否启用
     */
    private Boolean isEnable = Boolean.valueOf(true);

    private String realName = "";
    private String nickname = "";
    private String sex = "0";
    private String province = "";
    private String city = "";
    private String country = "";
    private String area="";
    private String provinceCode="";
    private String cityCode;
    private String areaCode;
    private String headImgUrl;
    private String mobile;
    private String wxOpenId;
    private String wxWebOpenId;
    private String wxUnionId;
    private String email;

    public User() {
    }

    public User(WeiXinUserInfo userInfo) {
        this.createTime = new Date();
        this.wxOpenId = userInfo.getOpenid();
        this.wxUnionId = userInfo.getUnionid();
        this.city = userInfo.getCity();
        this.country = userInfo.getCountry();
        this.nickname = userInfo.getNickname();
        this.province = userInfo.getProvince();
        this.sex = userInfo.getSex();
        this.headImgUrl = userInfo.getHeadimgurl();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getIsEnableStr() {
        return (isEnable != null && isEnable) ? TrueFalseEnum.TRUE.getLabel() : TrueFalseEnum.FALSE.getLabel();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxWebOpenId() {
        return wxWebOpenId;
    }

    public void setWxWebOpenId(String wxWebOpenId) {
        this.wxWebOpenId = wxWebOpenId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }
}
