# Steps to Reproduce

1. Launch the application
2. curl --request GET --url http://localhost:8080/hello  --header 'B3: 6ca651329a7d693a-5f034638765deb09-0' 

TraceIds are propagated, but not logged by **Netty**:

```
2023-05-04T12:05:58.538+02:00[0;39m [32mDEBUG [tracing-sampleebflux,,][0;39m [35m4504[0;39m [2m---[0;39m [2m[ctor-http-nio-5][0;39m [36mr.n.http.server.HttpServerOperations    [0;39m [2m:[0;39m [34f778ba, L:/127.0.0.1:8080 - R:/127.0.0.1:64838] New http connection, requesting read
[2m2023-05-04T12:05:58.539+02:00[0;39m [32mDEBUG [tracing-sampleebflux,,][0;39m [35m4504[0;39m [2m---[0;39m [2m[ctor-http-nio-5][0;39m [36mr.n.http.server.HttpServerOperations    [0;39m [2m:[0;39m [34f778ba, L:/127.0.0.1:8080 - R:/127.0.0.1:64838] Increasing pending responses, now 1
[2m2023-05-04T12:05:58.539+02:00[0;39m [32mDEBUG [tracing-sampleebflux,,][0;39m [35m4504[0;39m [2m---[0;39m [2m[ctor-http-nio-5][0;39m [36mreactor.netty.http.server.HttpServer    [0;39m [2m:[0;39m [34f778ba-1, L:/127.0.0.1:8080 - R:/127.0.0.1:64838] Handler is being applied: org.springframework.http.server.reactive.ReactorHttpHandlerAdapter@250d6428
[2m2023-05-04T12:05:58.540+02:00[0;39m [32m INFO [tracing-sampleebflux,6ca651329a7d693a,a159db76e539a845][0;39m [35m4504[0;39m [2m---[0;39m [2m[ctor-http-nio-5][0;39m [36me.tasgroup.poc.TracingWebfluxController [0;39m [2m:[0;39m Hello
[2m2023-05-04T12:05:58.897+02:00[0;39m [32mDEBUG [tracing-sampleebflux,,][0;39m [35m4504[0;39m [2m---[0;39m [2m[ctor-http-nio-5][0;39m [36mr.netty.http.client.HttpClientConnect   [0;39m [2m:[0;39m [dcb2a15e-1, L:/10.1.82.125:64839 - R:httpbin.org/34.235.32.249:443] Handler is being applied: {uri=https://httpbin.org/get, method=GET}

```
  
