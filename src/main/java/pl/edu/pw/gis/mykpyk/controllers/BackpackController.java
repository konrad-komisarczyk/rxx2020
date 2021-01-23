package pl.edu.pw.gis.mykpyk.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import pl.edu.pw.gis.mykpyk.displays.BackpackSlotToDisplay;
import pl.edu.pw.gis.mykpyk.displays.ItemToDisplay;
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
                User user = optionalUser.get(0);
                List<Hero> optionalHero = heroRepository.findByUserId(user.getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId((int ) (long) hero.getId());
                    List<BackpackSlotToDisplay> backpackSlotsToDisplay = backpack.stream().map(backpackSlot -> {
                        Optional<Item> optionalItem = itemRepository.findById((long) backpackSlot.getItemId());
                        String image = null;
                        String type = null;
                        Integer itemIdentifier = 0;
                        if (optionalItem.isPresent()) {
                            image = optionalItem.get().getImage();
                            itemIdentifier = optionalItem.get().getItemIdentifier();
                            type = optionalItem.get().getType();
                        }

                        return new BackpackSlotToDisplay(
                                backpackSlot.getId(),
                                backpackSlot.getItemId(),
                                type,
                                itemIdentifier,
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
    HttpResponse<ItemToDisplay> getItemDetails(HttpRequest<?> request) {
        Optional<String> itemPositionParameter = request.getParameters().getFirst("position");
        Optional<String> loginParameter = request.getParameters().getFirst("login");
        if(itemPositionParameter.isEmpty()) return HttpResponse.badRequest();
        if(loginParameter.isEmpty()) return HttpResponse.badRequest();
        int itemPosition = Integer.parseInt(itemPositionParameter.get());
        List<User> optionalUser = userRepository.findByLogin(loginParameter.get());
        if(optionalUser.size() == 1) {
            List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
            if(optionalHero.size() == 1) {
                Hero hero = optionalHero.get(0);
                List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId( (int ) (long) hero.getId());
                Optional<BackpackSlot> optionalSlot = backpack.stream().
                        filter(backpackSlot -> backpackSlot.getPosition() == itemPosition).
                        findAny();
                if(optionalSlot.isPresent()){
                    Optional<Item> optionalItem = itemRepository.findById((long) optionalSlot.get().getItemId());
                    if(optionalItem.isPresent()){
                        Item item = optionalItem.get();
                        return HttpResponse.ok(new ItemToDisplay( item.getId(), item.getName(), item.getDescription() ));
                    }
                }
            }

        }
        return HttpResponse.badRequest();

    }


    @Get("/moveItem")
    HttpResponse<Integer> moveItem(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> posBeforeParameter = request.getParameters().getFirst("posBefore");
        Optional<String> posAfterParameter = request.getParameters().getFirst("posAfter");
        System.out.println(login.get() + posBeforeParameter.get() + posAfterParameter.get());
        if (login.isPresent() && posAfterParameter.isPresent() && posBeforeParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            int posBefore = Integer.parseInt(posBeforeParameter.get());
            int posAfter = Integer.parseInt(posAfterParameter.get());
            if (optionalUser.size() == 1) {
                List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
                if (optionalHero.size() == 1) {
                    System.out.println("STEP 1");
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId( (int ) (long) hero.getId());
                    Optional<BackpackSlot> optionalSlotBefore = backpack.stream().
                            filter(backpackSlot -> backpackSlot.getPosition() == posBefore).
                            findAny();
                    if (optionalSlotBefore.isPresent()) {
                        System.out.println("STEP 2");
                        BackpackSlot slotBefore = optionalSlotBefore.get();
                        Optional<BackpackSlot> optionalSlotAfter = backpack.stream().
                                filter(backpackSlot -> backpackSlot.getPosition() == posAfter).
                                findAny();
                        Optional<Item> optionalItemBefore = itemRepository.findById((long) slotBefore.getItemId());

                        System.out.println("STEP 3");
                        if(optionalSlotAfter.isPresent()) return HttpResponse.badRequest();
                        // cannot switch items
                        if (optionalItemBefore.isEmpty()) {
                            System.out.println("EEE :(");
                            return HttpResponse.badRequest();
                        }
                        Item itemBefore = optionalItemBefore.get();
                        if(posAfter == -1 && !itemBefore.getType().equals( "weapon" )) return HttpResponse.badRequest();
                        if(posAfter == -2 && !itemBefore.getType().equals( "armor" )) return HttpResponse.badRequest();



                        //adding the item effect
                        if (posAfter == MainConf.backpackWeaponPosition) {
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

                        //removing the item effect
                        if (posBefore == MainConf.backpackWeaponPosition) {
                            if (itemBefore.isWeapon()) {
                                hero.setStrength(hero.getStrength() - itemBefore.getDamage());
                            } else {
                                return HttpResponse.notModified();
                            }
                        }
                        if (posBefore == MainConf.backpackArmorPosition) {
                            if (itemBefore.isArmor()) {
                                hero.setDefense(hero.getDefense() - itemBefore.getProtection());
                            } else {
                                return HttpResponse.notModified();
                            }
                        }

                        slotBefore.setPosition(posAfter);
                        backpackSlotRepository.update(slotBefore);

                        heroRepository.update(hero);

                        return HttpResponse.ok(1);
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Get("/consumeItem")
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
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId( (int ) (long) hero.getId());
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

                        if (!item.isConsumable()) {
                            return HttpResponse.notModified();
                        }

                        //healing
                        hero.setHealth(Math.min(hero.getHealth() + item.getHealing(), hero.getMaxHealth()));

                        backpackSlotRepository.delete(slot);
                        heroRepository.update(hero);

                        return HttpResponse.ok();
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }

    @Delete("/deleteItem")
    HttpResponse<Integer> deleteItem(HttpRequest<?> request) {
        Optional<String> login = request.getParameters().getFirst("login");
        Optional<String> posParameter = request.getParameters().getFirst("pos");
        if (login.isPresent() && posParameter.isPresent()) {
            List<User> optionalUser = userRepository.findByLogin(login.get());
            int pos = Integer.parseInt(posParameter.get());
            if (optionalUser.size() == 1) {
                List<Hero> optionalHero = heroRepository.findByUserId(optionalUser.get(0).getId());
                if (optionalHero.size() == 1) {
                    Hero hero = optionalHero.get(0);
                    List<BackpackSlot> backpack = backpackSlotRepository.findByHeroId( (int ) (long) hero.getId());
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

                        backpackSlotRepository.delete(slot);
                        heroRepository.update(hero);

                        return HttpResponse.ok();
                    }
                }
            }
        }
        return HttpResponse.badRequest();
    }


}
