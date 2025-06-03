package com.mousecall.mousecall.complaint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintUpdateRequest {
    private String title;
    private String content;
}
