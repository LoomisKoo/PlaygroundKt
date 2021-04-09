package com.example.playgroundkt.activity.jetpack

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.activity.BaseEntranceActivity

@Route(path = RouterPath.RoomActivity)
class RoomActivity : BaseEntranceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //   以下所有语结尾都需要带分号，这里为了偷懒没写
    /**
     *  数据库基础语句
     *  1.语句之间的分割需要半角空格
     *  2.语句可以回车换行，但不允许存在空行
     *  3.DISTINCT关键字只能写在key之前
     *  4.运算查询中的运算数包含NULL 则结果是NULL
     *  5.字符串比较大小，以字段顺序进行比较。比如11比2靠在前面
     *  6.不能对NULL使用比较运算符
     *  7.字符串类型不能使用SUM/AVG复合函数，但可以使用MAX/MIN函数
     */

    // CREATE TABLE product                   创建表名为 product 的表
    // DROP TABLE product                     删除表名为 product 的表
    // ALERT TABLE product ADD dec            在 product 表中新增dec字段
    // ALERT TABLE product ADD (dec,img)      在 product 表中新增dec、img字段
    // ALERT TABLE product DROP dec           在 product 表中删除dec字段
    // ALERT TABLE product DROP (dec,img)     在 product 表中删除dec、img字段

    // SELECT * FROM product                                         在product表中查询所有
    // SELECT name FROM product                                      在product表中查询name字段
    // SELECT name,des FROM product                                  在product表中查询name、des字段
    // SELECT name AS product_name,des AS description                为name和des设置别名，即查询出来的key，而不改变数据库原有的key。别名可以是中文，但是需要用双引号括起来
    // SELECT '商品' AS string, 38 AS number,name,des FROM product    常数查询。"商品"和"38"本不存在数据库，查询之后会添加进结果。
    // SELECT DISTINCT name FROM product                             去重查询（单个字段）。NULL的value也会被当做数据，重复的时候会被合并成一条数据
    // SELECT DISTINCT name,des FROM product                         去重查询（两个字段）。
    // SELECT name FROM product WHERE type = '衣服'                   在product表中查询类型为"衣服"的名称（name）

    /* 注释方法1
    -- 这里是注释(本行和下一行可以作为一条SQL语句)
    SELECT name FROM product WHERE type = '衣服'
     */

    /* 注释方法2
    /* 这里是注释
    (本行和上下行可以作为一条SQL语句) */
    SELECT name FROM product WHERE type = '衣服'
    */

    /* 注释方法3，可以在语句中插入注释（方法2的注释格式也行）
    SELECT name FROM product
    -- 这里是注释(本行和上下行可以作为一条SQL语句)
    WHERE type = '衣服'
     */

    // SELECT price * 2 AS "priceX2" FROM product                          把各个商品以"priceX2"的key形式读取出来 （value是商品价格乘以2）
    // SELECT name FROM product WHERE price <> 500                         查询价格不等于500的商品名称
    // SELECT name FROM product WHERE NOT price <> 500                     查询价格等于500的商品名称
    // SELECT name FROM product WHERE sale_price - purchase_price >= 500   查询销售价减去购买价大于500的商品名称
    // SELECT name FROM product IS NOT NULL                                查询不为NULL的商品名称
    // SELECT name FROM product IS NULL                                    查询为NULL的商品名称

    // SELECT name FROM product WHERE price > 500 AND type = '衣服'                      查询价格大于500且类型为衣服的商品名称
    // SELECT name FROM product WHERE price > 500 OR type = '衣服'                       查询价格大于500或类型为衣服的商品名称
    // SELECT name FROM product WHERE price > 500 AND (type = '衣服' OR type = '文具')    查询价格大于500且类型为衣服/文具的商品名称

    // SELECT COUNT(*) FROM product                  从product表中计算所有列（包含NULL）的行数
    // SELECT COUNT(name) FROM product               从product表中计算name列（不包含NULL）的行数
    // SELECT COUNT(DISTINCT name) FROM product      从product表中计算name列（不包含NULL）去重的行数
    // SELECT DISTINCT COUNT(name) FROM product      从product表中计算name列（不包含NULL）的行数，然后再删除重复的行
    // SELECT SUM(price) FROM product                计算所有商品的价格总和（若存在NULL则删除NULL再当计算）
    // SELECT AVG(price) FROM product                计算所有商品的平均价格（若存在NULL则删除NULL再当计算）
    // SELECT MAX(price) FROM product                计算所有商品的最大价格（若存在NULL则删除NULL再当计算）
    // SELECT MIN(price) FROM product                计算所有商品的最小价格（若存在NULL则删除NULL再当计算）

    /**
     * GROUP BY 子句
     * 1.聚合键中包含NULL时，在结果中会以“不确定”行（空行）的形式表现出来
     * 2.只能写在SELECT子句之中
     * 3.GROUP BY 子句中不能使用SELECT子句中列的别名
     * 4.GROUP BY 子句的聚合结果是无序的
     * 5.WHERE 子句中不能使用聚合函数
     * 6.GROUP BY 子句的书写位置也有严格要求，一定要写在FROM语句之后（如果有 WHERE 子句的话需要写在 WHERE 子句之后）
     * 7.GROUP BY 和 WHERE 并用时 SELECT 语句的执行顺序：FROM→WHERE→GROUP BY→SELECT
     * 8.SELECT 的列名必须包含在 GROUP BY 的列名内。SELECT name,product_type,COUNT(*) FROM product GROUP BY name
     *   product_type没有GROUP BY，所以报错
     * 9.GROUP BY子句中不能使用SELECT子句中定义的别名。比如：SELECT product_type AS type FROM product GROUP BY type
     *   这会报错。因为会先执行GROUP BY，这时数据库并不知道别名
     * 10.只有 SELECT 子句和 HAVING 子句（以及 ORDER BY 子句）中能够使用聚合函数
     */
    // SELECT product_type,COUNT(*) FROM product GROUP BY product_type     查询出表里储存的所有商品类型，每种类型对应多少行（COUNT）
    /*
      -- 首先使用了 WHERE 子句对记录进行过滤，得到衣服类型的商品后再进行分组（GROUP BY）
       SELECT purchase_price,COUNT(*) FROM product
       WHERE product_type = '衣服'
       GROUP BY purchase_price
     */

    /**
     * HAVING 子句
     * 1.使用 COUNT 函数等对表中数据进行汇总操作时，为其指定条件的不是 WHERE 子句，而是 HAVING 子句
     * 2.聚合函数可以在 SELECT 子句、HAVING 子句和 ORDER BY 子句中使用
     * 3.HAVING 子句要写在 GROUP BY 子句之后
     * 4.WHERE 子句用来指定数据行的条件，HAVING子句用来指定分组的条件
     * 5.使用 HAVING 子句时SELECT语句的顺序:SELECT→FROM→WHERE→GROUP BY→HAVING
     * 6.HAVING 子句的字段需要包含在 GROUP BY 字句
     * 7.WHERE 和 HAVING 可以搜索到同样的结果，但是 WHERE 速度较快
     */
    //  SELECT product_type,COUNT(*) FROM product GROUP BY product_type HAVING COUNT(*) = 2   查询行数为2的所有商品类型
    //  SELECT product_type,AVG(price) FROM product GROUP BY price HAVING AVG(price) > 2000   查询商品平均价格大于2000的商品类型


    /**
     * ORDER BY 子句
     * 1.在 ORDER BY 子句中列名的后面使用关键字 ASC 可以进行升序排序，使用 DESC 关键字可以进行降序排序
     * 2.不论何种情况，ORDER BY 子句都需要写在SELECT语句的末尾
     * 3.在 ORDER BY 子句中可以使用 SELECT 子句中定义的别名
     * 4.使用 HAVING、ORDER BY 的顺序：FROM→WHERE→GROUP BY→HAVING→SELECT→ORDER BY
     * 5.ORDER BY 子句中也可以使用存在于表中、但并不包含在 SELECT 子句之中的列
     */
    // SELECT name,type,price FROM product ORDER BY price  根据价格升序
    // SELECT name,type,price FROM product ORDER BY price DESC 根据价格降序
    // SELECT name,type,price,id FROM product ORDER BY price,id 两个排序键，优先顺序是从左到右的键
    // SELECT name,type,price,id FROM product ORDER BY price DESC,id  price降序，id升序
    // SELECT name,type,price,id FROM product ORDER BY 1 DESC,2  第一个key（name）降序 第二个key（type）升序。这种写法不建议
    // SELECT name,type,price FROM product ORDER BY id 排序的键可以不存在 select 字句，但存在数据库的键
    // SELECT name,type,price FROM product ORDER BY COUNT(*) 使用聚合函数排序

    /**
     * INSERT 语句
     * 1.原则上，INSERT 语句每次执行一行数据的插入
     * 2.将列名和值用逗号隔开，分别括在（）内，这种形式称为清单
     * 3.表中所有列进行 INSERT 操作时可以省略表名后的列清单
     * 3.插入 NULL 时需要在 VALUES 子句的值清单中写入 NULL
     * 4.可以为表中的列设定默认值（初始值），默认值可以通过在 CREATE TABLE 语句中为列设置 DEFAULT 约束来设定
     * 5.插入默认值可以通过两种方式实现，即在 INSERT 语句的 VALUES 子句中指定 DEFAULT 关键字（显式方法），或省略列清单（隐式方法）
     * 6.使用 INSERT…SELECT 可以从其他表中复制数据
     * 7.表名后面的列清单和 VALUES 子句中的值清单的列数必须保持一致
     * 8.可以插入 NULL 值，但是插入的列设置了 NOT NULL 则会报错
     * 9.如果 key 设置有默认值，则插入的时候不插入对应的 key 和 value，则使用默认值（隐式）。但是建议使用显式插入默认值。便于识别。
     * 10.如果某列没有设定默认值，则插入的时候省略该列，则会插入 NULL。如果省略的列是 NOT NULL，则会报错
     * 11.INSERT 语句的 SELECT 语句中，可以使用 WHERE 子句或者 GROUP BY 子句等任何 SQ L语法（但使用 ORDER BY 子句并不会产生任何效果）
     */
    // INSERT INTO product (name,type,price) VALUES ('商品','衣服','130')
    // INSERT INTO product VALUES ('商品1','衣服','130')，('商品2','衣服','130')，('商品'3,'衣服','130') 多行插入也是大部分DBMS支持的，但是该写法不容易排错
    // CREATE TABLE product (product_id CHAR(4),name CHAR(10),price INTEGER DEFAULT 0, PRIMARY KEY(product_id)) // 创建表的时候price默认值是0
    // INSERT INTO product VALUES ('1','商品',DEFAULT)   插入的时候，price使用默认值（显式）
    // INSERT INTO product_copy (name,type,price) SELECT name,type,price FROM product  复制product表的数据到product_copy表中

    /**
     * DELETE 语句
     * 1.如果想将整个表全部删除，可以使用 DROP TABLE 语句，如果只想删除表中全部数据，需使用 DELETE 语句
     * 2.如果想删除部分数据行，只需在 WHERE 子句中书写对象数据的条件即可。通过 WHERE 子句指定删除对象的 DELETE 语句称为搜索型 DELETE 语句
     * 3.DELETE 语句中不能使用 GROUP BY、HAVING 和 ORDER BY 三类子句，而只能使用 WHERE 子句
     */
    // DELETE FROM product 清空product表
    // DELETE FROM product WHERE price >= 500 删除price大于等于500的商品记录

    /**
     * UPDATE 语句
     * 1.UPDATE 语句可以将列的值更新为NULL
     * 2.同时更新多列时，可以在 UPDATE 语句的 SET 子句中，使用逗号分隔更新对象的多个列
     */
    // UPDATE product SET name = 'newName'                                 更新所有的name
    // UPDATE product SET name = 'newName' WHERE id = '1'                  更新id为1的name
    // UPDATE product SET name = 'newName',des = 'newDes' WHERE id = '1'   更新id为1的name和des
    // UPDATE product SET (name ,des)=('newName','newDes') WHERE id = '1'  更新id为1的name和des   一般是使用上一种方法

    /**
     * 事务
     * 1.事务是需要在同一个处理单元中执行的一系列更新处理的集合。通过使用事务，可以对数据库中的数据更新处理的提交和取消进行管理
     * 2.事务处理的终止指令包括 COMMIT（提交处理）和 ROLLBACK（取消处理）两种
     * 3.DBMS 的事务具有原子性（Atomicity）、一致性（Consistency）、隔离性（Isolation）和持久性（Durability）四种特性。
     *   通常将这四种特性的首字母结合起来，统称为ACID特性
     * 4.COMMIT 是提交事务包含的全部更新处理的结束指令（图4-3），相当于文件处理中的覆盖保存。一旦提交，就无法恢复到事务开始前的状态了。
     *   因此，在提交之前一定要确认是否真的需要进行这些更新
     * 5.每条 SQL 语句就是一个事务（自动提交模式）
     * 6.直到用户执行 COMMIT或者 ROLLBACK 为止算作一个事务
     * 7.如果不是自动提交（使用事务），即使使用 DELETE 语句删除了数据表，也可以通过 ROLLBACK 命令取消该事务的处理，恢复表中的数据
     */
    /*
    // 开始和提交事务的语句根据各DB平台不同
    BEGIN TRANSACTION;
    -- 将运动衬衫的单价降低10元
    UPDATE product SET price = price - 10 WHERE type = '运动衬衫';

    -- 将T恤的单价提升10元
    UPDATE product SET price = price + 10 WHERE type = 'T恤';
    COMMIT;

    // 回滚上面的语句
    BEGIN TRANSACTION;
    -- 将运动衬衫的单价降低10元
    UPDATE product SET price = price - 10 WHERE type = '运动衬衫';

    -- 将T恤的单价提升10元
    UPDATE product SET price = price + 10 WHERE type = 'T恤';
    ROLLBACK;
     */

    /**
     * 视图
     * 1.从 SQL 的角度来看，视图和表是相同的，两者的区别在于表中保存的是实际的数据，而视图中保存的是 SELECT 语句（视图本身并不存储数据）
     * 2.使用视图，可以轻松完成跨多表查询数据等复杂操作
     * 3.创建视图需要使用 CREATE VIEW 语句
     * 4.可以将常用的 SELECT 语句做成视图来使用
     * 5.视图包含“不能使用 ORDER BY”和“可对其进行有限制的更新”两项限制
     * 6.删除视图需要使用 DROP VIEW 语句
     * 7.应该将经常使用的 SELECT 语句做成视图
     * 8.视图可以嵌套多层，但会降低性能，不建议使用
     * 9.定义视图时可以使用任何 SELECT 语句，但其实有一种情况例外，那就是不能使用 ORDER BY
     * 10.视图和表需要同时进行更新，因此通过汇总得到的视图无法进行更新
     */
    // CREATE VIEW productSum(type,cnt) AS SELECT type,COUNT(*) FROM GROUP BY type // 创建productSum视图。这里的AS并不是别名，只是语法规定
    // DROP VIEW productSum  删除productSum视图。
    // DROP VIEW productSum CASCADE  删除 productSum 并删除其关联的视图。
    // SELECT type，cnt FROM productSum   从视图中查询type和cnt

    /**
     * 子查询
     * 1.子查询就是一次性视图（SELECT语句）。与视图不同，子查询在SELECT语句执行完毕之后就会消失
     * 2.由于子查询需要命名，因此需要根据处理内容来指定恰当的名称
     * 3.标量子查询就是只能返回一行一列的子查询
     * 4.子查询作为内层查询会首先执行
     * 5.标量子查询绝对不能返回多行结果，比如AVG子查询。如果返回多行，就是普通子查询
     */
    // SELECT type,name FROM (SELECT name,type FROM product) AS productCopy  从一次性视图中查询type、name
    /*
     -- 选取出按照商品种类计算出的销售单价高于全部商品的平均销售单价的商品种类
     SELECT type,name,AVG(price) AS avg_price
     FROM product
     GROUP BY type
     HAVING AVG(price) > (SELECT AVG(price)
     FROM product)
     */

    /**
     * 关联子查询
     * 1.关联子查询会在细分的组内进行比较时使用
     * 2.关联子查询和GROUP BY子句一样，也可以对表中的数据进行切分
     * 3.关联子查询的结合条件如果未出现在子查询之中就会发生错误
     */
    /*
       -- 以商品种类分组为单位，对销售单价和平均单价进行比较
       SELECT name,price FROM product AS P1
       WHERE price > (
       SELECT AVG(price)
       FROM product AS P2
       WHERE P1.type = P2.type
       GROUP BY type
       )
     */

    /**
     * 函数
     * 1.种类：算术函数（用来进行数值计算的函数
     *        字符串函数（用来进行字符串操作的函数
     *        日期函数（用来进行日期操作的函数）
     *        转换函数（用来转换数据类型和值的函数）
     *        聚合函数（用来进行数据聚合的函数）
     */
    // SELECT price ABS(price) AS absPrice FROM product  查询price和absPrice  ABS函数是求绝对值
    // SELECT n,p MOD(n,p) AS mod FROM product  n%p
    // SELECT n,p ROUND(n,p) AS round FROM product  n/p 四舍五入
    // SELECT str1,str2，str3 str1||str2||str3 AS str FROM product  字符串拼接（||在SQL Server和MySQL中无法使用。SQL Server使用“+”运算符；MySQL使用CONCAT函数）
    // SELECT str LENGTH(str) AS str_length FROM product  字符串长度。不同DBMS计算的单位不一样
    // SELECT str LOWER(str) AS str_lower FROM product  字符串小写
    // SELECT str UPPER(str) AS str_lower FROM product  字符串大写
    // SELECT name,str1,str2 REPLACE(name，str1,str2) AS str_replace FROM product  str1:目标字符串；str2：需要替换成的字符串
    // SELECT str1 SUBSTRING(str FROM 3 FOR 2) AS str_sub FROM product  从第三个字符开始，截取其后的两个字符（包含自身）
    // SELECT CURRENT_DATE 查询日期，什么时候查询就返回什么日期
    // SELECT CURRENT_TIME 查询时间，什么时候查询就返回什么时间
    // SELECT CURRENT_TIMESTAMP 查询日期和时间，什么时候查询就返回什么日期时间
    /*
       -- 截取日期元素
       SELECT CURRENT_TIMESTAMP,
       EXTRACT(YEAR FROM CURRENT_TIMESTAMP) AS year,
       EXTRACT(MONTH FROM CURRENT_TIMESTAMP) AS m month,
       EXTRACT(DAY FROM CURRENT_TIMESTAMP) AS day,
       EXTRACT(HOUR FROM CURRENT_TIMESTAMP) AS hour,
       EXTRACT(MINUTE FROM CURRENT_TIMESTAMP) AS minute,
       EXTRACT(SECOND FROM CURRENT_TIMESTAMP) AS second;
     */
    // SELECT CAST('2020-09-09' AS DATE) AS date_col  转换日期
    // SELECT COALESCE(NULL,1,'2020-09-09') AS col FROM product 替换NULL为COALESCE()的参数，从左往右取值，不为NULL则返回，全为NULL则返回NULL

    /**
     * 谓词
     * 1.谓词就是返回值为真值的函数
     * 2.BETWEEN 包含三个参数
     * 3.想要取得 NULL 数据时必须使用 IS NULL
     * 4.可以将子查询作为 IN 和 EXISTS 的参数
     * 5.对通常的函数来说，返回值有可能是数字、字符串或者日期等，但是谓词的返回值全都是真值（TRUE/FALSE/UNKNOWN）
     * 6.%是代表“0字符以上的任意字符串”的特殊符号
     * 7._代表任意一个字符
     * 8.为了选取出某些值为 NULL 的列的数据，不能使用=，而只能使用特定的谓词 IS NULL
     * 9.LIKE 谓词——字符串的部分一致查询
     * 10.BETWEEN 谓词——范围查询
     * 11.IN 谓词 —— OR 的简便用法
     */

    //  SELECT name FROM product LIKE 'ddd%'   以ddd开头的所有字符串
    //  SELECT name FROM product LIKE '%ddd%'  包含ddd的所有字符串
    //  SELECT name FROM product LIKE 'ddd__'  ddd开头且后面还有两个字符的所有字符串
    //  SELECT name,price FROM product BETWEEN 100 AND 1000  价格在100~1000的所有商品名称、价格
    //  SELECT name FROM product WHERE price IS NULL  价格为NULL的商品名称
    //  SELECT name FROM product WHERE price IS NOT NULL  价格不为NULL的商品名称
    //  SELECT name FROM product WHERE price = 200 OR price = 300 OR price = 600  价格为200/300/600的商品名称
    //  SELECT name FROM product WHERE price IN (200,300,600)  价格为200/300/600的商品名称
    /*
      -- 从 product 表中查询出 product_id 存在于子查询的 name/price
      SELECT name,price
      FROM product
      WHERE product_id IN(
      SELECT product_id
      FROM shop_product
      WHERE shop_id = 'shop1'
      )
     */
    /*
      -- 从 product 表中查询出 product_id 不存在于子查询的 name/price
      SELECT name,price
      FROM product
      WHERE product_id NOT IN(
      SELECT product_id
      FROM shop_product
      WHERE shop_id = 'shop1'
      )
     */
    /*
      -- 查询是否存在 product 表是否存在于子查询
      SELECT * FROM product AS P
      WHERE EXISTS(
      SELECT * FROM shop AS SP
      WHERE SP.shop_id = 'shop1'
      AND
      SP.product_id = P.product_id
      )
     */
    /*
      -- 查询是否不存在 product 表是否不存在于子查询
      SELECT * FROM product AS P
      WHERE NOT EXISTS(
      SELECT * FROM shop AS SP
      WHERE SP.shop_id = 'shop1'
      AND
      SP.product_id = P.product_id
      )
     */

    /**
     * CASE 表达式
     * 1.CASE 表达式分为简单 CASE 表达式和搜索 CASE 表达式两种。搜索 CASE 表达式包含简单 CASE 表达式的全部功能
     * 2.虽然 CASE 表达式中的 ELSE 子句可以省略，但为了让 SQL 语句更加容易理解，建议不要省略
     * 3.CASE 表达式中的 END 不能省略
     * 4.使用CASE表达式能够将 SELECT 语句的结果进行组合
     * 5.虽然有些DBMS提供了各自特有的 CASE 表达式的简化函数，例如 Oracle 中的 DECODE 和 MySQL 中的 IF，等等，但由于它们并非通用的函数，
     *   功能上也有些限制，因此有些场合无法使用
     */
    /*
       -- 从 product 表中查询出三种类型的商品，并在类型前拼接相应的字母，以 new_product_type 返回
       SELECT name,
       CASE WHEN type = '衣服' THEN 'A:' || type
       CASE WHEN type = '鞋子' THEN 'B:' || type
       CASE WHEN type = '文具' THEN 'C:' || type
       ELSE NULL
       END AS new_product_type
       FROM product
     */
    /*
        -- 与上一条语句的结果相同
        SELECT name,
        CASE type
        WHEN '衣服' THEN 'A:' || type
        WHEN '鞋子' THEN 'B:' || type
        WHEN '文具' THEN 'C:' || type
        ELSE NULL
        END AS new_product_type
        FROM product
     */
    /*
       -- 查询出衣服、鞋子、文具的总价
       SELECT SUM(CASE WHEN type = '衣服' THEN price ELSE 0 END) AS cloth_price,
       SUM(CASE WHEN type = '鞋子' THEN price ELSE 0 END) AS shoes_price,
       SUM(CASE WHEN type = '文具' THEN price ELSE 0 END) AS stationery_price
       FROM product
     */


    /**
     * 表的加减法
     * 1.集合运算符会除去重复的记录
     * 2.作为运算对象的记录中列的类型、列数必须一致
     * 3.可以使用任何 SELECT 语句，但 ORDER BY 子句只能在最后使用一次
     * 4.在集合运算符中使用ALL选项，可以保留重复行
     */
    /*
       -- 查询两个表的 name 的合集
       SELECT name FROM product
       UNION
       SELECT name FROM product2
     */
    /*
       -- 查询两个表的 name,type,price 的合集,且以 type 排序
       SELECT name,type,price FROM product
       UNION
       SELECT name,type,price FROM product2
       ORDER BY type
    */
    /*
       -- 查询两个表的 name 的合集，包含重复行
       SELECT name FROM product
       UNION ALL
       SELECT name FROM product2
     */
    /*
       -- 查询两个表的 name 的交集
       SELECT name FROM product
       INTERSECT
       SELECT name FROM product2
     */
    /*
       -- 查询product表中减去存在 product2 表的name
       SELECT name FROM product
       EXCEPT
       SELECT name FROM product2
     */


    /**
     * 联结
     * 1.联结（JOIN）就是将其他表中的列添加过来，进行“添加列”的集合运算。UNION是以行（纵向）为单位进行操作，而联结则是以列（横向）为单位进行的
     * 2.联结大体上分为内联结和外联结两种。首先请大家牢牢掌握这两种联结的使用方法
     * 3.进行联结时需要在 FROM 子句中使用多张表
     * 4.进行内联结时必须使用 ON 子句，并且要书写在 FROM 和 WHERE 之间，WHERE 可以不用
     * 5.使用联结时 SELECT 子句中的列需要按照“<表的别名>.<列名>”的格式进行书写
     * 6.内联结只会查询出两表（多表）共同的 key，外联结会查询出两表（多表）所有的 key
     * 7.外联结还有一点非常重要，那就是要把哪张表作为主表。最终的结果中会包含主表内所有的数据。指定主表的关键字是 LEFT 和 RIGHT
     * 8.外联结中使用 LEFT、RIGHT 来指定主表。使用二者所得到的结果完全相同
     */

    /*
      -- 联合查询两个表的不同信息。这里可以不使用别名，但是原名太长，这里就使用了别名
      SELECT SP.shop_id,SP.shop_name,P.product_product_id,P.product_name,P.price
      FROM shopProduct AS SP
      INNER JOIN
      product AS P
      ON
      SP.product_id = P.product_id
     */
    /*
      -- 联合查询两个表的不同信息，且商店 id 为'商店A'
      SELECT SP.shop_id,SP.shop_name,P.product_product_id,P.product_name,P.price
      FROM shopProduct AS SP
      INNER JOIN
      product AS P
      ON
      SP.product_id = P.product_id
      WHERE SP.shop_id = '商店A'
     */
    /*
       -- 与上一条语句效果相同，这里只是多了 LEFT 关键字
       SELECT SP.shop_id,SP.shop_name,P.product_product_id,P.product_name,P.price
       FROM shopProduct AS SP
       LEFT INNER JOIN
       product AS P
       ON
       SP.product_id = P.product_id
       WHERE SP.shop_id = '商店A'
      */
     /*
       -- 查询两表的所有 SELECT 中的 key，不管某个表是否存在该 key（只要其中一个表存在，即会查出该行）
       SELECT SP.shop_id,SP.shop_name,P.product_product_id,P.product_name,P.price
       FROM shopProduct AS SP
       OUTER JOIN
       product AS P
       ON
       SP.product_id = P.product_id
       WHERE SP.shop_id = '商店A'
      */
     /*
      -- 三表查询
      SELECT SP.shop_id,SP.shop_name,P.product_product_id,P.product_name,P.price,IP.sale_price
      FROM shopProduct AS SP
      INNER JOIN
      product AS P
      ON
      SP.product_id = P.product_id
      INNER JOIN
      product2 AS IP
      ON SP.product_id = IP.product_id
      WHERE IP.sale_price = '300'
     */

    /**
     * 交叉联结
     * 1.交叉联结是对两张表中的全部记录进行交叉组合，因此结果中的记录数通常是两张表中行数的乘积
     */
    /*
      -- 查询学生的各科成绩
      SELECT ST.name,CR.score
      FROM
      student AS ST
      CROSS JOIN
      course AS CR
     */

    /**
     * 窗口函数
     * 1.窗口函数可以进行排序、生成序列号等一般的聚合函数无法实现的高级操作
     * 2.能够作为窗口函数的聚合函数（SUM、AVG、COUNT、MAX、MIN）
     * 3.RANK、DENSE_RANK、ROW_NUMBER 等专用窗口函数
     * 4.通过 PARTITION BY 分组后的记录集合称为窗口
     * 5.RANK 计算排序时，如果存在相同位次的记录，则会跳过之后的位次。比如前三记录一致时：1位、1位、1位、4位……
     * 6.DENSE_RANK 计算排序时，如果存在相同位次的记录，则不会跳过之后的位次。比如前三记录一致时：1位、1位、1位、2位……
     * 7.ROW_NUMBER 计算排序时，赋以唯一位次。比如前三记录一致时：1位、2位、3位、4位……
     * 8.由于专用窗口函数无需参数，因此通常括号中都是空的
     * 9.原则上窗口函数只能在 SELECT 子句中使用
     *
     */
    /*
       -- 根据不同的商品种类，按照销售单价从低到高的顺序创建排序表
       -- PARTITION BY 能够设定排序的对象范围
       -- PARTITION BY 在横向上对表进行分组，而 ORDER BY 决定了纵向排序的规则
       -- 也就是先把 type 进行分类，然后再在 type 组内进行价格排序
       SELECT name,type,price,
       RANK () OVER (PARTITION BY type ORDER BY price)
       AS ranking
       FROM product
     */
    /*
      -- 以 id 排序并计算总价 sum
      SELECT name,type,price,
      SUM(price) OVER(ORDER BY id) AS sum
      FROM product
     */
    /*
      -- 查询某一行+其前两行的平均值作为 avg
      SELECT name,type,price,
      AVG(price) OVER(ORDER BY id ROW 2 PRECEDING) AS avg
      FROM product
     */
    /*
      -- 查询某一行+其后两行的平均值作为 avg
      SELECT name,type,price,
      AVG(price) OVER(ORDER BY id ROW 2 FOLLOWING) AS avg
      FROM product
     */

    /**
     * GROUPING 运算符
     * 1.只使用 GROUP BY 子句和聚合函数是无法同时得出小计和合计的。如果想要同时得到，可以使用 GROUPING 运算符
     * 2.虽然 GROUPING 运算符是标准 SQL 的功能，但还是有些 DBMS 尚未支持这一功能
     * 3.ROLLUP 可以同时得出合计和小计，是非常方便的工具
     */
    /*
      -- 查询多一行"合计"
      -- 第一行查询所有类型的总价
      SELECT '合计' AS type,SUM(price) FROM product
      UNION ALL
      -- 这里计算各个类型各自的总价
      SELECT type,SUM(price) FROM product
      ORDER BY type
     */
    /*
      -- 这句的效果与上句基本相同，只是 sum 有值，其对应的 type 无值（因为合并后不知道是什么类型）
      SELECT type,SUM(price) AS sum
      FROM product GROUPING BY ROLLUP(type)
     */
    /*
      -- 这句会计算每个 type 的 price 汇总（这里的 sum 有对应的 type，因为同种类型合起来，还是同种类型）、所有 type 加起来的汇总（这汇总没有对应的 type）。
      SELECT type,SUM(price) AS sum
      FROM product GROUP BY ROLLUP(type，date)
     */
    /*
      -- 查询出因为 ROLLUP 产生的 NULL 则返回对应的 value 为1
      SELECT GROUPING(type),AS type,GROUPING(price) AS price,GROUPING(date) AS date
      FROM product
      GROUP BY ROLLUP(type,date)
     */
    /*
     -- 因为 ROLLUP 产生的 NULL 则填充某些v alue
     -- 把 ROLLUP 改为CUBE 则会多出以 date 作为 value（对应的 type 的 value 是'商品种类 合计'）的几行
     -- 把 ROLLUP 改为 GROUPING SET ，结果是本来的 type/date 行数加上以 date 作为 value（对应的 type 的 value 是'商品种类 合计'）的几行
     SELECT CASE WHEN GROUPING(type) = 1 THEN '商品种类 合计' ELSE type END AS type
            CASE WHEN GROUPING(date) = 1 THEN '等级日期 合计' ELSE date END AS date
     SUM(price) AS price
     FROM product
     GROUP BY ROLLUP(type,date)
     */

}