# Flux file sharing platform - Requirements

## Functional Requirements

1. Users should be able to upload a file from any device.
2. Users should be able to download a file from any device.
3. Users should be able to share a file with other users and view the files shared with them.
4. Files should be automatically synced across devices.
5. Users should be able to view files without downloading them.
6. Users should be able to search for files.
7. Users should be able to organize files into folders.
8. Users should be able to assign labels to files.

### File sharing
1. Users should be able to share files by providing a username.
2. Users should be able to share files via generated links.
3. Users should be able to assign roles while sharing via username (owner/editor/viewer).
4. Users should be able to assign roles while sharing via links (editor/viewer).
5. Owner role should allow to further share the file or change the permissions.
6. Users should be notified when a file has been shared to them.

### Search
1. Users should be able to search files by name.
2. Users should be able to search files by creation/modification date.
3. Users should be able to filter files by type/labels.
4. Users should be able to perform full text search for text documents and images.

## Non-Functional Requirements

1. The system should be highly available (prioritizing availability over consistency).
2. System should support files as large as 50GB.
3. The system should be secure and reliable.
4. The system should have a storage limit per user.
5. The system should support file versioning.
6. The system should scan files for viruses and malware.
