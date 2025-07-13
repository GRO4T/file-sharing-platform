package com.gro4t.flux;

public class GoogleCloudStorage implements BlobStorage {
    @Override
    public void upload(String bucket, String objectName, byte[] objectBytes) {

    }

    @Override
    public byte[] download(String bucket, String objectName) {
        return new byte[0];
    }
}
