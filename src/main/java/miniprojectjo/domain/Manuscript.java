package miniprojectjo.domain;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.time.LocalDate;
// import java.util.Collections;
import java.util.Date;
// import java.util.List;
// import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import miniprojectjo.WritingApplication;
// import miniprojectjo.domain.ManuscriptEdited;
// import miniprojectjo.domain.ManuscriptRegistered;

@Entity
@Table(name = "Manuscript_table")
@Data
//<<< DDD / Aggregate Root
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    private AuthorId authorId;

    private Date createdAt;
    private Date updatedAt;

// ✅ 1. onPostPersist()에서 ManuscriptRegistered 이벤트 발행 추가

    @PrePersist
    public void onPrePersist() {
        this.status = Status.WRITING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
        
    @PostPersist
    public void onPostPersist() {
        ManuscriptRegistered registered = new ManuscriptRegistered(this);
        registered.publishAfterCommit();
    }



    @PreUpdate
    public void onPreUpdate() {
        if(this.status == Status.EDITED){
            ManuscriptEdited edited = new ManuscriptEdited(this);
            edited.publishAfterCommit();
        }
    }

    public static ManuscriptRepository repository() {
        return WritingApplication.applicationContext.getBean(ManuscriptRepository.class);
    }

    //<<< Clean Arch / Port Method
    // 원고 수정
    public void editManuscript(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
        this.status = Status.EDITED;
        this.updatedAt = new Date();
        // 저장은 컨트롤러/서비스에서
    }


    // 출간 요청
    public void requestPublish(RequestPublishCommand cmd) {

        if (this.status != Status.EDITED) {
            throw new IllegalStateException("현재 상태가 '" + this.status + "'이므로 출간 요청이 불가능합니다. 편집 상태(EDITED)여야 합니다.");
        }

        if (cmd.getTitle() != null && !cmd.getTitle().equals(this.title)) {
            this.title = cmd.getTitle();
        }

        if (cmd.getContent() != null && !cmd.getContent().equals(this.content)) {
            this.content = cmd.getContent();
        }

        this.status = Status.REQUESTED;
        this.updatedAt = new Date();


        // 저장은 컨트롤러 또는 서비스 계층에서 수행할 수도 있음. 지금은 유지
        repository().save(this);

        PublishingRequested event = new PublishingRequested(this);
        event.publishAfterCommit();

    }


    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
