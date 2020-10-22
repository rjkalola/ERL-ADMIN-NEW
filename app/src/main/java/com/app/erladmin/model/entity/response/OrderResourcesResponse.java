
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.PickUpTimeInfo;
import com.app.erladmin.model.entity.request.SaveAddressRequest;

import java.util.List;

public class OrderResourcesResponse extends BaseResponse {
    private List<PickUpTimeInfo> pickup_hours;
    private SaveAddressRequest info;
    private int wallet;

    public List<PickUpTimeInfo> getPickup_hours() {
        return pickup_hours;
    }

    public void setPickup_hours(List<PickUpTimeInfo> pickup_hours) {
        this.pickup_hours = pickup_hours;
    }

    public SaveAddressRequest getInfo() {
        return info;
    }

    public void setInfo(SaveAddressRequest info) {
        this.info = info;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}



