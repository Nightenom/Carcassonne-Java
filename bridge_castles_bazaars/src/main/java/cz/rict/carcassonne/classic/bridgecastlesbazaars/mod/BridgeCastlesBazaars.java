package cz.rict.carcassonne.classic.bridgecastlesbazaars.mod;

import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.mod.Mod;

@Mod(BridgeCastlesBazaars.MOD_ID)
public class BridgeCastlesBazaars
{
    public static final String MOD_ID = "bridgecastlesbazaars";

    public BridgeCastlesBazaars()
    {
        TestEvent.registerListener(e -> System.out.println(MOD_ID + e.getRandomDataToPass()));
    }
}
