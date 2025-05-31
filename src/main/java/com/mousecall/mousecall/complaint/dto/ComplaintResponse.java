package com.mousecall.mousecall.complaint.dto;

import com.mousecall.mousecall.complaint.domain.Complaint;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ComplaintResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;

    public ComplaintResponse(Complaint complaint) {
        this.id = complaint.getId();
        this.title = complaint.getTitle();
        this.content = complaint.getContent();
        this.createdAt = complaint.getCreateAt();
    }
}
