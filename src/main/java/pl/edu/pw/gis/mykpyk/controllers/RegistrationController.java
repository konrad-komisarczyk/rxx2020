package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import pl.edu.pw.gis.mykpyk.domain.Hero;
import pl.edu.pw.gis.mykpyk.domain.HeroRepository;
import pl.edu.pw.gis.mykpyk.domain.UserRepository;
import pl.edu.pw.gis.mykpyk.domain.User;
import pl.edu.pw.gis.mykpyk.services.Hashing;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/registered")
public class RegistrationController {

    @Inject
    private UserRepository userRepository;
    @Inject
    private HeroRepository heroRepository;

    @Get
    @View("auth")
    public Map<String, Object> register(HttpRequest<?> request) {
        HashMap<String, Object> map = new HashMap<>();
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> password = request.getParameters().getFirst("password");

        if (login.isEmpty() || password.isEmpty()) {
            map.put("registered", true);
            return map;
        } else {
            List<User> foundUser = userRepository.findByLogin(login.get());
            if (foundUser.size() > 0) {
                return map;
            } else {
                userRepository.save(login.get(), Hashing.sha256(password.get()));

                User user = userRepository.findByLogin(login.get()).get(0);

                Hero hero = new Hero(user.getId());

                //creating hero for user
                heroRepository.save(hero);


                map.put("registered", true);
                return map;
            }
        }
    }
}
