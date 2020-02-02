package cz.rict.carcassonne.classic.bridgecastlesbazaars.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

@Mod(BridgeCastlesBazaars.MOD_ID)
public class BridgeCastlesBazaars
{
    public static final String MOD_ID = "bridgecastlesbazaars";

    public BridgeCastlesBazaars()
    {
        EventBus.register(BridgeCastlesBazaars.class);
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println(MOD_ID + event.getRandomDataToPass());
    }
}
