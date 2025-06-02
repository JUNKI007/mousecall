package com.mousecall.mousecall.complaint.service;

import com.mousecall.mousecall.complaint.domain.Complaint;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.repository.ComplaintRepository;
import com.mousecall.mousecall.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void 민원_생성_성공() {
        // given
        ComplaintCreateRequest request = new ComplaintCreateRequest("제목", "내용");
        User user = new User("username", "password", "USER");

        // when
        complaintService.createComplaints(request, user);

        // then
        verify(complaintRepository, times(1)).save(any(Complaint.class));
    }
}
