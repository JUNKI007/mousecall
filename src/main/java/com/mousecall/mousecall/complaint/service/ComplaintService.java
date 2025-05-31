package com.mousecall.mousecall.complaint.service;

import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintResponse;
import com.mousecall.mousecall.complaint.repository.ComplaintRepository;
import com.mousecall.mousecall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public void createComplaints(ComplaintCreateRequest complaintCreateRequest, User user){
        Complaint complaint = Complaint.builder()
                .title(complaintCreateRequest.getTitle())
                .content(complaintCreateRequest.getContent())
                .user(user)
                .build();
        complaintRepository.save(complaint);
    }

    public List<Complaint> getUserComplaint(User user){
        return complaintRepository.findByUser(user);
    }

    public Page<ComplaintResponse> getAllComplaints(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createAt").descending());
        return complaintRepository.findAll(pageRequest)
                .map(ComplaintResponse::new);
    }
}
