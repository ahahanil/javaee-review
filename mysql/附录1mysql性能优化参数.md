mysql的监控方法大致分为两类
- 连接到mysql数据库内部，使用`show status`、`show variables`、`flush status` 来查看mysql的各种性能指标。
  ```mysql
  # 服务器状态变量，运行服务器的统计和状态指标
  mysql>SHOW STATUS; 
  # 服务器系统变量，实际上使用的变量的值
  mysql> SHOW VARIABLES;
  ```
- 直接使用`mysqladmin`查看其性能指标 比如 `UserParameter=mysql.uptime,mysqladmin -uroot status|cut -f2 -d":"|cut -f1 -d"T"`

mysqladmin两个参数`status`、`extended-status`

```shell script
shell > mysqladmin  -uroot -ppassword  variables status
# 可得到以下信息（后面详解）
-----------------------------------------------------------------------------------------------------
Uptime: 4557887      #mysql运行的秒数
Threads: 1                #连接数
# Questions = Com_* + Qcache_hits
Questions: 1684130    #The number of questions (queries) from clients since the server was started.
Slow queries: 0      #The number of queries that have taken more than long_query_time seconds
Opens: 221872     #The number of tables the server has opened.
Flush tables: 1     #The number of flush-*, refresh, and reload commands the server has executed.
Open tables: 64     #The number of tables that currently are open.
Queries per second avg: 0.369  #从上次运行开始计算，每秒钟平均查询次数
-----------------------------------------------------------------------------------------------------
```


最完整的信息
```shell script
shell > mysqladmin  -uroot -ppassword  variables extended-status
```

其他的信息
```
# 这个命令生成所有mysqld选项和可配置变量的列表
shell > /usr/libexec/mysqld --verbose --help
# 服务器状态变量，运行服务器的统计和状态指标
mysql>SHOW STATUS; 
# 服务器系统变量，实际上使用的变量的值
mysql> SHOW VARIABLES;
# 模糊匹配关键字
mysql>SHOW STATUS LIKE  '%变量名% ' ;
```

对配置参数的说明
- 配置参数的格式如下
```shell script
shell > mysqladmin  -uroot -ppassword  variables extended-status

+-----------------------------------------+------------------------------------------------------------+
| Variable_name                           |              Value # 值单位可能是byte要得到M需除以2次1024    |
+-----------------------------------------+------------------------------------------------------------+
| auto_increment_increment                |          1                                                 |
| auto_increment_offset                   |              1                                             |
| automatic_sp_privileges                 |            ON                                              |
| .........                                                                                            |
| .........                                                                                            |
| Uptime # MySQL服务器已经运行的秒数       |      4405546                                               |
+-----------------------------------------+------------------------------------------------------------+
```

`auto_increment_increment`、`auto_increment_offset`两个变量值都只能为1到65,535之间的整数值。设置为非整数值，则会给出错误。这两个变量影响`AUTO_INCREMENT`列。
- `auto_increment_increment`控制列中的值的增量值（步进量）。
- `auto_increment_offset`确定`AUTO_INCREMENT`列值的初始值。

一般不去更改,更改方法
```mysql
mysql> SET @auto_increment_offset=5;
```

```shell script
shell > mysqladmin  -uroot -ppassword  variables extended-status

+-----------------------------------------+------------------------------------------------------------+
| Variable_name                           |              Value # 值单位可能是byte要得到M需除以2次1024    |
+-----------------------------------------+------------------------------------------------------------+
| max_connections                         |          100                                               |
| table_cache                             |              64                                            |
| open_files_limit                        |            1024                                            |
| Opened_tables                           |            187690                                          |
| .........                                                                                            |
+-----------------------------------------+------------------------------------------------------------+

mysql> show processlist;
+----+------+-----------------+--------+---------+------+-------+------------------+
| Id | User | Host            | db     | Command | Time | State | Info             |
+----+------+-----------------+--------+---------+------+-------+------------------+
| 31 | root | localhost:60594 | sakila | Query   |    0 | init  | show processlist |
+----+------+-----------------+--------+---------+------+-------+------------------+
1 row in set (0.00 sec)

mysql> show full processlist;
+----+------+-----------------+--------+---------+------+-------+-----------------------+
| Id | User | Host            | db     | Command | Time | State | Info                  |
+----+------+-----------------+--------+---------+------+-------+-----------------------+
| 31 | root | localhost:60594 | sakila | Query   |    0 | init  | show full processlist |
+----+------+-----------------+--------+---------+------+-------+-----------------------+
1 row in set (0.00 sec)
```

- 这几个参数的关系：`table_cache * 2 + max_connections ＝ max_open_files`
- max_open_files 由 open_files_limit 参数决定。
- max_open_files mysql打开的最大文件数，受两个参数的影响：系统打开的最大文件数 ulimit -n 和 open_files_limit
- 加大 max_open_files 的值
  - 在/etc/my.cnf加入open_files_limit=8192
  - 在/etc/security/limits.conf添加

    ```
    *               soft    nofile          8192
    *               hard    nofile          8192
    ```

