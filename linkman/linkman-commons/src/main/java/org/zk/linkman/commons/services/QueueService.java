package org.zk.linkman.commons.services;

import org.zk.linkman.commons.dto.QueueMessage;

import java.util.function.Consumer;

public interface QueueService {

    public void send(String queue, QueueMessage<?> queueMessage) throws Exception;
    public void send(String queue, String groupId, QueueMessage<?> queueMessage)throws Exception;
    public void consume(String queue, Consumer<QueueMessage<?>> action) throws Exception;
}
