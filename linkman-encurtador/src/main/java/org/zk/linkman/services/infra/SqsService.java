package org.zk.linkman.services.infra;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.zk.linkman.dto.QueueMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@ApplicationScoped
public class SqsService implements QueueService{
    @Inject
    SqsClient sqsClient;
    @Inject
    ObjectMapper objectMapper;

    @Override
    public void send(String queue, QueueMessage<?> message) throws JsonProcessingException {
        final String json = objectMapper.writeValueAsString(message);

        final SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queue)
                .messageBody(json)
                .build();

        sqsClient.sendMessage(request);
    }


    @Override
    public void send(final String queue, final String groupId, final QueueMessage<?> message) throws Exception {
        final String json = objectMapper.writeValueAsString(message);

        final SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queue)
                .messageGroupId(groupId)
                .messageDeduplicationId(UUID.randomUUID().toString())
                .messageBody(json)
                .build();

        sqsClient.sendMessage(request);
    }
}