https://www.cnblogs.com/aspirant/p/8657681.html

`Lock`和`synchronized`有以下几点不同：
- `Lock`是一个接口，而`synchronized`是Java中的关键字，`synchronized`是内置的语言实现；
- 当`synchronized`块结束时，会自动释放锁，`Lock`一般需要在`finally`中自己释放。
  - `synchronized`在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生
  - 而`Lock`在发生异常时，如果没有主动通过`unLock()`去释放锁，则很可能造成死锁现象，因此使用`Lock`时需要在`finally`块中释放锁；
- `Lock`等待锁过程中可以用`interrupt`来终端等待，而`synchronized`只能等待锁的释放，不能响应中断。
- `Lock`可以通过`trylock`来知道有没有获取锁，而`synchronized`不能； 
- 当`synchronized`块执行时，只能使用非公平锁，无法实现公平锁，而`Lock`可以通过`new ReentrantLock(true)`设置为公平锁，从而在某些场景下提高效率。
- `Lock`可以提高多个线程进行读操作的效率。（可以通过`readwritelock`实现读写分离）
- `synchronized` 锁类型`可重入`、`不可中断`、`非公平` 而 `Lock` 是： `可重入`、`可判断`、`可公平`在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时`Lock`的性能要远远优于`synchronized`(锁升级后不一定)。所以说，在具体使用时要根据适当情况选择。 