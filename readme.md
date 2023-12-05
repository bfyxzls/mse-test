# webflux
## 1. 什么是webflux
webflux是一种响应式开发框架，提高服务器的吞吐量，降低服务器的资源消耗，提高服务器的性能。
## 2. webflux的核心组件
### DispatcherHandler
在Spring WebFlux中，DispatcherHandler是一个核心组件，用于处理HTTP请求的调度和分发。它负责将传入的HTTP请求映射到相应的处理器（Handler），并协调整个请求-响应过程中的各个环节。

具体来说，DispatcherHandler的主要作用包括：

1. **路由匹配**：DispatcherHandler根据请求的URL和其他条件，选择合适的处理器来处理请求。它可以根据路由规则将请求分发给对应的处理器函数或控制器方法。

2. **执行处理器**：一旦确定了要使用的处理器，DispatcherHandler就会调用相应的处理器函数或控制器方法来处理请求，并获取处理器返回的结果。

3. **错误处理**：DispatcherHandler负责捕获处理器执行过程中可能出现的异常，并将异常信息转换为合适的HTTP响应。

4. **请求-响应转换**：DispatcherHandler负责将处理器返回的结果转换为HTTP响应，并发送给客户端。

总之，DispatcherHandler在Spring WebFlux中扮演着请求调度和分发的关键角色，它负责协调整个请求-响应过程中的各个环节，确保请求能够得到正确地处理，并生成合适的HTTP响应返回给客户端。

### DispatcherHandler实例

> RouterFunction和HandlerFunction来动态扩展你的http请求，不修改现有代码的情况下，扩展新功能。

在实际的Spring WebFlux应用中，通常不会直接使用DispatcherHandler，而是通过RouterFunction和HandlerFunction来定义路由和处理器函数，然后交给WebHandler来处理。下面是一个简单的示例，演示了如何使用RouterFunction和HandlerFunction以及DispatcherHandler来构建一个完整的WebFlux应用：

```java
@Configuration
public class WebConfig {

    // 注册一个路由/hello
    @Bean
    public RouterFunction<ServerResponse> route(HelloHandler helloHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/hello"), helloHandler);
    }
}

// http请求
@Component
public class HelloHandler implements HandlerFunction<ServerResponse> {
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .bodyValue("Hello, WebFlux!");
    }
}

@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }
    
}
```

在这个示例中，我们首先创建了一个名为WebConfig的配置类，其中定义了一个RouterFunction来处理/hello路径的请求，并将其映射到HelloHandler的hello方法上。HelloHandler中定义了hello方法，用于处理请求并返回"Hello, WebFlux!"的响应。

在WebfluxApplication类中，我们创建了一个HttpHandler bean，它使用DispatcherHandler来处理HTTP请求。我们将RouterFunction注册到DispatcherHandler中，从而使得DispatcherHandler能够根据路由规则来分发请求。

通过这种方式，我们使用了RouterFunction和HandlerFunction来定义路由和处理器函数，然后将它们交给DispatcherHandler来处理，从而构建了一个完整的Spring WebFlux应用。

### Mono.defer
在Reactor中，`Mono.defer`方法的作用是延迟创建对应的Mono对象。它允许你通过一个`Supplier<Mono>`来创建Mono，这意味着Mono的创建会被推迟，直到订阅时才会执行Supplier中的逻辑。

这种方式可以用于一些需要根据订阅者动态生成Mono的场景，例如基于条件判断、外部状态等来确定最终要返回的Mono对象。这样可以确保Mono的创建和订阅之间的逻辑解耦，提高了灵活性和效率。

以下是一个简单的示例，演示了`Mono.defer`的使用：

```java
Mono<String> deferredMono = Mono.defer(() -> {
    if (condition) {
        return Mono.just("Condition is true");
    } else {
        return Mono.error(new RuntimeException("Condition is false"));
    }
});
```

在这个示例中，我们使用`Mono.defer`方法根据条件动态地创建了一个Mono对象。如果条件为true，就返回一个包含特定值的Mono；如果条件为false，就返回一个包含异常的Mono。无论哪种情况，实际的创建过程都会被推迟，直到有订阅者订阅这个Mono时才会执行。