最好用sysctl或者修改/etc/sysctl.conf文件，同时还要在配置文件中把open_files_limit这个参数增大，对于4G内存服务器，open_files_limit至少要增大到4096，非特殊情况，设置成8192就可以了。

MySQL 5.0升级到5.1，table_cache 改名table_open_cache 设置表高速缓存的数目。
表缓存
- 当 Mysql 访问一个表时，如果该表在缓存中已经被打开，则可以直接访问缓存；如果还没有被缓存，但是在 Mysql 表缓冲区中还有空间，那么这个表就被打开并放入表缓冲区；如果表缓存满了，则会按照一定的规则将当前未用的表释放，或者临时扩大表缓存来存放，使用表缓存的好处是可以更快速地访问表中的内容。
- 每个连接进来，都会至少打开一个表缓存。因此， table_cache 的大小应与 max_connections 的设置有关。例如，对于 200 个并行运行的连接，应该让表的缓存至少有 200 × N ，这里 N 是网站程序一次查询所用到的表的最大值。
- 每个线程会独自持有一个数据文件的文件描述符，而索引文件的文件描述符是公用的。当table cache不够用的时候，MySQL会采用LRU算法踢掉最长时间没有使用的表。如果table_cache设置过小，MySQL就会反复打开、关闭 frm文件，造成一定的性能损失。如果table_cache设置过大，MySQL将会消耗很多CPU去做 table cache的算法运算。
- 而InnoDB的元数据管理是放在共享表空间里面做的，所以获取表的结构不需要去反复解析frm文件，这是比MyISAM强的地方。即使 table_cache设置过小，对于InnoDB的影响也是很小的，因为它根本不需要反复打开、关闭frm文件去获取元数据。

合理设置table_cache的大小：通过查看 open_tables、Opened_tables、Flush tables 的值来比较。
查看当前的表缓存情况
```shell script
shell > mysqladmin  -uroot -ppassword  variables status

+-----------------------------------------------+-------------+
| Variable_name                                 | Value       |
+-----------------------------------------------+-------------+
| Open_tables                                   | 90          |
| Opened_tables                                 | 150         |
+-----------------------------------------------+-------------+

mysql> show global status like 'open%_tables';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Open_tables   | 90    |
| Opened_tables | 150   |
+---------------+-------+
2 rows in set (0.00 sec)

# open_tables 是当前打开的表的数量，
# Opened_tables 表示打开过的表数量
----------------------------------
```

清空表缓存
```mysql
mysql> flush tables;
```
如果发现 open_tables 接近 table_cache 的时候，如果 Opened_tables 随着重新运行 SHOW STATUS 命令快速增加，就说明缓存命中率不够。并且多次执行FLUSH TABLES(通过shell > mysqladmin  -uroot -ppassword  variables status )，那就说明可能 table_cache 设置的偏小，经常需要将缓存的表清出，将新的表放入缓存，这时可以考虑增加这个参数的大小来改善访问的效率。
如果 Open_tables 比 table_cache 设置小很多，就说明table_cache 设的太大了。
table_cache的值在2G内存以下的机器中的值默认时256到512，如果机器有4G内存,则默认这个值是2048，但这决意味着机器内存越大，这个值应该越大，因为table_cache加大后，使得mysql对SQL响应的速度更快了，不可避免的会产生更多的死锁（dead lock），这样反而使得数据库整个一套操作慢了下来，严重影响性能。
注意，不能盲目地把table_cache设置成很大的值。如果设置得太高，可能会造成文件描述符不足，从而造成性能不稳定或者连接失败。
对于有1G内存的机器，推荐值是128－256。


```shell script
shell > mysqladmin  -uroot -ppassword  variables extended-status
shell > mysqladmin -uroot -ppassword  variable status

+-----------------------------------------------+-------------+
| Variable_name                                 | Value       |
+-----------------------------------------------+-------------+
| key_buffer_size                               | 67108864    |
| Key_read_requests                             | 58          |
| Key_reads                                     | 6           |
| Key_write_requests                            | 39          |
| Key_writes                                    | 18          |
+-----------------------------------------------+-------------+

mysql> show status like '%key_read%';
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| Key_read_requests | 58    |
| Key_reads         | 6     |
+-------------------+-------+
2 rows in set (0.00 sec)
```

- key_buffer_size(67108864/1024/1024=64M)
- Key_read_requests 从缓存读键的数据块的请求数。
- Key_reads 从硬盘读取键的数据块的次数。
- Key_write_requests 将键的数据块写入缓存的请求数。
- Key_writes 向硬盘写入将键的数据块的物理写操作的次数。

key_buffer_size设置索引块（index blocks）缓存的大小，保存了 MyISAM 表的索引块。它被所有线程共享，决定了数据库索引处理的速度，尤其是索引读的速度。理想情况下，对于这些块的请求应该来自于内存，而不是来自于磁盘。
只对MyISAM表起作用。即使你不使用MyISAM表，但是内部的临时磁盘表是MyISAM表，也要使用该值。
key_buffer_size 如果不使用MyISAM存储引擎，16MB足以，用来缓存一些系统表信息等。如果使用 MyISAM存储引擎，在内存允许的情况下，尽可能将所有索引放入内存，简单来说就是“越大越好”
合理设置key_buffer_size的方法：查看Key_read_requests和Key_reads的比例
- Key_reads 代表命中磁盘的请求个数， Key_read_requests 是总数。命中磁盘的读请求数除以读请求总数就是不中比率。如果每 1,000 个请求中命中磁盘的数目超过 1 个，就应该考虑增大关键字缓冲区了。

