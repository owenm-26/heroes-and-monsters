package GameBoard;

import Common.Effect;

public class HMEffect extends Effect {

    private int roundsLeft;

    public HMEffect(String name, int rounds){
        this.name = name;
        roundsLeft = rounds;
    }

    public int getRoundsLeft() {
        return roundsLeft;
    }

    public void decrementRoundsLeft(){
        roundsLeft--;
    }
}
