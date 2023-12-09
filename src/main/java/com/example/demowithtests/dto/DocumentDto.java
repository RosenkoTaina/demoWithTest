package com.example.demowithtests.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentDto {

    public Integer id;

    public String number;

    public String uuid;

    public LocalDateTime expireDate;

    public Boolean isHandled;

    public Integer employeeId;


}
