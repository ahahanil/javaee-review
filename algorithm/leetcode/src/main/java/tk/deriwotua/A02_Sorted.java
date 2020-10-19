package tk.deriwotua;

import java.util.*;

/**
 * 快速选择
 *      用于求解 Kth Element 问题，也就是第 K 个元素的问题。
 *      可以使用快速排序的 partition() 进行实现。需要先打乱数组，否则最坏情况下时间复杂度为 O(N^2)
 * 堆
 *      用于求解 TopK Elements 问题，也就是 K 个最小元素的问题。
 *      可以维护一个大小为 K 的最小堆，最小堆中的元素就是最小元素。最小堆需要使用大顶堆来实现，大顶堆表示堆顶元素是堆中最大元素。
 *      这是因为我们要得到 k 个最小的元素，因此当遍历到一个新的元素时，需要知道这个新元素是否比堆中最大的元素更小，更小的话就把堆中最大元素去除，并将新元素添加到堆中。
 *      所以我们需要很容易得到最大元素并移除最大元素，大顶堆就能很好满足这个要求。
 *
 * 堆也可以用于求解 Kth Element 问题，得到了大小为 k 的最小堆之后，因为使用了大顶堆来实现，因此堆顶元素就是第 k 大的元素。
 * 快速选择也可以求解 TopK Elements 问题，因为找到 Kth Element 之后，再遍历一次数组，所有小于等于 Kth Element 的元素都是 TopK Elements。
 * 即快速选择和堆排序都可以求解 Kth Element 和 TopK Elements 问题
 */
public class A02_Sorted {

    /**
     * Kth Largest Element in an Array
     *      Input: [3,2,1,5,6,4] and k = 2
     *      Output: 5
     * 找到倒数第 k 个的元素
     *  基础排序方式实现
     *      时间复杂度 O(NlogN)，空间复杂度 O(1)
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargestBySorted(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    /**
     * Kth Largest Element in an Array
     *      Input: [3,2,1,5,6,4] and k = 2
     *      Output: 5
     * 找到倒数第 k 个的元素
     *  最小堆实现
     *      时间复杂度 O(NlogK)，空间复杂度 O(K)
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargestByPriorityQueue(int[] nums, int k) {
        /**
         * 优先队列 基于数组实现的小顶堆(任意一个非叶子节点的权值，都不大于其左右子节点的权值)
         */
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(k);
        for (int val : nums) {
            pq.add(val);
            // 只取 k 个元素 满足 k个元素后弹出堆顶最小元素
            // 维护堆的大小为 K
            if (pq.size() > k)
                pq.poll();
        }
        return pq.peek();
    }

    /**
     * Kth Largest Element in an Array
     *      Input: [3,2,1,5,6,4] and k = 2
     *      Output: 5
     * 找到倒数第 k 个的元素
     *  快速选择实现
     * @param nums
     * @param k
     * @return
     */
    public int findKthLargestByQuickSelection(int[] nums, int k) {
        k = nums.length - k;
        int l = 0, h = nums.length - 1;
        while (l < h) {
            int j = partition(nums, l, h);
            if (j == k) {
                break;
            } else if (j < k) {
                l = j + 1;
            } else {
                h = j - 1;
            }
        }
        return nums[k];
    }

    private int partition(int[] a, int l, int h) {
        int i = l, j = h + 1;
        while (true) {
            while (a[++i] < a[l] && i < h) ;
            while (a[--j] > a[l] && j > l) ;
            if (i >= j) {
                break;
            }
            swap(a, i, j);
        }
        swap(a, l, j);
        return j;
    }


    /**
     * 桶排序问题
     */

    /**
     * Top K Frequent Elements
     *  Given [1,1,1,2,2,3] and k = 2, return [1,2].
     *
     * 出现频率最多的 k 个元素
     *  桶排序
     *      设置若干个桶，每个桶存储出现频率相同的数。桶的下标表示数出现的频率，即第 i 个桶中存储的数出现的频率为 i。
     *      把数都放到桶之后，从后向前遍历桶，最先得到的 k 个数就是出现频率最多的的 k 个数
     */
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequencyForNum = new HashMap<Integer, Integer>(nums.length);
        // 统计元素出现次数
        for (int num : nums) {
            // 元素以 num值为key value值存在该key原值加1否则默认值为1
            frequencyForNum.put(num, frequencyForNum.getOrDefault(num, 0) + 1);
        }
        List<Integer>[] buckets = new ArrayList[nums.length + 1];
        for (int key : frequencyForNum.keySet()) {
            // 获取当前key频率值 对应数组下标位置
            int frequency = frequencyForNum.get(key);
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(key);
        }
        List<Integer> topK = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && topK.size() < k; i--) {
            if (buckets[i] == null) {
                continue;
            }
            // 下标位元素个数小于 topK剩余空位添加所有
            if (buckets[i].size() <= (k - topK.size())) {
                topK.addAll(buckets[i]);
            }
            // 否则取前剩余空位数个
            else {
                topK.addAll(buckets[i].subList(0, k - topK.size()));
            }
        }
        return topK;
    }

    /**
     * Sort Characters By Frequency
     *  Input:
     *  "tree"
     *  Output:
     *  "eert"
     *  Explanation:
     *  'e' appears twice while 'r' and 't' both appear once.
     *  So 'e' must appear before both 'r' and 't'. Therefore "eetr" is also a valid answer.
     *
     * 按照字符出现次数对字符串排序
     *  桶排序
     *
     * @param s
     * @return
     */
    public String frequencySort(String s) {
        Map<Character, Integer> frequencyForNum = new HashMap<>();
        // 统计字符出现次数
        for (char c : s.toCharArray())
            frequencyForNum.put(c, frequencyForNum.getOrDefault(c, 0) + 1);

        // 以频次作为下标把字符值插入数组
        List<Character>[] frequencyBucket = new ArrayList[s.length() + 1];
        for (char c : frequencyForNum.keySet()) {
            // 获取当前key频率值 对应数组下标位置
            int f = frequencyForNum.get(c);
            if (frequencyBucket[f] == null) {
                frequencyBucket[f] = new ArrayList<>();
            }
            frequencyBucket[f].add(c);
        }
        // 打印
        StringBuilder str = new StringBuilder();
        for (int i = frequencyBucket.length - 1; i >= 0; i--) {
            if (frequencyBucket[i] == null) {
                continue;
            }
            for (char c : frequencyBucket[i]) {
                for (int j = 0; j < i; j++) {
                    str.append(c);
                }
            }
        }
        return str.toString();
    }

    /**
     * 荷兰国旗问题
     *  荷兰国旗包含三种颜色：红、白、蓝。
     *  有三种颜色的球，算法的目标是将这三种球按颜色顺序正确地排列。
     *  它其实是三向切分快速排序的一种变种，在三向切分快速排序中，每次切分都将数组分成三个区间：小于切分元素、等于切分元素、大于切分元素，
     *  而该算法是将数组分成三个区间：等于红色、等于白色、等于蓝色。
     *
     * Sort Colors
     *  Input: [2,0,2,1,1,0]
     *  Output: [0,0,1,1,2,2]
     * 只有 0/1/2 三种颜色 按颜色进行排序
     */
    public void sortColors(int[] nums) {
        // 区间
        int zero = -1, one = 0, two = nums.length;
        while (one < two) {
            //  0 区间
            if (nums[one] == 0) {
                swap(nums, ++zero, one++);
            }
            // 2 区间
            else if (nums[one] == 2) {
                swap(nums, --two, one);
            }
            // 1 区间
            else {
                ++one;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
