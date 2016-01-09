package com.vibeosys.rorderapp.data;

import java.util.List;

/**
 * Created by mahesh on 10/22/2015.
 */
public class Upload extends ServerSync {
    UploadUser user;

    public Upload(UploadUser user, List<TableDataDTO> tableDataList) {
        this.user = user;
        this.data = tableDataList;
    }

    public Upload() {

    }

    public UploadUser getUser() {
        return user;
    }

    public void setUser(UploadUser user) {
        this.user = user;
    }

}