key_reads / key_read_requests的值应该尽可能的低，比如1:100，1:1000 ，1:10000。
对于内存在4GB左右的服务器该参数可设置为256M或384M。注意：该参数值设置的过大反而会是服务器整体效率降低！ 

256MB内存和许多表，想要在中等数量的客户时获得最大性能，应使用
```shell script
shell> mysqld_safe --key_buffer_size=64M --table_cache=256 --sort_buffer_size=4M --read_buffer_size=1M &
```

每个连接到MySQL服务器的线程都需要有自己的缓冲，默认为其分配256K。事务开始之后，则需要增加更多的空间。运行较小的查询可能仅给指定的线程增加少量的内存消耗，例如存储查询语句的空间等。但如果对数据表做复杂的操作比较复杂，例如排序则需要使用临时表，此时会分配大约read_buffer_size，sort_buffer_size，read_rnd_buffer_size，tmp_table_size大小的内存空间。不过它们只是在需要的时候才分配，并且在那些操作做完之后就释放了。

myisam_sort_buffer_size 当在REPAIR TABLE或用CREATE INDEX创建索引或ALTER TABLE过程中排序 MyISAM索引分配之缓冲区。

sort_buffer_size 每个排序线程分配的缓冲区的大小。增加该值可以加快ORDER BY或GROUP BY操作。
- 该参数对应的分配内存是每个连接独享，如果有100个连接，那么实际分配的总共排序缓冲区大小为100 × 6 ＝ 600MB。所以，对于内存在4GB左右的服务器推荐设置为6-8M。

```mysql
mysql> SHOW STATUS LIKE "sort%";
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| Sort_merge_passes | 0     |
| Sort_range        | 0     |
| Sort_rows         | 0     |
| Sort_scan         | 0     |
+-------------------+-------+
4 rows in set (0.00 sec)
```

如果 sort_merge_passes 很大，就表示需要注意 sort_buffer_size。
当 MySQL 必须要进行排序时，就会在从磁盘上读取数据时分配一个排序缓冲区来存放这些数据行。如果要排序的数据太大，那么数据就必须保存到磁盘上的临时文件中，并再次进行排序。如果 sort_merge_passes 状态变量很大，这就指示了磁盘的活动情况。

```mysql
mysql> show variables like 'read%';
+----------------------+--------+
| Variable_name        | Value  |
+----------------------+--------+
| read_buffer_size     | 65536  |
| read_only            | OFF    |
| read_rnd_buffer_size | 262144 |
+----------------------+--------+
3 rows in set (0.00 sec)
```
read_buffer_size 是MySql读入缓冲区大小。对表进行顺序扫描的请求将分配一个读入缓冲区，MySql会为它分配一段内存缓冲区。read_buffer_size变量控制这一缓冲区的大小。
- 每个线程连续扫描时为扫描的每个表分配的缓冲区的大小(字节)。如果进行多次连续扫描，可能需要增加该值， 默认值为131072。和sort_buffer_size一样，该参数对应的分配内存也是每连接独享。

read_rnd_buffer_size是MySql的随机读缓冲区大小。当按任意顺序读取行时(例如，按照排序顺序)，将分配一个随机读缓存区。进行排序查询时，MySql会首先扫描一遍该缓冲，以避免磁盘搜索，提高查询速度，如果需要排序大量数据，可适当调高该值。该参数对应的分配内存也是每连接独享。

join_buffer_size 联合查询操作所能使用的缓冲区大小，和sort_buffer_size一样，该参数对应的分配内存也是每个连接独享。

max_allowed_packet net_buffer_length 包消息缓冲区初始化为net_buffer_length字节，但需要时可以增长到max_allowed_packet字节。该值默认很小，以捕获大的(可能是错误的)数据包。

thread_stack 每个线程的堆栈大小。用crash-me测试检测出的许多限制取决于该值。 默认值足够大，可以满足普通操作。

```shell script
shell > mysqladmin  -uroot -ppassword  variables extended-status
shell > mysqladmin -uroot -ppassword  variable status

thread_cache_size                        0
query_cache_size                         0
tmp_table_size                          33554432
innodb_thread_concurrency             8
max_connections                         100
max_connect_errors                      10

mysql> show status LIKE 'threads%';
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| Threads_cached    | 3     |
| Threads_connected | 1     |
| Threads_created   | 4     |
| Threads_running   | 1     |
+-------------------+-------+
4 rows in set (0.00 sec)
```
线程缓存。mysqld 在接收连接时会根据需要生成线程。在一个连接变化很快的繁忙服务器上，对线程进行缓存便于以后使用可以加快最初的连接。
此处重要的值是 Threads_created，每次 mysqld 需要创建一个新线程时，这个值都会增加。如果这个数字在连续执行 SHOW STATUS 命令时快速增加，就应该尝试增大线程缓存。


