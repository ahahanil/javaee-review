package tk.deriwotua.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 这里并不是做对比测试
 * 而是测试相关方法的吞吐量
 */
public class PS {

    static List<Integer> nums = new ArrayList<>();
    static {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) nums.add(1000000 + r.nextInt(1000000));
    }

    /**
     * lambda表达式方式判断是否是质数
     */
    static void foreach() {
        nums.forEach(v->isPrime(v));
    }

    /**
     * 并行处理流判断是否是质数
     */
    static void parallel() {
        nums.parallelStream().forEach(PS::isPrime);
    }

    static boolean isPrime(int num) {
        for(int i=2; i<=num/2; i++) {
            if(num % i == 0) return false;
        }
        return true;
    }
}