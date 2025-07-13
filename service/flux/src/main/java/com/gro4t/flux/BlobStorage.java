package com.gro4t.flux;

public interface BlobStorage {
    void upload(String bucket, String objectName, byte[] objectBytes);
    byte[] download(String bucket, String objectName);
}
