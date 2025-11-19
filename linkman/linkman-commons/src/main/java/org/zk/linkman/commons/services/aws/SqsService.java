package org.zk.linkman.commons.services.aws;

import java.util.UUID;
import java.util.function.Consumer;

import org.zk.linkman.commons.dto.QueueMessage;
import org.zk.linkman.commons.services.QueueService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

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
    public void consume(String queue, Class<?> dataClass, Consumer<QueueMessage<?>> action) throws JsonProcessingException {

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queue)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .visibilityTimeout(30)
                .build();

        ReceiveMessageResponse response = sqsClient.receiveMessage(receiveRequest);

        for (Message message : response.messages()) {

            try {
                JavaType type = objectMapper.getTypeFactory().constructParametricType(QueueMessage.class, dataClass);

                QueueMessage<?> msg = objectMapper.readValue(message.body(), type);

                action.accept(msg);
            } catch (Exception e) {

                throw new RuntimeException(e);

            }finally {
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queue)
                        .receiptHandle(message.receiptHandle())
                        .build());
            }

        }
    }
}