package com.mousecall.mousecall.complaint.controller;

import com.mousecall.mousecall.auth.token.JwtTokenProvider;
import com.mousecall.mousecall.complaint.dto.ComplaintCreateRequest;
import com.mousecall.mousecall.complaint.dto.ComplaintResponse;
import com.mousecall.mousecall.complaint.dto.ComplaintUpdateRequest;
import com.mousecall.mousecall.complaint.service.ComplaintService;
import com.mousecall.mousecall.user.domain.User;
import com.mousecall.mousecall.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@RequestMapping("/complaints")
public class ComplaintController {
    private final ComplaintService complaintService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping
    public ResponseEntity<String> createComplaints(@RequestHeader("Authorization") String bearerToken,
                                                   @RequestBody ComplaintCreateRequest dto){

        // Bearer 라는 글자까지 자르고 그뒤(토큰값)을 사용하기 위해 substring 7
        String username = jwtTokenProvider.getUsername(bearerToken.substring(7));
        User user = userRepository.findByUsername(username);
        complaintService.createComplaints(dto, user);
        return ResponseEntity.ok("민원 작성 완료");
    }


    // 성능고려 : 한 유저가 갖는 민원의 수는 많지 않아 오버헤드는 아주 작기때문에 stream 방식으로 조회
    // stream은 10-40개 정도의 데이터가 적당
    @GetMapping("/me")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(@RequestHeader("Authorization") String bearerToken){
        String username = jwtTokenProvider.getUsername(bearerToken.substring(7));
        User user = userRepository.findByUsername(username);

        List<ComplaintResponse> response = complaintService.getUserComplaint(user).stream()
                .map(ComplaintResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    // 성능고려 : 모든 유저의 민원을 불러내는데 데이터가 많아 페이지식으로
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ComplaintResponse>> getAllComplaint(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(complaintService.getAllComplaints(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintsById(@PathVariable Long id,
                                                               @RequestHeader("Authorization") String bearerToken){

        String username = jwtTokenProvider.getUsername(bearerToken.substring(7));
        User user = userRepository.findByUsername(username);

        return ResponseEntity.ok(complaintService.getComplaintById(id, user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaint(
            @PathVariable Long id,
            @RequestBody ComplaintUpdateRequest request,
            @RequestHeader("Authorization") String bearerToken
    ) {
        String username = jwtTokenProvider.getUsername(bearerToken.substring(7));
        User user = userRepository.findByUsername(username);

        ComplaintResponse response = complaintService.updateComplaint(id, request, user);
        return ResponseEntity.ok(response);
    }
}
