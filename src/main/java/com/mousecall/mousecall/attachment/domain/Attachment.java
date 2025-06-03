package com.mousecall.mousecall.attachment.domain;

import com.mousecall.mousecall.complaint.domain.Complaint;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 업로드한 원본 파일 경로
    @Column(nullable = false)
    private String filePath;

    // 썸네일 이미지 경로
    private String thumbnailPath;

    // 워터마크 적용된 이미지 경로
    private String watermarkPath;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaint complaint;

    @PrePersist
    public void prePersist() {
        uploadedAt = LocalDateTime.now();
    }

    public void updateThumbnailPath(String path) {
        this.thumbnailPath = path;
    }

    public void updateWatermarkPath(String path) {
        this.watermarkPath = path;
    }
}
