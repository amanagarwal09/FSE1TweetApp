package com.tweetapp.consumer;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class TweetConsumer {
    @Autowired
    private TweetRepository tweetRepository;

    static final String TOPIC_NAME = ServiceConstants.TOPIC_NAME;
    static final String GROUP_ID_NAME = ServiceConstants.GROUP_ID;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID_NAME)

    /**
     * To consume message from Kafka Topic and save it to Database
     */
    public void consume(TweetEntity tweetEntity) {
        log.info("Kafka Topic Consume Message: {}", tweetEntity.toString());
        tweetEntity.setCreatedDate(LocalDateTime.now());
        tweetRepository.save(tweetEntity);
    }
}
