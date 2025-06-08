package com.mousecall.mousecall.kafka.consumer;

import com.mousecall.mousecall.complaint.domain.ComplaintStatus;
import com.mousecall.mousecall.complaint.repository.ComplaintRepository;
import com.mousecall.mousecall.kafka.dto.ComplaintKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintConsumer {

    private final ComplaintRepository complaintRepository;

    @KafkaListener(topics = "complaint-topic", groupId = "mousecall-group")
    public void listenComplaint(ComplaintKafkaDto dto) {
        log.info("📥 Kafka 수신: {}", dto);

        complaintRepository.findById(dto.getComplaintId()).ifPresentOrElse(
                complaint -> {
                    complaint.setStatus(ComplaintStatus.IN_PROGRESS);
                    complaintRepository.save(complaint);
                    log.info("민원 {} 상태 변경 완료", complaint.getId());
                },
                () -> log.warn("민원 ID {} 를 찾을 수 없음", dto.getComplaintId())
        );
    }
}