```mysql
mysql> SHOW VARIABLES LIKE 'have_query_cache';
+------------------+-------+
| Variable_name    | Value |
+------------------+-------+
| have_query_cache | YES   |
+------------------+-------+
1 row in set (0.00 sec)

mysql> show variables like '%query%';
+------------------------------+--------------------------+
| Variable_name                | Value                    |
+------------------------------+--------------------------+
| binlog_rows_query_log_events | OFF                      |
| ft_query_expansion_limit     | 20                       |
| have_query_cache             | YES                      |
| long_query_time              | 1.000000                 |
| query_alloc_block_size       | 8192                     |
| query_cache_limit            | 1048576                  |
| query_cache_min_res_unit     | 4096                     |
| query_cache_size             | 0                        |
| query_cache_type             | OFF                      |
| query_cache_wlock_invalidate | OFF                      |
| query_prealloc_size          | 8192                     |
| slow_query_log               | ON                       |
| slow_query_log_file          | DESKTOP-UB593L9-slow.log |
+------------------------------+--------------------------+
13 rows in set (0.00 sec)
```
- have_query_cache 是否有查询缓存
- query_cache_limit 指定单个查询能够使用的缓冲区大小，缺省为1M
- query_cache_type 变量影响其工作方式。这个变量可以设置为下面的值：
  - 0 或OFF 将阻止缓存或查询缓存结果。
  - 1 或ON 将允许缓存，以SELECT SQL_NO_CACHE 开始的查询语句除外。
  - 2 或DEMAND ，      仅对以SELECT SQL_CACHE 开始的那些查询语句启用缓存。
  - 如果全部使用innodb存储引擎，建议为0，如果使用MyISAM 存储引擎，建议为2
- query_cache_min_res_unit 是在4.1版本以后引入的，它指定分配缓冲区空间的最小单位，缺省为4K。检查状态值Qcache_free_blocks，如果该值非常大，则表明缓冲区中碎片很多，这就表明查询结果都比较小，此时需要减小 query_cache_min_res_unit。
- query_cache_size 为了存储老的查询结果而分配的内存数量 (以字节指定) 。如果设置它为 0 ，查询缓冲将被禁止(缺省值为 0 )。 根据 命中率(Qcache_hits/(Qcache_hits+Qcache_inserts)*100))进行调整，一般不建议太大，256MB可能已经差不多了，大型的配置型静态数据可适当调大


```mysql
mysql> SHOW STATUS LIKE 'qcache%';
+-------------------------+-------+
| Variable_name           | Value |
+-------------------------+-------+
| Qcache_free_blocks      | 0     |
| Qcache_free_memory      | 0     |
| Qcache_hits             | 0     |
| Qcache_inserts          | 0     |
| Qcache_lowmem_prunes    | 0     |
| Qcache_not_cached       | 0     |
| Qcache_queries_in_cache | 0     |
| Qcache_total_blocks     | 0     |
+-------------------------+-------+
8 rows in set (0.00 sec)
```

- Qcache_free_blocks 缓存中相邻内存块的个数。数目大说明可能有碎片。FLUSH QUERY CACHE 会对缓存中的碎片进行整理，从而得到一个空闲块。
- Qcache_free_memory 缓存中的空闲内存。
- Qcache_hits 每次查询在缓存中命中时就增大。
- Qcache_inserts 每次插入一个查询时就增大。 未命中然后插入。
- Qcache_lowmem_prunes 的值非常大，则表明经常出现缓冲不够的情况,同时Qcache_hits的值非常大，则表明查询缓冲使用非常频繁，此时需要增加缓冲大小，Qcache_hits的值不大，则表明你的查询重复率很低，这种情况下使用查询缓冲反而会影响效率，那么可以考虑不用查询缓冲。这个数字最好长时间来看；如果这个数字在不断增长，就表示可能碎片非常严重，或者内存很少。（上面的 free_blocks 和 free_memory 可以告诉您属于哪种情况）。
- Qcache_not_cached 不适合进行缓存的查询的数量，通常是由于这些查询不是 SELECT 语句。
- Qcache_queries_in_cache 当前缓存的查询（和响应）的数量。
- Qcache_total_blocks 缓存中块的数量。

Total number of queries = Qcache_inserts + Qcache_hits + Qcache_not_cached.

查询命中率 = Qcache_hits -Qcache_inserts /Qcache_hits

查询插入率 = Qcache_inserts / Com_select;

未插入率 = Qcache_not_cached / Com_select;

