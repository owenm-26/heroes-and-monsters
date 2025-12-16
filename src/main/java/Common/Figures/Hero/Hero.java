package Common.Figures.Hero;
/*
FILE HEADER:
The most logic-heavy class that defines the heroes the users play with
 */

import Common.Data.LoadableFromText;
import Common.Data.TextDataLoader;
import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Items.*;
import Common.Items.Potion.Potion;
import Common.Items.Potion.PotionType;
import Common.Items.Spell.Spell;
import Common.HMLVEffect;
import HeroesAndMonsters.GameBoard.HMGameState;
import Utility.UI.ConsoleColors;
import Utility.UI.GeneralPrints;
import Utility.Validators.Integers;

import java.util.*;

import static Common.Data.TextDataLoader.getAllSourceFileNames;
import static Common.Items.Inventory.tradeItem;
import static Common.Items.Potion.PotionType.POTION_DURATION_LENGTH;
import static Utility.UI.ConsoleColors.*;
import static Utility.UI.UserInputs.showMenuAndGetUserAnswer;
import static Utility.Validators.Integers.validatePositiveIntegers;

public class Hero extends Figure implements LoadableFromText {
    private int xp;
    private int mp;
    private int mpMax;
    private int gold;
    private List<Armor> armorWorn;
    private List<Weapon> weaponsEquipped;
    private int numberOfHands;
    private HeroType heroType;

    private Inventory inventory;

    // attributes
    private int agility;
    private int strength;
    private int dexterity;

    public Hero(){
        //equipment
        initializeEmptyEquipment();
        xp= 0;
        baseDefense = 0;
        level = 1;
        numberOfHands = 2;
    }

    @Override
    public boolean dodgedSuccessfully() {
        return Math.random() > (agility * 0.002);
    }

