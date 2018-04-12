package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 100096 on 2017/4/12.
 */
public class PaInfo3RepDTO extends WbspBaseDTO {
    PaInfo3DTO data = new PaInfo3DTO();
    public PaInfo3DTO getData() {
        return data;
    }

    public void setData(PaInfo3DTO data) {
        this.data = data;
    }
}
