
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.ServiceInfo;

import java.util.List;

public class ServiceItemsResponse extends BaseResponse {
    private List<ServiceInfo> info;

    public List<ServiceInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ServiceInfo> info) {
        this.info = info;
    }
}



