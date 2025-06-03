package com.mousecall.mousecall.complaint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ComplaintCreateRequest {
    private String title;
    private String content;
}
