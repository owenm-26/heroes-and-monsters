package Common.Figures.Monster.Strategies;

import java.util.List;

import Common.Figures.Figure;

public class MostHealthTarget extends MonsterAttackStrategy{

    public MostHealthTarget(){
        name = "Most Health";
    }
    @Override
    public int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes) {
        int mostHealth = victims.get(0).getHp();
        int i = availableTargetIndexes.get(0);

        for(int j: availableTargetIndexes){
            if (victims.get(j).getHp() > mostHealth){
                mostHealth = victims.get(j).getHp();
                i = j;
            }
        }
        return i;
    }
}
