package miniprojectjo.domain;

import lombok.*;
import miniprojectjo.infra.AbstractEvent;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PublishingApproved extends AbstractEvent {
    private Long id;

    public PublishingApproved(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
    }
}
