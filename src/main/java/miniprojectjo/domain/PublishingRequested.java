package miniprojectjo.domain;

// import java.time.LocalDate;
// import java.util.*;
import lombok.*;
// import miniprojectjo.domain.*;
import miniprojectjo.infra.AbstractEvent;

//<<< DDD / Domain Event
// @Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PublishingRequested extends AbstractEvent {

    private Long id;
    private String title;
    private AuthorId authorId;
    private Status status;
    private String content;

    public PublishingRequested(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.title = aggregate.getTitle();
        this.authorId = aggregate.getAuthorId();
        this.status = aggregate.getStatus();
        this.content = aggregate.getContent();
    }
}
//>>> DDD / Domain Event
