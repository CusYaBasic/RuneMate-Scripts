package com.DefiledOne.scripts.Shared;

import com.DefiledOne.scripts.Shared.Enums.TreesEnum;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Landmark;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.web.WebPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import java.util.Arrays;
import java.util.List;

public class TreesShared
{
    public final static Area draynorOakTreeArea = new Area.Rectangular(new Coordinate(3102,3246,0), new Coordinate(3100,3244,0));
    public final static Area draynorTreeArea = new Area.Rectangular(new Coordinate(3105,3231,0), new Coordinate(3104,3230,0));
    public final static Area draynorWillowTreeArea = new Area.Rectangular(new Coordinate(3091,3230,0), new Coordinate(3084,3235,0));

    public final static List<Coordinate> draynorOakCoords = List.of(new Coordinate(3102, 3242, 0));
    public final static List<Coordinate> draynorTreeCoords = Arrays.asList(new Coordinate(3105,3228,0),
            new Coordinate(3106,3231,0), new Coordinate(3107,3229,0), new Coordinate(3104,3232,0));
    public final static List<Coordinate> draynorWillowCoords = Arrays.asList(new Coordinate(3088,3234,0), new Coordinate(3087,3231,0));

    public static boolean isChopping = false;
    private static boolean allTreesDead = true;

    public static List<Coordinate> getTreeCoords(TreesEnum tree)
    {

        return switch (tree) {
            case TREE -> draynorTreeCoords;
            case OAK -> draynorOakCoords;
            case WILLOW -> draynorWillowCoords;
        };
    }
    public static Area getTreeArea(TreesEnum tree)
    {
        new Area.Rectangular(new Coordinate(0, 0, 0), new Coordinate(0, 0, 0));

        return switch (tree)
        {
            case TREE -> draynorTreeArea;
            case OAK -> draynorOakTreeArea;
            case WILLOW -> draynorWillowTreeArea;
        };
    }

    public static String getStumpName(TreesEnum tree)
    {

        return switch (tree)
        {
            case TREE, OAK -> "Tree stump";
            case WILLOW -> "Tree Stump";
        };
    }

    public static void moveToTreeAndChop(TreesEnum tree)
    {
        final var player = Players.getLocal();
        final Coordinate treeCoord = getTreeArea(tree).getRandomCoordinate();
        final var treeObject = GameObjects.newQuery().names(tree.getGameName()).on(getTreeCoords(tree)).results().nearest();

        if(treeObject != null)
        {
            if(treeObject.isVisible())
                if (PlayerSense.getAsBoolean(String.valueOf(BasePlayerSense.Key.RANDOM_DOUBLE_CLICK)))
                    for (int i  = 0; i < 2; i++)
                    {
                        Execution.delay(PlayerSense.getAsLong(String.valueOf(BasePlayerSense.Key.REACTION_TIME)));
                        chopTree(treeObject);
                    }
                    else
                        chopTree(treeObject);
            else
            {
                final WebPath path = WebPath.buildTo(treeObject);

                if (!PlayerShared.isPlayerMoving() || Distance.between(Players.getLocal().getPosition(), treeCoord) > Random.nextInt(15,20))
                    if (path != null)
                    {
                        System.out.print("Moving to tree!\n");
                        Execution.delay(1000,5500);
                        path.step();
                    }
            }
        }
    }

    public static void chopTree(GameObject tree)
    {
        final var player = Players.getLocal();

        if (player != null && player.getAnimationId() != -1)
        {
            isChopping = true;
            Execution.delay(3000, 5500);
            return;
        }
        else
        {
            isChopping = false;
        }

        if(PlayerShared.isInventoryFull() || PlayerShared.isPlayerMoving())
        {
            return;
        }

        if (tree != null)
        {
            System.out.print("Tree found!\n");
            allTreesDead = false;

            if (!isChopping)
            {
                Execution.delay(2000, 4000);
                tree.interact("Chop down");
                Execution.delay(1000, 3000);
            }
        }
        else
            allTreesDead = true;

        if (!allTreesDead)
        {
            System.out.print("Waiting for tree respawn!\n");
            Execution.delay(4000, 8000);
            return;
        }
    }
}