很多 LAMP 应用程序都严重依赖于数据库，但却会反复执行相同的查询。每次执行查询时，数据库都必须要执行相同的工作 —— 对查询进行分析，确定如何执行查询，从磁盘中加载信息，然后将结果返回给客户机。MySQL 有一个特性称为查询缓存，查询缓存会存储一个 SELECT 查询的文本与被传送到客户端的相应结果。如果之后接收到一个同样的查询，服务器将从查询缓存中检索结果，而不是再次分析和执行这个同样的查询。在很多情况下，这会极大地提高性能。不过，问题是查询缓存在默认情况下是禁用的。
通常，间隔几秒显示这些变量就可以看出区别，这可以帮助确定缓存是否正在有效地使用。运行 FLUSH STATUS 可以重置一些计数器，如果服务器已经运行了一段时间，这会非常有帮助。
使用非常大的查询缓存，期望可以缓存所有东西，这种想法非常诱人。但如果表有变动时，首先要把Query_cache和该表相关的语句全部置为失效，然后在写入更新。
那么如果Query_cache非常大，该表的查询结构又比较多，查询语句失效也慢，一个更新或是Insert就会很慢，这样看到的就是Update或是Insert怎么这么慢了。
所以在数据库写入量或是更新量也比较大的系统，该参数不适合分配过大。而且在高并发，写入量大的系统，建系把该功能禁掉。
作为一条规则，如果 FLUSH QUERY CACHE 占用了很长时间，那就说明缓存太大了。

wait_timeout 服务器关闭非交互连接之前等待活动的秒数。
- 在线程启动时，根据全局wait_timeout值或全局interactive_timeout值初始化会话wait_timeout值，取决于客户端类型

connect_timeout mysqld服务器用Bad handshake响应前等待连接包的秒数。

interactive_timeout 服务器关闭交互式连接前等待活动的秒数。交互式客户端定义为在mysql_real_connect()中使用CLIENT_INTERACTIVE选项的客户端。

```mysql
mysql> SHOW STATUS LIKE "com_select";
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Com_select    | 0     |
+---------------+-------+
1 row in set (0.00 sec)

mysql> SHOW STATUS LIKE "handler_read_rnd_next";
+-----------------------+-------+
| Variable_name         | Value |
+-----------------------+-------+
| Handler_read_rnd_next | 20    |
+-----------------------+-------+
1 row in set (0.00 sec)
```

MySQL 也会分配一些内存来读取表。理想情况下，索引提供了足够多的信息，可以只读入所需要的行，但是有时候查询（设计不佳或数据本性使然）需要读取表中大量数据。要理解这种行为，需要知道运行了多少个 SELECT 语句，以及需要读取表中的下一行数据的次数（而不是通过索引直接访问）。
Handler_read_rnd_next / Com_select 得出了表扫描比率 —— 在本例中是 521:1。如果该值超过 4000，就应该查看 read_buffer_size，例如 read_buffer_size = 4M。如果这个数字超过了 8M，就应该与开发人员讨论一下对这些查询进行调优了！

```mysql
mysql> SHOW STATUS LIKE 'created_tmp%';
+-------------------------+-------+
| Variable_name           | Value |
+-------------------------+-------+
| Created_tmp_disk_tables | 0     |
| Created_tmp_files       | 8     |
| Created_tmp_tables      | 3     |
+-------------------------+-------+
3 rows in set (0.00 sec)
```
临时表可以在更高级的查询中使用，其中数据在进一步进行处理（例如 GROUP BY 字句）之前，都必须先保存到临时表中；理想情况下，在内存中创建临时表。但是如果临时表变得太大，就需要写入磁盘中。
每次使用临时表都会增大 Created_tmp_tables；基于磁盘的表也会增大 Created_tmp_disk_tables。对于这个比率，并没有什么严格的规则，因为这依赖于所涉及的查询。长时间观察 Created_tmp_disk_tables 会显示所创建的磁盘表的比率，您可以确定设置的效率。 tmp_table_size 和 max_heap_table_size 都可以控制临时表的最大大小，因此请确保在 my.cnf 中对这两个值都进行了设置。

日志相关`log-bin=mysql-bin`、`binlog_format=mixed`
- mysql-bin.000001、mysql-bin.000002等文件是数据库的操作日志，例如UPDATE一个表，或者DELETE一些数据，即使该语句没有匹配的数据，这个命令也会存储到日志文件中，还包括每个语句执行的时间，也会记录进去的。

InnoDB

innodb_buffer_pool_size 定义了 InnoDB 存储引擎的表数据和索引数据的最大内存缓冲区大小。和 MyISAM 存储引擎不同， MyISAM 的 key_buffer_size 只能缓存索引键，而 innodb_buffer_pool_size 却可以缓存数据块和索引键。适当的增加这个参数的大小，可以有效的减少 InnoDB 类型的表的磁盘 I/O 。为Innodb加速优化首要参数。默认值8M
这个参数不能动态更改，所以分配需多考虑。分配过大，会使Swap占用过多，致使Mysql的查询特慢。如果你的数据量不大，并且不会暴增，那么可分配是你的数据大小＋１０％左右做为这个参数的值。例如：数据大小为50M,那么给这个值分配innodb_buffer_pool_size＝64M

