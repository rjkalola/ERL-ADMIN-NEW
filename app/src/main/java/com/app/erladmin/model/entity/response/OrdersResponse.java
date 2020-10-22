
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.OrderInfo;

import java.util.List;

public class OrdersResponse extends BaseResponse {
    private List<OrderInfo> info;
    private int offset;

    public List<OrderInfo> getInfo() {
        return info;
    }

    public void setInfo(List<OrderInfo> info) {
        this.info = info;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}