    @Override
    public void updateEffect(HMLVEffect e, boolean adding) {
        int addition = adding ? (1 * e.getValue()) : (-1 * e.getValue());

        switch (e.getType()){
            case DEXTERITY:
                dexterity += addition;
                break;
            case STRENGTH:
                strength += addition;
                break;
            case AGILITY:
                agility += addition;
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

        ConsoleColors.printInColor(COLOR, "===== HERO STATUS =====");

        ConsoleColors.printInColor(COLOR, "Name: " + name + " (" + heroType.getName() + ")");
        ConsoleColors.printInColor(COLOR, "Level: " + level);
        ConsoleColors.printInColor(COLOR, "HP: " + hp + "/" + hpMax);
        ConsoleColors.printInColor(COLOR, "MP: " + mp + "/" + mpMax);

        ConsoleColors.printInColor(COLOR, "--- Attributes ---");
        ConsoleColors.printInColor(COLOR, "Strength:  " + strength);
        ConsoleColors.printInColor(COLOR, "Dexterity: " + dexterity);
        ConsoleColors.printInColor(COLOR, "Agility:   " + agility);

        // When OUTSIDE of battle
        if (state != HMGameState.BATTLING) {
            ConsoleColors.printInColor(COLOR, "XP: " + xp + "/" + getXpUntilLevelUp());
            ConsoleColors.printInColor(COLOR, "Gold: " + gold);

        } else {
            // When IN battle
            ConsoleColors.printInColor(COLOR, "--- Equipped Weapons ---");

            if (weaponsEquipped == null || weaponsEquipped.isEmpty()) {
                ConsoleColors.printInColor(COLOR, "None");
            } else {
                for (Weapon w : weaponsEquipped) {
                    ConsoleColors.printInColor(COLOR, "- " + w.getName());
                }
            }

            ConsoleColors.printInColor(COLOR, "--- Armor Worn ---");

            if (armorWorn == null || armorWorn.isEmpty()) {
                ConsoleColors.printInColor(COLOR, "None");
            } else {
                for (Armor a : armorWorn) {
                    ConsoleColors.printInColor(COLOR, "- " + a.getName());
                }
            }
        }

        ConsoleColors.printInColor(COLOR, "========================\n");
    }

    @Override
    public int getDamageBlocked() {
        int protection = 0;
        for (Armor a: armorWorn) protection += a.getDamageReduction();
        return (int)(baseDefense + protection * 0.05);
    }

    @Override
    public int getPunchDamage() {
        return strength/25;
    }

    public void reviveAfterBattle(){
        hp = hpMax / 2;
        mp = mpMax /2;
    }

    public void rewardHero(int goldGained, int xpGained){
        gold += goldGained;
        addXP(xpGained);
    }

    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();

        // Top border
//        b.append(returnInColor(WHITE_BACKGROUND, GeneralPrints.returnDoubleThickHorizontalLine()));

        // NAME AND TITLE
        b.append(returnInColor(PURPLE_BOLD,
                "HERO: " + name + "   (" + heroType + ")"));

        // BASIC STATS
        b.append(returnInColor(CYAN_BOLD,
                "Level: " + level));
        b.append(returnInColor(GREEN_BOLD,
                "HP: " + hp + "/" + hpMax));
        b.append(returnInColor(BLUE_BOLD,
                "MP: " + mp + "/" + mpMax));
        b.append(returnInColor(YELLOW_BOLD,
                "XP: " + xp + "/" + getXpUntilLevelUp()));
        b.append(returnInColor(RED_BOLD,
                "Gold: " + gold));

        // ATTRIBUTES
        b.append(returnInColor(WHITE_BOLD, "ATTRIBUTES"));
        b.append(returnInColor(GREEN,
                "Strength:  " + strength));
        b.append(returnInColor(BLUE,
                "Dexterity: " + dexterity));
        b.append(returnInColor(PURPLE,
                "Agility:   " + agility));

        // EQUIPMENT SECTIONS
        b.append(returnInColor(WHITE_BOLD, "EQUIPMENT"));

        // ----- WEAPONS -----
        b.append(returnInColor(YELLOW_BOLD, "Weapons Equipped:"));
        if (weaponsEquipped == null || weaponsEquipped.isEmpty()) {
            b.append(returnInColor(YELLOW, "  None"));
        } else {
            for (Weapon w : weaponsEquipped) {
                b.append(returnInColor(YELLOW, "  - " + w.getName()));
            }
        }

        // ----- ARMOR -----
        b.append(returnInColor(CYAN_BOLD, "Armor Worn:"));
        if (armorWorn == null || armorWorn.isEmpty()) {
            b.append(returnInColor(CYAN, "  None"));
        } else {
            for (Armor a : armorWorn) {
                b.append(returnInColor(CYAN, "  - " + a.getName()));
            }
        }

        // Bottom border
        b.append(returnInColor(WHITE_BACKGROUND, GeneralPrints.returnDoubleThickHorizontalLine(), false));

        return b.toString();
    }

    private void setMana(int mpMax){
        this.mpMax = mpMax;
        mp = mpMax;
    }

    private void initializeAttributes(int strength, int dexterity, int agility){
        /*
        Sets strength, dexterity, and agility
         */
        this.strength = strength + heroType.getStrengthBonus();
        this.dexterity = dexterity + heroType.getDexterityBonus();
        this.agility = agility + heroType.getAgilityBonus();

    }
    private void initializeEmptyEquipment(){
        weaponsEquipped = new ArrayList<>();
        armorWorn = new ArrayList<>();
        activeEffects = new HashSet<>();
        inventory = new Inventory();
    }

    private void setHeroTypeFromFileName(String fileName){
        if (fileName.length() == 0) throw new IllegalArgumentException("Filename is length 0 in setHeroTypeFromFileName()");
        switch (fileName){
            case "paladins":
                heroType = HeroType.PALADIN;
                break;
            case "sorcerers":
                heroType = HeroType.SORCERER;
                break;
            case "warriors":
                heroType = HeroType.WARRIOR;
                break;
            default:
                throw new IllegalArgumentException("Something went wrong in setHeroTypeFromFileName()");
        }
    }