```mysql
mysql> show variables like 'innodb%';
+------------------------------------------+------------------------+
| Variable_name                            | Value                  |
+------------------------------------------+------------------------+
| innodb_adaptive_flushing                 | ON                     |
| innodb_adaptive_flushing_lwm             | 10                     |
| innodb_adaptive_hash_index               | ON                     |
| innodb_adaptive_max_sleep_delay          | 150000                 |
| innodb_additional_mem_pool_size          | 5242880                |
| innodb_api_bk_commit_interval            | 5                      |
| innodb_api_disable_rowlock               | OFF                    |
| innodb_api_enable_binlog                 | OFF                    |
| innodb_api_enable_mdl                    | OFF                    |
| innodb_api_trx_level                     | 0                      |
| innodb_autoextend_increment              | 64                     |
| innodb_autoinc_lock_mode                 | 1                      |
| innodb_buffer_pool_dump_at_shutdown      | OFF                    |
| innodb_buffer_pool_dump_now              | OFF                    |
| innodb_buffer_pool_filename              | ib_buffer_pool         |
| innodb_buffer_pool_instances             | 8                      |
| innodb_buffer_pool_load_abort            | OFF                    |
| innodb_buffer_pool_load_at_startup       | OFF                    |
| innodb_buffer_pool_load_now              | OFF                    |
| innodb_buffer_pool_size                  | 199229440              |
| innodb_change_buffer_max_size            | 25                     |
| innodb_change_buffering                  | all                    |
| innodb_checksum_algorithm                | crc32                  |
| innodb_checksums                         | ON                     |
| innodb_cmp_per_index_enabled             | OFF                    |
| innodb_commit_concurrency                | 0                      |
| innodb_compression_failure_threshold_pct | 5                      |
| innodb_compression_level                 | 6                      |
| innodb_compression_pad_pct_max           | 50                     |
| innodb_concurrency_tickets               | 5000                   |
| innodb_data_file_path                    | ibdata1:12M:autoextend |
| innodb_data_home_dir                     |                        |
| innodb_disable_sort_file_cache           | OFF                    |
| innodb_doublewrite                       | ON                     |
| innodb_fast_shutdown                     | 1                      |
| innodb_file_format                       | Antelope               |
| innodb_file_format_check                 | ON                     |
| innodb_file_format_max                   | Antelope               |
| innodb_file_per_table                    | ON                     |
| innodb_flush_log_at_timeout              | 1                      |
| innodb_flush_log_at_trx_commit           | 1                      |
| innodb_flush_method                      |                        |
| innodb_flush_neighbors                   | 1                      |
| innodb_flushing_avg_loops                | 30                     |
| innodb_force_load_corrupted              | OFF                    |
| innodb_force_recovery                    | 0                      |
| innodb_ft_aux_table                      |                        |
| innodb_ft_cache_size                     | 8000000                |
| innodb_ft_enable_diag_print              | OFF                    |
| innodb_ft_enable_stopword                | ON                     |
| innodb_ft_max_token_size                 | 84                     |
| innodb_ft_min_token_size                 | 3                      |
| innodb_ft_num_word_optimize              | 2000                   |
| innodb_ft_result_cache_limit             | 2000000000             |
| innodb_ft_server_stopword_table          |                        |
| innodb_ft_sort_pll_degree                | 2                      |
| innodb_ft_total_cache_size               | 640000000              |
| innodb_ft_user_stopword_table            |                        |
| innodb_io_capacity                       | 200                    |
| innodb_io_capacity_max                   | 2000                   |
| innodb_large_prefix                      | OFF                    |
| innodb_lock_wait_timeout                 | 50                     |
| innodb_locks_unsafe_for_binlog           | OFF                    |
| innodb_log_buffer_size                   | 3145728                |
| innodb_log_compressed_pages              | ON                     |
| innodb_log_file_size                     | 50331648               |
| innodb_log_files_in_group                | 2                      |
| innodb_log_group_home_dir                | .\                     |
| innodb_lru_scan_depth                    | 1024                   |
| innodb_max_dirty_pages_pct               | 75                     |
| innodb_max_dirty_pages_pct_lwm           | 0                      |
| innodb_max_purge_lag                     | 0                      |
| innodb_max_purge_lag_delay               | 0                      |
| innodb_mirrored_log_groups               | 1                      |
| innodb_monitor_disable                   |                        |
| innodb_monitor_enable                    |                        |
| innodb_monitor_reset                     |                        |
| innodb_monitor_reset_all                 |                        |
| innodb_old_blocks_pct                    | 37                     |
| innodb_old_blocks_time                   | 1000                   |
| innodb_online_alter_log_max_size         | 134217728              |
| innodb_open_files                        | 300                    |
| innodb_optimize_fulltext_only            | OFF                    |
| innodb_page_size                         | 16384                  |
| innodb_print_all_deadlocks               | OFF                    |
| innodb_purge_batch_size                  | 300                    |
| innodb_purge_threads                     | 1                      |
| innodb_random_read_ahead                 | OFF                    |
| innodb_read_ahead_threshold              | 56                     |
| innodb_read_io_threads                   | 4                      |
| innodb_read_only                         | OFF                    |
| innodb_replication_delay                 | 0                      |
| innodb_rollback_on_timeout               | OFF                    |
| innodb_rollback_segments                 | 128                    |
| innodb_sort_buffer_size                  | 1048576                |
| innodb_spin_wait_delay                   | 6                      |
| innodb_stats_auto_recalc                 | ON                     |
| innodb_stats_method                      | nulls_equal            |
| innodb_stats_on_metadata                 | OFF                    |
| innodb_stats_persistent                  | ON                     |
| innodb_stats_persistent_sample_pages     | 20                     |
| innodb_stats_sample_pages                | 8                      |
| innodb_stats_transient_sample_pages      | 8                      |
| innodb_status_output                     | OFF                    |
| innodb_status_output_locks               | OFF                    |
| innodb_strict_mode                       | OFF                    |
| innodb_support_xa                        | ON                     |
| innodb_sync_array_size                   | 1                      |
| innodb_sync_spin_loops                   | 30                     |
| innodb_table_locks                       | ON                     |
| innodb_thread_concurrency                | 9                      |
| innodb_thread_sleep_delay                | 0                      |
| innodb_undo_directory                    | .                      |
| innodb_undo_logs                         | 128                    |
| innodb_undo_tablespaces                  | 0                      |
| innodb_use_native_aio                    | ON                     |
| innodb_use_sys_malloc                    | ON                     |
| innodb_version                           | 5.6.21                 |
| innodb_write_io_threads                  | 4                      |
+------------------------------------------+------------------------+
119 rows in set (0.00 sec)

mysql> show status like 'innodb%';
+---------------------------------------+-------------+
| Variable_name                         | Value       |
+---------------------------------------+-------------+
| Innodb_buffer_pool_dump_status        | not started |
| Innodb_buffer_pool_load_status        | not started |
| Innodb_buffer_pool_pages_data         | 1034        |
| Innodb_buffer_pool_bytes_data         | 16941056    |
| Innodb_buffer_pool_pages_dirty        | 0           |
| Innodb_buffer_pool_bytes_dirty        | 0           |
| Innodb_buffer_pool_pages_flushed      | 1512        |
| Innodb_buffer_pool_pages_free         | 11112       |
| Innodb_buffer_pool_pages_misc         | 14          |
| Innodb_buffer_pool_pages_total        | 12160       |
| Innodb_buffer_pool_read_ahead_rnd     | 0           |
| Innodb_buffer_pool_read_ahead         | 0           |
| Innodb_buffer_pool_read_ahead_evicted | 0           |
| Innodb_buffer_pool_read_requests      | 502142      |
| Innodb_buffer_pool_reads              | 488         |
| Innodb_buffer_pool_wait_free          | 0           |
| Innodb_buffer_pool_write_requests     | 258500      |
| Innodb_data_fsyncs                    | 959         |
| Innodb_data_pending_fsyncs            | 0           |
| Innodb_data_pending_reads             | 0           |
| Innodb_data_pending_writes            | 0           |
| Innodb_data_read                      | 10162176    |
| Innodb_data_reads                     | 709         |
| Innodb_data_writes                    | 2256        |
| Innodb_data_written                   | 57477120    |
| Innodb_dblwr_pages_written            | 1512        |
| Innodb_dblwr_writes                   | 62          |
| Innodb_have_atomic_builtins           | ON          |
| Innodb_log_waits                      | 0           |
| Innodb_log_write_requests             | 16194       |
| Innodb_log_writes                     | 267         |
| Innodb_os_log_fsyncs                  | 325         |
| Innodb_os_log_pending_fsyncs          | 0           |
| Innodb_os_log_pending_writes          | 0           |
| Innodb_os_log_written                 | 7900672     |
| Innodb_page_size                      | 16384       |
| Innodb_pages_created                  | 547         |
| Innodb_pages_read                     | 487         |
| Innodb_pages_written                  | 1512        |
| Innodb_row_lock_current_waits         | 0           |
| Innodb_row_lock_time                  | 0           |
| Innodb_row_lock_time_avg              | 0           |
| Innodb_row_lock_time_max              | 0           |
| Innodb_row_lock_waits                 | 0           |
| Innodb_rows_deleted                   | 1           |
| Innodb_rows_inserted                  | 47300       |
| Innodb_rows_read                      | 71256       |
| Innodb_rows_updated                   | 1           |
| Innodb_num_open_files                 | 45          |
| Innodb_truncated_status_writes        | 0           |
| Innodb_available_undo_logs            | 128         |
+---------------------------------------+-------------+
51 rows in set (0.00 sec)
```

