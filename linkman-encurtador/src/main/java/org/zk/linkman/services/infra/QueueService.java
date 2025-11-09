package org.zk.linkman.services.infra;

import org.zk.linkman.dto.QueueMessage;

public interface QueueService {

    public void send(String queue, QueueMessage<?> queueMessage);
}
