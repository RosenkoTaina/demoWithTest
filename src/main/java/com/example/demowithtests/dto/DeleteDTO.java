package com.example.demowithtests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Date;

public record DeleteDTO(
        @Schema(description = "Id in DB")
        Integer id,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "Date delete")
        Date deleteDate) {

    public DeleteDTO(Integer id,
                     Date deleteDate
    ) {
        this.id = id;
        this.deleteDate = deleteDate != null ? deleteDate : Date.from(Instant.now());
    }


}
