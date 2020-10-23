
package com.app.erladmin.model.entity.response;


import com.app.erladmin.model.entity.info.ModuleInfo;

import java.util.List;

public class ModuleResponse extends BaseResponse {
    private List<ModuleInfo> info;

    public List<ModuleInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ModuleInfo> info) {
        this.info = info;
    }
}

