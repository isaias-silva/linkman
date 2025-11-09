package org.zk.linkman.services.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.zk.linkman.dto.QueueMessage;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@ApplicationScoped
public class SqsService implements QueueService{
    @Inject
    SqsClient sqsClient;

    @Override
    public void send(String queue, QueueMessage<?> message) {
        final SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queue)
                .messageBody(message.toString())
                .build();

        sqsClient.sendMessage(request);
    }
}
