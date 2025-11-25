package Figures.Monster.Strategies;

import Figures.Figure;
import Figures.Party;

public abstract class MonsterAttackStrategy {
    protected String name;

    public abstract int getVictimPartyIndex(Party<Figure> victimParty);
    /*
    Returns the index of the person in the hero party to attack based on the specific strategy
     */

    public String getName() {
        return name;
    }
}
