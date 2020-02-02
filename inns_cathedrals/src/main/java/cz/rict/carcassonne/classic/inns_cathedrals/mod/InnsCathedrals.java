package cz.rict.carcassonne.classic.inns_cathedrals.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

@Mod(InnsCathedrals.MOD_ID)
public class InnsCathedrals
{
    public static final String MOD_ID = "innscathedrals";

    public InnsCathedrals()
    {
        EventBus.register(InnsCathedrals.class);
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println(MOD_ID + event.getRandomDataToPass());
    }
}
