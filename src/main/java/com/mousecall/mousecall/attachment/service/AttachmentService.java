package com.mousecall.mousecall.attachment.service;

import com.mousecall.mousecall.attachment.domain.Attachment;
import com.mousecall.mousecall.attachment.repository.AttachmentRepository;
import com.mousecall.mousecall.complaint.domain.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FileService fileService;

    public void saveAttachment(MultipartFile file, Complaint complaint) throws Exception {

        String originalPath = fileService.saveOriginalFile(file);

        String thumbnailPath = fileService.generateThumbnail(originalPath);

        String watermarkPath = fileService.applyWatermark(originalPath);

        Attachment attachment = Attachment.builder()
                .filePath(originalPath)
                .thumbnailPath(thumbnailPath)
                .watermarkPath(watermarkPath)
                .complaint(complaint)
                .build();

        attachmentRepository.save(attachment);
    }
}
