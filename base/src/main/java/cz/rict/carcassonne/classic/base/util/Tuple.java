package cz.rict.carcassonne.classic.base.util;

/**
 * Class for making bound value pairs
 *
 * @param <A> first value type
 * @param <B> second value type
 */
public class Tuple<A, B>
{
    private A a;
    private B b;

    public Tuple(final A a, final B b)
    {
        this.a = a;
        this.b = b;
    }

    public void setFirst(final A aIn)
    {
        this.a = aIn;
    }

    public void setSecond(final B bIn)
    {
        this.b = bIn;
    }

    public A getFirst()
    {
        return a;
    }

    public B getSecond()
    {
        return b;
    }
}
