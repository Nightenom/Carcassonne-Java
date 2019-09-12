package cz.rict.carcassonne.classic.base.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Various util methods
 */
public final class Utils
{
    public static final OS OS_TYPE;

    /**
     * Private constructor to hide the implicit public one
     */
    private Utils()
    {
    }

    static
    {
        final String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (s.contains("mac") || s.contains("darwin"))
        {
            OS_TYPE = OS.OSX;
        }
        else if (s.contains("win"))
        {
            OS_TYPE = OS.WINDOWS;
        }
        else if (s.contains("solaris") || s.contains("sunos"))
        {
            OS_TYPE = OS.SOLARIS;
        }
        else if (s.contains("linux") || s.contains("unix"))
        {
            OS_TYPE = OS.LINUX;
        }
        else
        {
            OS_TYPE = OS.UNKNOWN;
        }
    }

    /**
     * Enum for holding OS specific variables or capabilities
     */
    public enum OS
    {
        LINUX("natives-linux"),
        SOLARIS(""),
        WINDOWS("natives-windows"),
        OSX("natives-macos"),
        UNKNOWN("");

        private String lwjglNatives;

        OS(final String lwjglNatives)
        {
            this.lwjglNatives = lwjglNatives;
        }

        /**
         * @return maven classifier for lwjgl natives
         */
        public String getLwjglNatives()
        {
            return lwjglNatives;
        }

        /**
         * @return true if OS is supported
         */
        public boolean isSupported()
        {
            return this == WINDOWS || this == LINUX || this == OSX;
        }
    }

    /**
     * @return which OS we are running on
     */
    public static OS getOSType()
    {
        return OS_TYPE;
    }

    /**
     * @return enviroment value "user.dir" used as pointer data directory
     */
    public static Path getRunDirPath()
    {
        return Paths.get(System.getProperty("user.dir"));
    }
}
