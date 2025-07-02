package miniprojectjo.infra;

import miniprojectjo.domain.Manuscript;
import miniprojectjo.domain.ManuscriptRepository;
import miniprojectjo.domain.RequestPublishCommand;
import miniprojectjo.domain.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value = "/manuscripts")
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    /**
     * 원고 등록
     */
    @PostMapping
    public Manuscript create(@RequestBody Manuscript manuscript) {
        manuscript.setStatus(Status.WRITING);
        manuscript.setCreatedAt(new Date());
        manuscript.setUpdatedAt(new Date());

        Manuscript saved = manuscriptRepository.save(manuscript);
        return saved;
    }

    /**
     * 원고 수정
     */
    @PutMapping("/{id}/edit")
    public Manuscript edit(@PathVariable Long id, @RequestBody RequestPublishCommand command) {

        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Manuscript not found: id=" + id));

        manuscript.editManuscript(command.getTitle(), command.getContent());
        manuscript.setUpdatedAt(new Date());

        manuscriptRepository.save(manuscript);

        return manuscript;
    }

    /**
     * 출간 요청
     */
    @PutMapping("/{id}/requestpublish")
    public Manuscript requestPublish(
        @PathVariable(value = "id") Long id,
        @RequestBody RequestPublishCommand requestPublishCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new Exception("해당 ID의 원고가 존재하지 않습니다: id=" + id));


        try {
            manuscript.requestPublish(requestPublishCommand);
            manuscript.setUpdatedAt(new Date());

            manuscriptRepository.save(manuscript);
        } catch (IllegalStateException e) {
            throw e;
        }

        return manuscript;
    }
}
