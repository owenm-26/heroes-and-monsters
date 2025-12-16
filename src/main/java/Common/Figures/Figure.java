package Common.Figures;
/*
FILE HEADER:
Inherited from player and extended by Hero and Monster, but share basic characterstics of both
 */

import Common.Gameboard.Player;
import Common.HMLVEffect;
import HeroesAndMonsters.GameBoard.HMGameState;
import Utility.UI.ConsoleColors;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Figure extends Player {

    protected String name;
    protected int level;
    protected int hp;
    protected int hpMax;
    protected int baseDefense;
    protected Set<HMLVEffect> activeEffects;

//    SETTERS

    public void gainHp(int val){
        if (val < 0){
            throw new IllegalArgumentException("Cannot gain negative health");
        }
        int new_hp = val + hp;
        hp = Math.min(new_hp, hpMax);
    }

    public boolean loseHp(int val){
        /*
        Reduces the HP of the figure and returns True if the figure dies (has 0 HP)
         */
        if (val < 0){
            throw new IllegalArgumentException("Cannot lose negative health");
        }
        int new_hp = hp - val;
        hp = Math.max(new_hp, 0);

        return hp == 0;

    }

    public abstract boolean dodgedSuccessfully();

    protected void calculateHpMax(){
        hpMax = level * 100;
        hp = hpMax;
    }

//    GETTERS

    public String getName(){
        return name;
    }

    public boolean isAlive(){
        return hp > 0;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getLevel() {
        return level;
    }

    public Set<HMLVEffect> getActiveEffects() {
        return activeEffects;
    }

    public void decrementTimeOnAllEffects(){
        List<HMLVEffect> effectsEnded = new ArrayList<>();
        for(HMLVEffect e: activeEffects){
            e.decrementRoundsLeft();
            int roundsLeft = e.getRoundsLeft();
            if (roundsLeft == 0){
                effectsEnded.add(e);
                updateEffect(e, false);
            }

        }

        if(effectsEnded.size()> 0){
            StringBuilder b = new StringBuilder();
            b.append("Effects that ended: [");
            for (HMLVEffect e: effectsEnded){
                b.append(e.getName() + ",");
            }
            if (effectsEnded.size() != 0) b.deleteCharAt(b.length()-1); // remove the extra comma
            b.append("]");
            ConsoleColors.printInColor(ConsoleColors.YELLOW, b.toString());
        }

    }

    public abstract void updateEffect(HMLVEffect e, boolean adding);

   public abstract void displayFigureStatistics(HMGameState state);

    public int getBaseDefense() {
        return baseDefense;
    }

    public abstract int getDamageBlocked();

    public abstract int getPunchDamage();
}
