package Common.Figures.Monster.Strategies;

import java.util.List;

import Common.Figures.Figure;

public class RandomTarget extends MonsterAttackStrategy{

    public RandomTarget(){
        name = "Random";
    }
    @Override
    public int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes) {
        return availableTargetIndexes.get((int)(availableTargetIndexes.size() * Math.random()));
    }
}
