package Figures;

import Common.Player;
import GameBoard.HMEffect;
import GameBoard.HMGameState;
import UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Figure extends Player {

    protected String name;
    protected int level;
    protected int hp;
    protected int hpMax;
    protected int baseDefense;
    protected Set<HMEffect> activeEffects;

//    SETTERS

    public void gainHp(int val){
        if (val < 0){
            throw new ValueException("Cannot gain negative health");
        }
        int new_hp = val + hp;
        hp = Math.min(new_hp, hpMax);
    }

    public boolean loseHp(int val){
        /*
        Reduces the HP of the figure and returns True if the figure dies (has 0 HP)
         */
        if (val < 0){
            throw new ValueException("Cannot lose negative health");
        }
        int new_hp = hp - val;
        hp = Math.max(new_hp, 0);

        return hp == 0;

    }

//    GETTERS

    public String getName(){
        return name;
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

    public Set<HMEffect> getActiveEffects() {
        return activeEffects;
    }

    public void decrementTimeOnAllEffects(){
        List<HMEffect> effectsEnded = new ArrayList<>();
        for(HMEffect e: activeEffects){
            e.decrementRoundsLeft();
            int roundsLeft = e.getRoundsLeft();
            if (roundsLeft == 0){
                effectsEnded.add(e);
                activeEffects.remove(e);
            }

        }
        StringBuilder b = new StringBuilder();
        b.append("Effects that ended: [");
        for (HMEffect e: effectsEnded){
            b.append(e.getName() + ",");
        }
        if (effectsEnded.size() != 0) b.deleteCharAt(b.length()-1); // remove the extra comma
        b.append("]");
        ConsoleColors.printInColor(ConsoleColors.YELLOW, b.toString());
    }

   public abstract void displayFigureStatistics(HMGameState state);
}
