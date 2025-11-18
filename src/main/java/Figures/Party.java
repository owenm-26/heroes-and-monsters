package Figures;

import UI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

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
        ConsoleColors.printInColor("🦸‍%s joined your party!", member.getName());
    }

    public void removeMember(T member){
        if (!members.contains(member)) throw new IllegalArgumentException(String.format("%s not in party to be removed from", member.getName()));

        members.remove(member);
        ConsoleColors.printInColor("👋 %s has left your party!", member.getName());
    }
}
