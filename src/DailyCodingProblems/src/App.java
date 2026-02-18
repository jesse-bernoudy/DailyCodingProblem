import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        //System.out.println("Hello, World!");
        //System.out.println(MediumProblems.minDrops(4, 10));
        // System.out.println(EasyProblems.rearrange("aab"));     // "aba"
        // System.out.println(EasyProblems.rearrange("aaab"));    // "None"
        // System.out.println(EasyProblems.rearrange("aaabc")); // e.g. "abaca"

        // System.out.println(EasyProblems.rearrangeMaxHeap("aab"));     // "aba"
        // System.out.println(EasyProblems.rearrangeMaxHeap("aaab"));    // "None"
        // System.out.println(EasyProblems.rearrangeMaxHeap("aaabc"));   // e.g. "abaca"

        //System.out.println(MediumProblems.arrangeInts(new int[] { 121, 12}));

        testBasic2x2();
        testDiagonalAndNoReuse();
        testClassic4x4();
        testDuplicatesInDictionary();
        testEmptyAndNoMatches();
        System.out.println("All tests passed.");
    }

    private static void assertEquals(int expected, int actual, String name) {
        if (expected != actual) {
            throw new AssertionError(name + " expected=" + expected + " actual=" + actual);
        }

    }

    private static void testBasic2x2() {
        char[][] board = {
                { 'a', 'b' },
                { 'c', 'd' }
        };
        List<String> dict = List.of(
                "ab", "abc", "abd", "ac", "ad", "bd", "abcd",
                "aa", // impossible (reuse same cell)
                "dcba" // possible if path exists: d->c->b->a is valid diagonally? (d->c adjacent yes, c->b adjacent yes, b->a adjacent yes) => YES on 2x2 actually works
        );

        int count = MediumProblems.boggle(dict, board);

        // Let's count valid ones:
        // "ab" yes (a->b)
        // "abc" yes (a->b->c) b adjacent to c (diagonal) yes
        // "abd" yes (a->b->d)
        // "ac" yes
        // "ad" yes (diagonal)
        // "bd" yes
        // "abcd" yes (a->b->d->c OR a->b->c->d etc.)
        // "aa" no
        // "dcba" yes (d->c->b->a)
        assertEquals(8, count, "testBasic2x2");
    }

    private static void testDiagonalAndNoReuse() {
        char[][] board = {
                { 'a', 'x' },
                { 'x', 'b' }
        };
        List<String> dict = List.of("ab", "ax", "xb", "aba");
        int count = MediumProblems.boggle(dict, board);

        // "ab" yes (diagonal)
        // "ax" yes (a->x)
        // "xb" yes (x->b)
        // "aba" no (would need to return to a)
        assertEquals(3, count, "testDiagonalAndNoReuse");
    }

    private static void testClassic4x4() {
        char[][] board = {
                { 'o', 'a', 'a', 'n' },
                { 'e', 't', 'a', 'e' },
                { 'i', 'h', 'k', 'r' },
                { 'i', 'f', 'l', 'v' }
        };

        // These are known to be formable on this board:
        // "oath": o(0,0)->a(0,1)->t(1,1)->h(2,1)
        // "eat":  e(1,3)->a(1,2)->t(1,1)
        // "oat":  o->a->t
        // "hike": h(2,1)->i(2,0)->k(2,2)->e(1,3)  (check adjacency: i(2,0) to k(2,2) is NOT adjacent, so "hike" is NOT valid here)
        // "rain" might not be valid either (depends on adjacency).
        List<String> dict = List.of(
                "oath", "eat", "oat",
                "pea", "rain", "hike", "oatn");

        int count = MediumProblems.boggle(dict, board);

        // Valid: oath, eat, oat
        assertEquals(3, count, "testClassic4x4");
    }

    private static void testDuplicatesInDictionary() {
        char[][] board = {
                { 'a', 'b' },
                { 'c', 'd' }
        };
        List<String> dict = List.of("ab", "ab", "ab", "abc");
        int count = MediumProblems.boggle(dict, board);
        // "ab" and "abc" should be counted once each
        assertEquals(2, count, "testDuplicatesInDictionary");
    }

    private static void testEmptyAndNoMatches() {
        char[][] board = {
                { 'a', 'b' },
                { 'c', 'd' }
        };

        assertEquals(0, MediumProblems.boggle(List.of(), board), "testEmptyDict");

        List<String> dict = List.of("zzz", "eeee", "mnop");
        assertEquals(0, MediumProblems.boggle(dict, board), "testNoMatches");
    }
}
