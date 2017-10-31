package utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void joinsStringArray() {
        String[] strings = {"test", "array", "of", "strings"};

        Optional<String> joined = Strings.joinStrings(strings);

        assertThat(joined.orElse(null), is("test array of strings"));
    }

    @Test
    public void joinerHandlesNull() {
        Optional<String> joined = Strings.joinStrings(null);

        assertThat(joined.orElse(null), is(nullValue()));
    }

    @Test
    public void appendRightAppendsGivenChars() {
        String padded = Strings.padRight("test", 80, ' ');

        assertThat(padded.length(), is(80));
        assertThat(padded.charAt(80/2), is(' '));
        assertThat(padded, containsString("test"));
    }

    @Test
    public void recognizesLargerLengthThanDesiredOne() {
        String padded = Strings.padRight("testing", 6, ' ');

        assertThat(padded.length(), is("testing".length()));
        assertThat(padded, containsString("testing"));
    }

    @Test(expected = NullPointerException.class)
    public void padRightDoesNotAcceptNull() {
        Strings.padRight(null, 1, ' ');
    }

    @Test
    public void appendLeftAppendsGivenChars() {
        String padded = Strings.padLeft("test", 80, ' ');

        assertThat(padded.charAt(0), is(' '));
        assertThat(padded.length(), is(80));
        assertThat(padded, containsString("test"));
    }

    @Test
    public void padLeftRecognizesLargerLengthThanDesiredOne() {
        String padded = Strings.padLeft("testing", 6, ' ');

        assertThat(padded.length(), is("testing".length()));
        assertThat(padded, containsString("testing"));
    }

    @Test(expected = NullPointerException.class)
    public void padLeftDoesNotAcceptNull() {
        Strings.padRight(null, 1, ' ');
    }

    @Test
    public void repeatsCharacters() {
        String string = Strings.repeat('a', 9);

        assertThat(string.length(), is(9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNegativeNumbers() {
        Strings.repeat('a', -1);
    }

    @Test
    public void returnEmptyStringWhenTimesIsZero() {
        assertThat(Strings.repeat('a', 0), is(""));
    }

    @Test
    public void chopsString() {
        List<String> result = Strings.chopString("Testing", 4);

        assertThat(result.size(), is(2));
        assertThat(result.get(1), is("Testing".substring(4)));
    }

    @Test
    public void doesNotChopStringsThatAreShortEnough() {
        List<String> result = Strings.chopString("Test", 4);

        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("Test"));
    }

    @Test
    public void doesNotAcceptNullStringToBeChopped() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("String to be chopped should not be null");

        Strings.chopString(null, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAcceptNonPositiveSize() {
        Strings.chopString("test", 0);
    }
}