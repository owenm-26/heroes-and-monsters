package Figures.Monster;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Figure;
import Figures.Hero.Hero;
import Figures.Hero.HeroType;
import Figures.Party;
import GameBoard.HMGameState;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Data.TextDataLoader.getAllSourceFileNames;

public class Monster extends Figure implements LoadableFromText {
    private int baseDamage;
    private int baseDodge;
    private MonsterType monsterType;

    public Monster(String name, int baseDamage, int baseDefense, int baseDodge, MonsterType monsterType){
        this.name = name;
        this.baseDamage = baseDamage + monsterType.getBaseDamageBonus();
        this.baseDefense = baseDefense + monsterType.getBaseDefenseBonus();
        this.baseDodge = baseDodge + monsterType.getBaseAgilityBonus();
        this.monsterType = monsterType;
    }

    public Monster(){
    }

    @Override
    public boolean dodgedSuccessfully() {
        return Math.random() > (baseDodge * 0.01);
    }

    public void displayFigureStatistics(HMGameState state){
        /*
        Displays statistics of figure based on the game state
         */
    }

    private void setMonsterTypeFromFileNae(String fileName){
        if (fileName.length() == 0) throw new IllegalArgumentException("Filename is length 0 in setMonsterTypeFromFileNae()");
        switch (fileName){
            case "dragons":
                monsterType = MonsterType.DRAGON;
                break;
            case "exoskeletons":
                monsterType = MonsterType.EXOSKELETON;
                break;
            case "spirits":
                monsterType = MonsterType.SPIRIT;
                break;
            default:
                throw new ValueException("Something went wrong in setMonsterTypeFromFileNae()");
        }
    }

    public void loadFromMap(Map<String, String> map){
//        Name/level/damage/defense/dodge chance
        this.name = map.get("Name");
        this.level= Integer.parseInt(map.get("level"));
        this.baseDamage = Integer.parseInt(map.get("damage"));
        this.baseDefense = Integer.parseInt(map.get("defense"));
        this.baseDodge = Integer.parseInt(map.get("dodge chance"));

        // Hero Type
        setMonsterTypeFromFileNae(map.get("file name"));

    }

    public static List<Monster> getAllMonsterOptions(){
        ArrayList<Monster> monsters = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Monsters")){
                monsters.addAll(TextDataLoader.load("Monsters", filename, Monster.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all monsters: %s\n", e);
        }
        return monsters;
    }

    private static HashMap<Integer, List<Monster>> getAllMonsterOptionsHashmap() {
        /*
        Helper method to more efficiently find the level-match via a HashMap data structure
         */
        HashMap<Integer, List<Monster>> map = new HashMap<>();
        for (Monster m : getAllMonsterOptions()) {
            int monsterLevel = m.level;
            List<Monster> l;
            if (map.containsKey(monsterLevel)) l = map.get(monsterLevel);
            else l = new ArrayList<>();

            l.add(m);
            map.put(monsterLevel, l);
        }
        return map;
    }

    private static Monster pickMonster(HashMap<Integer, List<Monster>> monsterMap, Hero h){
        /*
        Logic that chooses the monster based on certain criteria that can be tweaked as needed.
         */
        int heroLevel; List<Monster> candidates; Monster m;
        heroLevel = h.getLevel();
        candidates = monsterMap.get(heroLevel);
        Monster chosen = candidates.get((int)(candidates.size()*Math.random()));

        //remove from options
        candidates.remove(chosen);
        monsterMap.put(heroLevel, candidates);
        return chosen;
    }

    public static Party<Monster> assembleMonsterBattleParty(Party<Hero> heroes){
        /*
        Returns a party of monsters that matches the levels of the heroes battling
         */
        HashMap<Integer, List<Monster>> monsterMap = getAllMonsterOptionsHashmap();
        Party<Monster> p = new Party<>(heroes.getMembers().size());


        for(Hero h: heroes.getMembers()){
            Monster m = pickMonster(monsterMap, h);
            p.addMember(m, false);
        }
        return p;
    }
}