Innodb_buffer_pool_pages_data 分配出去， 正在被使用页的数量，包括脏页。单位是page
Innodb_buffer_pool_pages_dirty 脏页但没有被flush除去的页面数。单位是page
Innodb_buffer_pool_pages_flushed 已经flush的页面数。单位是page
Innodb_buffer_pool_pages_free 当前空闲页面数。单位是page
Innodb_buffer_pool_pages_misc 缓存池中当前已经被用作管理用途或hash index而不能用作为普通数据页的数目。单位是page
Innodb_buffer_pool_pages_total 缓冲区总共的页面数。单位是page
Innodb_buffer_pool_read_ahead_rnd 随机预读的次数
Innodb_buffer_pool_read_ahead_seq 顺序预读的次数
Innodb_buffer_pool_read_requests 从缓冲池中读取页的次数
Innodb_buffer_pool_reads 从磁盘读取页的次数。缓冲池里面没有， 就会从磁盘读取
Innodb_buffer_pool_wait_free 缓冲池等待空闲页的次数，当需要空闲块而系统中没有时，就会等待空闲页面
Innodb_buffer_pool_write_requests 缓冲池总共发出的写请求次数
Innodb_data_fsyncs 总共完成的fsync次数
Innodb_data_pending_fsyncs innodb当前等待的fsync次数
Innodb_data_pending_reads innodb当前等待的读的次数
Innodb_data_pending_writes innodb当前等待的写的次数
Innodb_data_read 总共读入的字节数
Innodb_data_reads innodb完成的读的次数
Innodb_data_writes innodb完成的写的次数
Innodb_data_written 总共写出的字节数
Innodb_log_waits 因日志缓存太小而必须等待其被写入所造成的等待数。单位是次。       


