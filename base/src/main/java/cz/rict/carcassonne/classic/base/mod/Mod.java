package cz.rict.carcassonne.classic.base.mod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mod annotation to determine main mod class
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod
{
    /**
     * Mod ID, lowercase.
     *
     * @return mod id
     */
    String value();
}
