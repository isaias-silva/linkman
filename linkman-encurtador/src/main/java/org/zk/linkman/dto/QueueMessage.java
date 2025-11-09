package org.zk.linkman.dto;

import java.util.Map;

public record QueueMessage<T extends Record>(String action,T data) {
}
