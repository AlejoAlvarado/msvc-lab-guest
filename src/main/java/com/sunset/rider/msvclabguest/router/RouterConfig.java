package com.sunset.rider.msvclabguest.router;


import com.sunset.rider.msvclabguest.handler.GuestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> rutasRoom(GuestHandler guestHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/guests"), guestHandler::findAll)
                .andRoute(RequestPredicates.GET("/guests/{id}"), guestHandler::findById)
                .andRoute(RequestPredicates.POST("/guests"), guestHandler::save)
                .andRoute(RequestPredicates.PUT("/guests/{id}"), guestHandler::update)
                .andRoute(RequestPredicates.DELETE("/guests/{id}"), guestHandler::delete);
    }
}
