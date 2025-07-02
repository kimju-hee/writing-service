package miniprojectjo.infra;

import miniprojectjo.domain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

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

        return manuscriptRepository.save(manuscript);
    }

    /**
     * 원고 수정
     */
    @PutMapping("/{id}/edit")
    public Manuscript edit(@PathVariable Long id, @RequestBody RequestPublishCommand command) {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("✏️ 원고 수정 실패: 해당 ID의 원고를 찾을 수 없습니다. (id=" + id + ")"));

        manuscript.editManuscript(command.getTitle(), command.getContent());
        manuscript.setUpdatedAt(new Date());

        return manuscriptRepository.save(manuscript);
    }

    /**
     * 출간 요청
     */
    @PutMapping("/{id}/requestpublish")
    public Manuscript requestPublish(
        @PathVariable Long id,
        @RequestBody RequestPublishCommand requestPublishCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new Exception("📨 출간 요청 실패: 해당 ID의 원고가 존재하지 않습니다. (id=" + id + ")"));

        try {
            manuscript.requestPublish(requestPublishCommand);
            manuscript.setUpdatedAt(new Date());
            return manuscriptRepository.save(manuscript);
        } catch (IllegalStateException e) {
            throw e;
        }
    }

    /**
     * 출간 승인
     */
    @PutMapping("/{id}/approve")
    public void approve(@PathVariable Long id) {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("✅ 출간 승인 실패: 해당 ID의 원고를 찾을 수 없습니다. (id=" + id + ")"));
        new PublishingApproved(manuscript).publish();
    }

    /**
     * 출간 거절
     */
    @PutMapping("/{id}/reject")
    public void reject(@PathVariable Long id) {
        Manuscript manuscript = manuscriptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("❌ 출간 거절 실패: 해당 ID의 원고를 찾을 수 없습니다. (id=" + id + ")"));
        new PublishingRejected(manuscript).publish();
    }
}
