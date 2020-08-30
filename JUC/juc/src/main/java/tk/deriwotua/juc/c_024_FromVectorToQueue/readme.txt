同步容器类

1：Vector Hashtable ：早期使用synchronized实现 
2：ArrayList HashSet ：未考虑多线程安全（未实现同步）
3：HashSet vs Hashtable StringBuilder vs StringBuffer
4：Collections.synchronized***工厂方法使用的也是synchronized

使用早期的同步容器以及Collections.synchronized***方法的不足之处，请阅读：
http://blog.csdn.net/itm_hadf/article/details/7506529

使用新的并发容器
http://xuganggogo.iteye.com/blog/321630

集合类Map下子类亦或Collection下没有谁替代谁主要要看业务场景
    从同一个数据源中取数据可以放到各种容器里实现上可以想怎样就怎样
    但是问题有些实现需要考虑线程安全问题那么非线程安全的就不合时宜
        线程安全的又需要考虑并发量的大小、主要读操作(还是写操作)
        根据相应的情况选择
        更有甚至会有多个实现，并发小执行采用A容器逻辑并发大执行采用高并发的容器
        还有线程安全还要考虑采用CAS还是synchronized
            CAS 执行占用CPU资源不耗时操作可用
            synchronized 执行不占用CPU资源适合耗时操作
    有些不需要考虑线程安全的问题那么线程安全的就不合时宜