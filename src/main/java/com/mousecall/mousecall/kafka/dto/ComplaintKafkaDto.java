package com.mousecall.mousecall.kafka.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintKafkaDto {
    private Long complaintId;
    private String title;
    private String content;
    private Long userId;

}
