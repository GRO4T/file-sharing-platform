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
            ag = container "API Gateway" {
                description "Handles authentication, routing and rate limiting"
            }
            as = container "API Service" {
                technology "Spring Boot"
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
            dn = container "NoSQL Database" {
                technology "MongoDB"
                tags "Database"
            }
            db = container "Blob Storage" {
                technology "Google Cloud Storage"
                tags "Database"
            }
            cdn = container "CDN" {
                tags "Database"
            }
        }

        u -> ss.wa "Views, uploads, downloads and shares files using"

        ss.wa -> ss.ag "Makes API calls to" "JSON/HTTPS"
        ss.wa -> ss.db "Uploads, downloads files" 
        ss.wa -> ss.cdn "Downloads frequently accessed files"

        ss.ag -> ss.as.file "Routes API calls to" "JSON/HTTPS"
        ss.ag -> ss.as.account "Routes API calls to" "JSON/HTTPS"
        ss.ag -> ss.as.search "Routes API calls to" "JSON/HTTPS"

        ss.as -> ss.dn "Reads from and writes to" "SQL/TLS"
        ss.db -> ss.as.file "Notifies that the file has been uploaded"
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