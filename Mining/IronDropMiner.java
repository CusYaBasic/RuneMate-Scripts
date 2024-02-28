package com.DefiledOne.scripts.Mining;

import com.DefiledOne.scripts.States.DropMinerState;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

import java.awt.event.KeyEvent;

public class IronDropMiner extends LoopingBot
{

    DropMinerState state;

    @Override
    public void onStart(String... args)
    {
        super.onStart(args);
        state = DropMinerState.MINE;
    }
    @Override
    public void onLoop()
    {
        switch (state)
        {
            case DROP:
                if(!Inventory.getItems("Iron ore").isEmpty())
                {
                    dropOre();
                    System.out.println("Dropping ore");
                    Execution.delay(250, 460);
                }
                else
                {
                    state = DropMinerState.MINE;
                }
                break;

            case MINE:
                if (Inventory.isFull())
                {
                    state = DropMinerState.DROP;
                }
                else if (Players.getLocal().getAnimationId() == -1)
                {
                    mineOre();
                    System.out.println("Mining ore");
                    Execution.delay(1250, 1850);
                }
                break;
        }
    }

    private void dropOre()
    {

        var ore = Inventory.getItems().first();
        if(ore != null)
        {
            Keyboard.pressKey(KeyEvent.VK_SHIFT);
            ore.interact("Drop");
        }
    }

    private void mineOre()
    {
        var ore = GameObjects.newQuery().names("Iron rocks").results().nearest();
        if(ore != null && ore.isVisible())
        {
            ore.interact("Mine");
        }
    }
}
