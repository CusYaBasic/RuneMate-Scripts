package com.DefiledOne.scripts.Combat;

import com.DefiledOne.scripts.States.GrizzleyBearState;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingBot;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VarrockGrizzleyBear extends LoopingBot
{
    GrizzleyBearState state;
    int clickAmount = 2;

    List<String> bows = Arrays.asList(
            "Shortbow", "Longbow", "Oak shortbow", "Oak longbow", "Willow shortbow", "Willow longbow",
            "Maple shortbow", "Maple longbow", "Yew shortbow", "Yew longbow", "Magic shortbow", "Magic longbow",
            "Seercull", "Dark bow", "Crystal bow", "Zaryte bow", "Twisted bow", "Phoenix crossbow", "Bronze crossbow",
            "Blurite crossbow", "Iron crossbow", "Steel crossbow", "Mithril crossbow", "Adamant crossbow",
            "Runite crossbow", "Karil's crossbow", "Hunters' crossbow", "Dragon crossbow", "Armadyl crossbow",
            "Dragon hunter crossbow", "Twisted crossbow");
    List<String> crossbows = Arrays.asList(
            "Phoenix crossbow", "Bronze crossbow",
            "Blurite crossbow", "Iron crossbow", "Steel crossbow", "Mithril crossbow", "Adamant crossbow",
            "Runite crossbow", "Karil's crossbow", "Hunters' crossbow", "Dragon crossbow", "Armadyl crossbow",
            "Dragon hunter crossbow", "Twisted crossbow");
    List<String> staffsWithAutocast = Arrays.asList(
            "Staff of air", "Staff of water", "Staff of earth", "Staff of fire",
            "Mystic air staff", "Mystic water staff", "Mystic earth staff", "Mystic fire staff",
            "Slayer's staff (e)", "Slayer's staff (i)", "Trident of the seas", "Trident of the swamp",
            "Trident of the abyss", "Sanguinesti staff", "Iban's staff (u)", "Flames of Zamorak",
            "Saradomin staff", "Zamorak staff", "Master wand", "Kodai wand", "Harmonised orb", "Eldritch orb",
            "Volatile orb"
    );
    // Arrows
    List<String> arrows = Arrays.asList("Bronze arrow", "Iron arrow", "Steel arrow", "Mithril arrow", "Adamant arrow");
    List<String> bolts = Arrays.asList("Bronze bolts", "Iron bolts", "Steel bolts", "Mithril bolts", "Adamant bolts", "Runite bolts");
    // Attack Runes
    List<String> attackRunes = Arrays.asList("Mind rune", "Chaos rune", "Death rune", "Blood rune");
    String airRune = "Air rune";
    String airStaff = "Staff of air";
    String mysticAirStaff = "Mystic air staff";
    String attackStyle = "";

    @Override
    public void onStart(String... args)
    {
        super.onStart(args);
        getAttackStyle();
        state = GrizzleyBearState.ATTACK;
    }

    @Override
    public void onLoop()
    {
        switch (state)
        {
            case WAIT:
                if (!hasArrowsOrCasts())
                {
                    ClientUI.showAlert(ClientUI.AlertLevel.ERROR, "AMMO STATE: Player does not have runes or arrows, Please equip some");
                    RuneScape.logout();
                    stop();
                }

                state = GrizzleyBearState.ATTACK;
                break;

            case ATTACK:
                if(ChatDialog.getContinue() != null)
                {
                    ChatDialog.getContinue().select();
                    Execution.delay(500, 1000);
                    return;
                }

                if(!isBearInCombat() && !isPlayerInCombat())
                    attackBear();
                else
                {
                    Execution.delay(1000, 3000);
                    state = GrizzleyBearState.WAIT;
                }
                break;
        }
    }

    private void attackBear()
    {

        final var bear = Npcs.newQuery().names("Grizzly bear").results().nearest();

        if (bear != null && bear.getAnimationId() != 4929)
        {
            int doubleChance = Random.nextInt(1, 200);
            if(doubleChance >= 190)
            {

                for (int i = 0; i < clickAmount; i++)
                {
                    bear.interact("Attack");
                    Execution.delay(210,350);
                }
            }
            else
            {
                bear.interact("Attack");
            }

            System.out.print("Attacking bear!" + doubleChance + "\n");
            Execution.delay(2000,3000);
        }
        else
        {
            System.out.print("Waiting for bear to respawn!\n");
            Execution.delay(1000,4000);
        }
        randomAFK();
    }


    private boolean isBearInCombat()
    {
        final var bear = Npcs.newQuery().names("Grizzly bear").results().nearest();

        return bear != null && bear.getTarget() != null;
    }

    private void getAttackStyle()
    {
        for (String item : bows)
        {
            if (Equipment.containsAnyOf(item))
                attackStyle = "Range";
            return;
        }

        for (String item : staffsWithAutocast)
        {
            if (Equipment.containsAnyOf(item))
                attackStyle = "Magic";
            return;
        }

        ClientUI.showAlert(ClientUI.AlertLevel.ERROR, "Check for weapon: No viable weapon found, Please equip a staff or bow");
        stop();
    }

    private boolean hasArrowsOrCasts()
    {
        for (String item : arrows)
        {
            if (Equipment.newQuery().names(item).results().first() != null)
                return true;
        }

        for (String item : attackRunes)
        {
            if (Inventory.containsAnyOf(item) && Inventory.containsAnyOf(airRune))
                return true;
        }

        return false;
    }

    private boolean isPlayerInCombat()
    {
        final var player = Players.getLocal().getTarget();

        return player != null;
    }

    private void randomAFK()
    {
        int AFKChance = Random.nextInt(1, 800);
        var delayTime = Random.nextLong(35000, 180000);
        if (AFKChance >= 795)
        {
            System.out.print("Random AFK!" + delayTime);
            Execution.delay(delayTime);
        }
        System.out.print("Random AFK chance:" + AFKChance + "\n");
    }
}
