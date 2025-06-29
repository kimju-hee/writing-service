package miniprojectjo.domain;

// import java.time.LocalDate;
// import java.util.*;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class RequestPublishCommand {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
