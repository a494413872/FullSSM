
package com.lvmama.user;


import com.lvmama.scenic.comm.utils.PriceUtil;

import java.io.Serializable;
import java.util.Date;

public class UserUser implements Serializable {
    private static final long serialVersionUID = 8290356086399689311L;
    protected Long userId;
    protected String userNo;
    protected String groupId;
    protected String cityId;
    protected String userName;
    protected String userPassword;
    protected String passwordPrompt;
    protected String passwordAnswer;
    protected String realName;
    protected String address;
    protected String isLocked;
    protected Date createdDate;
    protected Date updatedDate;
    protected String isValid;
    protected String mobileNumber;
    protected String email;
    protected String gender;
    protected String idNumber;
    protected Long point;
    protected String nickName;
    protected String memo;
    protected Date birthday;
    protected String qqAccount;
    protected String msnAccount;
    protected String imageUrl;
    protected String spaceUrl;
    protected String registStepId;
    protected String isEmailChecked;
    protected String realPass;
    protected String cardId;
    protected String phoneNumber;
    protected String isAcceptEdm;
    protected String isMobileChecked;
    protected String zipCode;
    protected String memberShipCard;
    private String userStatus;
    protected Long isCashRegister = 1L;
    protected Date activeMscardDate;
    protected String homeCity;
    protected Long awardBalance;
    protected Long withdraw;
    protected String channel;
    protected String grade = "NORMAL";
    protected Date levelValidityDate;
    private Date lastLoginDate;
    private String nameIsUpdate;
    protected Long cashBalance;
    protected Long bonusBalance;
    protected String cancellationReason;
    private String registerIp;
    private Long registerPort;
    protected String wechatId;
    protected String subScribe;
    private boolean cashFrozen = false;

    public UserUser() {
    }

    public Long getId() {
        return this.userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public final String getUserId() {
        return this.getUserNo();
    }

    public final void setUserId(String userId) {
        this.userNo = userId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCityId() {
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPasswordPrompt() {
        return this.passwordPrompt;
    }

    public void setPasswordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
    }

    public String getPasswordAnswer() {
        return this.passwordAnswer;
    }

    public void setPasswordAnswer(String passwordAnswer) {
        this.passwordAnswer = passwordAnswer;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getIsValid() {
        return this.isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getPoint() {
        return this.point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getQqAccount() {
        return this.qqAccount;
    }

    public void setQqAccount(String qqAccount) {
        this.qqAccount = qqAccount;
    }

    public String getMsnAccount() {
        return this.msnAccount;
    }

    public void setMsnAccount(String msnAccount) {
        this.msnAccount = msnAccount;
    }

    public String getSpaceUrl() {
        return this.spaceUrl;
    }

    public void setSpaceUrl(String spaceUrl) {
        this.spaceUrl = spaceUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegistStepId() {
        return this.registStepId;
    }

    public void setRegistStepId(String registStepId) {
        this.registStepId = registStepId;
    }

    public String getIsEmailChecked() {
        return this.isEmailChecked;
    }

    public void setIsEmailChecked(String isEmailChecked) {
        this.isEmailChecked = isEmailChecked;
    }

    public String getRealPass() {
        return this.realPass;
    }

    public void setRealPass(String realPass) {
        this.realPass = realPass;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsAcceptEdm() {
        return this.isAcceptEdm;
    }

    public void setIsAcceptEdm(String isAcceptEdm) {
        this.isAcceptEdm = isAcceptEdm;
    }

    public String getHomeCity() {
        return this.homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getIsMobileChecked() {
        return this.isMobileChecked;
    }

    public void setIsMobileChecked(String isMobileChecked) {
        this.isMobileChecked = isMobileChecked;
    }

    public String getMemberShipCard() {
        return this.memberShipCard;
    }

    public void setMemberShipCard(String memberShipCard) {
        this.memberShipCard = memberShipCard;
    }

    public Date getActiveMscardDate() {
        return this.activeMscardDate;
    }

    public void setActiveMscardDate(Date activeMscardDate) {
        this.activeMscardDate = activeMscardDate;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getAwardBalance() {
        return this.awardBalance;
    }

    public void setAwardBalance(Long awardBalance) {
        this.awardBalance = awardBalance;
    }

    public Long getWithdraw() {
        return this.withdraw;
    }

    public void setWithdraw(Long withdraw) {
        this.withdraw = withdraw;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getLevelValidityDate() {
        return this.levelValidityDate;
    }

    public void setLevelValidityDate(Date levelValidityDate) {
        this.levelValidityDate = levelValidityDate;
    }

    public String getUserNo() {
        return this.userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Date getLastLoginDate() {
        return this.lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getNameIsUpdate() {
        return this.nameIsUpdate;
    }

    public void setNameIsUpdate(String nameIsUpdate) {
        this.nameIsUpdate = nameIsUpdate;
    }

    public String getCancellationReason() {
        return this.cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Long getCashBalance() {
        return this.cashBalance;
    }

    public void setCashBalance(Long cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Long getBonusBalance() {
        return this.bonusBalance;
    }

    public void setBonusBalance(Long bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public float getCashBalanceFloat() {
        return this.cashBalance != null ? PriceUtil.convertToYuan(this.cashBalance) : 0.0F;
    }

    public float getBonusBalanceFloat() {
        return this.bonusBalance != null ? PriceUtil.convertToYuan(this.bonusBalance) : 0.0F;
    }

    public String getRegisterIp() {
        return this.registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getWechatId() {
        return this.wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getSubScribe() {
        return this.subScribe;
    }

    public void setSubScribe(String subScribe) {
        this.subScribe = subScribe;
    }

    public Long getRegisterPort() {
        return this.registerPort;
    }

    public void setRegisterPort(Long registerPort) {
        this.registerPort = registerPort;
    }

    public boolean isCashNotFrozen() {
        return !this.isCashFrozen();
    }

    public boolean isCashFrozen() {
        return this.cashFrozen;
    }

    public void setCashFrozen(boolean cashFrozen) {
        this.cashFrozen = cashFrozen;
    }

    public String getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
