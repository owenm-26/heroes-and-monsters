package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

import java.util.List;

public class RandomTarget extends MonsterAttackStrategy{

    public RandomTarget(){
        name = "Random";
    }
    @Override
    public int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes) {
        return availableTargetIndexes.get((int)(availableTargetIndexes.size() * Math.random()));
    }
}
