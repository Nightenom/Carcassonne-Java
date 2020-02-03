package cz.rict.carcassonne.classic.base.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import cz.rict.carcassonne.classic.base.client.Carcassonne;

/**
 * Various IO util methods, please use NIO as much as possible
 */
public final class IOUtils
{
    /**
     * Private constructor to hide the implicit public one
     */
    private IOUtils()
    {
    }

    /**
     * Downloads and saves to file given url
     *
     * @param  url      valid url
     * @param  filePath valid filepath, i.e. not a directory
     * @return          true if succeeded, false otherwise
     */
    public static boolean downloadURL(final String url, final Path filePath)
    {
        try
        {
            return downloadURL(new URL(url), filePath);
        }
        catch (final MalformedURLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Downloads and saves to file given url
     *
     * @param  url      valid url
     * @param  filePath valid filepath, i.e. not a directory
     * @return          true if succeeded, false otherwise
     */
    public static boolean downloadURL(final URL url, final Path filePath)
    {
        try
        {
            final ReadableByteChannel urlChannel = Channels.newChannel(url.openStream());
            final FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
            fileOutputStream.getChannel().transferFrom(urlChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            urlChannel.close();
            return true;
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Downloads given url and returs its content as string
     *
     * @param  url valid url
     * @return     content if succeeded, empty string otherwise
     */
    public static String readURL(final URL url)
    {
        try
        {
            final ReadableByteChannel urlChannel = Channels.newChannel(url.openStream());
            final BufferedReader urlReader = new BufferedReader(Channels.newReader(urlChannel, "UTF-8"));
            final StringWriter stringWriter = new StringWriter();
            urlReader.transferTo(stringWriter);
            urlChannel.close();
            return stringWriter.toString();
        }
        catch (final IOException e)
        {
            Carcassonne.getLogger().error("", e);
            return "";
        }
    }

    /**
     * Checks if file sha matches sha downloaded from given url
     *
     * @param  path              valid filepath, i.e. not a directory
     * @param  urlWithShaToCheck valid url which only contains sha as readable string
     * @return                   true if sha matches, false otherwise
     */
    public static boolean checkSHAfromURL(final Path path, final String urlWithShaToCheck)
    {
        try
        {
            return checkSHAfromURL(path, new URL(urlWithShaToCheck));
        }
        catch (final MalformedURLException e)
        {
            Carcassonne.getLogger().error("", e);
            return false;
        }
    }

    /**
     * Checks if file sha matches sha downloaded from given url
     *
     * @param  path              valid filepath, i.e. not a directory
     * @param  urlWithShaToCheck valid url which only contains sha as readable string
     * @return                   true if sha matches or attemp to read URL failed, false otherwise
     */
    public static boolean checkSHAfromURL(final Path path, final URL urlWithShaToCheck)
    {
        final String shaFromUrl = readURL(urlWithShaToCheck);
        if (shaFromUrl.isEmpty())
        {
            Carcassonne.getLogger().error("Error during read SHA from {}", urlWithShaToCheck);
            return true;
        }
        return checkSHA(path, shaFromUrl);
    }

    /**
     * Checks if file sha matches given sha
     *
     * @param  path       valid filepath, i.e. not a directory
     * @param  shaToCheck sha as readable string
     * @return            true if sha matches, false otherwise
     */
    public static boolean checkSHA(final Path path, final String shaToCheck)
    {
        return calculateSHA(path).equals(shaToCheck);
    }

    /**
     * Calculates file sha, does check for file existence
     *
     * @param  path valid filepath, i.e. not a directory
     * @return      sha as readable string if succeeded, empty string otherwise
     */
    public static String calculateSHA(final Path path)
    {
        if (!Files.exists(path))
        {
            return "";
        }

        final byte[] sha;
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(path.toFile()), MessageDigest.getInstance("SHA-1")))
        {
            final byte[] buffer = new byte[1024];
            while (dis.read(buffer, 0, buffer.length) != -1)
            {
                // Intentionally left empty
            }
            sha = dis.getMessageDigest().digest();
        }
        catch (final IOException | NoSuchAlgorithmException e)
        {
            Carcassonne.getLogger().error("Unable to calculate sha for: " + path.toAbsolutePath().toString(), e);
            return "";
        }

        final StringBuilder result = new StringBuilder();
        for (final byte b : sha)
        {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
