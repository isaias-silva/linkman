package org.zk.linkman.commons.services.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.zk.linkman.commons.dto.QueueMessage;

import org.zk.linkman.commons.services.QueueService;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


import java.util.UUID;
import java.util.function.Consumer;

@ApplicationScoped
public class SqsService implements QueueService {
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

    @Override
    public void consume(String queue, Consumer<QueueMessage<?>> action) throws JsonProcessingException {

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queue)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .visibilityTimeout(30)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveRequest);

        for (Message message : response.messages()) {
            QueueMessage<?> queueMessage = objectMapper.readValue(message.body(), QueueMessage.class);

            action.accept(queueMessage);

            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queue)
                    .receiptHandle(message.receiptHandle())
                    .build());
        }
    }
}