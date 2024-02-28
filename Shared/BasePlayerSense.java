package com.DefiledOne.scripts.Shared;

import com.google.common.base.Supplier;
import com.runemate.game.api.hybrid.player_sense.PlayerSense;
import com.runemate.game.api.hybrid.util.calculations.Random;

public class BasePlayerSense
{
    public static void initializeKeys()
    {
        for (Key key : Key.values())
        {
            if (PlayerSense.get(key.name) == null)
            {
                PlayerSense.put(key.name, key.supplier.get());
            }
        }
    }


    public enum Key
    {
        ACTIVENESS_FACTOR_WHILE_WAITING("prime_activeness_factor", () -> Random.nextDouble(0.3, 0.9)),
        REACTION_TIME("prime_reaction_time", () -> Random.nextLong(260, 500)),
        RANDOM_DOUBLE_CLICK("prime_random_double_click", () -> Random.nextInt(1,200) == 10),
        RANDOM_AFK("prime_random_time", () -> Random.nextLong(10000, 65000));


        private final String name;
        private final Supplier supplier;

        Key(String name, Supplier supplier)
        {
            this.name = name;
            this.supplier = supplier;
        }

        public String getKey()
        {
            return name;
        }

        public Integer getAsInteger()
        {
            return PlayerSense.getAsInteger(name);
        }

        public Double getAsDouble()
        {
            return PlayerSense.getAsDouble(name);
        }

        public Long getAsLong()
        {
            return PlayerSense.getAsLong(name);
        }

        public Boolean getAsBoolean()
        {
            return PlayerSense.getAsBoolean(name);
        }
    }
}
