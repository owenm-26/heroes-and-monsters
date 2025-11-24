package Figures.Monster;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Figure;
import Figures.Hero.Hero;
import Figures.Hero.HeroType;
import GameBoard.HMGameState;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
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
}
