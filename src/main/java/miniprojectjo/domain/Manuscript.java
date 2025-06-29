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

    @PostPersist
    public void onPostPersist() {
        this.status = Status.WRITING;
        this.createdAt = new Date();
        this.updatedAt = new Date();
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
        repository().save(this);
    }

    // 출간 요청
public void requestPublish(RequestPublishCommand cmd) {
    if (this.status != Status.EDITED) {
        throw new IllegalStateException("편집된 상태의 원고만 출간 요청이 가능합니다.");
    }

    // 제목/내용 최신화
    if (cmd.getTitle() != null) this.title = cmd.getTitle();
    if (cmd.getContent() != null) this.content = cmd.getContent();

    this.status = Status.REQUESTED;
    this.updatedAt = new Date();
    repository().save(this);

    PublishingRequested event = new PublishingRequested(this);
    event.publishAfterCommit();
}

    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
