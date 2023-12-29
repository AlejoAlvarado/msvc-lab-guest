package com.sunset.rider.msvclabguest.handler;

import com.sunset.rider.msvclabguest.model.Guest;
import com.sunset.rider.msvclabguest.model.request.GuestRequest;
import com.sunset.rider.msvclabguest.service.GuestService;
import com.sunset.rider.msvclabguest.utils.ErrorNotFound;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class GuestHandler {
    private GuestService guestService;
    private Validator validator;

    public GuestHandler(GuestService guestService, Validator validator){
        this.validator = validator;
        this.guestService = guestService;
    }
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(guestService.findAll(), Guest.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return guestService.findById(id)
                .flatMap(guest -> ServerResponse
                        .ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(guest)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters.fromValue(ErrorNotFound.error(id))));
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<GuestRequest> guestRequest = request.bodyToMono(GuestRequest.class);

        return guestRequest
                .flatMap(rq -> {
                    Errors errors = new BeanPropertyBindingResult(rq, GuestRequest.class.getName());
                    validator.validate(rq, errors);


                    if (errors.hasErrors()) {
                        Map<String, Object> erroresMap = new HashMap<>();
                        List<String> errorList = new ArrayList<>();
                        errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                        erroresMap.put("errores", errorList);
                        erroresMap.put("timestamp", LocalDateTime.now());

                        return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
                    } else {
                        return guestService.save(buildGuest(rq, null,null))
                                .flatMap(room -> ServerResponse.created(URI.create("/guests/".concat(room.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(room)));
                    }

                })
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;

                    return Mono.error(errorResponse);
                });
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<GuestRequest> guestRequestMono = serverRequest.bodyToMono(GuestRequest.class);

        return guestService.findById(id)
                .flatMap(guest ->
                {

                    Errors errors = new BeanPropertyBindingResult(guestRequestMono, GuestRequest.class.getName());
                    validator.validate(guestRequestMono, errors);


                    if (errors.hasErrors()) {
                        Map<String, Object> erroresMap = new HashMap<>();
                        List<String> errorList = new ArrayList<>();
                        errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                        erroresMap.put("errores", errorList);

                        return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
                    } else {
                        return guestRequestMono
                                .flatMap(rq -> guestService.update(buildGuest(rq, id, guest)))
                                .flatMap(roomDb -> ServerResponse
                                        .created(URI.create("/guests/".concat(roomDb.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(roomDb)));
                    }

                })
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters.fromValue(ErrorNotFound.error(id))))
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;

                    return Mono.error(errorResponse);
                });
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return guestService.delete(id)
                .then(ServerResponse.noContent().build());
    }

    public Guest buildGuest(GuestRequest guestRequest, String id, Guest guest) {

        return Guest.builder()
                .id(StringUtils.isEmpty(id) ? null : id)
                .type(guestRequest.getGuestType())
                .email(guestRequest.getEmail())
                .firstName(guestRequest.getFirstName())
                .lastName(guestRequest.getLastName())
                .createdAt(StringUtils.isEmpty(id) ? LocalDateTime.now() : guest.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

    }
}
