package miniprojectjo.domain;

// import java.time.LocalDate;
import java.util.*;
import lombok.*;
// import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
// @Data
@Getter
@ToString
@NoArgsConstructor
public class ManuscriptRegistered extends AbstractEvent {

    private Long id;
    private String title;
    private String content;
    private String status;
    private Long authorId;
    private Date createdAt;
    private Date updatedAt;


    public ManuscriptRegistered(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.status = aggregate.getStatus().name();
        this.createdAt = aggregate.getCreatedAt();
        this.updatedAt = aggregate.getUpdatedAt(); // 정확한 필드명 확인 필요
        if (aggregate.getAuthorId() != null) {
            this.authorId = aggregate.getAuthorId().getValue();
        }
    }
}
//>>> DDD / Domain Event
