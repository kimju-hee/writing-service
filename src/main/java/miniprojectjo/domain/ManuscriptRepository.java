package miniprojectjo.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "manuscripts",
    path = "manuscripts"
)
public interface ManuscriptRepository
    extends PagingAndSortingRepository<Manuscript, Long> {

    // 특정 작가의 원고 조회
    List<Manuscript> findByAuthorId(AuthorId authorId);

    // 원고 상태로 조회 (e.g., EDITED, WRITING 등)
    List<Manuscript> findByStatus(Status status);

    // 제목 키워드 포함된 원고 조회
    List<Manuscript> findByTitleContaining(String keyword);
}