    public void loadFromMap(Map<String, String> map){
//        Name/mana/strength/agility/dexterity/starting money/starting experience
        this.name = map.get("Name");
        this.gold= Integer.parseInt(map.get("starting money"));
        int xp_og = Integer.parseInt(map.get("starting experience"));
        addXP(xp_og);

        // Hero Type
        setHeroTypeFromFileName(map.get("file name"));
        // Hero attributes
        initializeAttributes(Integer.parseInt(map.get("strength")), Integer.parseInt(map.get("dexterity")), Integer.parseInt(map.get("agility")));
        // Mana
        setMana(Integer.parseInt(map.get("mana")));
        // Health
        calculateHpMax();

        validatePositiveIntegers(gold, numberOfHands, xp_og, mp, strength, dexterity, agility);
    }

    public static List<Hero> getAllHeroOptions(){
        ArrayList<Hero> heroes = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Heroes")){
                heroes.addAll(TextDataLoader.load("Heroes", filename, Hero.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all heroes: %s\n", e);
        }
//        System.out.format("Number of heroes gotten from the files: %d\n", heroes.size());
        return heroes;
    }

    public boolean canBuyItem(Item i){
        return i.getPrice() <= gold;
    }

    public int getXpUntilLevelUp(){
        return (level+1) * 10;
    }

//    SETTERS
    public void addXP(int p){
        /*
        Handles the level up logic in the case where xp + p > xp_map
         */
        xp = xp + p;
        int xpMax = getXpUntilLevelUp();
        if (xp > xpMax) levelUpAttributes();
    }

    public void levelUpAttributes(){
        /*
        Handles all health, mp, and attribute increase logic based on hero type
         */
        int xpMax = getXpUntilLevelUp();
        level += 1;
        xp = xp - xpMax;
        calculateHpMax(); // increase max Health
        setMana((int)(mpMax*1.1)); // increase max MP

        // increase all of their attributes
        switch (heroType.getFavoredTrait()){
            case STRENGTH:
                strength *= 1.1;
                agility *= 1.05;
                dexterity *= 1.05;
                break;
            case DEXTERITY:
                strength *= 1.05;
                agility *= 1.05;
                dexterity *= 1.1;
                break;
            case AGILITY:
                strength *= 1.05;
                agility *= 1.1;
                dexterity *= 1.05;
                break;
        }
        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("%s has leveled up to level %d", name, level));
    }

    public void gainMp(int p){
        mp = Math.min(mpMax, mp + p);
    }

    public void adjustGold(int g){
        /*
        To use when buying an item or selling an item or winning a battle
         */
        gold += g;
    }

