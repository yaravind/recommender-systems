package useruser.dao;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.grouplens.lenskit.core.Parameter;

/**
 * Parameter annotation for the move tag file.
 * 
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.PARAMETER)
@Qualifier
@Parameter (File.class)
public @interface TitleFile{}
