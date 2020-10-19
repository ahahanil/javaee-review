package tk.deriwotua;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 双指针主要用于遍历数组，两个指针指向不同的元素，从而协同完成任务。
 */
public class A01_DoublePointer {

    /**
     * Two Sum II - Input array is sorted
     *
     * 在有序数组中找出两个数，使它们的和为 target
     *  数组中的元素最多遍历一次，时间复杂度为 O(N)。只使用了两个额外变量，空间复杂度为 O(1)
     *
     * @param numbers   Input: numbers={2, 7, 11, 15}, target=9
     * @param target  Output: index1=1, index2=2
     * @return
     */
    public static int[] twoSum(int[] numbers, int target) {
        if (numbers == null) return null;
        // 双指针，一个指针指向值较小的元素，一个指针指向值较大的元素。
        // 指向较小元素的指针从头向尾遍历，指向较大元素的指针从尾向头遍历。
        int i = 0, j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];
            if (sum == target) {
                return new int[]{i + 1, j + 1};
            }
            // sum < target，移动较小的元素，使 sum 变大一些
            else if (sum < target) {
                i++;
            }
            // sum > target，移动较大的元素，使 sum 变小一些；
            else {
                j--;
            }
        }
        return null;
    }

    /**
     *  Sum of Square Numbers
     *      Input: 5
     *      Output: True
     *      Explanation: 1 * 1 + 2 * 2 = 5
     *
     * 判断一个非负整数是否为两个整数的平方和
     *      可以看成是在元素为 0~target 的有序数组中查找两个数，使得这两个数的平方和为 target，
     *      如果能找到，则返回 true，表示 target 是两个整数的平方和。
     *      最多只需要遍历一次 0~sqrt(target)，所以时间复杂度为 O(sqrt(target))。只使用了两个额外的变量，因此空间复杂度为 O(1)
     * @param target
     * @return
     */
    public static boolean judgeSquareSum(int target) {
        if (target < 0) return false;
        /**
         * 本题的关键是右指针的初始化，实现剪枝，从而降低时间复杂度。
         * 设右指针为 x，左指针固定为 0，为了使 0^2 + x^2 的值尽可能接近 target，我们可以将 x 取为 sqrt(target)。
         */
        int i = 0, j = (int) Math.sqrt(target);
        while (i <= j) {
            int powSum = i * i + j * j;
            if (powSum == target) {
                return true;
            }
            // 移动较大的元素，使 powSum 变小一些
            else if (powSum > target) {
                j--;
            }
            // 移动较小的元素，使 powSum 变大一些
            else {
                i++;
            }
        }
        return false;
    }

    /**
     * 元音列表
     */
    private final static HashSet<Character> vowels = new HashSet<Character>(
            Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'));

    /**
     * Reverse Vowels of a String
     *  Given s = "leetcode", return "leotcede"
     *
     * 反转字符串中的元音字符
     *  一个指针从头向尾遍历，一个指针从尾到头遍历，当两个指针都遍历到元音字符时，交换这两个元音字符
     *
     * 只需要遍历所有元素一次时间复杂度为 O(N)；只需要使用两个额外变量空间复杂度 O(1)
     *
     * @param s
     * @return
     */
    public String reverseVowels(String s) {
        if (s == null) return null;
        // 双指针，一个指针从头向尾遍历，一个指针从尾到头遍历
        int i = 0, j = s.length() - 1;
        char[] result = new char[s.length()];
        while (i <= j) {
            char ci = s.charAt(i);
            char cj = s.charAt(j);
            if (!vowels.contains(ci)) {
                // 头指针处非元音插入头指针位置
                result[i++] = ci;
            } else if (!vowels.contains(cj)) {
                // 尾指针处非元音插入尾指针位置
                result[j--] = cj;
            } else {
                // 两指针处都是元音交换位置插入
                result[i++] = cj;
                result[j--] = ci;
            }
        }
        return new String(result);
    }

    /**
     * Valid Palindrome II
     *  Input: "abca"
     *  Output: True
     *  Explanation: You could delete the character 'c'.
     *
     * 可以删除一个字符，判断是否能构成回文字符串
     *  双指针可以很容易判断一个字符串是否是回文字符串：令一个指针从左到右遍历，一个指针从右到左遍历，
     *  这两个指针同时移动一个位置，每次都判断两个指针指向的字符是否相同，如果都相同，字符串才是具有左右对称性质的回文字符串。
     *
     *
     * @param s
     * @return
     */
    public boolean validPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            /**
             * 本题的关键是处理删除一个字符。
             * 在使用双指针遍历字符串时，如果出现两个指针指向的字符不相等的情况，就试着删除一个字符，再判断删除完之后的字符串是否是回文字符串。
             */
            if (s.charAt(i) != s.charAt(j)) {
                // 在试着删除字符时，既可以删除右指针指向的字符，也可以删除左指针指向的字符。
                return isPalindrome(s, i, j - 1) || isPalindrome(s, i + 1, j);
            }
        }
        return true;
    }

    /**
     * 跳过(删除)一个字符后再判断删除完之后的字符串是否是回文字符串
     * @param s
     * @param i 在试着删除字符时，可以删除右指针指向的字符
     * @param j 在试着删除字符时，也可以删除左指针指向的字符
     * @return
     */
    private boolean isPalindrome(String s, int i, int j) {
        /**
         * 不需要判断整个字符串，因为左指针左边和右指针右边的字符之前已经判断过具有对称性质，所以只需要判断中间的子字符串即可。
         */
        while (i < j) {
            // 再判断删除完之后的字符串是否是回文字符串
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

    /**
     *  Merge Sorted Array
     *  Input:
     *  nums1 = [1,2,3,0,0,0], m = 3
     *  nums2 = [2,5,6],       n = 3
     *  Output: [1,2,2,3,5,6]
     *
     * 归并两个有序数组把归并结果存到第一个数组上
     * @param nums1
     * @param m 指定归并位置
     * @param nums2
     * @param n nums2数组长度
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int index1 = m - 1, index2 = n - 1;
        // 归并后数组长度
        int indexMerge = m + n - 1;
        // 需要从尾开始遍历，否则在 nums1 上归并得到的值会覆盖还未进行归并比较的值
        while (index1 >= 0 || index2 >= 0) {
            // nums1 数组元素都归并完后逆序依次取出 nums2 数组元素插入
            if (index1 < 0) {
                nums1[indexMerge--] = nums2[index2--];
            }
            // nums2 数组元素都归并完后逆序依次取出 nums1 数组元素插入
            else if (index2 < 0) {
                nums1[indexMerge--] = nums1[index1--];
            }
            // index1 位置值大就把该值插入
            else if (nums1[index1] > nums2[index2]) {
                nums1[indexMerge--] = nums1[index1--];
            }
            // index2 位置值大就把该值插入
            else {
                nums1[indexMerge--] = nums2[index2--];
            }
        }
    }

    static class ListNode {
        ListNode next;
    }

    /**
     * Linked List Cycle
     *
     * 判断链表是否存在环
     *  双指针，一个指针每次移动一个节点，一个指针每次移动两个节点，如果存在环，那么这两个指针一定会相遇
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode l1 = head, l2 = head.next;
        while (l1 != null && l2 != null && l2.next != null) {
            if (l1 == l2) {
                return true;
            }
            // 一个指针每次移动一个节点，一个指针每次移动两个节点，如果存在环，那么这两个指针一定会相遇
            l1 = l1.next;
            l2 = l2.next.next;
        }
        return false;
    }

    /**
     * Longest Word in Dictionary through Deleting
     *  Input:
     *  s = "abpcplea", d = ["ale","apple","monkey","plea"]
     *  Output:
     *  "apple"
     * 删除 s 中的一些字符，使得它构成字符串列表 d 中的一个字符串，找出能构成的最长字符串。
     * 如果有多个相同长度的结果，返回字典序的最小字符串
     *
     * @param s
     * @param d
     * @return
     */
    public String findLongestWord(String s, List<String> d) {
        String longestWord = "";
        for (String target : d) {
            int l1 = longestWord.length(), l2 = target.length();
            if (l1 > l2 || (l1 == l2 && longestWord.compareTo(target) < 0)) {
                continue;
            }
            // 删除指定字符串中的一些字符，目标字符串能成为其子串
            if (isSubstr(s, target)) {
                longestWord = target;
            }
        }
        return longestWord;
    }

    /**
     * 删除指定字符串中的一些字符，目标字符串是否能成为其子串
     * @param s 指定字符串
     * @param target 目标字符串
     * @return
     */
    private boolean isSubstr(String s, String target) {
        int i = 0, j = 0;
        while (i < s.length() && j < target.length()) {
            if (s.charAt(i) == target.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == target.length();
    }
}
