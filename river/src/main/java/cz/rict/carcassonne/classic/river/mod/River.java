package cz.rict.carcassonne.classic.river.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

@Mod(River.MOD_ID)
public class River
{
    public static final String MOD_ID = "river";

    public River()
    {
        EventBus.register(River.class);
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println(MOD_ID + event.getRandomDataToPass());
    }
}
