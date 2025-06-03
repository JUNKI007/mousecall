package com.mousecall.mousecall.complaint.controller;

import com.mousecall.mousecall.auth.token.JwtTokenProvider;
import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintResponse;
import com.mousecall.mousecall.complaint.service.ComplaintService;
import com.mousecall.mousecall.user.domain.User;
import com.mousecall.mousecall.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ComplaintControllerTest {

    @Mock
    private ComplaintService complaintService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private ComplaintController complaintController;

    @Test
    void createComplaints_WithFilesAndValidToken_ReturnsSuccessMessage() throws Exception {
        // given
        String token = "Bearer test.token";
        String username = "user1";
        ComplaintCreateRequest request = new ComplaintCreateRequest("title", "content");
        User user = User.builder().username(username).build();

        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);
        List<MultipartFile> files = List.of(file1, file2);

        when(jwtTokenProvider.getUsername("test.token")).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);

        // when
        ResponseEntity<String> response = complaintController.createComplaintWithFiles(token, request, files);

        // then
        assertEquals("민원 작성 완료", response.getBody());
        verify(complaintService).createComplaints(request, user, files);
    }


    @Test
    void getMyComplaints_WithValidToken_ReturnsComplaintList() {
        // given
        String token = "Bearer test.token";
        String username = "user1";
        User user = User.builder().username(username).build();
        Complaint complaint = Complaint.builder()
                .id(1L)
                .title("title")
                .content("content")
                .user(user)
                .build();

        when(jwtTokenProvider.getUsername("test.token")).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(complaintService.getUserComplaint(user)).thenReturn(List.of(complaint));

        // when
        ResponseEntity<List<ComplaintResponse>> response = complaintController.getMyComplaints(token);

        // then
        assertEquals(1, response.getBody().size());
        assertEquals("title", response.getBody().get(0).getTitle());
        verify(complaintService).getUserComplaint(user);
    }

}
