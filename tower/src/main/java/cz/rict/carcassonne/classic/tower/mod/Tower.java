package cz.rict.carcassonne.classic.tower.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(Tower.MOD_ID)
public class Tower
{
    public static final String MOD_ID = "tower";

    public Tower()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
