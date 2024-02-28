package com.DefiledOne.scripts.Tasks.TitheFarm;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class WaterCanCheck  extends Task
{
    @Override
    public boolean validate()
    {
        if (Inventory.getItems("Watering can(8)").size() < 6)
        {
            return false;
        }

        return true;
    }

    @Override
    public void execute()
    {
        if (Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1)
        {
            Execution.delay(550, 830);
            return;
        }

        var can = Inventory.getItems("Watering can").first();
        var barrel = GameObjects.newQuery().names("Water Barrel").on(new Coordinate(0,0,0)).results().first();

        can.interact("Use");
        Execution.delay(850, 1500);
        barrel.click();
        Execution.delay(8500, 13300);
    }
}
