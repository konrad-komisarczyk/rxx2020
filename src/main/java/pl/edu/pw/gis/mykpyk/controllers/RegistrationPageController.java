package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/register")
public class RegistrationPageController {

    @Produces(MediaType.TEXT_HTML)
    @Get
    @View("register")
    public Map<String, Object> registrationPage() {
        return new HashMap<>();
    }
}
