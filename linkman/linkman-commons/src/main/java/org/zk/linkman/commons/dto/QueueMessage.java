package org.zk.linkman.commons.dto;


public record QueueMessage<T extends Record>(String action,T data) {
}