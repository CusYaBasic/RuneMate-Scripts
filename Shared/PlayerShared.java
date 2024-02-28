package com.DefiledOne.scripts.Shared;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.region.Players;

public class PlayerShared
{
    /**
     * Checks if our local player is valid
     * @return Returns true if our local player is valid
     */
    public static boolean isPlayerValid()
    {
        if (Players.getLocal() == null)
        {
            System.out.print("Player is not valid!\n");
            return false;
        }

        return true;
    }

    /**
     * Checks if our local player is moving
     * @return Returns true if our local player is moving
     */
    public static boolean isPlayerMoving()
    {
        Player player = Players.getLocal();

        if (player != null)
            if (player.isMoving())
            {
                return true;
            }
            else
                return false;
        else
            return false;
    }

    /**
     * Checks if our local player inventory is full.
     * @return Returns true if our local player inventory is full.
     */
    public static boolean isInventoryFull()
    {
        if (!Inventory.isFull())
            return false;
        else
        {
            return true;
        }
    }

    /**
     * Checks if our local player is in a given area.
     * @param area Given Area that the player needs to be in.
     * More info on areas can be found here: {@link #com.runemate.game.api.hybrid.location.Area}
     * @return Returns false if our player is not in area or if local player is not valid.
     */
    public static boolean isPlayerInArea(Area area)
    {
        final var player = Players.getLocal();

        if (player != null)
        {
            if (area.contains(player))
                return true;
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    }

    /**
     * Checks if we are equipping a woodcutting axe
     * @return Returns true if we have a woodcutting axe equipped
     */
    public static boolean hasWoodcuttingAxeEquipped()
    {
        for (String item : ItemListsShared.woodcuttingAxes)
        {
            if (Equipment.containsAnyOf(item))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if we have a woodcutting axe in our inventory
     * @return Returns true if we have a woodcutting axe in our inventory
     */
    public static boolean hasWoodcuttingAxeInInventory()
    {
        boolean hasAxe = false;
        StringBuilder axesString = new StringBuilder();

        for (String item : ItemListsShared.woodcuttingAxes)
        {
            if (Inventory.containsAnyOf(item))
            {
               axesString.append(item).append(", ");
               hasAxe = true;
            }
        }

        return hasAxe;
    }

    /**
     * Checks if we have a woodcutting axe in our inventory OR if we have one equipped
     * @return Returns true if we have a woodcutting axe in our inventory OR if we have one equipped
     */
    public static boolean hasAnyWoodcuttingAxe()
    {
        if (hasWoodcuttingAxeEquipped() || hasWoodcuttingAxeInInventory())
        {
            return true;
        }

        return false;
    }
}