    private void unequipItem(Item i){
        if (!i.isEquipable()) throw new IllegalArgumentException("This item is not equipable");
        
        if (i.getItemType() == ItemType.ARMOR){
            if (!armorWorn.contains(i)) throw new IllegalArgumentException("This armor is not equipped");
            armorWorn.remove(i);
        }
        else{
            if (!weaponsEquipped.contains(i)) throw new IllegalArgumentException("This weapon is not equipped");
            weaponsEquipped.remove(i);
        }
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, String.format("%s has been unequipped.", i.getName()));
    }

    public void useMana(int mp){
        this.mp = Math.max(0, this.mp - mp);
    }

    public Item selectItem(ItemType t){
        /*
        Returns item selected by user
         */
        Map<String, ? extends Item> map = getNonEquippedItemsOfSubCategory(t);
        String[] itemOptions = new String[map.size()];
        Set<String> illegalChoices = new HashSet<>();
        if (t==ItemType.SPELL){
            int i = 0;
            for(String key: map.keySet()){
                String item = key;
                if(!canCastSpell((Spell) map.get(key))){
                    item = RED + key + RESET;
                    illegalChoices.add(key);
                }
                itemOptions[i++] = item;
            }
        }
        else{
            itemOptions = map.keySet().toArray(new String[0]);
        }
        int itemToEquipIndex = showMenuAndGetUserAnswer(itemOptions);
        if (itemToEquipIndex < 0) return null;

        Item choice = map.get(itemOptions[itemToEquipIndex]);
        if (illegalChoices.contains(itemOptions[itemToEquipIndex])){
            ConsoleColors.printInColor(BLUE, String.format("⚠️%s does not have the required mana to cast this spell (%d < %d)", name, mp, ((Spell)choice).getMpCost()));
            return selectItem(t);
        }
        return choice;
    }

    private boolean canCastSpell(Spell s){
        /*
        Returns whether the hero's mana is enough to cast the spell
         */
        return s.getMpCost() <= mp;
    }

    public List<Spell> spellsHeroCanAffordToCast(List<Spell> spells){
        List<Spell> res = new ArrayList<>();
        for (Spell s: spells){
            if (canCastSpell(s)) res.add(s);
        }
        return res;
    }

    public boolean equipItem(ItemType t){
        /*
        Public method that handles which equip and unequip logic to use. Returns true if successful
         */

        if(getNonEquippedItemsOfSubCategory(t).size() == 0){
            ConsoleColors.printInColor(ConsoleColors.RED, String.format("⚠️%s does not have any other %ss that aren't already equipped", name, t));
            return false;
        }
        Item toEquip = selectItem(t);
        if (toEquip == null) return false;

        if (t == ItemType.WEAPON) equipWeapon((Weapon) toEquip);
        else if (t == ItemType.ARMOR) equipArmor((Armor) toEquip);
        else usePotion((Potion) toEquip);
        return true;
    }

    public Map<String, ? extends Item> getNonEquippedItemsOfSubCategory(ItemType t){
        /*
        Returns whether the user has more items they could equip instead of what they current have
         */
        Map<String, ? extends Item> map = new LinkedHashMap<>();
        switch (t) {
            case WEAPON:
                map = inventory.getSubInventoryOptions(inventory.getWeapons());
                map.remove(weaponsEquipped);
                break;
            case ARMOR:
                map = inventory.getSubInventoryOptions(inventory.getArmor());
                map.remove(armorWorn);
                break;
            case POTION:
                map = inventory.getSubInventoryOptions(inventory.getPotions());
                break;
            case SPELL:
                map = inventory.getSubInventoryOptions(inventory.getSpells());
                break;
        }

        return map;
    }

    private void equipWeapon(Weapon w){
        /*
        Equips a new weapon and deequips as many weapons as needed
         */
//        CASE #1 & #2: Need two hands for a weapon
        if (w.isTwoHanded()){
            while (weaponsEquipped.size() + 2 > numberOfHands){
                unequipItem(weaponsEquipped.get(0));
            }
            weaponsEquipped.add(w);
        }
//        CASE #3: Need one hand and there's one available
        else if(!w.isTwoHanded() && weaponsEquipped.size() < 2){
            weaponsEquipped.add(w);
        }
//        CASE #4: Need one hand and there's not one available
        else{
            unequipItem(weaponsEquipped.get(0));
            weaponsEquipped.add(w);
        }

        printItemEquipped(w);
    }

    private void printItemEquipped(Item i){
        if (!i.isEquipable()) throw new IllegalArgumentException("This item is not equipable");
        String m = "";

        switch (i.getItemType()){
            case ARMOR:
                m = String.format("🛡️ %s armor equipped!", i.getName());
                break;
            case WEAPON:
                m = String.format("⚔️ %s weapon equipped!", i.getName());
                break;
            default:
                throw new IllegalArgumentException("Something went wrong in printItemEquipped");
        }
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, m);
    }

    private void equipArmor(Armor a){
        for (Armor ar: armorWorn){
            unequipItem(ar);
        }
        armorWorn.add(a);
        printItemEquipped(a);
    }

    public void usePotion(Potion p){
        HashMap<PotionType, Integer> effects = p.getEffects();
        String message = String.format("🧪 Taking %s potion...", p.getName());
        ConsoleColors.printInColor(ConsoleColors.PURPLE_BOLD, message);
        for (PotionType type: effects.keySet()){
            triggerPotionEffect(p.getName(), type, effects.get(type));
        }
    }

    private void triggerPotionEffect(String potionName, PotionType type, int val){
        /*
        Internal method to apply a single effect of a potion
         */
        if (type == PotionType.HP){
            gainHp(val);
        }
        else if(type == PotionType.MP){
            gainMp(val);
        }
        else{
            String name = String.format("%s-%s", potionName, type.getName());
            HMLVEffect e = new HMLVEffect(name, val, POTION_DURATION_LENGTH, type.getType());
            updateEffect(e, true);
        }
        String message = String.format("- Gaining %s effect of %d", type.getName(), val);
        ConsoleColors.printInColor(ConsoleColors.PURPLE, message);
    }

    // Trading mechanics

    public void buyItem(Item i, Inventory merchant){
        /*
        Handles all validation, gold adjustment, and inventory swapping required
         */

        // eligible?
        if(this.level < i.getLevel()){
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s's level is too low to buy this item: %d < %d", name, level, i.getLevel()));

        }
        else if(!canBuyItem(i)) {
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s's gold is too low to buy this item: %d < %d", name, gold, i.getPrice()));
        }
        else{
            tradeItem(i, merchant, this.inventory);
            gold -= i.getPrice();
            i.setPrice(i.getPrice()/ 2);

        }

    }

    private boolean itemIsEquipped(Item i){
        /*
        Helper method that tells whether an item is equipped
         */
        switch (i.getItemType()){
            case WEAPON:
                return weaponsEquipped.contains(i);
            case ARMOR:
                return armorWorn.contains(i);
            default:
                return false;
        }
    }

    public void sellItem(Item i, Inventory merchant){
        /*
        Handles all handover logic and gold gaining and equipped or not validation
         */

        // not equipped?
        if (itemIsEquipped(i)){
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s can't sell an item that is equipped", name));
        }
        else{
            tradeItem(i, this.inventory, merchant);
            gold += i.getPrice();
            i.setPrice(i.getPrice() * 2);
        }

    }

    public static Party<Hero> selectYourHeroes(int maxPartySize){
        /*
        Hero selection screen
         */
        Integers.validatePositiveIntegers(maxPartySize);
        List<Hero> options = getAllHeroOptions();
        Party<Hero> party = new Party<>(maxPartySize);

        while(options.size() > 0 && party.canAddAnotherMember()){
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Pick which hero(es) you want to add to your party!");
            Hero choice = getUserHeroChoice(options);
            party.addMember(choice);
            options.remove(choice);
        }
        return party;
    }

    public static Hero getUserHeroChoice(List<Hero> optionsLeft){
        /*
        Randomly picks 3 heroes from optionsLeft and prints them along with the menu to select them or not
         */
        // Pick random choices to show user
        if (optionsLeft.size() < 1) throw new IllegalArgumentException("Hero optionsLeft List is empty");
        int number_of_choices = 3;
        String[] choices = new String[number_of_choices];
        Set<Integer> usedIndices = new HashSet<>();
        int i =0;

        HashMap<Integer, Hero> backMap = new HashMap<>();
        while (i < number_of_choices){
            Random random = new Random();
            int randomIndex = random.nextInt(optionsLeft.size());
            if (usedIndices.contains(randomIndex)) continue; //prevent printing the same character twice
            Hero h = optionsLeft.get(randomIndex);
            backMap.put(i, h);
            usedIndices.add(randomIndex);
            choices[i] = h.getName();
            System.out.println(h);
            i++;
        }

        int heroChoiceIndex = -1;
        heroChoiceIndex = showMenuAndGetUserAnswer(choices);
        while(heroChoiceIndex < 0){
            ConsoleColors.printInColor(RED, "Illegal Option chosen, sorry. Please try again.");
            heroChoiceIndex = showMenuAndGetUserAnswer(choices);
        }
        return backMap.get(heroChoiceIndex);
    }

//    GETTERS
    public int getGold() {
        return gold;
    }

    public int getMp() {
        return mp;
    }

    public int getXp() {
        return xp;
    }

    public int getMpMax() {
        return mpMax;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public List<Weapon> getWeaponsEquipped() {
        return weaponsEquipped;
    }
}
