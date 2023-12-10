package com.example.demowithtests.dto;

import com.example.demowithtests.util.annotations.dto.DocumentNumberFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentDto {
    @Schema(description = "Id in DB")
    public Integer id;

    @NotNull
    @Size(min = 6, max = 6, message = "Number must be 6 characters long")
    @DocumentNumberFormat
    @Schema(description = "Name of an document.", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    public String number;

    @Schema(description = "Auto generate field")
    public String uuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Expire date")
    public LocalDateTime expireDate;

    public Boolean isHandled;

    @Schema(description = "Id employee in DB", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    public Integer employeeId;


}
