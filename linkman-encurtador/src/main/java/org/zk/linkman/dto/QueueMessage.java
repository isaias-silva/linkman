package org.zk.linkman.dto;


public record QueueMessage<T extends Record>(String action,T data) {
}