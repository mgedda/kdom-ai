package kingdominoplayer.tinyrepresentation.datastructures;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-17<br>
 * Time: 10:45<br><br>
 *
 * Constants for efficient byte array representation.<br>
 * <br>
 * The types specify the structure of the byte array. The different types are defined as follows.
 *
 * <pre>
 *
 *   KINGDOM_TERRAINS :     [ {KINGDOM_TERRAINS_ROW_0} | {KINGDOM_TERRAINS_ROW_1} | ... | {KINGDOM_TERRAINS_ROW_8} ]
 *
 *
 *   KINGDOM_TERRAINS_ROW : [ T(-4, R) | T(-3, R) | ... | T(4, R) ]   (R=Row number : [-4, 4])
 *
 *                            T(X, Y) : Terrain at coordinate (X, Y)  (X: [-4, 4], Y: [-4, 4])
 *
 *
 *   KINGDOM_CROWNS :       [ {KINGDOM_CROWNS_ROW_0} | {KINGDOM_CROWNS_ROW_1} | ... | {KINGDOM_CROWNS_ROW_8} ]
 *
 *
 *   KINGDOM_CROWNS_ROW :   [ C(-4, R) | C(-3, R) | ... | C(4, R) ]   (R=Row number : [-4, 4])
 *
 *                            C(X, Y) : Crowns at coordinate (X, Y)  (X: [-4, 4], Y: [-4, 4])
 *
 *
 *   DRAFT :                [ {DRAFT_ELEMENT_0} | {DRAFT_ELEMENT_1} | ... | {DRAFT_ELEMENT_N-1} ]  (N=Number of draft elements)
 *
 *
 *   DRAFT_ELEMENT :        [ {DOMINO} | PID ]
 *
 *                            PID : Player ID (: [0, N-1], N=Number of players)
 *
 *
 *   DRAW_PILE :            [ {DOMINO_0} | {DOMINO_1} | ... | {DOMINO_N-1} ]  (N=Number of dominoes)
 *
 *
 *   DOMINO :               [ ID | T1_T | T1_C | T2_T | T2_C | {DOMINOPOSITION} ]
 *
 *                            ID   : Domino ID (/domino number)
 *                            T1_T : Tile 1 terrain
 *                            T1_C : Tile 1 crowns
 *                            T2_T : Tile 2 terrain
 *                            T2_C : Tile 2 crowns
 *
 *
 *   DOMINOPOSITION :       [ T1_X | T1_Y | T2_X | T2_Y ]
 *
 *                            T1_X : Tile 1 X-coordinate (X: [-4, 4])  [default: INVALID_PLACEMENT_VALUE]
 *                            T1_Y : Tile 1 Y-coordinate (Y: [-4, 4])  [default: INVALID_PLACEMENT_VALUE]
 *                            T2_X : Tile 2 X-coordinate (X: [-4, 4])  [default: INVALID_PLACEMENT_VALUE]
 *                            T2_Y : Tile 2 Y-coordinate (Y: [-4, 4])  [default: INVALID_PLACEMENT_VALUE]
 *
 *
 *   MOVE :                 [ ID | {DOMINO_C} | {DOMINO_P} ]
 *
 *                            ID       : Move ID (/move number)
 *                            DOMINO_C : Chosen domino
 *                            DOMINO_P : Placed domino
 *
 * </pre>
 *
 *
 * Note that DRAFTs are always constant size. If a DRAFT_ELEMENT is removed from the draft, the element is
 * replaced by INVALID_DOMINO_VALUEs to signify an empty slot.
 * The order in the a draft is always kept from low to high domino values with any empty slots at the end
 * of the draft. So if a middle element is removed
 * from the draft the succeeding elements in the draft are moved up one place.
 * This enables us to check if a draft is empty by simply checking the first element
 * in the array.<br>
 * <br>
 * The opposite is true for the DRAW_PILE. It decreases in size when dominoes are drawn.
 *
 */
public class TinyConst
{
    // KINGDOM

    public static int KINGDOM_X_SIZE = 9;
    public static int KINGDOM_Y_SIZE = 9;
    public static int SINGLE_PLAYER_KINGDOM_SIZE = KINGDOM_X_SIZE * KINGDOM_Y_SIZE;   // 9x9 terrains or crowns


    // DOMINO

    public static int DOMINO_SIZE = 9;
    public static int DOMINO_ID_INDEX = 0;
    public static int DOMINO_TILE_1_TERRAIN_INDEX = 1;
    public static int DOMINO_TILE_1_CROWNS_INDEX = 2;
    public static int DOMINO_TILE_2_TERRAIN_INDEX = 3;
    public static int DOMINO_TILE_2_CROWNS_INDEX = 4;
    public static int DOMINO_TILE_1_X_INDEX = 5;
    public static int DOMINO_TILE_1_Y_INDEX = 6;
    public static int DOMINO_TILE_2_X_INDEX = 7;
    public static int DOMINO_TILE_2_Y_INDEX = 8;


    // DRAFT

    public static int DRAFT_ELEMENT_SIZE = DOMINO_SIZE + 1;
    public static int DRAFT_ELEMENT_PLAYER_ID_INDEX = DRAFT_ELEMENT_SIZE - 1;


    // DOMINOPOSITION

    public static int DOMINOPOSITION_ELEMENT_SIZE = 4;
    public static int DOMINOPOSITION_TILE_1_X_INDEX = 0;
    public static int DOMINOPOSITION_TILE_1_Y_INDEX = 1;
    public static int DOMINOPOSITION_TILE_2_X_INDEX = 2;
    public static int DOMINOPOSITION_TILE_2_Y_INDEX = 3;


    // MOVE

    public static int MOVE_ELEMENT_SIZE = 1 + 2 * DOMINO_SIZE;
    public static int MOVE_NUMBER_INDEX = 0;
    public static int MOVE_CHOSEN_DOMINO_INDEX = 1;
    public static int MOVE_PLACED_DOMINO_INDEX = MOVE_CHOSEN_DOMINO_INDEX + DOMINO_SIZE;


    // INVALID VALUES

    public static byte INVALID_PLAYER_ID = (byte) -1;
    public static byte INVALID_PLACEMENT_VALUE = (byte) -99;
    public static byte INVALID_DOMINO_VALUE = (byte) -1;
}
