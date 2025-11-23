package Figures;

import GameBoard.HMGame;
import GameBoard.HMGameState;
import UI.ConsoleColors;
import UI.UserInputs;

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

    public static void pickTeamAndDisplayStatistics(Map<String, Party<? extends Figure>> teams, HMGame g){
        if(teams.size() == 0) throw new IllegalArgumentException("teams argument is an empty map in displayPartyStatistics()");
        String[] options = teams.keySet().toArray(new String[0]);
        if (teams.size() > 1){
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "Which group's statistics would you like to see?");

            int optionChosenIndex = UserInputs.showMenuAndGetUserAnswer(options);
            teams.get(options[optionChosenIndex]).displayPartyStatistics(g);
        }
        else{
            teams.get(options[0]).displayPartyStatistics(g);
        }
    }

    private void displayPartyStatistics(HMGame g){
        HashMap<String, T> map = getMembersNamesMap();

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "Which statistics would you like to open?");

        String[] options = map.keySet().toArray(new String[0]);
        int optionChosenIndex = UserInputs.showMenuAndGetUserAnswer(options, true, g);

        if (optionChosenIndex != -1){
            map.get(options[optionChosenIndex]).displayFigureStatistics(g.getState());
        }
    }

    public HashMap<String, T> getMembersNamesMap(){
        HashMap<String, T> res = new HashMap<>();
        for(T member: members){
            res.put(member.name, member);
        }
        return res;
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

    public void addMember(T member){
        if(members.size() == maxSize) throw new IllegalArgumentException(String.format("Adding member %s would exceed the max size of %d", member.getName(), maxSize));

        members.add(member);
        String message = String.format("🦸‍%s joined your party!", member.getName());
        ConsoleColors.printInColor(ConsoleColors.YELLOW, message);
    }

    public void removeMember(T member){
        if (!members.contains(member)) throw new IllegalArgumentException(String.format("%s not in party to be removed from", member.getName()));

        members.remove(member);
        ConsoleColors.printInColor("👋 %s has left your party!", member.getName());
    }
}
