package com.mousecall.mousecall.complaint.domain;

import com.mousecall.mousecall.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    // Complaint 조회시 user정보가 필요할때만 가져오게 Lazy
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime createAt;

    @PrePersist
    public void prePersist(){
        createAt = LocalDateTime.now();
    }


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
