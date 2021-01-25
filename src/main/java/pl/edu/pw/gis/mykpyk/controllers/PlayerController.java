package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.displays.AvatarLink;
import pl.edu.pw.gis.mykpyk.domain.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/player")
public class PlayerController {
    @Inject private HeroRepository heroRepository;
    @Inject private UserRepository userRepository;

    @Get()
    HttpResponse<Hero> getPlayer(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        if (login.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            if (optionalUser.size() == 1) {
                User user = optionalUser.get(0);
                List<Hero> optionalHero = heroRepository.findByUserId(user.getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    return HttpResponse.ok(hero);
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Get("/spendTalentPoint")
    HttpResponse<Hero> spendTalentPoint(HttpRequest<?> request) {
        Optional<String> loginOptional = request.getParameters().getFirst("login");
        Optional<String> whichOptional = request.getParameters().getFirst("which");

        if (loginOptional.isPresent() && whichOptional.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(loginOptional.get());
            if (optionalUser.size() == 1) {
                User user = optionalUser.get(0);
                List<Hero> optionalHero = heroRepository.findByUserId(user.getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    String which = whichOptional.get();

                    if (hero.getTalentPoints() <= 0) {
                        return HttpResponse.notModified();
                    } else {
                        switch (which) {
                            case "strength" -> {
                                hero.setStrength(hero.getStrength() + 2);
                            }
                            case "defense" -> {
                                hero.setDefense(hero.getDefense() + 1);
                            }
                            case "health" -> {
                                hero.setMaxHealth(hero.getMaxHealth() + 5);
                                hero.setHealth(hero.getHealth() + 5);
                            }
                            case "speed" -> {
                                hero.setSpeed(hero.getSpeed() + 2);
                            }
                            default -> {
                                return HttpResponse.badRequest();
                            }
                        }
                        hero.setTalentPoints(hero.getTalentPoints() - 1);

                        heroRepository.update(hero);

                        return HttpResponse.ok(hero);
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Get("/setAmbrosiaHealth")
    HttpResponse<Hero> setAmbrosiaHealth(HttpRequest<?> request) {
        Optional<String> loginOptional = request.getParameters().getFirst("login");

        if (loginOptional.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(loginOptional.get());
            if (optionalUser.size() == 1) {
                User user = optionalUser.get(0);
                List<Hero> optionalHero = heroRepository.findByUserId(user.getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    hero.setMaxHealth(10000);
                    hero.setHealth(10000);
                    heroRepository.update(hero);

                    return HttpResponse.ok(hero);
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Post("/setAvatar")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<Integer> setAvatar(@Body AvatarLink avatarLink) {
        System.out.println("request to set avatar to " + avatarLink.getLink());
        String link = avatarLink.getLink();
        String login = avatarLink.getLogin();

        List<User> userList = userRepository.findByLogin(login);

        if (userList.size() == 1) {
            List<Hero> heroList = heroRepository.findByUserId(userList.get(0).getId());
            if (heroList.size() == 1) {
                Hero hero = heroList.get(0);
                hero.setAvatarLink(link);
                heroRepository.update(hero);

                return HttpResponse.ok(1);
            }
        }
        return HttpResponse.badRequest();

    }
}
