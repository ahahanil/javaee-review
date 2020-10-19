package tk.deriwotua;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 贪心思想保证每次操作都是局部最优的，并且最后得到的结果是全局最优的
 */
public class A03_GreedyThought {

    /**
     * Assign Cookies
     * Input: grid[1,3], size[1,2,4]
     * Output: 2
     * <p>
     * 每个孩子都有一个满足度 grid，每个饼干都有一个大小 size，
     * 只有饼干的大小大于等于一个孩子的满足度，该孩子才会获得满足。求解最多可以获得满足的孩子数量
     * <p>     给一个孩子的饼干应当尽量小并且又能满足该孩子，这样大饼干才能拿来给满足度比较大的孩子。
     * <p>     因为满足度最小的孩子最容易得到满足，所以先满足满足度最小的孩子
     * <p>         此解法中，我们只在每次分配时饼干时选择一种看起来是当前最优的分配方法，但无法保证这种局部最优的分配方法最后能得到全局最优解。
     * <p>         我们假设能得到全局最优解，并使用反证法进行证明，即假设存在一种比我们使用的贪心策略更优的最优策略。
     * <p>         如果不存在这种最优策略，表示贪心策略就是最优策略，得到的解也就是全局最优解。
     * <p>     证明：假设在某次选择中，贪心策略选择给当前满足度最小的孩子分配第 m 个饼干，第 m 个饼干为可以满足该孩子的最小饼干。
     * <p>     假设存在一种最优策略，可以给该孩子分配第 n 个饼干，并且 m < n。我们可以发现，经过这一轮分配，
     * <p>     贪心策略分配后剩下的饼干一定有一个比最优策略来得大。因此在后续的分配中，贪心策略一定能满足更多的孩子。
     * <p>     也就是说不存在比贪心策略更优的策略，即贪心策略就是最优策略。
     *
     * @param grid 满足度
     * @param size
     * @return
     */
    public int findContentChildren(int[] grid, int[] size) {
        if (grid == null || size == null) return 0;
        // 先排序
        Arrays.sort(grid);
        Arrays.sort(size);
        // gi可满足数量
        int gi = 0, si = 0;
        while (gi < grid.length && si < size.length) {
            if (grid[gi] <= size[si]) {
                gi++;
            }
            si++;
        }
        return gi;
    }

    /**
     * Non-overlapping Intervals
     *  Input: [ [1,2], [1,2], [1,2] ]
     *  Output: 2
     *  Explanation: You need to remove two [1,2] to make the rest of intervals non-overlapping
     *  Input: [ [1,2], [2,3] ]
     *  Output: 0
     *  Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
     *
     * 计算让一组区间不重叠所需要移除的区间个数
     *      先计算最多能组成的不重叠区间个数，然后用区间总个数减去不重叠区间的个数。
     *      在每次选择中，区间的结尾最为重要，选择的区间结尾越小，留给后面的区间的空间越大，那么后面能够选择的区间个数也就越大。
     *      按区间的结尾进行排序，每次选择结尾最小，并且和前一个区间不重叠的区间。
     *
     * @param intervals 一组区间
     * @return
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) {
            return 0;
        }
        // 区间起始为基准排序
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[1]));
        // 不重叠区间个数
        int cnt = 1;
        // 区间结尾值
        int end = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < end) {
                continue;
            }
            end = intervals[i][1];
            cnt++;
        }
        return intervals.length - cnt;
    }
}
