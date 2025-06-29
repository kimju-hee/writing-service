package miniprojectjo.infra;

import miniprojectjo.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ManuscriptHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Manuscript>> {

    @Override
    public EntityModel<Manuscript> process(EntityModel<Manuscript> model) {
        Manuscript entity = model.getContent();
        if (entity != null && entity.getId() != null) {
            Long id = entity.getId();
            model.add(Link.of("/manuscripts/" + id + "/edit").withRel("edit"));
            model.add(Link.of("/manuscripts/" + id + "/request-publish").withRel("request-publish"));
        }
        return model;
    }
}
