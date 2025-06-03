package com.mousecall.mousecall.complaint.dto;

import com.mousecall.mousecall.attachment.domain.Attachment;
import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.domain.ComplaintStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ComplaintResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final ComplaintStatus status;
    private final List<String> attachments;

    public ComplaintResponse(Complaint complaint) {
        this.id = complaint.getId();
        this.title = complaint.getTitle();
        this.content = complaint.getContent();
        this.createdAt = complaint.getCreateAt();
        this.status = complaint.getStatus();
        this.attachments = complaint.getAttachments().stream()
                .map(Attachment::getFilePath)
                .toList();
    }
}
