package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.displays.CombatReport;
import pl.edu.pw.gis.mykpyk.domain.*;
import pl.edu.pw.gis.mykpyk.services.MainConf;

import javax.inject.Inject;
import javax.script.ScriptException;
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
    public HttpResponse<CombatReport> engage(HttpRequest<?> request){
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
                        CombatReport report = new CombatReport(enemyType, hero);
                        Integer heroHealth = hero.getHealth();
                        Integer enemyHealth = enemyType.getHealth();
                        boolean heroAttacks = true;

                        while (heroHealth >= 0 && enemyHealth > 0) {
                            int damageDealt;
                            if (heroAttacks) {
                                if(Math.random() * 100 > enemyType.getSpeed()) {
                                    damageDealt = (int)Math.round(nextGaussian(hero.getStrength() - enemyType.getDefense(), hero.getLevel()));
                                    damageDealt = Math.max(damageDealt, 1);
                                    enemyHealth -= hero.getStrength();
                                    report.addLine("You attacked, dealing " + damageDealt + " damage points. " + enemyType.getName() + "'s remaining health points: " + enemyHealth);
                                }
                                else
                                    report.addLine("You missed!");
                                heroAttacks = false;
                            } else {
                                if(Math.random() * 100 > hero.getSpeed()) {
                                    damageDealt = (int)Math.round(nextGaussian(enemyType.getStrength() - hero.getDefense(), hero.getLevel()));
                                    damageDealt = Math.max(damageDealt, 1);
                                    heroHealth -= damageDealt;
                                    report.addLine(enemyType.getName() + " attacted, dealing " + damageDealt + " damage points. Your remaining health: " + heroHealth);
                                }
                                else
                                    report.addLine(enemyType.getName() + " missed!");
                                heroAttacks = true;
                            }
                        }

                        if (heroHealth <= 0) { //lost and died
                            report.finish(false);
                            hero.setHealth(0);
                            hero.setExp(MainConf.neededExpForLvl.get(hero.getLevel()));
                        } else { //won
                            report.finish(true);
                            hero.setExp(hero.getExp() + enemyType.getExp());
                            hero.setHealth(heroHealth);
                            if (hero.getLevel() < MainConf.neededExpForLvl.size()) {
                                while (hero.getExp() >= MainConf.neededExpForLvl.get(hero.getLevel() + 1)) {
                                    hero.setLevel(hero.getLevel() + 1);
                                    hero.setTalentPoints(hero.getTalentPoints() + 10);
                                }
                            }

                            Random random = new Random();

                            if (Math.random() <= 0.4) {
                                changeEquipmentBackgroundColor();
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
                            }
                            enemyRepository.delete(enemy);
                        }

                        heroRepository.update(hero);

                        return HttpResponse.ok(report);
                    } else {
                        CombatReport report = new CombatReport();
                        return HttpResponse.ok(report);
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }

    private void changeEquipmentBackgroundColor(){
        // TODO
        return;
    }

    public static double nextGaussian(double mean, double deviation){
        return new Random().nextGaussian() * deviation + mean;
    }
}
