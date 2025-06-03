package com.mousecall.mousecall.complaint.service;

import com.mousecall.mousecall.attachment.service.FileService;
import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.domain.ComplaintStatus;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintUpdateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintResponse;
import com.mousecall.mousecall.complaint.repository.ComplaintRepository;
import com.mousecall.mousecall.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void createComplaint_WithFiles_SavesWithAttachments() throws Exception {
        // given
        ComplaintCreateRequest request = new ComplaintCreateRequest("title", "content");

        User user = User.builder()
                .username("testuser")
                .password("password")
                .role("USER")
                .build();

        // 가짜 MultipartFile 2개 생성
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(file1, file2);

        // FileService 동작 모킹
        when(fileService.saveOriginalFile(any())).thenReturn("uploads/original/file.jpg");
        when(fileService.generateThumbnail(any())).thenReturn("uploads/thumbnail/file.jpg");
        when(fileService.applyWatermark(any())).thenReturn("uploads/watermark/file.jpg");

        // when
        complaintService.createComplaints(request, user, files);

        // then
        verify(complaintRepository, times(1)).save(any(Complaint.class));
        verify(fileService, times(2)).saveOriginalFile(any());
        verify(fileService, times(2)).generateThumbnail(any());
        verify(fileService, times(2)).applyWatermark(any());
    }


    @Test
    void getAllComplaints_WithPagination_ReturnsPage() {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createAt").descending());

        Complaint complaint = Complaint.builder()
                .title("title")
                .content("content")
                .status(ComplaintStatus.WAITING)
                .build();

        Page<Complaint> pageResult = new PageImpl<>(List.of(complaint));
        when(complaintRepository.findAll(pageRequest)).thenReturn(pageResult);

        // when
        Page<ComplaintResponse> result = complaintService.getAllComplaints(page, size);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(ComplaintStatus.WAITING, result.getContent().get(0).getStatus());
        verify(complaintRepository).findAll(pageRequest);
    }

    @Test
    void getComplaintById_WhenUserIsOwner_ReturnsComplaint() {
        // given
        User user = User.builder().username("user1").role("USER").build();
        Complaint complaint = Complaint.builder()
                .title("title")
                .content("content")
                .status(ComplaintStatus.IN_PROGRESS)
                .user(user)
                .build();

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        // when
        ComplaintResponse result = complaintService.getComplaintById(1L, user);

        // then
        assertEquals("title", result.getTitle());
        assertEquals(ComplaintStatus.IN_PROGRESS, result.getStatus());
        verify(complaintRepository).findById(1L);
    }

    @Test
    void getComplaintById_WhenUserIsNotOwner_ThrowsSecurityException() {
        // given
        User requester = User.builder().username("user2").role("USER").build();
        User owner = User.builder().username("user1").role("USER").build();

        Complaint complaint = Complaint.builder()
                .title("title")
                .status(ComplaintStatus.DONE)
                .user(owner)
                .build();

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        // when & then
        assertThrows(SecurityException.class, () -> complaintService.getComplaintById(1L, requester));
    }

    @Test
    void updateComplaint_WithValidUser_UpdatesSuccessfully() {
        // given
        User user = User.builder().username("user1").role("USER").build();
        Complaint complaint = Complaint.builder()
                .title("old")
                .content("old")
                .status(ComplaintStatus.WAITING)
                .user(user)
                .build();

        ComplaintUpdateRequest request = new ComplaintUpdateRequest("new title", "new content");

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        // when
        ComplaintResponse response = complaintService.updateComplaint(1L, request, user);

        // then
        assertEquals("new title", response.getTitle());
        assertEquals(ComplaintStatus.WAITING, response.getStatus()); // 상태 변화 없음
        verify(complaintRepository).findById(1L);
    }

    @Test
    void deleteComplaint_WhenUserIsOwner_DeletesSuccessfully() {
        // given
        User user = User.builder().username("user1").role("USER").build();
        Complaint complaint = Complaint.builder()
                .title("to be deleted")
                .status(ComplaintStatus.DONE)
                .user(user)
                .build();

        when(complaintRepository.findById(1L)).thenReturn(Optional.of(complaint));

        // when
        complaintService.deleteComplaint(1L, user);

        // then
        verify(complaintRepository).delete(complaint);
    }
}
