default:
    just --list

docs:
    docker run -it --rm -p 8081:8080 -v $PWD/docs:/usr/local/structurizr structurizr/lite

mongo:
    docker run --name flux-mongo -d -p 27017:27017 mongo:8.0.11-noble

fmt:
    cd service/flux && ./gradlew spotlessCheck
    cd view/flux-web && npx prettier . --check

fmt_fix:
    cd service/flux && ./gradlew spotlessApply
    cd view/flux-web && npx prettier . --write
