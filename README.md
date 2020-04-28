#stress-test-example
压力测试Spring Cloud项目

包含一个注册中心,一个网关,一个简单的授权服务,一个提供接口的服务


#网关过滤器顺序
OPTIONS过滤器 -> 白名单过滤器 -> 限流过滤器 -> Token过滤器 -> 基于标签限制过滤器 —> 日志记录过滤器  

#2020-04-19记录
不要再Spring Cloud Gateway里面调用feign接口,因为Gateway是用的webflux的异步响应式编程,如果调用了feign接口就是在异步的代码里加入了同步代码,
会导致异步失效,qps下降的厉害.

所以本项目换成了基于reactor-netty包中的HttpClient客户端（Gateway就是用的它,连接池是elastic模式,无上限）