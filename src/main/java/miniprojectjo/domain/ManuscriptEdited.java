package miniprojectjo.domain;

// import java.time.LocalDate;
import java.util.*;
import lombok.*;
// import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
@Getter
@ToString
@NoArgsConstructor
public class ManuscriptEdited extends AbstractEvent {

    private Long id;
    private String title;
    private String content;
    private String status;
    private Date updatedAt;
    private Long authorId;

    public ManuscriptEdited(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.title = aggregate.getTitle();
        this.content = aggregate.getContent();
        this.status = aggregate.getStatus().name();
        this.updatedAt = aggregate.getUpdatedAt();
        if (aggregate.getAuthorId() != null) {
            this.authorId = aggregate.getAuthorId().getValue();
        }
    }
}
//>>> DDD / Domain Event
