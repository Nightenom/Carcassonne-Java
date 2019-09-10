package cz.rict.carcassonne.classic.base.launcher.dependency;

import java.nio.file.Path;
import cz.rict.carcassonne.classic.base.launcher.dependency.DependencyUpdater.Dependency;
import cz.rict.carcassonne.classic.base.util.IOUtils;

public class MavenResolver
{
    private static final String MAVEN_CENTRAL_URL = "https://repo.maven.apache.org/maven2/";
    private static final char URL_DELIMETER = '/';
    private static final char ARTIFACT_DELIMETER = '-';
    private static final String JAR_CLASSIFIER = ".jar";

    private MavenResolver()
    {
    }

    protected static String resolveMavenCentral(final Dependency dep, final Path directory)
    {
        if (dep.getGroup().isEmpty() || dep.getArtifact().isEmpty() || dep.getVersion().isEmpty())
        {
            return "";
        }

        final StringBuilder url = new StringBuilder(MAVEN_CENTRAL_URL);
        url.append(dep.getGroup().replace('.', URL_DELIMETER));
        url.append(URL_DELIMETER);
        url.append(dep.getArtifact());
        url.append(URL_DELIMETER);
        url.append(dep.getVersion());
        url.append(URL_DELIMETER);
        final StringBuilder fileName = new StringBuilder();
        fileName.append(dep.getArtifact());
        fileName.append(ARTIFACT_DELIMETER);
        fileName.append(dep.getVersion());
        if (!dep.getClassifier().isEmpty())
        {
            fileName.append(ARTIFACT_DELIMETER);
            fileName.append(dep.getClassifier());
        }
        fileName.append(JAR_CLASSIFIER);
        url.append(fileName);

        final Path filePath = directory.resolve(fileName.toString());
        final String filePathString = filePath.toAbsolutePath().toString();
        final String urlString = url.toString();
        System.out.println("Downloading " + urlString + " to " + filePathString);
        IOUtils.downloadURL(urlString, filePath);

        return filePathString;
    }
}
