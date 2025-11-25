package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

import java.util.List;

public class LowestLevelTarget extends MonsterAttackStrategy{

    public LowestLevelTarget(){
        name = "Lowest Level";
    }
    @Override
    public int getVictimPartyIndex(Party<Figure> victimParty) {
        List<Figure> victims = victimParty.getMembers();
        int lowestLevel = victims.get(0).getLevel();
        int i = 0;
        for(int j = 1; j < victims.size(); j++){
            if (victims.get(j).getLevel() < lowestLevel){
                lowestLevel = victims.get(j).getLevel();
                i = j;
            }
        }
        return i;
    }
}
