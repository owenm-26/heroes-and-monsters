# Legends of Valor

CS611 Final Project — Java / Maven

---

## Project Overview

This project implements two turn-based role-playing games in Java:

- **Heroes and Monsters**
- **Legends of Valor** (an extension of Heroes and Monsters)

The project emphasizes object-oriented design, code reuse, extensibility, and data-driven configuration.

Legends of Valor reuses the Heroes and Monsters framework while adding a lane-based board, movement constraints, teleportation, recall mechanics, terrain bonuses, and AI-controlled monsters.

The project is implemented as a **Maven project** and is fully compatible with **Java 8**.

---

## Legends of Valor

Legends of Valor extends Heroes and Monsters into a tactical, lane-based strategy game played on a grid.

---

### Board Layout

- 8x8 board
- Three vertical lanes:
  - Top lane
  - Middle lane
  - Bottom lane
- Lanes are separated by inaccessible wall columns

Each lane has:
- Hero Nexus at the bottom
- Monster Nexus at the top

---

### Turn System

Lane-based turn order:

- Lane 1: Hero → Monster
- Lane 2: Hero → Monster
- Lane 3: Hero → Monster

The sequence repeats every round.

---

### Movement Rules

- No diagonal movement
- Movement allowed only up, down, left, or right

Heroes **cannot move**:
- Into inaccessible squares
- Into a square occupied by another hero
- Behind a monster without killing it

Monsters follow the same movement constraints.

Strategic blocking is allowed.

---

### Special Actions

**Teleport**  
A hero may teleport to a square adjacent to another hero in a different lane, subject to movement legality.

**Recall**  
A hero may return instantly to their original Nexus.

**Pass Turn**  
A hero or monster may choose to pass their turn.

---

### Terrain Effects

- **Bush**: Dexterity bonus
- **Cave**: Agility bonus
- **Koulou**: Strength bonus

Terrain bonuses apply while a hero stands on the square.

---

## Design and Architecture

### Object-Oriented Design

**Inheritance**
- `Game` → `HMGame`, `LVGame`
- `Board` → `HMBoard`, `LVBoard`
- `Figure` → `Hero`, `Monster`

**Composition**
- Inventory
- Effects
- Items

**Encapsulation**
- UI, game logic, and data loading are clearly separated

---

### Design Patterns

- Template Method Pattern: Game lifecycle
- Strategy Pattern: Monster targeting behavior
- Enum-based command abstraction for user input
- Data-driven configuration using text files

---

### Extensibility

- New heroes, monsters, and items can be added via text files
- New monster AI strategies can be added without modifying existing logic
- Additional game modes can be implemented using the shared framework

---

## Project Structure

<pre>
src/main/java
├── Common
│   ├── Data
│   │   ├── Armor
│   │   │   └── Armory.txt
│   │   ├── Heroes
│   │   │   ├── Paladins.txt
│   │   │   ├── Sorcerers.txt
│   │   │   └── Warriors.txt
│   │   ├── Monsters
│   │   │   ├── Dragons.txt
│   │   │   ├── Exoskeletons.txt
│   │   │   └── Spirits.txt
│   │   ├── Potions
│   │   │   └── Potions.txt
│   │   ├── Spells
│   │   │   ├── FireSpells.txt
│   │   │   ├── IceSpells.txt
│   │   │   └── LightningSpells.txt
│   │   ├── Weapons
│   │   │   └── Weaponry.txt
│   │   ├── LoadableFromText.java
│   │   └── TextDataLoader.java
│   │
│   ├── Figures
│   │   ├── Hero
│   │   │   ├── Hero.java
│   │   │   └── HeroType.java
│   │   ├── Monster
│   │   │   ├── Monster.java
│   │   │   ├── MonsterType.java
│   │   │   └── Strategies
│   │   │       ├── MonsterAttackStrategy.java
│   │   │       ├── RandomTarget.java
│   │   │       ├── MostHealthTarget.java
│   │   │       ├── LeastHealthTarget.java
│   │   │       └── LowestLevelTarget.java
│   │   ├── Figure.java
│   │   ├── Party.java
│   │   └── TraitType.java
│   │
│   └── Gameboard
│       ├── Board.java
│       ├── Game.java
│       ├── Square.java
│       ├── Player.java
│       ├── Piece.java
│       └── Effect.java
│
└── PlayGame.java
</pre>


---

## How to Compile and Run (Java 8)

### Requirements

- Java 8 or higher
- Apache Maven

Verify installations:

```bash
java -version
mvn -version
```

**Compile**

From the project root (where pom.xml is located):
```bash
mvn clean compile
```
**Run**

Using Maven:
```bash
mvn exec:java
```

**Or using compiled classes:**
```bash
mvn package
java -cp target/classes PlayGame
```

**All commands are terminal-based and IDE-independent.**

## Controls
**Main Menu**

- 1 — Play Heroes and Monsters
- 2 — Play Legends of Valor
- q — Quit

**Legends of Valor**

- W A S D — Move
- T — Teleport
- R — Recall
- P — Pass turn
- Q — Quit game

**Notes for Graders**

- This is a Maven project
- Compatible with Java 8
- No external libraries required
- Entry point: PlayGame.java
- Entire project runs from the terminal
