
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MediumProblems {

    public static int minDrops(int eggs, int floors) {
        if (floors <= 0) {
            return 0;
        }
        if (eggs <= 0) {
            return Integer.MAX_VALUE / 4; // "infinity"
        }

        // dp[e] = dp[m][e] for current m (moves), updated in-place each loop
        long[] dp = new long[eggs + 1];
        int moves = 0;

        // Keep increasing moves until we can cover at least 'floors'
        while (dp[eggs] < floors) {
            moves++;

            // Update dp from high eggs -> low eggs to use previous move values correctly
            for (int e = eggs; e >= 1; e--) {
                dp[e] = dp[e] + dp[e - 1] + 1;
            }
        }

        return moves;
    }

    public static String arrangeInts(int[] values) {
        List<String> stringArray = Arrays.stream(values)
                .mapToObj(String::valueOf).collect(Collectors.toList());
        stringArray.sort((a, b) -> (b + a).compareTo(a + b));

        // handle [0,0,0]
        if (stringArray.get(0).equals("0")) {
            return "0";
        }

        return String.join("", stringArray);
    }

    public static String arrangeInts2(int[] values) {
        String[] arr = Arrays.stream(values)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        Arrays.sort(arr, (a, b) -> (b + a).compareTo(a + b));

        if (arr[0].equals("0"))
            return "0";

        return String.join("", arr);
    }
    
    private static class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;
        String word;

        public TrieNode() {
            children = new TrieNode[26];
            isEndOfWord = false;
        }
    }

    private static class Trie {
        private final TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public TrieNode getRoot() {
            return root;
        }

        // Insert a word into the Trie
        public void insert(String word) {
            word = word.toLowerCase();
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (current.children[ch - 'a'] == null) {
                    current.children[ch - 'a'] = new TrieNode();
                }
                current = current.children[ch - 'a'];
            }
            current.isEndOfWord = true;
            current.word = word;
        }

        // // Search for a word in the Trie
        // public boolean search(String word) {
        //     TrieNode current = root;

        //     for (int i = 0; i < word.length(); i++) {
        //         char ch = word.charAt(i);

        //         if (current.children[ch - 'a'] == null) {
        //             // Word not found
        //             return false;
        //         }

        //         current = current.children[ch - 'a'];
        //     }
        //     return current != null && current.isEndOfWord;
        // }

        // // Check if a given prefix exists in the Trie
        // public boolean startsWith(String prefix) {
        //     TrieNode current = root;
        //     for (int i = 0; i < prefix.length(); i++) {
        //         char ch = prefix.charAt(i);

        //         if (current.children[ch - 'a'] == null) {
        //             // Prefix not found
        //             return false;
        //         }

        //         current = current.children[ch - 'a'];
        //     }
        //     return true;
        // }
    }

    private static void boggleDfs(char[][] board,
                             long visitedMask,
                             TrieNode node,
                             int r, int c,
                             int[] path, int depth,
                             Map<String, List<int[]>> wordToPaths) {
        // Boundary check
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
            return;
        }

        // Check Visited
        int cols = board[0].length;
        int id = r * cols + c;
        long bit = 1L << id;
        if ((visitedMask & bit) != 0L) {
            return;
        }

        char ch = board[r][c];
        if (ch < 'a' || ch > 'z') {
            return; // optional safety
        }

        TrieNode next = node.children[ch - 'a'];
        if (next == null) {
            return;
        }
        
        // Visit the letter
        visitedMask |= bit;
        path[depth++] = id;

        if (next.isEndOfWord) {
            wordToPaths
                    .computeIfAbsent(next.word, k -> new ArrayList<>())
                    .add(java.util.Arrays.copyOf(path, depth));
        }

        // 8 directions
        int[] dr = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] dc = { -1, 0, 1, -1, 1, -1, 0, 1 };

        for (int i = 0; i < dr.length; i++) {
            boggleDfs(board, visitedMask, next, r + dr[i], c + dc[i], path, depth, wordToPaths);
        }
    }

    public static int boggle(List<String> dictionary, char[][] board) {
        if (board == null || board.length == 0) {
            return 0;
        }

        Trie dictionayTrie = new Trie();
        for (String word : dictionary) {
            dictionayTrie.insert(word);
        }

        int rows = board.length;
        int cols = board[0].length;

        long visitedMask = 0L;

        Map<String, List<int[]>> wordToPaths = new HashMap<>();

        int maxLen = board.length * board[0].length;
        int[] path = new int[maxLen];
        TrieNode root = dictionayTrie.getRoot();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boggleDfs(board, visitedMask, root, r, c, path, 0, wordToPaths);
            }
        }
        return wordToPaths.size();
    }

    private static int NumUnlockPatternsDFS(boolean[] visited, int[][]skip, int curKey, int remainingLen) {
        if (remainingLen == 0) {
            return 1;
        }
        visited[curKey] = true;

        int paths = 0;
        for (int next = 1; next <= 9; next++) {
            int mid = skip[curKey][next];
            if (!visited[next] && (mid == 0 || visited[mid])) {
                paths += NumUnlockPatternsDFS(visited, skip, next, remainingLen - 1);
            }
        }
        visited[curKey] = false;
        return paths;
    }
    
    public static int NumUnlockPatterns(int n) {
        if (n == 0) {
            return 0;
        }

        boolean[] visited = new boolean[10];
        int[][] skip = new int[10][10];
        skip[1][3] = skip[3][1] = 2;
        skip[4][6] = skip[6][4] = 5;
        skip[7][9] = skip[9][7] = 8;

        skip[1][7] = skip[7][1] = 4;
        skip[2][8] = skip[8][2] = 5;
        skip[3][9] = skip[9][3] = 6;

        skip[1][9] = skip[9][1] = 5;
        skip[3][7] = skip[7][3] = 5;

        int total = 0;
        total += 4 * NumUnlockPatternsDFS(visited, skip, 1, n - 1);
        total += 4 * NumUnlockPatternsDFS(visited, skip, 2, n - 1);
        total += NumUnlockPatternsDFS(visited, skip, 5, n - 1);
        return total;

    }
}

