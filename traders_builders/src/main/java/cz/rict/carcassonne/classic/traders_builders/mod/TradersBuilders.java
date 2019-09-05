package cz.rict.carcassonne.classic.traders_builders.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(TradersBuilders.MOD_ID)
public class TradersBuilders
{
    public static final String MOD_ID = "tradersbuilders";

    public TradersBuilders()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
