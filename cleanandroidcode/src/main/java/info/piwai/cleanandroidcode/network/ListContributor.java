package info.piwai.cleanandroidcode.network;

import java.util.ArrayList;

/**
 * Necessary for RoboSpice. All types included in results of request's type tree must be real Java
 * types. As Java doesn't accept expression like <tt>List<Contributor>.class</tt>, we must create a
 * full java type as a workaround.
 * @author SNI
 */
public class ListContributor extends ArrayList<Contributor> {

    private static final long serialVersionUID = -2518157580598657864L;

}
