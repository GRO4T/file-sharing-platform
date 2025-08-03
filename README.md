# file-sharing-platform

## Prerequisites
* Git
* Docker
* Java Development Kit (JDK): Version 24 or higher
* npm: Version 10.9.2 or higher
* Node: Version 22.13.0 or higher
* [Just](https://github.com/casey/just): Version 1.40 or higher

## Installation

1. Clone the repository

```
git clone https://github.com/GRO4T/file-sharing-platform.git
cd file-sharing-platform
```

2. Install npm packages
```
cd view/flux-web
npm install
```

3. Set up Cloud Storage bucket
    1. Create GCP project.
    2. Create Cloud Storage bucket.
    3. Configure bucket [CORS](https://cloud.google.com/storage/docs/using-cors#command-line) to allow traffic from `http://localhost:5173`.
        ```
        # cors.json
        [
            {
                "origin": ["http://localhost:5173"],
                "method": ["PUT"],
                "responseHeader": ["Content-Type"],
                "maxAgeSeconds": 3600
            }
        ]
        ```
        ```
        gcloud storage buckets update gs://BUCKET_NAME --cors-file=CORS_CONFIG_FILE
        ```
    4. Create service accounts with the following permissions:
        * Service Account Token Creator
        * Storage Object Creator
        * Storage Object User
        * Storage Object Viewer

    5. Export service account key to JSON

4. Export the following environment variables:
    * `GOOGLE_APPLICATION_CREDENTIALS`: should point to the file with exported service account key
    * `FLUX_BLOB_STORAGE_PROJECT_ID`: project ID the storage bucket sits in
    * `FLUX_BLOB_STORAGE_BUCKET`: name of the storage bucket

## Running the Application

1. Start local MongoDB instance with `just mongo`

2. Start backend service
```
cd service/flux && ./gradlew bootRun
```
3. Start frontend
```
cd view/flux-web && npm run dev
```
4. Go to `http://localhost:5173`.
