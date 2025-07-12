workspace "Flux" "Flux file sharing platform" {

    !identifiers hierarchical
    !adrs decisions
    !docs docs

    model {
        u = person "User"
        ss = softwareSystem "File Sharing System" {
            wa = container "Web Application" {
                technology "TypeScript and React"
            }
            as = container "API Service" {
                technology "Spring Boot"
                auth = component "Auth Controller" {
                    technology "Spring MVC Rest Controller + OAuth2"
                    description "Generates OAuth2 Access Tokens."
                }
                file = component "File Controller" {
                    technology "Spring MVC Rest Controller"
                    description "Allows users to add, remove and share files."
                }
                account = component "Account Controller" {
                    technology "Spring MVC Rest Controller"
                    description "Allows users to manage their accounts (settings and password)."
                }
                search = component "Search Controller" {
                    technology "Spring MVC Rest Controller"
                    description "Allows users to search for files."
                }
            }
            db = container "Database" {
                technology "MongoDB"
                tags "Database"
            }
        }

        u -> ss.wa "Views, uploads, downloads and shares files using"

        ss.wa -> ss.as.file "Makes API calls to" "JSON/HTTPS"
        ss.wa -> ss.as.account "Makes API calls to" "JSON/HTTPS"
        ss.wa -> ss.as.search "Makes API calls to" "JSON/HTTPS"

        ss.as.file -> ss.as.auth "Requests Access Token"
        ss.as.account -> ss.as.auth "Requests Access Token"
        ss.as.search -> ss.as.auth "Requests Access Token"

        ss.as -> ss.db "Reads from and writes to" "SQL/TLS"
    }

    views {
        systemContext ss "Diagram1" {
            include *
            autolayout tb
        }

        container ss "Diagram2" {
            include *
            autolayout tb
        }

        component ss.as "Diagram3" {
            include *
            autoLayout tb
        }

        styles {
            element "Element" {
                color #ffffff
            }
            element "Person" {
                background #05527d
                shape person
            }
            element "Software System" {
                background #066296
            }
            element "Container" {
                background #0773af
            }
            element "Component" {
                background #6CAFE0
            }
            element "Database" {
                shape cylinder
            }
        }
    }

    configuration {
        scope softwaresystem
    }

}