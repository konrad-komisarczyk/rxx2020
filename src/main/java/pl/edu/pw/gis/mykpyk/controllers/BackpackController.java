package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.displays.BackpackSlotToDisplay;
import pl.edu.pw.gis.mykpyk.domain.*;

import javax.inject.Inject;
import io.micronaut.http.HttpResponse;
import pl.edu.pw.gis.mykpyk.services.MainConf;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/backpack")
public class BackpackController {
    @Inject private HeroRepository heroRepository;
    @Inject private UserRepository userRepository;
    @Inject private ItemRepository itemRepository;
    @Inject private BackpackSlotRepository backpackSlotRepository;

    @Get
    HttpResponse<List<BackpackSlotToDisplay>> getBackpack(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        if (login.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            if (optionalUser.size() == 1) {
                List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId((int) (long) hero.getId());
                    List<BackpackSlotToDisplay> backpackSlotsToDisplay = backpack.stream().map(backpackSlot -> {
                        Optional<Item> optionalItem = itemRepository.findById((long) backpackSlot.getItemId());
                        String image = null;
                        if (optionalItem.isPresent()) {
                            image = optionalItem.get().getImage();
                        }

                        return new BackpackSlotToDisplay(
                                backpackSlot.getId(),
                                backpackSlot.getItemId(),
                                backpackSlot.getPosition(),
                                image
                        );
                    }).collect(Collectors.toList());

                    return HttpResponse.ok(backpackSlotsToDisplay);
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Get("/itemDetails")
    HttpResponse<Item> getItemDetails(HttpRequest<?> request) {
        Optional<String> itemIdParameter = request.getParameters().getFirst("itemId");
        if (itemIdParameter.isPresent()) {
            int itemId = Integer.parseInt(itemIdParameter.get());
            Optional<Item> optionalItem = itemRepository.findById((long) itemId);
            if (optionalItem.isPresent()) {
                return HttpResponse.ok(optionalItem.get());
            }
        }
        return HttpResponse.badRequest();
    }


    @Get("/moveItem")
    HttpResponse<Integer> moveItem(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> posBeforeParameter = request.getParameters().getFirst("posBefore");
        Optional<String> posAfterParameter = request.getParameters().getFirst("posAfter");
        if (login.isPresent() && posAfterParameter.isPresent() && posBeforeParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            int posBefore = Integer.parseInt(posBeforeParameter.get());
            int posAfter = Integer.parseInt(posAfterParameter.get());
            if (optionalUser.size() == 1) {
                List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId((int) (long) hero.getId());
                    Optional<BackpackSlot> optionalSlotBefore = backpack.stream().
                            filter(backpackSlot -> backpackSlot.getPosition() == posBefore).
                            findAny();
                    if (optionalSlotBefore.isPresent()) {
                        BackpackSlot slotBefore = optionalSlotBefore.get();
                        Optional<BackpackSlot> optionalSlotAfter = backpack.stream().
                                filter(backpackSlot -> backpackSlot.getPosition() == posAfter).
                                findAny();
                        Optional<Item> optionalItemBefore = itemRepository.findById((long) slotBefore.getItemId());
                        if (optionalItemBefore.isEmpty()) {
                            return HttpResponse.badRequest();
                        }
                        Item itemBefore = optionalItemBefore.get();


                        //adding the item effect
                        if (posAfter == MainConf.backapckWeaponPosition) {
                            if (itemBefore.isWeapon()) {
                                hero.setStrength(hero.getStrength() + itemBefore.getDamage());
                            } else {
                                return HttpResponse.notModified();
                            }
                        }
                        if (posAfter == MainConf.backpackArmorPosition) {
                            if (itemBefore.isArmor()) {
                                hero.setDefense(hero.getDefense() + itemBefore.getProtection());
                            } else {
                                return HttpResponse.notModified();
                            }
                        }

                        slotBefore.setPosition(posAfter);
                        if (optionalSlotAfter.isPresent()) {
                            BackpackSlot slotAfter = optionalSlotAfter.get();
                            Optional<Item> optionalItemAfter = itemRepository.findById((long) slotAfter.getItemId());
                            if (optionalItemAfter.isEmpty()) {
                                return HttpResponse.badRequest();
                            }
                            Item itemAfter = optionalItemAfter.get();

                            //removing the item effect
                            if (posBefore == MainConf.backapckWeaponPosition) {
                                if (itemAfter.isWeapon()) {
                                    hero.setStrength(hero.getStrength() - itemAfter.getDamage());
                                } else {
                                    return HttpResponse.notModified();
                                }
                            }
                            if (posBefore == MainConf.backpackArmorPosition) {
                                if (itemAfter.isArmor()) {
                                    hero.setDefense(hero.getDefense() - itemAfter.getProtection());
                                } else {
                                    return HttpResponse.notModified();
                                }
                            }

                            slotAfter.setPosition(posBefore);


                            //applying updates only here when we know everything went ok
                            backpackSlotRepository.update(slotAfter);
                        }

                        //applying updates only here when we know everything went ok
                        backpackSlotRepository.update(slotBefore);
                        heroRepository.update(hero);

                        return HttpResponse.ok();
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }

    // at the moment only for healing items
    @Get("/useItem")
    HttpResponse<Integer> useItem(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> posParameter = request.getParameters().getFirst("pos");
        if (login.isPresent() && posParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            int pos = Integer.parseInt(posParameter.get());
            if (optionalUser.size() == 1) {
                List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId((int) (long) hero.getId());
                    Optional<BackpackSlot> optionalSlot = backpack.stream().
                            filter(backpackSlot -> backpackSlot.getPosition() == pos).
                            findAny();
                    if (optionalSlot.isPresent()) {
                        BackpackSlot slot = optionalSlot.get();
                        Optional<Item> optionalItem = itemRepository.findById((long) slot.getItemId());
                        if (optionalItem.isEmpty()) {
                            return HttpResponse.badRequest();
                        }
                        Item item = optionalItem.get();

                        if (!item.isUsable()) {
                            return HttpResponse.notModified();
                        }
                        backpackSlotRepository.delete(slot);

                        //healing
                        hero.setHealth(hero.getHealth() + item.getHealing());
                        heroRepository.update(hero);

                        return HttpResponse.ok();
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }


}
