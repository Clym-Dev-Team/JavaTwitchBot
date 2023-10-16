package main.system.eventSystem;

import java.lang.annotation.*;

/**
 * This Annotation is for Subscribing to an Object on the EventStream.
 * A Subscriber can be created by creating by Annotating a Method with this Annotation, and specifying
 * the Class it subscribes to via this Annotations EventClass Parameter. <br/>
 * <br/>
 * For a Subscriber to be valid it needs to be: <br/>
 * - located in a subfolder of main.modules
 * - static <br/>
 * - only have one Parameter <br/>
 * - the Parameter needs to be of the same Class as specified in the Annotation <br/>
 *
 * @see EventDispatcher
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Subscriber {
}