package cz.rict.carcassonne.classic.base.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Utils
{
    public static final OS OS_TYPE;

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

    public static enum OS
    {
        LINUX("natives-linux"),
        SOLARIS(""),
        WINDOWS("natives-windows"),
        OSX("natives-macos"),
        UNKNOWN("");

        private String lwjglNatives;

        OS(String lwjglNatives)
        {
            this.lwjglNatives = lwjglNatives;
        }

        public String getLwjglNatives()
        {
            return lwjglNatives;
        }

        public boolean isSupported()
        {
            return this == WINDOWS || this == LINUX || this == OSX;
        }
    }

    public static OS getOSType()
    {
        return OS_TYPE;
    }

    public static Path getRunDirPath()
    {
        return Paths.get(System.getProperty("user.dir"));
    }
}
