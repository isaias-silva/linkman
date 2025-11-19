package org.zk.linkman.commons.dto;


public record QueueMessage<T>(String action,T data) {
}