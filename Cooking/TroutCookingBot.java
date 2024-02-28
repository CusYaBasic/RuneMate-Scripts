package com.DefiledOne.scripts.Cooking;

import com.DefiledOne.scripts.States.TroutCookingState;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

import java.awt.event.KeyEvent;

public class TroutCookingBot extends LoopingBot
{
    TroutCookingState state;
    Boolean hasClicked = false;

    @Override
    public void onStart(String... args)
    {
        super.onStart(args);
        state = TroutCookingState.COOK;
    }
    @Override
    public void onLoop()
    {
        switch(state)
        {
            case BANK:
                bankAll();
                break;
            case COOK:
                cookTrout();
                break;
            case WAIT:
                Execution.delay(3000,5000);
                state = TroutCookingState.COOK;
                break;
        }
    }

    private void cookTrout()
    {

        if (Players.getLocal().getAnimationId() != -1)
            return;

        if (Inventory.containsAnyOf("Raw trout"))
        {
            var range = GameObjects.newQuery().names("Range").results().nearest();

            if(range != null && !Players.getLocal().isMoving())
            {
                if (!hasClicked)
                {
                    range.interact("Cook");
                    hasClicked = true;
                    Execution.delay(2000,3000);
                }
                else
                {
                    if(Distance.between(Players.getLocal(), range) <= 1)
                    {
                        Keyboard.pressKey(KeyEvent.VK_SPACE);
                        Execution.delay(1250,1500);
                        hasClicked = false;
                        state = TroutCookingState.WAIT;
                    }
                }
            }
        }
        else
        {
            state = TroutCookingState.BANK;
        }
    }

    private void bankAll()
    {
        var bank = GameObjects.newQuery().names("Bank booth").results().first();

        if (Bank.isOpen())
        {
            Bank.depositInventory();
            Execution.delay(550, 850);
            Bank.withdraw("Raw trout", 28);
            Execution.delay(350, 650);
            state = TroutCookingState.COOK;
        }
        else if (bank != null && !Players.getLocal().isMoving())
        {
            bank.interact("Bank");
            Execution.delay(3000, 5000);
        }
    }
}
