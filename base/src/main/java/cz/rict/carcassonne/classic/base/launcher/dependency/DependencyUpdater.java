package cz.rict.carcassonne.classic.base.launcher.dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import cz.rict.carcassonne.classic.base.util.Utils;

public class DependencyUpdater
{
    public static final String DEP_FILE_PATH = "/META-INF/dependencies.cfg";
    public static final Path LIB_DIRECTORY = Utils.getRunDirPath().resolve("libraries");

    /**
     * Private constructor to hide the implicit public one
     */
    private DependencyUpdater()
    {
        // TODO: check if dependency locally found is used (otherwise remove it)
        // TODO: mod library dependencies
    }

    // use supplied file
    public static List<String> checkDependencies(final Path jarFile, final String dependencyFilePath)
    {
        final String deps;

        try (ZipFile zipFile = new ZipFile(jarFile.toAbsolutePath().toString());
            InputStream is = zipFile.getInputStream(zipFile.getEntry(dependencyFilePath)))
        {
            deps = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        }
        catch (final IOException | UncheckedIOException e)
        {
            return new ArrayList<>();
        }

        return checkDependencies0(deps);
    }

    // use current classloader
    public static List<String> checkDependencies(final String dependencyFilePath)
    {
        final String deps;

        try (InputStream is = DependencyUpdater.class.getResourceAsStream(dependencyFilePath))
        {
            deps = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        }
        catch (final IOException | UncheckedIOException e)
        {
            return new ArrayList<>();
        }

        return checkDependencies0(deps);
    }

    private static List<String> checkDependencies0(final String dependencies)
    {
        final List<String> result = new ArrayList<>();

        for (String dep : dependencies.split("\\r?\\n"))
        {
            dep = dep.trim();
            if (!dep.isEmpty())
            {
                final String path = resolveDependency(dep);
                if (!path.isEmpty())
                {
                    result.add(path);
                }
            }
        }

        return result;
    }

    private static String resolveDependency(final String depString)
    {
        final String modified = depString.replace("$lwjglNatives", Utils.getOSType().getLwjglNatives());
        final Dependency dep = new Dependency(modified);

        return DependencyResolvers.findResolver(dep).resolve(dep, LIB_DIRECTORY);
    }

    protected static class Dependency
    {
        private final String repository;
        private final String group;
        private final String artifact;
        private final String version;
        private final String classifier;

        protected Dependency(final String dep)
        {
            final String[] subs = dep.split(":");
            int subIndex = 0;
            this.repository = subs.length > subIndex ? subs[subIndex] : "";
            this.group = subs.length > ++subIndex ? subs[subIndex] : "";
            this.artifact = subs.length > ++subIndex ? subs[subIndex] : "";
            this.version = subs.length > ++subIndex ? subs[subIndex] : "";
            this.classifier = subs.length > ++subIndex ? subs[subIndex] : "";
        }

        public String getRepository()
        {
            return repository;
        }

        public String getGroup()
        {
            return group;
        }

        public String getArtifact()
        {
            return artifact;
        }

        public String getVersion()
        {
            return version;
        }

        public String getClassifier()
        {
            return classifier;
        }
    }

    private enum DependencyResolvers
    {
        MAVEN_CENTRAL("mavenCentral", MavenResolver::resolveMavenCentral),
        UNKNOWN("", (dep, dir) -> "");

        private String repositoryName;
        private BiFunction<Dependency, Path, String> resolver;

        DependencyResolvers(final String repositoryName, final BiFunction<Dependency, Path, String> resolver)
        {
            this.repositoryName = repositoryName;
            this.resolver = resolver;
        }

        private String resolve(final Dependency dep, final Path directory)
        {
            return resolver.apply(dep, directory);
        }

        private static DependencyResolvers findResolver(final Dependency dep)
        {
            for (final DependencyResolvers d : DependencyResolvers.values())
            {
                if (dep.getRepository().equals(d.repositoryName))
                {
                    return d;
                }
            }
            System.out.println("Unknown repository: " + dep.getRepository());
            return UNKNOWN;
        }
    }
}
