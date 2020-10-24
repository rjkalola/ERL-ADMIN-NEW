
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.ChatUserInfo;

import java.util.List;

public class ChatListResponse extends BaseResponse {
    private List<ChatUserInfo> info;

    public List<ChatUserInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ChatUserInfo> info) {
        this.info = info;
    }

}

