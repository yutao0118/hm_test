//package cn.itcast.gateway.filters;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//// 设置全局过滤器的执行顺序,值越小优先级越高
//@Order(-1)
//@Component
//public class AuthorizeFilter implements GlobalFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 1.获取请求参数
//        ServerHttpRequest request = exchange.getRequest();
//        MultiValueMap<String, String> params = request.getQueryParams();
//        // 2.获取authorization参数
//        String auth = params.getFirst("authorization");
//        // 3.校验
//        if ("admin".equals(auth)) {
//            // 放行
//            return chain.filter(exchange);
//        }
//        // 4.拦截
//        // 4.1.禁止访问，设置状态码
//        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//        // 4.2.结束处理
//        return exchange.getResponse().setComplete();
//    }
//}