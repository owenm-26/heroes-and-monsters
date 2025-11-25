package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

import java.util.List;

public class MostHealthTarget extends MonsterAttackStrategy{

    public MostHealthTarget(){
        name = "Most Health";
    }
    @Override
    public int getVictimPartyIndex(Party<Figure> victimParty) {
        List<Figure> victims = victimParty.getMembers();
        int mostHealth = victims.get(0).getHp();
        int i = 0;

        for(int j = 1; j < victims.size(); j++){
            if (victims.get(j).getHp() > mostHealth){
                mostHealth = victims.get(j).getHp();
                i = j;
            }
        }
        return i;
    }
}
