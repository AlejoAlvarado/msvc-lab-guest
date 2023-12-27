package com.sunset.rider.msvclabguest.model.request;

import com.sunset.rider.msvclabguest.model.GuestType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class GuestRequest {
    @NotNull(message = "guestType no puede ser nulo")
    private GuestType guestType;
    @NotEmpty(message = "firstName no puede ser vacio o nulo")
    private String firstName;
    @NotEmpty(message = "lastName no puede ser vacio o nulo")
    private String lastName;
    @Email
    @NotEmpty(message = "email no puede ser vacio o nulo")
    private String email;
}
