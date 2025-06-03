package com.mousecall.mousecall.attachment.repository;

import com.mousecall.mousecall.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
