package com.mousecall.mousecall.complaint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintCreateRequest {
    private String title;
    private String content;
}
