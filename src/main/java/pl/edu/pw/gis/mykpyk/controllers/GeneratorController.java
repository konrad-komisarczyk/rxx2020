package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.displays.EnemyToDisplay;
import pl.edu.pw.gis.mykpyk.domain.*;
import pl.edu.pw.gis.mykpyk.services.Generator;
import pl.edu.pw.gis.mykpyk.services.MainConf;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/generate")
public class GeneratorController {

    @Inject
    private EnemyRepository enemyRepository;
    @Inject private EnemyTypeRepository enemyTypeRepository;
    @Inject private HeroRepository heroRepository;
    @Inject private UserRepository userRepository;

    @Inject private Generator generator;

    @Get
    HttpResponse<Integer> generate(HttpRequest<?> request) {
        Optional<String> loginParameter = request.getParameters().getFirst("login");
        Optional<String> whereLonParameter = request.getParameters().getFirst("lon");
        Optional<String> whereLatParameter = request.getParameters().getFirst("lat");

        if (loginParameter.isPresent() && whereLatParameter.isPresent() && whereLonParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(loginParameter.get());
            double whereLon = Double.parseDouble(whereLonParameter.get());
            double whereLat = Double.parseDouble(whereLatParameter.get());

            if (optionalUser.size() == 1) {
                //User user = optionalUser.get(0);

                generator.generateEnemies(whereLat, whereLon);
                return HttpResponse.ok();
            }
        }
        return HttpResponse.badRequest();
    }

}
