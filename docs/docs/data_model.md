# Flux file sharing platform - Data model

## File

File contents stored in blob storage, e.g. S3.

## File Metadata

* Id (string) - identifier.
* Name (string) - file name.
* Size (int) - file size.
* MimeType (string) - file type, e.g. "text/plain".
* Owner (string) - file owner (id).

## Account

* Id (string) - identifier.
* Username (string) - identifier.
* Email (string) - user email.
* Password (string) - password.
* Status (string) - account status, e.g. "active".
