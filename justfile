default:
    just --list

docs:
    docker run -it --rm -p 8081:8080 -v $PWD/docs:/usr/local/structurizr structurizr/lite

mongo:
    docker run --name flux-mongo -d -p 27017:27017 mongo:8.0.11-noble

install:
    pre-commit install --hook-type pre-commit --hook-type pre-push
    cd view/flux-web && npm install

lint:
    cd view/flux-web && npm run lint

fmt: _fmt_service _fmt_view

_fmt_service:
    cd service/flux && ./gradlew spotlessCheck

_fmt_view:
    cd view/flux-web && npx prettier . --check

fmt_fix: _fmt_service_fix _fmt_view_fix

_fmt_service_fix:
    cd service/flux && ./gradlew spotlessApply

_fmt_view_fix:
    cd view/flux-web && npx prettier . --write

test:
    cd service/flux && ./gradlew test
