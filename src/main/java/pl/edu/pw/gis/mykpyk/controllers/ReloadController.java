package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
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


@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/reload")
public class ReloadController {

    @Inject private EnemyRepository enemyRepository;
    @Inject private EnemyTypeRepository enemyTypeRepository;
    @Inject private HeroRepository heroRepository;
    @Inject private UserRepository userRepository;

    @Inject private Generator generator;

    @Get
    HttpResponse<List<EnemyToDisplay>> getEnemies(HttpRequest<?> request) {
        Optional<String> loginParameter = request.getParameters().getFirst("login");
        Optional<String> whereLonParameter = request.getParameters().getFirst("lon");
        Optional<String> whereLatParameter = request.getParameters().getFirst("lat");

        if (loginParameter.isPresent() && whereLatParameter.isPresent() && whereLonParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(loginParameter.get());
            double whereLon = Double.parseDouble(whereLonParameter.get());
            double whereLat = Double.parseDouble(whereLatParameter.get());

            if (optionalUser.size() == 1) {
                User user = optionalUser.get(0);
                List<Hero> optionalHero = heroRepository.findByUserId(user.getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    double progress = Math.sqrt(Math.pow(whereLon - hero.getLastLon(), 2) + Math.pow(whereLat - hero.getLastLat(), 2));

                    if (progress >= MainConf.minimalProgress) {
                        //enemy generation refresh
                        //generator.generateEnemies(whereLat, whereLon); //TODO UNCOMMENT

                        //hero position update
                        hero.setLastLat(whereLat);
                        hero.setLastLon(whereLon);

                        //health regeneration
                        if (progress >= MainConf.regenerationDistance) {
                            hero.setHealth(min(hero.getHealth() + 1, hero.getMaxHealth()));
                        }

                        heroRepository.update(hero);
                    }

                    List<Enemy> seenEnemies = enemyRepository.selectFromRectangle(
                            whereLon + MainConf.enemyShowRadius,
                            whereLon - MainConf.enemyShowRadius,
                            whereLat - MainConf.enemyShowRadius,
                            whereLat + MainConf.enemyShowRadius
                    );

                    List<EnemyToDisplay> seenEnemiesToDisplay = seenEnemies.stream().
                            map(this::displayEnemy).
                            collect(Collectors.toList());

                    return HttpResponse.ok(seenEnemiesToDisplay);
                }
            }
        }
        return HttpResponse.badRequest();
    }

    private EnemyToDisplay displayEnemy(Enemy enemy) {
        EnemyType enemyType = enemyTypeRepository.findById((long) enemy.getEnemyTypeId()).get();

        return new EnemyToDisplay(enemy.getId(), enemyType.getName(), enemy.getEnemyTypeId(),
                enemy.getLng(), enemy.getLat(), enemyType.getImage());
    }

}