
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class EasyProblems {

    private static class Node {
        char ch;
        int count;

        Node(char ch, int count) {
            this.ch = ch;
            this.count = count;
        }
    }

    public static String rearrangeMaxHeap(String input) {
        if (input == null || input.isEmpty()) {
            return "None";
        }
        
        int n = input.length();

        // 1) Frequency map: O(n)
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : input.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Optional quick impossibility check:
        // If any count > (n+1)/2, can't rearrange without adjacent duplicates.
        int limit = (n + 1) / 2;
        for (int c : freq.values()) {
            if (c > limit)
                return "None";
        }
        
        // 2) Max-heap by remaining count: O(k)
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.count, a.count));
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            pq.offer(new Node(e.getKey(), e.getValue()));
        }

        // 3) Build result, holding back the previously used char: O(n log k)
        StringBuilder sb = new StringBuilder(n);
        Node prev = null; // last used char with remaining count (to be re-added next step)

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            // Use current char
            sb.append(cur.ch);
            cur.count--;

            // Reinsert previous char (if it still has remaining count)
            if (prev != null && prev.count > 0) {
                pq.offer(prev);
            }

            // Hold back current char if it still has remaining count
            prev = cur;
        }

        // If we placed all characters, we're good; otherwise impossible
        return sb.length() == n ? sb.toString() : "None";
    }

    public static String rearrange(String input) {
        if (input == null || input.isEmpty()) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        HashMap<Character, Integer> charPool = new HashMap<>();
        for (char c : input.toCharArray()) {
            charPool.put(c, charPool.getOrDefault(c, 0) + 1);
        }

        // Repeat until all counts are consumed
        while (!charPool.isEmpty()) {
            // Snapshot keys so we can safely update/remove from the map
            List<Character> keys = new ArrayList<>(charPool.keySet());

            for (char ch : keys) {
                // adjacency check
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ch) {
                    return "None";
                }

                sb.append(ch);

                int newCount = charPool.get(ch) - 1; // safe: ch exists in map here
                if (newCount == 0) {
                    charPool.remove(ch); // safe because we're iterating over 'keys', not the map view
                } else {
                    charPool.put(ch, newCount);
                }
            }
        }
        return sb.toString();
    }
 
    
}
