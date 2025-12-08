package Common.Figures.Monster;

import HeroesAndMonsters.GameBoard.HMEffect;
import HeroesAndMonsters.GameBoard.HMGameState;
import Utility.UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import static Common.Data.TextDataLoader.getAllSourceFileNames;

import java.util.*;

import Common.Data.LoadableFromText;
import Common.Data.TextDataLoader;
import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Figures.TraitType;
import Common.Figures.Hero.Hero;

public class Monster extends Figure implements LoadableFromText {
    private int baseDamage;
    private int baseDodge;
    private MonsterType monsterType;

    public Monster(){
        activeEffects = new HashSet<>();
    }

    @Override
    public boolean dodgedSuccessfully() {
        return Math.random() > (baseDodge * 0.01);
    }

    @Override
    public void updateEffect(HMEffect e, boolean adding) {
        int addition = adding ? (1 * e.getValue()) : (-1 * e.getValue());

        switch (e.getType()){
            case STRENGTH:
                baseDamage += addition;
                break;
            case AGILITY:
                baseDodge += addition;
                break;
            default:
                break;
        }
        if (!adding){
            activeEffects.remove(e);
        }
        else{
            activeEffects.add(e);
        }
    }

    public void displayFigureStatistics(HMGameState state){
        final String COLOR = ConsoleColors.CYAN_BOLD;

        ConsoleColors.printInColor(COLOR, "===== Monster STATUS =====");

        ConsoleColors.printInColor(COLOR, "Name: " + name + " (" + monsterType.getName() + ")");
        ConsoleColors.printInColor(COLOR, "Level: " + level);
        ConsoleColors.printInColor(COLOR, "HP: " + hp + "/" + hpMax);
        ConsoleColors.printInColor(COLOR, "Attack Strategy: " + monsterType.getStrategy().getName());

        ConsoleColors.printInColor(COLOR, "--- Attributes ---");
        ConsoleColors.printInColor(COLOR, "Base Damage:  " + baseDamage);
        ConsoleColors.printInColor(COLOR, "Base Defense: " + baseDefense);
        ConsoleColors.printInColor(COLOR, "Dodge Chance:   " + baseDodge);

        ConsoleColors.printInColor(COLOR, "========================\n");
    }

    @Override
    public int getPunchDamage() {
        return baseDamage/10;
    }

    @Override
    public int getDamageBlocked() {

        return (int)(baseDefense * 0.05);
    }

    private void setMonsterTypeFromFileName(String fileName){
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

    private void initializeAttributes(int baseDamage, int baseDefense, int baseDodge){
        this.baseDamage = baseDamage + monsterType.getBaseDamageBonus();
        this.baseDefense = baseDefense + monsterType.getBaseDefenseBonus();
        this.baseDodge = baseDodge + monsterType.getBaseAgilityBonus();
    }

    public void loadFromMap(Map<String, String> map){
//        Name/level/damage/defense/dodge chance
        this.name = map.get("Name");
        this.level= Integer.parseInt(map.get("level"));

        // Hero Type
        setMonsterTypeFromFileName(map.get("file name"));
        initializeAttributes(Integer.parseInt(map.get("damage")),
                             Integer.parseInt(map.get("defense")),
                             Integer.parseInt(map.get("dodge chance")));

        // Health
        calculateHpMax();
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
    public MonsterType getMonsterType() {
        return monsterType;
    }
}
