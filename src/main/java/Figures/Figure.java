package Figures;

import Common.Player;
import GameBoard.HMEffect;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.List;

public abstract class Figure extends Player {

    protected String name;
    protected int level;
    protected int hp;
    protected int hpMax;
    protected List<HMEffect> activeEffects;

//    SETTERS

    public void gainHp(int val){
        if (val < 0){
            throw new ValueException("Cannot gain negative health");
        }
        int new_hp = val + hp;
        hp = Math.max(new_hp, hpMax);
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

    public List<HMEffect> getActiveEffects() {
        return activeEffects;
    }
}
