package com.mousecall.mousecall.complaint.service;

import com.mousecall.mousecall.attachment.domain.Attachment;
import com.mousecall.mousecall.attachment.service.FileService;
import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.domain.ComplaintStatus;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintResponse;
import com.mousecall.mousecall.complaint.dto.ComplaintUpdateRequest;
import com.mousecall.mousecall.complaint.repository.ComplaintRepository;
import com.mousecall.mousecall.exception.ComplaintNotFoundException;
import com.mousecall.mousecall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final FileService fileService;



    // 민원 생성
    public void createComplaints(ComplaintCreateRequest request, User user, List<MultipartFile> files) throws IOException {
        Complaint complaint = Complaint.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(ComplaintStatus.WAITING)
                .user(user)
                .build();

        if (files != null && !files.isEmpty()) {
            List<Attachment> attachments = files.stream().map(file -> {
                try {
                    String originalPath = fileService.saveOriginalFile(file);
                    String thumbnailPath = fileService.generateThumbnail(originalPath);
                    String watermarkPath = fileService.applyWatermark(originalPath);

                    return Attachment.builder()
                            .filePath(originalPath)
                            .thumbnailPath(thumbnailPath)
                            .watermarkPath(watermarkPath)
                            .complaint(complaint)
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException("파일 처리 중 오류 발생", e);
                }
            }).toList();

            complaint.updateAttachments(attachments);
        }

        complaintRepository.save(complaint);
    }


    // user 1명이 작성한 민원리스트 조회
    public List<Complaint> getUserComplaint(User user){
        return complaintRepository.findByUser(user);
    }

    // 민원 전체 조회
    public Page<ComplaintResponse> getAllComplaints(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createAt").descending());
        return complaintRepository.findAll(pageRequest)
                .map(ComplaintResponse::new);
    }

    // 민원아이디로 특정 민원 조회 (본인 및 관리자만 가능)
    public ComplaintResponse getComplaintById(Long id, User user) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException(id));

        boolean isOwner = complaint.getUser().getUsername().equals(user.getUsername());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin) {
            throw new SecurityException("민원 조회 권한이 없습니다.");
        }

        return new ComplaintResponse(complaint);
    }

    // 민원 수정 (본인 및 관리자만 가능)
    public ComplaintResponse updateComplaint(Long id, ComplaintUpdateRequest complaintUpdateRequest, User user){
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException(id));

        boolean isOwner = complaint.getUser().getUsername().equals(user.getUsername());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if(!isOwner && !isAdmin){
            throw new SecurityException("민원 조회 권한이 없습니다.");
        }
        complaint.updateTitle(complaintUpdateRequest.getTitle());
        complaint.updateContent(complaintUpdateRequest.getContent());
        return new ComplaintResponse(complaint);
    }

    // 민원 삭제
    public void deleteComplaint(Long id, User user) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException(id));

        boolean isOwner = complaint.getUser().getUsername().equals(user.getUsername());

        if (!isOwner) {
            throw new SecurityException("본인의 민원만 삭제할 수 있습니다.");
        }

        complaintRepository.delete(complaint);
    }

}
