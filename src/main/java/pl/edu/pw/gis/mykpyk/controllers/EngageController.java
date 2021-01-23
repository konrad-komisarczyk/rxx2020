package pl.edu.pw.gis.mykpyk.controllers;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.domain.*;
import pl.edu.pw.gis.mykpyk.services.MainConf;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/engage")
public class EngageController {

    @Inject private EnemyRepository enemyRepository;
    @Inject private EnemyTypeRepository enemyTypeRepository;
    @Inject private HeroRepository heroRepository;
    @Inject private UserRepository userRepository;
    @Inject private DropProbabilityRepository dropProbabilityRepository;
    @Inject private BackpackSlotRepository backpackSlotRepository;

    @Get()
    public HttpResponse<String> engage(HttpRequest<?> request) {
        Optional<String> userLoginParameter = request.getParameters().getFirst("login");
        Optional<String> enemyIdParameter = request.getParameters().getFirst("enemyId");
        Optional<String> lonParameter = request.getParameters().getFirst("lon");
        Optional<String> latParameter = request.getParameters().getFirst("lat");

        if (userLoginParameter.isPresent() && enemyIdParameter.isPresent() && lonParameter.isPresent() && latParameter.isPresent()) {
            Optional<Enemy> enemyOptional = enemyRepository.findById(Long.parseLong(enemyIdParameter.get()));
            List<User> userList = userRepository.findByLogin(userLoginParameter.get());
            double lon = Double.parseDouble(lonParameter.get());
            double lat = Double.parseDouble(latParameter.get());

            if (enemyOptional.isPresent() && userList.size() == 1) {
                Enemy enemy = enemyOptional.get();
                Optional<EnemyType> enemyTypeOptional = enemyTypeRepository.findById((long) enemy.getEnemyTypeId());
                User user = userList.get(0);
                List<Hero> heroList = heroRepository.findByUserId(user.getId());
                if (heroList.size() == 1 && enemyTypeOptional.isPresent()) {
                    Hero hero = heroList.get(0);
                    EnemyType enemyType = enemyTypeOptional.get();

                    double howFarToEnemy = Math.sqrt(Math.pow(lon - enemy.getLng(), 2) + Math.pow(lat - enemy.getLat(), 2));

                    if (howFarToEnemy <= MainConf.engageDistance) {

                        Integer heroHealth = hero.getHealth();
                        Integer enemyHealth = enemyType.getHealth();
                        boolean heroAttacks = true;

                        while (heroHealth >= 0 && enemyHealth > 0) {
                            if (heroAttacks) {
                                enemyHealth -= hero.getStrength();
                                heroAttacks = false;
                            } else {
                                heroHealth -= enemyType.getStrength() - hero.getDefense();
                                heroAttacks = true;
                            }
                        }

                        String result = "UNKNOWN";
                        if (heroHealth < 0) { //lost and died
                            //die
                            hero.setHealth(0);
                            hero.setExp(MainConf.neededExpForLvl.get(hero.getLevel()));
                            result = "LOST";
                        } else { //won
                            hero.setExp(hero.getExp() + enemyType.getExp());
                            hero.setHealth(heroHealth);
                            if (hero.getLevel() < MainConf.neededExpForLvl.size()) {
                                while (hero.getExp() >= MainConf.neededExpForLvl.get(hero.getLevel())) {
                                    hero.setLevel(hero.getLevel() + 1);
                                    hero.setTalentPoints(hero.getTalentPoints() + 10);
                                }
                            }
                            result = "WON";

                            Random random = new Random();

                            List<DropProbability> dropProbabilities =
                                    dropProbabilityRepository.findByEnemyTypeId((int) (long) enemyType.getId());

                            for (DropProbability dropProbability : dropProbabilities) {
                                //over all drop probabilities for this enemy type
                                if (random.nextDouble() < dropProbability.getProbability()) {
                                    List<Integer> usedSlots =
                                            backpackSlotRepository.findByHeroId((int) (long) hero.getId()).stream()
                                                    .map(BackpackSlot::getPosition)
                                                    .collect(Collectors.toList());
                                    for (Integer i = 0; i < MainConf.backpackSize; i++) {
                                        // checking all backpack slots if is free
                                        if (!usedSlots.contains(i)) { //found free backpack slot

                                            BackpackSlot backpackSlotNew = new BackpackSlot(
                                                    (int) (long) hero.getId(),
                                                    dropProbability.getItemId(),
                                                    i
                                            );

                                            backpackSlotRepository.save(backpackSlotNew);
                                            break;
                                        }
                                    }
                                    //here going from break

                                }
                            }

                            
                            enemyRepository.delete(enemy);
                        }

                        heroRepository.update(hero);

                        return HttpResponse.ok(result);
                    } else {
                        return HttpResponse.notModified();
                    }
                }
            }
        }

        return HttpResponse.badRequest();
    }
}
