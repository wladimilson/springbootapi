# Springboot Api

This sample is based in [wladimilson/springbootapi](https://github.com/wladimilson/springbootapi) write from [Wladimilson M. Nascimento](https://www.treinaweb.com.br/blog/autor/wladimilson-m-nascimento)

I included in project the files [Dockerfile](Dockerfile) and [docker-compose](docker-compose.yml) and show in this document how to build java application and deploy the infrastructure.

Please read the comments in the files [Dockerfile](Dockerfile) and [docker-compose](docker-compose.yml) to more informations. 

---

* [Criando uma API REST com o Spring Boot](https://www.treinaweb.com.br/blog/criando-uma-api-rest-com-o-spring-boot)

## build and run
You need only [docker](https://docs.docker.com/get-docker/)  in your machine.
You dont need `javac` (JAVA JDK®) or `java` (JAVA Runtime®) to build or run the application.
### run application
`docker-compose up --build`
### run application without log
`docker-compose up --build --detach`

---

After run the `docker-compose` command the application is started in your machine.

![stack](./img/docker-compose.png)

## access application
* http://localhost:8080/swagger-ui.html
### get token
```bash
$ curl http://localhost:8080/login  -H 'Content-Type: application/json' -d '{"username":"admin", "password":"password"}' --silent | jq '.token' | xargs printf "Basic %s\n"
Basic eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYzMzg4MDg1M30.nCBiANNApoRGOUTRQKc59RAHnMVPzT7krW-U9Zv_ZX9eVH9WeAoZio4gE56ceOv59MjO5OVYKsXVuAe8fVnpcA

$ curl http://localhost:8080/login  -H 'Content-Type: application/json' -d '{"username":"admin", "password":"password"}' --silent | jq
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYzMzg3NjYwN30.ZoSddm5qy18C2g2blU9xbhmd7VwtNWjGL63Mai4aIsxgEE5UhyFuUiB2g_D9RmlbloG-LHYHrnWI3up2JlUJlA"
}
```

## access database
* http://localhost:9090

|key|value|
|---|-----|
|host|mysql|
|user|root|
|passwd|rootpwd|
---

[Pedro Robson Leão](mailto:pedro.leao@gmail.com)