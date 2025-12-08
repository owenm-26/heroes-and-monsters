package HeroesAndMonsters.GameBoard;

import Common.Figures.TraitType;
import Common.Gameboard.Effect;

public class HMEffect extends Effect {
    private int roundsLeft;
    private int value;
    private TraitType type;

    public HMEffect(String name, int value, int rounds, TraitType type){
        this.name = name;
        this.value = value;
        this.type = type;
        roundsLeft = rounds;
    }

    public int getRoundsLeft() {
        return roundsLeft;
    }

    public int getValue(){
        return value;
    }

    public void decrementRoundsLeft(){
        roundsLeft--;
    }

    public TraitType getType() {
        return type;
    }
}
