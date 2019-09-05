package cz.rict.carcassonne.classic.base.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Hack to run app, gradle not ready for module, setting module path for jar running is not user-friendly
 */
public class Launcher
{
    private static final String MODULE_MAIN_CLASS = "cz.rict.carcassonne.classic.base/cz.rict.carcassonne.classic.base.launcher.Main";

    /**
     * Util class
     */
    private Launcher()
    {
        /*
         * Intentionally left empty
         */
    }

    /**
     * Launches app correctly, otherwise module path is broken
     */
    public static void main(final String[] args) throws URISyntaxException, IOException
    {
        new ProcessBuilder(
            System.getProperty("java.home") + "/bin/java",
            "--module-path",
            Paths.get(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().normalize().toString(),
            "--module",
            MODULE_MAIN_CLASS).inheritIO().start();
    }
}
