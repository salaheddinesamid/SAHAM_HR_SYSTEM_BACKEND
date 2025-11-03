package com.saham.hr_system.dto;

import lombok.Data;

import java.util.List;

@Data
public class DocumentRequestDto {
    private List<String> documents;
    private String entity;
}
