package com.mousecall.mousecall.complaint.domain;

import com.mousecall.mousecall.attachment.domain.Attachment;
import com.mousecall.mousecall.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    // Complaint 조회시 user정보가 필요할때만 가져오게 Lazy
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();


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

    public void updateAttachments(List<Attachment> attachments){
        this.attachments.clear();
        this.attachments.addAll(attachments);
    }
}
