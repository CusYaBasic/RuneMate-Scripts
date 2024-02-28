package com.DefiledOne.scripts.Thieving;

import com.DefiledOne.scripts.States.ManPickpocketingState;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Path;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.web.WebPath;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

public class ManPickpocket extends LoopingBot
{

    Area bankArea = new Area.Rectangular(new Coordinate(3092,3244,0), new Coordinate(3093,3245,0));
    Area womenArea = new Area.Rectangular(new Coordinate(3096,3266,1), new Coordinate(3102,3102,1));
    ManPickpocketingState state = ManPickpocketingState.MOVETOWOMEN;
    @Override
    public void onStart(String... args)
    {
        super.onStart(args);
    }

    @Override
    public void onLoop()
    {
        switch(state)
        {
            case BANK:
                bankAll();
                break;

            case MOVETOBANK:
                if (!hasFood())
                {
                    walkToBank();
                }
                else
                {
                    state = ManPickpocketingState.MOVETOWOMEN;
                }
                break;

            case MOVETOWOMEN:
                walkToWomen();
                break;

            case PICKPOCKET:
                pickpocket();
                break;
        }
    }

    private Boolean hasFood()
    {
        return Inventory.containsAnyOf("Trout");
    }

    private Boolean isLowHealth()
    {
        return Players.getLocal().getHealthGauge().getPercent() <= 10;
    }

    private void walkToBank()
    {
        var webpath = WebPath.buildTo(bankArea.getRandomCoordinate());

        if (webpath != null)
        {
            webpath.step(Path.TraversalOption.MANAGE_DISTANCE_BETWEEN_STEPS, Path.TraversalOption.MANAGE_RUN);
            Execution.delay(1200, 2200);
        }

        if (bankArea.contains(Players.getLocal()))
        {
            state = ManPickpocketingState.BANK;
        }

    }

    private void walkToWomen()
    {
        var webpath = WebPath.buildTo(womenArea.getRandomCoordinate());

        if (webpath != null)
        {
            webpath.step(Path.TraversalOption.MANAGE_DISTANCE_BETWEEN_STEPS, Path.TraversalOption.MANAGE_RUN);
            Execution.delay(1200, 2200);
        }
        else
        {
            ClientUI.showAlert("Path is null");
        }

        if (womenArea.contains(Players.getLocal()))
        {
            state = ManPickpocketingState.PICKPOCKET;
        }

    }

    private void bankAll()
    {
        var bank = GameObjects.newQuery().names("Bank booth").actions("Bank").results().first();

        if (bank != null)
        {
            if (!Bank.isOpen())
            {
                bank.interact("Bank");
            }
            else
            {
                Bank.depositInventory();
                Bank.withdraw("Trout", 26);
                Execution.delay(1100, 1600);
                state = ManPickpocketingState.MOVETOWOMEN;
            }

        }
    }

    private void pickpocket()
    {
        if (isLowHealth())
        {
            Inventory.getItems("Trout").first().interact("Eat");
            Execution.delay(465, 632);
        }

        if (Players.getLocal().getSpotAnimationIds().contains(245))
            Execution.delay(2665, 3732);

        var women = Npcs.newQuery().names("Women").actions("Pickpocket").results().nearest();

        if (women != null)
        {
            women.interact("Pickpocket");
            Execution.delay(265, 332);
        }
    }
}
