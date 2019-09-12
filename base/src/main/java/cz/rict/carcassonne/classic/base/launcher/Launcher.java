package cz.rict.carcassonne.classic.base.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import cz.rict.carcassonne.classic.base.launcher.dependency.DependencyUpdater;
import cz.rict.carcassonne.classic.base.util.Utils;

/**
 * Pre-main enviroment checking and updating
 */
public final class Launcher
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
        // TODO: log enviroment: os type, java exec and version
        if (!Utils.getOSType().isSupported())
        {
            // TODO: proper error message
            System.exit(1);
        }

        final List<String> modulePath = DependencyUpdater.checkDependencies(DependencyUpdater.DEP_FILE_PATH);
        modulePath.add(Paths.get(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().normalize().toString());

        new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "--module-path", String.join(";", modulePath), "--module", MODULE_MAIN_CLASS)
            .inheritIO()
            .start();
    }
}
