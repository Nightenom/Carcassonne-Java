package cz.rict.carcassonne.classic.princess_dragon.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(PrincessDragon.MOD_ID)
public class PrincessDragon
{
    public static final String MOD_ID = "princessdragon";

    public PrincessDragon()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
