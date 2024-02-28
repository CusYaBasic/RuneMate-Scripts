package com.DefiledOne.scripts.Fishing;

import com.DefiledOne.scripts.States.BarbFishAndCookState;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Distance;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

import java.awt.event.KeyEvent;

public class BarbFishAndCook extends LoopingBot
{
    BarbFishAndCookState state;
    Boolean isCooking = false;
    Boolean troutDoOnce = false;
    Boolean salmonDoOnce = false;

    @Override
    public void onStart(String... args)
    {
        super.onStart(args);
        state = BarbFishAndCookState.FISH;
    }
    @Override
    public void onLoop()
    {
        switch (state)
        {
            case FISH:
                clickFishSpot();
                break;
            case COOK:
                if (Players.getLocal().getAnimationId() != -1)
                {
                    Execution.delay(3100, 5700);
                }
                else
                {
                    cookFish();
                }
                break;
            case WAIT:
                botWait();
                break;
            case DROP:
                dropFish();
                break;
        }
    }

    private void clickFishSpot()
    {

        salmonDoOnce = false;
        troutDoOnce = false;

        if (Inventory.isFull())
        {
            state = BarbFishAndCookState.COOK;
            return;
        }
        if(Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1)
        {
            state = BarbFishAndCookState.WAIT;
            return;
        }
        var fishSpot = Npcs.newQuery().names("Rod Fishing spot").results().nearest();

        if (fishSpot != null)
        {
           fishSpot.interact("Lure");
           Execution.delay(3100, 5200);
        }
    }

    private void cookFish()
    {
        var fire = GameObjects.newQuery().names("Fire").results().nearest();

        if(ChatDialog.getContinue() != null)
        {
            isCooking = false;
            ChatDialog.getContinue().select();
            Execution.delay(500, 1000);
            return;
        }

        if (!troutDoOnce)
            if (!Inventory.containsAnyOf("Raw trout"))
            {
                troutDoOnce = true;
                isCooking = false;
            }

        if (!salmonDoOnce)
            if (!Inventory.containsAnyOf("Raw salmon"))
            {
                salmonDoOnce = true;
                isCooking = false;
            }

        if (Inventory.containsAnyOf("Raw salmon"))
        {
            if (fire != null && !Players.getLocal().isMoving())
            {
                if (!isCooking)
                {
                    fire.interact("Cook");
                    Execution.delay(4550, 5800);
                    Keyboard.pressKey(KeyEvent.VK_SPACE);
                    isCooking = true;
                    state = BarbFishAndCookState.WAIT;

                    if (!Inventory.containsAnyOf("Raw salmon"))
                        isCooking = false;
                }
            }
            return;
        }
        else
        {
            isCooking = false;
        }

        if (Inventory.containsAnyOf("Raw trout"))
        {
            if (fire != null && !Players.getLocal().isMoving())
            {
                if (!isCooking)
                {
                    fire.interact("Cook");
                    Execution.delay(4650, 5850);
                    Keyboard.pressKey(KeyEvent.VK_SPACE);
                    isCooking = true;
                    state = BarbFishAndCookState.WAIT;

                    if (!Inventory.containsAnyOf("Raw troutroy"))
                        isCooking = false;
                }
            }
            return;
        }
        else
        {
            isCooking = false;
        }

        state = BarbFishAndCookState.DROP;
    }

    private void botWait()
    {
        if(Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1)
        {
            Execution.delay(8100, 12500);
        }
        else
        {
            state = BarbFishAndCookState.FISH;
        }
    }

    private void dropFish()
    {
        if (Inventory.containsAnyOf("Trout", "Salmon", "Burnt fish"))
        {
            Keyboard.pressKey(KeyEvent.VK_SHIFT);
            Inventory.getItems("Trout", "Salmon", "Burnt fish").first().interact("Drop");
            Execution.delay(260, 390);
        }
        else
        {
            state = BarbFishAndCookState.FISH;
        }
    }
}
