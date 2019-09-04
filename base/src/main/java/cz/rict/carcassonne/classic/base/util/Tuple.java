package cz.rict.carcassonne.classic.base.util;

public class Tuple<A, B>
{
    private A a;
    private B b;

    public Tuple(final A a, final B b)
    {
        this.a = a;
        this.b = b;
    }

    public void setFirst(final A a)
    {
        this.a = a;
    }

    public void setSecond(final B b)
    {
        this.b = b;
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