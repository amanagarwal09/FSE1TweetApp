package com.tweetapp.producer;

import com.tweetapp.entity.TweetEntity;
import com.tweetapp.utils.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TweetProducer {

    @Autowired
    private KafkaTemplate<String, TweetEntity> kafkaTemplate;

    /**
     * To send message to Kafka Topic
     *
     * @param tweetEntity
     */
    public void sendMessage(TweetEntity tweetEntity) {
        log.info("Kafka Topic Produce Message: {}", tweetEntity.toString());
        this.kafkaTemplate.send(ServiceConstants.TOPIC_NAME, tweetEntity);
    }

}
