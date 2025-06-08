package com.mousecall.mousecall.kafka.producer;

import com.mousecall.mousecall.kafka.dto.ComplaintKafkaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplaintProducer {
    private final KafkaTemplate<String, ComplaintKafkaDto> kafkaTemplate;
    private final String TOPIC_NAME = "complaint-topic";

    public void sendComplaint(ComplaintKafkaDto dto) {
        kafkaTemplate.send(TOPIC_NAME, dto);
    }
}
