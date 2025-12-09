package Common.Figures.Monster.Strategies;

import java.util.List;

import Common.Figures.Figure;

public class LeastHealthTarget extends MonsterAttackStrategy{

    public LeastHealthTarget(){
        name = "Least Health";
    }
    @Override
    public int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes) {
        int leastHealth = victims.get(0).getHp();
        int i = availableTargetIndexes.get(0);
        for(int j: availableTargetIndexes){
            if (victims.get(j).getHp() < leastHealth){
                leastHealth = victims.get(j).getHp();
                i = j;
            }
        }
        return i;
    }
}
