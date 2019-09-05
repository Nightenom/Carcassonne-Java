package cz.rict.carcassonne.classic.river.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(River.MOD_ID)
public class River
{
    public static final String MOD_ID = "river";

    public River()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
