
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.ModuleInfo;
import com.app.erladmin.model.entity.request.SaveAddressRequest;

import java.util.List;

public class AddressListResponse extends BaseResponse {
    private List<ModuleInfo> area, cities;
    private List<SaveAddressRequest> info;

    public List<ModuleInfo> getArea() {
        return area;
    }

    public void setArea(List<ModuleInfo> area) {
        this.area = area;
    }

    public List<ModuleInfo> getCities() {
        return cities;
    }

    public void setCities(List<ModuleInfo> cities) {
        this.cities = cities;
    }

    public List<SaveAddressRequest> getInfo() {
        return info;
    }

    public void setInfo(List<SaveAddressRequest> info) {
        this.info = info;
    }
}

