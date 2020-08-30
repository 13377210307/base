## 系统模块
============================**EasyExcel**==================================

参考文档：https://www.yuque.com/easyexcel/doc/easyexcel
#### 1：EsayExcel实现导入
**1：读**

一：步骤：

1：创建需要读取的对象

2：创建一个监听类并继承AnalysisEventListener类

3：重写invoke方法与doAfterAllAnalysed方法

二：详解：

1：对象类：

（1）：@ExcelProperty(index = 2)：该注解表示当前字段的位置为excel中的第三列
不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配

（2）：@ExcelProperty("字符串标题")：该注解表示用名字去匹配，如果名字重复，会导致只有一个字段读取到数据

2：监听类：

（1）：监听类不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去

（2）：每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收

（3）：invoke方法指每解析到一条数据都会调用一次，可以在此方法中将解析到的数据放入集合中便于新增，当数据
到了规定的size之后，如3000条，可以调用新增方法然后清理list中的数据

（4）：doAfterAllAnalysed方法指所有数据解析完成后进行调用，可以在此方法进行数据新增

3：监听器：

（1）
 EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet()
            // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
            .headRowNumber(1).doRead();




=============================**redis**====================================== 
#### 1：redis实现秒杀案例
思路：
使用string类型设置商品库存数量：key为：skill:productId(商品id):count value为：商品数量
使用set类型记录抢购成功用户：key为：skill:productId(商品id):userId value为：已抢购成功用户id

分为三种情况
1：秒杀商品不存在，活动未开始
2：用户已抢购成功，不可重复抢购，set特性：key值唯一
3：秒杀商品存在
    1：库存数量小于等于0，抢购失败
    2：库存数量大于0，减库存，往用户set中存放抢购用户id
