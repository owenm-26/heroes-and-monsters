package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

import java.util.List;

public class LowestLevelTarget extends MonsterAttackStrategy{

    public LowestLevelTarget(){
        name = "Lowest Level";
    }
    @Override
    public int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes) {
        int lowestLevel = victims.get(0).getLevel();
        int i = availableTargetIndexes.get(0);
        for(int j: availableTargetIndexes){
            if (victims.get(j).getLevel() < lowestLevel){
                lowestLevel = victims.get(j).getLevel();
                i = j;
            }
        }
        return i;
    }
}
