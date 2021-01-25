package pl.edu.pw.gis.mykpyk.displays;

import io.micronaut.http.server.util.$DefaultHttpClientAddressResolverDefinitionClass;
import pl.edu.pw.gis.mykpyk.domain.EnemyType;
import pl.edu.pw.gis.mykpyk.domain.Hero;
import pl.edu.pw.gis.mykpyk.services.MainConf;

public class CombatReport {
    String message;
    EnemyType enemyType;
    Hero hero;

    public CombatReport(EnemyType enemyType, Hero hero) {
        message = "Battle started!";
        this.enemyType = enemyType;
        this.hero = hero;
    }

    public void addLine(String line){
        message += '\n' + line;
    }

    public void finish(boolean didPlayerWon){
        if(didPlayerWon)
            message += "\nCongratulations! You won. Your prize: " + enemyType.getExp();
        else
            message += "\nSuch a shame! You lost it. Your EXP goes down to: " + MainConf.neededExpForLvl.get(hero.getLevel());
        System.out.println(message); // TODO - not just Sopr()
    }
}
