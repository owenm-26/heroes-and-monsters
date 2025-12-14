package LegendsOfValor.GameBoard;

import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Gameboard.Board;
import Common.Gameboard.Piece;
import LegendsOfValor.GameBoard.LVSquare.LVSquare;
import LegendsOfValor.GameBoard.LVSquare.LVSquareType;
import Utility.UI.ConsoleColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LVBoard extends Board<LVSquare> {

    public static final int SIZE = 8;

    // Terrain distribution: 40% plain, 20% each special B/C/K
    private static final double PROB_PLAIN  = 0.45;
    private static final double PROB_OBSTACLE = 0.10;
    private static final double PROB_BUSH   = 0.15;
    private static final double PROB_CAVE   = 0.15;
    private static final double PROB_KOULOU = 0.15;

    // Track hero labels (H1, H2, H3) and spawn points for recall
    private final Map<Hero, String> heroLabels = new HashMap<>();
    private final Map<Hero, int[]> heroSpawnLocations = new HashMap<>();

    public LVBoard() {
        super(SIZE);
        //noinspection unchecked
        this.grid = (LVSquare[][]) new LVSquare[SIZE][SIZE];
        initializeBoard();
    }

    @Override
    protected void validateDimension(int n) {
        if (n != SIZE) {
            throw new IllegalArgumentException("LVBoard must be 8x8.");
        }
    }

    // ============================================================
    // ================ BOARD INITIALIZATION ======================
    // ============================================================

    private void initializeBoard() {
        List<int[]> laneCells = new ArrayList<>();
        int lastRow = SIZE - 1;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                // Wall columns (inaccessible)
                if (isWallColumn(c)) {
                    grid[r][c] = new LVSquare(LVSquareType.INACCESSIBLE);
                    continue;
                }

                // Nexus rows (top and bottom, excluding walls)
                if (r == 0 || r == lastRow) {
                    grid[r][c] = new LVSquare(LVSquareType.NEXUS);
                    continue;
                }

                // Lane cells get random terrain
                LVSquareType terrain = randomTerrain();
                grid[r][c] = new LVSquare(terrain);
                laneCells.add(new int[]{r, c});
            }
        }

        // Ensure at least one of each special terrain exists
        enforceTerrainVariety(laneCells);
    }

    private boolean isWallColumn(int col) {
        return col == 2 || col == 5;
    }

    private LVSquareType randomTerrain() {
        double p = random.nextDouble();

        if (p < PROB_PLAIN) return LVSquareType.PLAIN;
        p -= PROB_PLAIN;
        if (p < PROB_BUSH) return LVSquareType.BUSH;
        p -= PROB_BUSH;
        if (p < PROB_CAVE) return LVSquareType.CAVE;
        p -= PROB_CAVE;
        if (p < PROB_KOULOU) return LVSquareType.KOULOU;
        return LVSquareType.OBSTACLE;
    }

    private void enforceTerrainVariety(List<int[]> cells) {
        boolean bush = false, cave = false, koulou = false;

        for (int[] pos : cells) {
            LVSquareType t = grid[pos[0]][pos[1]].getType();
            if (t == LVSquareType.BUSH)   bush = true;
            if (t == LVSquareType.CAVE)   cave = true;
            if (t == LVSquareType.KOULOU) koulou = true;
        }

        Random rnd = new Random();

        if (!bush)   convertPlain(cells, LVSquareType.BUSH, rnd);
        if (!cave)   convertPlain(cells, LVSquareType.CAVE, rnd);
        if (!koulou) convertPlain(cells, LVSquareType.KOULOU, rnd);
    }

    private void convertPlain(List<int[]> cells, LVSquareType target, Random rnd) {
        List<int[]> plains = new ArrayList<>();
        for (int[] pos : cells) {
            if (grid[pos[0]][pos[1]].getType() == LVSquareType.PLAIN) {
                plains.add(pos);
            }
        }
        if (plains.isEmpty()) return;

        int[] spot = plains.get(rnd.nextInt(plains.size()));
        grid[spot[0]][spot[1]] = new LVSquare(target);
    }

    // ============================================================
    // ==================== DISPLAY BOARD =========================
    // ============================================================

    @Override
    public void displayBoard() {

        String border = buildBorderLine();

        System.out.println("\n=== Legends of Valor Map ===\n");

        for (int r = 0; r < SIZE; r++) {

            System.out.println(border);

            for (int c = 0; c < SIZE; c++) {
                LVSquare square = grid[r][c];
                LVSquareType t = square.getType();

                // Decide what to show: hero (H1/H2/H3), monster (M), or terrain
                String content = " ";
                String color   = t.getColor();

                String heroLabel = null;
                boolean monsterHere = false;

                for (Piece p : square.getPieces()) {
                    if (p instanceof Hero) {
                        Hero h = (Hero) p;
                        heroLabel = heroLabels.getOrDefault(h, "H");
                    } if (p instanceof Monster) {
                        monsterHere = true;
                    }
                }

                if(heroLabel!=null && monsterHere){
                    content = String.format("%s/M", heroLabel);
                }
                else if (heroLabel != null) {
                    content = heroLabel;
                } else if (monsterHere) {
                    content = "M";
                } else {
                    content = t.getSymbol();
                }
                String paddedContent = String.format("%-4s", content);
                String cell = color + paddedContent + ConsoleColors.RESET;
                System.out.print("| " + cell + " ");
            }

            System.out.println("|");
        }

        System.out.println(border);
        LVSquareType.displayLegend();
    }

    private String buildBorderLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            sb.append("+------");
        }
        sb.append("+");
        return sb.toString();
    }

    // ============================================================
    // ================== PIECE PLACEMENT =========================
    // ============================================================

    //TODO: allow the player to choose which lanes each hero goes in

    // Labeled placement used by LVGame (H1, H2, H3)
    public void placeHero(Hero h, int row, int col, String label) {
        grid[row][col].getPieces().add(h);
        heroLabels.put(h, label);
        heroSpawnLocations.putIfAbsent(h, new int[]{row, col});
    }

    public void placeMonster(Monster m, int row, int col) {
        grid[row][col].getPieces().add(m);
    }

    // ============================================================
    // ============ LOCATE HEROES / MONSTERS ON BOARD ============
    // ============================================================

    public int[] getHeroPosition(Hero h) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                for (Piece p : grid[r][c].getPieces()) {
                    if (p == h) {
                        return new int[]{r, c};
                    }
                }
            }
        }
        return null;
    }

    public int[] getMonsterPosition(Monster m) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                for (Piece p : grid[r][c].getPieces()) {
                    if (p == m) {
                        return new int[]{r, c};
                    }
                }
            }
        }
        return null;
    }

    private boolean squareHasHero(int r, int c) {
        for (Piece p : grid[r][c].getPieces()) {
            if (p instanceof Hero) return true;
        }
        return false;
    }

    private boolean squareHasMonster(int r, int c) {
        for (Piece p : grid[r][c].getPieces()) {
            if (p instanceof Monster) return true;
        }
        return false;
    }

    // Lane: 0 = columns 0–1, 1 = columns 3–4, 2 = columns 6–7
    private int getLane(int col) {
        if (col <= 1) return 0;
        if (col >= 3 && col <= 4) return 1;
        return 2;
    }

    private boolean isInsideBoard(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private boolean isBlockedTerrain(LVSquareType t) {
        return t == LVSquareType.INACCESSIBLE;
        // If you later add OBSTACLE as blocking, OR it here:
        // return t == LVSquareType.INACCESSIBLE || t == LVSquareType.OBSTACLE;
    }

    private void movePiece(Piece p, int oldR, int oldC, int newR, int newC) {
        grid[oldR][oldC].getPieces().remove(p);
        grid[newR][newC].getPieces().add(p);
    }

    // ============================================================
    // ==================== HERO MOVEMENT =========================
    // ============================================================

    /**
     * Move hero by direction code (e.g., "w","a","s","d").
     * Uses Board.dirs to translate into row/col deltas.
     * Returns true if move is successful, false if illegal.
     */
    public boolean moveHero(Hero h, String directionCode) {
        Integer[] delta = dirs.get(directionCode);
        if (delta == null) return false;

        int[] pos = getHeroPosition(h);
        if (pos == null) return false;

        int oldR = pos[0], oldC = pos[1];
        int newR = oldR + delta[0];
        int newC = oldC + delta[1];

        if (!canHeroMoveTo(h, oldR, oldC, newR, newC)) {
            return false;
        }

        movePiece(h, oldR, oldC, newR, newC);
        return true;
    }

    private boolean canHeroMoveTo(Hero h, int oldR, int oldC, int newR, int newC) {
        // Bounds check
        if (!isInsideBoard(newR, newC)) return false;

        LVSquareType destType = grid[newR][newC].getType();

        // Cannot enter walls / inaccessible / obstacles (if you treat as blocking)
        if (isBlockedTerrain(destType)) return false;

        // Cannot move into another hero
        if (squareHasHero(newR, newC)) return false;

        // "Cannot move behind a monster without killing it"
        // Interpret as: hero cannot move to a row that is in front of (toward enemy Nexus)
        // past the closest monster in the same lane.
        int lane = getLane(oldC);

        // Only relevant when moving UP (toward enemy Nexus: row index decreases)
        if (newR < oldR) {
            int nearestMonsterRow = findNearestMonsterInLane(lane, oldR);
            if (nearestMonsterRow != -1 && newR < nearestMonsterRow) {
                // Trying to move past the monster
                return false;
            }
        }

        return true;
    }

    private int findNearestMonsterInLane(int lane, int heroRow) {
        int closestRow = -1;
        int[] cols = laneColumns(lane);
        for (int r = 0; r <= heroRow; r++) {
            for (int c : cols) {
                if (squareHasMonster(r, c)) {
                    // Closest monster ABOVE hero is the one with largest row < heroRow
                    closestRow = Math.max(closestRow, r);
                }
            }
        }
        return closestRow;
    }

    private int[] laneColumns(int lane) {
        switch (lane) {
            case 0: return new int[]{0, 1};
            case 1: return new int[]{3, 4};
            case 2: return new int[]{6, 7};
            default: return new int[]{};
        }
    }

    // ============================================================
    // ==================== TELEPORT (HERO) =======================
    // ============================================================

    /**
     * Teleport hero h to a tile adjacent to target hero in a different lane.
     * Enforces:
     * - Cannot teleport within the same lane
     * - Must land on an adjacent tile to the target
     * - Cannot land in front of the target (toward enemy Nexus)
     * - Cannot land on a hero
     * - Cannot land behind a monster in the lane you teleport into
     * Returns true if teleport succeeds, false otherwise.
     */
    public boolean teleportHero(Hero h, Hero target) {
        if (h == target) return false;

        int[] srcPos = getHeroPosition(h);
        int[] tgtPos = getHeroPosition(target);
        if (srcPos == null || tgtPos == null) return false;

        int srcLane = getLane(srcPos[1]);
        int tgtLane = getLane(tgtPos[1]);
        if (srcLane == tgtLane) {
            // Must teleport to a different lane
            return false;
        }

        int tr = tgtPos[0];
        int tc = tgtPos[1];

        // Candidate adjacent positions (up, down, left, right)
        int[][] candidates = new int[][]{
                {tr - 1, tc}, // above
                {tr + 1, tc}, // below
                {tr, tc - 1}, // left
                {tr, tc + 1}  // right
        };

        for (int[] cand : candidates) {
            int nr = cand[0], nc = cand[1];

            if (!isInsideBoard(nr, nc)) continue;
            if (isWallColumn(nc)) continue; // don't teleport onto wall columns
            LVSquareType type = grid[nr][nc].getType();
            if (isBlockedTerrain(type)) continue;

            // Cannot land on top of another hero
            if (squareHasHero(nr, nc)) continue;

            // Cannot teleport AHEAD of target (toward enemy Nexus)
            // Heroes move upward (row decreasing). Ahead = any row < targetRow.
            if (nr < tr) continue;

            // New lane for landing
            int newLane = getLane(nc);

            // Cannot teleport behind a monster in that lane
            if (isBehindMonsterInLane(nr, newLane)) continue;

            // Passed all checks: perform teleport
            movePiece(h, srcPos[0], srcPos[1], nr, nc);
            return true;
        }

        return false;
    }

    private boolean isBehindMonsterInLane(int destRow, int lane) {
        // Being "behind" monster means there exists monster with row > destRow
        int[] cols = laneColumns(lane);
        for (int r = destRow + 1; r < SIZE; r++) {
            for (int c : cols) {
                if (squareHasMonster(r, c)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ============================================================
    // ====================== RECALL (HERO) =======================
    // ============================================================

    /**
     * Recall hero back to their original Nexus spawn tile.
     * Cannot recall if Nexus spawn tile is occupied by another hero.
     * Monster presence does NOT block recall.
     */
    public boolean recallHero(Hero h) {
        int[] spawn = heroSpawnLocations.get(h);
        if (spawn == null) return false;

        int destR = spawn[0];
        int destC = spawn[1];

        // Cannot recall if another hero is already at that Nexus
        if (squareHasHero(destR, destC)) {
            // But if the only hero there is this hero, it's fine
            boolean otherHeroPresent = false;
            for (Piece p : grid[destR][destC].getPieces()) {
                if (p instanceof Hero && p != h) {
                    otherHeroPresent = true;
                    break;
                }
            }
            if (otherHeroPresent) return false;
        }

        int[] cur = getHeroPosition(h);
        if (cur == null) return false;

        movePiece(h, cur[0], cur[1], destR, destC);
        return true;
    }

    // ============================================================
    // =================== MONSTER MOVEMENT (BASIC) ===============
    // ============================================================

    /**
     * Simple monster movement: attempt to move one tile downward (toward heroes' Nexus).
     * Applies similar "no passing heroes" rule and terrain constraints.
     */
    public boolean moveMonsterForward(Monster m) {
        int[] pos = getMonsterPosition(m);
        if (pos == null) return false;

        int oldR = pos[0], oldC = pos[1];
        int newR = oldR + 1; // move downwards
        int newC = oldC;

        if (!isInsideBoard(newR, newC)) return false;

        LVSquareType destType = grid[newR][newC].getType();
        if (isBlockedTerrain(destType)) return false;

        // "cannot move behind hero" – i.e., cannot move to a row that is further
        // toward heroes' Nexus past the closest hero in this lane.
        int lane = getLane(oldC);
        int nearestHeroRow = findNearestHeroInLaneBelow(lane, oldR);
        if (nearestHeroRow != Integer.MAX_VALUE && newR > nearestHeroRow) {
            return false;
        }

        movePiece(m, oldR, oldC, newR, newC);
        return true;
    }

    private int findNearestHeroInLaneBelow(int lane, int monsterRow) {
        int minRow = Integer.MAX_VALUE;
        int[] cols = laneColumns(lane);
        for (int r = monsterRow; r < SIZE; r++) {
            for (int c : cols) {
                if (squareHasHero(r, c)) {
                    minRow = Math.min(minRow, r);
                }
            }
        }
        return minRow;
    }
}
