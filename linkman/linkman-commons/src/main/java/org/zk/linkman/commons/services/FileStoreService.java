package org.zk.linkman.commons.services;

public interface FileStoreService {
    public String getFileURL(final String folder, final String fileName);

    public void storeFile(final String folder, final String fileName, final String contentType, byte[] bytes);

    public void deleteFile(final String folder, final String fileName);
}
