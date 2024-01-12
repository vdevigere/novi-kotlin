# Novi-Kotlin
Novi-Kotlin is a super simple, kotlin based feature flag and A/B testing application. As the name implies, the application is buit on top of Kotlin. The platform is super simple to understand and use, but at the same time it can be very easily extended to support complex use cases.

## Installation
The application as setup out of the box, relies on a Postgres database and relies on 3 database tables. It is very easy to port the application to use any JPA supported relational database (such as MySQL, MariaDb etc) if needed. Follow the steps below to setup and launch the application

### Clone the Repo

```
git clone https://github.com/vdevigere/novi-kotlin.git
cd novi-kotlin
docker compose up
```

The docker compose script will download the latest postgres db image, configure it by creating the 3 tables and populate it with some sample data. Once the application is up and running you can access the application home page at:- 

[Novi-Kotlin Home Page](http://localhost:8080)
