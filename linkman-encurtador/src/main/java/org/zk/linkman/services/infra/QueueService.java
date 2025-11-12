package org.zk.linkman.services.infra;

import org.zk.linkman.dto.QueueMessage;

public interface QueueService {

    public void send(String queue, QueueMessage<?> queueMessage) throws Exception;
    public void send(String queue, String groupId, QueueMessage<?> queueMessage)throws Exception;
}