package miniprojectjo.infra;

import miniprojectjo.config.kafka.KafkaProcessor;
import miniprojectjo.domain.*;

import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    // 원고 등록 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ManuscriptRegistered'"
    )
    public void onManuscriptRegistered(@Payload ManuscriptRegistered event) {
        System.out.println("📝 [원고 등록] 이벤트 수신: " + event);
    }

    // 원고 수정 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ManuscriptEdited'"
    )
    public void onManuscriptEdited(@Payload ManuscriptEdited event) {
        System.out.println("✏️ [원고 수정] 이벤트 수신: " + event);
    }

    // 출간 요청 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishingRequested'"
    )
    public void onPublishingRequested(@Payload PublishingRequested event) {
        System.out.println("📨 [출간 요청] 이벤트 수신: " + event);
        Optional<Manuscript> optional = manuscriptRepository.findById(event.getId());
        optional.ifPresent(manuscript -> {
            manuscript.setStatus(Status.REQUESTED);
            manuscriptRepository.save(manuscript);
        });
    }

    // 출간 승인 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishingApproved'"
    )
    public void onPublishingApproved(@Payload PublishingApproved event) {
        System.out.println("✅ [출간 승인] 이벤트 수신: " + event);
        Optional<Manuscript> optional = manuscriptRepository.findById(event.getId());
        optional.ifPresent(manuscript -> {
            manuscript.setStatus(Status.DONE);
            manuscriptRepository.save(manuscript);
        });
    }

    // 출간 거절 이벤트 수신
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PublishingRejected'"
    )
    public void onPublishingRejected(@Payload PublishingRejected event) {
        System.out.println("❌ [출간 거절] 이벤트 수신: " + event);
        Optional<Manuscript> optional = manuscriptRepository.findById(event.getId());
        optional.ifPresent(manuscript -> {
            manuscript.setStatus(Status.EDITED);
            manuscriptRepository.save(manuscript);
        });
    }
}
