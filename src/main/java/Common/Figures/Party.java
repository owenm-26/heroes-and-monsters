package Common.Figures;

import HeroesAndMonsters.GameBoard.HMGame;
import HeroesAndMonsters.GameBoard.HMGameState;
import Utility.UI.ConsoleColors;
import Utility.UI.UserInputs;

import java.util.*;

public class Party<T extends Figure>{
    private int maxSize;
    private List<T> members;

    public Party(int maxSize){
        this.maxSize = maxSize;
        members = new ArrayList<>();
    }

    public List<T> getMembers() {
        return members;
    }

    public T pickTeamMember(HMGame g, String message, boolean toggleAbility){
        /*
        Helper method that displays a menu of party members to pick from and returns the one picked
         */
        HashMap<String, T> map = getMembersNamesMap();

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, message);

        String[] options = map.keySet().toArray(new String[0]);
        int optionChosenIndex;
        optionChosenIndex = toggleAbility ? UserInputs.showMenuAndGetUserAnswer(options, true, g) : UserInputs.showMenuAndGetUserAnswer(options);

        return optionChosenIndex >= 0 ? map.get(options[optionChosenIndex]) : null;
    }
    public T pickTeamMember(HMGame g, String message){
        /*
        Helper method that displays a menu of party members to pick from and returns the one picked
         */
        return pickTeamMember(g, message, false);
    }

    public static void pickTeamAndDisplayStatistics(Map<String, Party<? extends Figure>> teams, HMGame g){
        if(teams.size() == 0) throw new IllegalArgumentException("teams argument is an empty map in displayPartyStatistics()");
        String[] options = teams.keySet().toArray(new String[0]);
        Party<?> p;
        if (teams.size() > 1){
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "Which group's statistics would you like to see?");
            int optionChosenIndex = UserInputs.showMenuAndGetUserAnswer(options);
            if (optionChosenIndex < 0) return;
            p = teams.get(options[optionChosenIndex]);
        }
        else{
            p = teams.get(options[0]);
        }
        Figure member;
        if (g!=null){
            member = p.pickTeamMember(g, "Which statistics would you like to open?", true);
        }
        else{
            member = p.pickTeamMember( null,"Which statistics would you like to open?");
        }

        if (member != null) {
            HMGameState state = g!=null ? g.getState() : HMGameState.BATTLING;
            member.displayFigureStatistics(state);
        };
    }

    public static void pickTeamAndDisplayStatistics(Map<String, Party<? extends Figure>> teams){
        pickTeamAndDisplayStatistics(teams, null);
    }

    public LinkedHashMap<String, T> getMembersNamesMap(){
        LinkedHashMap<String, T> res = new LinkedHashMap<>();
        for(T member: members){
            res.put(member.name, member);
        }
        return res;
    }

    public int size(){
        return this.members.size();
    }

    public boolean canAddAnotherMember(){
        return members.size() < maxSize;
    }

    //    SETTERS

    public void addMembers(List<T> members){
        for (T member: members){
            addMember(member);
        }
    }

    public void addMember(T member, boolean print){
        if(members.size() == maxSize) throw new IllegalArgumentException(String.format("Adding member %s would exceed the max size of %d", member.getName(), maxSize));

        members.add(member);
        if(print){
            String message = String.format("🦸‍%s joined your party!", member.getName());
            ConsoleColors.printInColor(ConsoleColors.YELLOW, message);
        }

    }

    public void addMember(T member){
        addMember(member, true);
    }

    public void removeMember(T member){
        if (!members.contains(member)) throw new IllegalArgumentException(String.format("%s not in party to be removed from", member.getName()));

        members.remove(member);
        ConsoleColors.printInColor("👋 %s has left your party!", member.getName());
    }

    public List<Integer> getFigureIndexesWithHealthRemaining(){
        List<Integer> aliveIndexes = new ArrayList<>();
        for(int i = 0; i < maxSize && members.get(i) != null; i++){
            T m = members.get(i);
            if(m.isAlive()) aliveIndexes.add(i);
        }
        return aliveIndexes;
    }


}
