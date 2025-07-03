package miniprojectjo.domain;
 
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import miniprojectjo.infra.AbstractEvent;
 
import java.util.List;
 
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorRegistered extends AbstractEvent {
    private Long id;
    private String email;
    private String authorName;
    private String introduction;
    private String featuredWorks;
    private List<Object> portfolios; // Portfolio 클래스가 없으면 Object로
    private Boolean isApprove;
}