命中率=innodb_buffer_pool_read_requests / (innodb_buffer_pool_read_requests + innodb_buffer_pool_read_ahead + innodb_buffer_pool_reads)

innodb_buffer_pool_size 如果不使用InnoDB存储引擎，可以不用调整这个参数，如果需要使用，在内存允许的情况下，尽可能将所有的InnoDB数据文件存放如内存中，同样将但来说也是“越大越好”

innodb_additional_pool_size 这个值不用分配太大，系统可以自动调。不用设置太高。通常比较大数据设置16Ｍ够用了，如果表比较多，可以适当的增大。如果这个值自动增加，会在error log有中显示的。20M足够了。

innodb_log_file_size 指定日志的大小
- 分配原则：几个日志成员大小加起来差不多和你的innodb_buffer_pool_size相等。在高写入负载尤其是大数据集的情况下很重要。这个值越大则性能相对越高，但是要注意到可能会增加恢复时间。
- 说明：这个值分配的大小和数据库的写入速度，事务大小，异常重启后的恢复有很大的关系。

innodb_log_buffer_size 事务在内存中的缓冲。
- 分配原则：控制在2-8M.这个值不用太多的。他里面的内存一般一秒钟写到磁盘一次。具体写入方式和你的事务提交方式有关。一般最大指定为3M比较合适。
- 参考：Innodb_os_log_written(show global status 可以拿到)
- 如果这个值增长过快，可以适当的增加innodb_log_buffer_size
- 另外如果你需要处理大理的text，或是blog字段，可以考虑增加这个参数的值。
- 默认的设置在中等强度写入负载以及较短事务的情况下，服务器性能还可以。如果存在更新操作峰值或者负载较大，就应该考虑加大它的值了。如果它的值设置太高了，可能会浪费内存 -- 它每秒都会刷新一次，因此无需设置超过1秒所需的内存空间。通常 8-16MB 就足够了。越小的系统它的值越小。
innodb_flush_logs_at_trx_commit 控制事务的提交方式
- 分配原则：这个参数只有3个值，0，1，2请确认一下自已能接受的级别。默认为1，主库请不要更改了。性能更高的可以设置为0或是2，但会丢失一秒钟的事务。
- 值为1时：innodb 的事务LOG在每次提交后写入日志文件，并对日志做刷新到磁盘。这个可以做到不丢任何一个事务。
- 值为2事，也就是不把日志刷新到磁盘上，而只刷新到操作系统的缓存上。日志仍然会每秒刷新到磁盘中去，因此通常不会丢失每秒1-2次更新的消耗。如果设置为 0 就快很多了，不过也相对不安全了 -- MySQL服务器崩溃时就会丢失一些事务。设置为 2 只会丢失刷新到操作系统缓存的那部分事务。

innodb_file_per_table 使每个Innodb的表，有自已独立的表空间。如删除文件后可以回收那部分空间。
- 分配原则：只有使用不使用。但DB还需要有一个公共的表空间。
- InnoDB 默认会将所有的数据库InnoDB引擎的表数据存储在一个共享空间中：ibdata1，增删数据库的时候，ibdata1文件不会自动收缩，单个数据库的备份也将成为问题。通常只能将数据使用mysqldump 导出，然后再导入解决这个问题。
- 查看是否开启：
  ```mysql
  mysql> show variables like '%per_table%';
  +-----------------------+-------+
  | Variable_name         | Value |
  +-----------------------+-------+
  | innodb_file_per_table | ON    |
  +-----------------------+-------+
  1 row in set (0.00 sec)
  ```

innodb_open_files 限制Innodb能打开的表的数据。
- 分配原则：如果库里的表特别多的情况，请增加这个。这个值默认是300。这个值必须超过你配置的innodb_data_file_path个数。
- 适当的增加table_cache
innodb_flush_method Innodb和系统打交道的一个IO模型
- 分配原则：Windows不用设置。UNIX可以设置：fdatasync (默认设置)，O_DIRECT，和O_DSYNC
- O_DIRECT跳过了操作系统的文件系统Disk Cache，让MySQL直接读写磁盘。 有数据表明，如果是大量随机写入操作，O_DIRECT会提升效率。但是顺序写入和读取效率都会降低。

innodb_max_dirty_pages_pct 控制Innodb的脏页在缓冲中在那个百分比之下，值在范围1-100,默认为90.
- O_DIRECT的flush_method更适合于操作系统内存有限的情况下（可以避免不必要的对交换空间的读写操作），否则，它会由于禁用了os的缓冲降低对数据的读写操作的效能。

使用memlock可以避免MySQL内存进入swap