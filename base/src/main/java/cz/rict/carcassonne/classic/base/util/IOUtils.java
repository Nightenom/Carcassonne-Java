package cz.rict.carcassonne.classic.base.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

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
            final ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            final FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }
}
