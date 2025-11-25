package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

public class RandomTarget extends MonsterAttackStrategy{

    public RandomTarget(){
        name = "Random";
    }
    @Override
    public int getVictimPartyIndex(Party<Figure> victimParty) {
        return (int)(victimParty.size() * Math.random());
    }
}
