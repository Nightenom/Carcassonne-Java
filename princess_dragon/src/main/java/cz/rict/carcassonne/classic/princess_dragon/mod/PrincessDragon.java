package cz.rict.carcassonne.classic.princess_dragon.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;
import cz.rict.carcassonne.classic.base.mod.event.EventBus;
import cz.rict.carcassonne.classic.base.mod.event.SubscribeEvent;

@Mod(PrincessDragon.MOD_ID)
public class PrincessDragon
{
    public static final String MOD_ID = "princessdragon";

    public PrincessDragon()
    {
        EventBus.register(PrincessDragon.class);
    }

    @SubscribeEvent
    public static void testEventListener(final TestEvent event)
    {
        System.out.println(MOD_ID + event.getRandomDataToPass());
    }
}
