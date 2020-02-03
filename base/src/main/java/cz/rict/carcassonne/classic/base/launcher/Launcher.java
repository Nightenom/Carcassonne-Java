package cz.rict.carcassonne.classic.base.launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cz.rict.carcassonne.classic.base.launcher.dependency.DependencyUpdater;
import cz.rict.carcassonne.classic.base.util.Utils;

/**
 * Pre-main enviroment checking and updating
 */
public final class Launcher
{
    private static final String MODULE_MAIN_CLASS = "cz.rict.carcassonne.classic.base/cz.rict.carcassonne.classic.base.launcher.Main";
    public static final Logger LOGGER = LogManager.getLogger("Launcher");

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
        LOGGER.info("Console arguments: {}", Arrays.toString(args));
        LOGGER.info("OS: {} {} {}", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        LOGGER.info("JVM: {} {} {}", System.getProperty("java.vendor"), System.getProperty("java.version"), System.getProperty("java.home"));

        if (!Utils.getOSType().isSupported())
        {
            LOGGER.error("Unsupported OS, your OS have to be capable of running LWJGL. Submit an issue if you think this is wrong.");
            System.exit(1);
        }

        final List<String> modulePath = DependencyUpdater.checkDependencies(DependencyUpdater.DEP_FILE_PATH);
        Files.list(Path.of("mods")).forEach(jarFilePath -> {
            if (jarFilePath.toString().contains(".jar"))
            {
                modulePath.addAll(DependencyUpdater.checkDependencies(jarFilePath, DependencyUpdater.DEP_FILE_PATH));
            }
        });
        modulePath.add(Path.of(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath().normalize().toString());

        new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "--module-path", String.join(";", modulePath), "--module", MODULE_MAIN_CLASS)
            .inheritIO()
            .start();
    }
}
