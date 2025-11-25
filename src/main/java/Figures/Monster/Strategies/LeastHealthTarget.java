package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

import java.util.List;

public class LeastHealthTarget extends MonsterAttackStrategy{

    public LeastHealthTarget(){
        name = "Least Health";
    }
    @Override
    public int getVictimPartyIndex(Party<Figure> victimParty) {
        List<Figure> victims = victimParty.getMembers();
        int leastHealth = victims.get(0).getHp();
        int i = 0;
        for(int j = 1; j < victims.size(); j++){
            if (victims.get(j).getHp() < leastHealth){
                leastHealth = victims.get(j).getHp();
                i = j;
            }
        }
        return i;
    }
}
