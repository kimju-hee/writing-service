package miniprojectjo.domain;

import lombok.*;
import miniprojectjo.infra.AbstractEvent;

@Getter
@ToString
@NoArgsConstructor
public class PublishingRejected extends AbstractEvent {
    private Long id;

    public PublishingRejected(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
    }
}
