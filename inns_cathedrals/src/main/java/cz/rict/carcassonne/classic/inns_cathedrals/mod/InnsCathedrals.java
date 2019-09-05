package cz.rict.carcassonne.classic.inns_cathedrals.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(InnsCathedrals.MOD_ID)
public class InnsCathedrals
{
    public static final String MOD_ID = "innscathedrals";

    public InnsCathedrals()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
