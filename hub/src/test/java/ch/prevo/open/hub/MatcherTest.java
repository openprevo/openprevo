package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.Assert.*;

public class MatcherTest {

    @Test
    public void findMatches() throws Exception {
        Set<InsurantInformation> exits = new HashSet<>();
        exits.add(new InsurantInformation("ahv1", "uid1"));
        Set<InsurantInformation> entries = new HashSet<>();
        entries.add(new InsurantInformation("ahv1", "uid2"));
        entries.add(new InsurantInformation("ahv2", "uid3"));

        List<Match> matches = new Matcher().findMatches(exits, entries);

        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertEquals("ahv1", match.getEntry().getEncryptedOasiNumber());
        assertEquals("ahv1", match.getExit().getEncryptedOasiNumber());
        assertNotSame(match.getEntry(), match.getExit());
    }

    @Test
    public void findMatchesEmptyInput() throws Exception {
        assertTrue(new Matcher().findMatches(emptySet(), emptySet()).isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void findMatchesWithDuplicates() throws Exception {
        Set<InsurantInformation> exits = new HashSet<>();
        exits.add(new InsurantInformation("ahv1", "uid1"));
        Set<InsurantInformation> entries = new HashSet<>();
        entries.add(new InsurantInformation("ahv1", "uid2"));
        entries.add(new InsurantInformation("ahv1", "uid3"));

        new Matcher().findMatches(exits, entries);
    }
}