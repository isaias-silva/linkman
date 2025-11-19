package org.zk.linkman.commons.services;

import java.util.function.Consumer;

import org.zk.linkman.commons.dto.QueueMessage;

public interface QueueService {

    public void send(String queue, QueueMessage<?> queueMessage) throws Exception;
    public void send(String queue, String groupId, QueueMessage<?> queueMessage)throws Exception;
    public void consume(String queue, Class<?> dataClass, Consumer<QueueMessage<?>> action) throws Exception;
}