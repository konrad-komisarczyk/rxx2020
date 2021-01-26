package pl.edu.pw.gis.mykpyk.displays;

import io.micronaut.http.server.util.$DefaultHttpClientAddressResolverDefinitionClass;
import pl.edu.pw.gis.mykpyk.domain.EnemyType;
import pl.edu.pw.gis.mykpyk.domain.Hero;
import pl.edu.pw.gis.mykpyk.services.MainConf;

public class CombatReport {
    private String message;
    private EnemyType enemyType;
    private Hero hero;

    public CombatReport(EnemyType enemyType, Hero hero) {
        this.enemyType = enemyType;
        this.hero = hero;
        message = "Battle started!";
    }

    public void addLine(String line){
        message += '\n' + line;
    }

    public void finish(boolean didPlayerWon){
        if(didPlayerWon)
            message += "\nCongratulations! You won. Your prize: " + enemyType.getExp();
        else
            message += "\nSuch a shame! You lost it. Your EXP goes down to: " + MainConf.neededExpForLvl.get(hero.getLevel());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
