package io.bushe.fastbuilder.User;

import cn.bmob.v3.BmobUser;
import io.bushe.bushe_application_security.QVM.QVMProtect;

@QVMProtect
public class BuSheID extends BmobUser {
    // UserType
    private String UserType;
    // BanType
    private String BanType;
    // UnBanDate
    private String UnBanDate;
    // BanDate
    private String BanDate;
    // BanDes
    private String BanDes;

    public String getUserType() {
        return UserType;
    }

    public String getBanType() {
        return BanType;
    }

    public BuSheID setBanType(String BanType) {
        this.UserType = BanType;
        return this;
    }

    public String getUnBanDate() {
        return UnBanDate;
    }

    public BuSheID setUnBanDate(String UnBanDate) {
        this.UnBanDate = UnBanDate;
        return this;
    }

    public String getBanDate() {
        return BanDate;
    }

    public String getBanDes() {
        return BanDes;
    }

    public BuSheID setBanDes(String BanDes) {
        this.BanDes = BanDes;
        return this;
    }
}
