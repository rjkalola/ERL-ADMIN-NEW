
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.ClientInfo;

import java.util.List;

public class ClientsResponse extends BaseResponse {
    private List<ClientInfo> info;
    private int offset;

    public List<ClientInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ClientInfo> info) {
        this.info = info;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}

