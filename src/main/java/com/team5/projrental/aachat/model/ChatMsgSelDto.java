package com.team5.projrental.aachat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ChatMsgSelDto {

    @Range(min = 1)
    @Schema(defaultValue = "1")
    private int page;

    @Range(min = 1)
    private int ichat;

    @JsonIgnore
    private Long loginedIuser;

    @JsonIgnore
    private int startIdx;

    @JsonIgnore
    private int rowCount = 20;

    public void setPage(int page) {
        this.startIdx = (page-1) * this.rowCount;
    }

}
