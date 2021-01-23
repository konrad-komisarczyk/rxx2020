package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.domain.Hero;
import pl.edu.pw.gis.mykpyk.domain.HeroRepository;
import pl.edu.pw.gis.mykpyk.domain.UserRepository;
import pl.edu.pw.gis.mykpyk.domain.User;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/registered")
public class RegistrationController {

    @Inject
    private UserRepository userRepository;
    @Inject
    private HeroRepository heroRepository;

    @Get
    public HttpResponse<String> register(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> password = request.getParameters().getFirst("password");

        if (login.isEmpty() || password.isEmpty()) {
            return HttpResponse.badRequest("You need to provide login and password");
        } else {
            List<User> foundUser = userRepository.findByLogin(login.get());
            if (foundUser.size() > 0) {
                return HttpResponse.badRequest("Login already used");
            } else {
                userRepository.save(login.get(), password.get());

                User user = userRepository.findByLogin(login.get()).get(0);

                Hero hero = new Hero(user.getId());

                //creating hero for user
                heroRepository.save(hero);


                System.out.println("registered new user");
                return HttpResponse.ok("Registered user.");
            }
        }
    }
}
