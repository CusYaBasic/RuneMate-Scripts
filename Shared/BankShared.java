package com.DefiledOne.scripts.Shared;

import com.DefiledOne.scripts.Shared.Enums.BanksEnum;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Regex;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class BankShared
{
    /**
     * Draynor Bank Area
     */
    public static Area draynorBankArea = new Area.Rectangular(new Coordinate(3092,3241,0), new Coordinate(3095,3245,0));

    public static List<Coordinate> draynorBankers = List.of(new Coordinate(3090, 3243, 0));

    public static Area getBankArea(BanksEnum bank)
    {
        Area area = new Area.Rectangular(new Coordinate(0,0,0), new Coordinate(0,0,0));

        switch (bank)
        {
            case DRAYNOR:
                area = draynorBankArea;
                break;
            case LUMBRIDGE:
                break;
        }
        return area;
    }

    public static Coordinate getBankerCoords(List<Coordinate> list)
    {
        return list.get(Random.nextInt(list.size()));
    }

    public static void moveToBankAndBankAllExecpt(BanksEnum bank, String name, List<Coordinate> list)
    {
        final var player = Players.getLocal();
        final Coordinate bankCoord = getBankArea(bank).getRandomCoordinate();
        final var bankObject = GameObjects.newQuery().names(bank.getGameName()).results().nearest();

        if(bankObject != null)
        {
            if (bankObject.isVisible())
                bankAllExecptPartial(name, list);
            else
            {
                final RegionPath path = RegionPath.buildTo(bankCoord);

                if (Distance.between(player.getPosition(), bankCoord) > Random.nextInt(15, 20))
                    if (path != null)
                    {
                        System.out.print("Moving to bank!\n");
                        path.step();
                    }
            }
        }
    }

    public static void bankAll(List<Coordinate> list)
    {
        final var banker = Npcs.newQuery().names("Banker").on(getBankerCoords(list)).results().nearest();

        if(banker != null)
        {
            if(!Bank.isOpen())
            {
                banker.interact("Bank");
                Execution.delay(2000,3000);
            }
            else
            {
                Bank.depositInventory();
                Execution.delay(1000,3000);
                Bank.close();
            }
        }
    }

    public static void bankAllExecptPartial(String name, List<Coordinate> list)
    {
        final var banker = Npcs.newQuery().names("Banker").on(getBankerCoords(list)).results().nearest();

        if(banker != null)
        {
            if(!Bank.isOpen())
            {
                banker.interact("Bank");
                Execution.delay(2000,3000);
            }
            else
            {
                Pattern pattern = Regex.getPatternForContainsString(name);
                if (Inventory.containsAnyOf(pattern))
                    Bank.depositAllExcept(pattern);
                else
                    Bank.depositInventory();

                Execution.delay(1000,3000);
                Bank.close();
            }
        }
    }
}