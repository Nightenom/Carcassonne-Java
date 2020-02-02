package cz.rict.carcassonne.classic.traders_builders.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

@Mod(TradersBuilders.MOD_ID)
public class TradersBuilders
{
    public static final String MOD_ID = "tradersbuilders";

    public TradersBuilders()
    {
        EventBus.register(TradersBuilders.class);
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println(MOD_ID + event.getRandomDataToPass());
    }
}
