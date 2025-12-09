package Common.Figures.Monster.Strategies;

import java.util.List;

import Common.Figures.Figure;

public abstract class MonsterAttackStrategy {
    protected String name;

    public abstract int getVictimPartyIndex(List<? extends Figure> victims, List<Integer> availableTargetIndexes);
    /*
    Returns the index of the person in the hero party to attack based on the specific strategy
     */

    public String getName() {
        return name;
    }
}
