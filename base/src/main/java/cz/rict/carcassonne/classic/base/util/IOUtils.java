package cz.rict.carcassonne.classic.base.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IOUtils
{
    private IOUtils()
    {
    }

    public static void downloadURL(final String url, final Path filePath)
    {
        try
        {
            downloadURL(new URL(url), filePath);
        }
        catch (final MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    public static void downloadURL(final URL url, final Path filePath)
    {
        try
        {
            final ReadableByteChannel urlChannel = Channels.newChannel(url.openStream());
            final FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
            fileOutputStream.getChannel().transferFrom(urlChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            urlChannel.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
            return "";
        }
    }

    public static boolean checkSHAFromURL(final Path path, final String urlWithShaToCheck)
    {
        try
        {
            return checkSHAFromURL(path, new URL(urlWithShaToCheck));
        }
        catch (final MalformedURLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkSHAFromURL(final Path path, final URL urlWithShaToCheck)
    {
        final String shaFromUrl = readURL(urlWithShaToCheck);
        if (shaFromUrl.isEmpty())
        {
            System.out.println("Could not check blabla , filepath nebo url a nejaky pekny error, jsem lina osoba");
            return true;
        }
        return checkSHA(path, shaFromUrl);
    }

    public static boolean checkSHA(final Path path, final String shaToCheck)
    {
        return calculateSHA(path).equals(shaToCheck);
    }

    public static String calculateSHA(final Path path)
    {
        if (!Files.exists(path))
        {
            return "";
        }

        final byte[] sha;
        try (
            DigestInputStream dis = new DigestInputStream(new FileInputStream(path.toFile()), MessageDigest.getInstance("SHA-1")))
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
            System.out.println("Unable to calculate sha for: " + path.toAbsolutePath().toString());
            e.printStackTrace();
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
