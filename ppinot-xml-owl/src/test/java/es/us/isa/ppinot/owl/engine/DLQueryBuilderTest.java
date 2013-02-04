package es.us.isa.ppinot.owl.engine;

import es.us.isa.ppinot.owl.notation.Vocabulary;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * User: resinas
 * Date: 03/02/13
 * Time: 21:19
 */
public class DLQueryBuilderTest {

    @Test
    public void shouldReturnTheSameQueryIfNoParams() {
        String query = "inv(?appliesTo) some value";
        DLQueryBuilder builder = new DLQueryBuilder(query);
        Assert.assertEquals(query, builder.toString());
    }

    @Test
    public void shouldReplaceTheQueryIfParams() {
        String query = "inv(?appliesTo) some value";
        DLQueryBuilder builder = new DLQueryBuilder(query);
        builder.setParameter("appliesTo", Vocabulary.APPLIESTO_URI);
        Assert.assertEquals("inv(" + Vocabulary.APPLIESTO_URI + ") some value", builder.toString());
    }

    @Test
    public void shouldReplaceSeveralParams() {
        String query = "inv(?appliesTo) some (inv(?inv) some ?state)";
        DLQueryBuilder builder = new DLQueryBuilder(query);
        builder.setParameter("appliesTo", Vocabulary.APPLIESTO_URI);
        builder.setParameter("inv", Vocabulary.FROM_URI);
        builder.setParameter("state", Vocabulary.STATE_URI);
        Assert.assertEquals("inv(" + Vocabulary.APPLIESTO_URI + ") some (inv(" + Vocabulary.FROM_URI + ") some " + Vocabulary.STATE_URI + ")", builder.toString());
    }

    @Test
    public void shouldReplaceSetsOfParams() {
        String query = "inv(?appliesTo) some ?values";
        DLQueryBuilder builder = new DLQueryBuilder(query);
        builder.setParameter("appliesTo", Vocabulary.APPLIESTO_URI);
        builder.setParameter("values", Arrays.asList("one", "two", "three"));
        Assert.assertEquals("inv(" + Vocabulary.APPLIESTO_URI + ") some {one, two, three}", builder.toString());
    }
}
