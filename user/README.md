## 用户模块
#### 1：使用redis统计以及记录用户登录数量
思路：

使用String类型存储某天登录用户数量：key值： "login:count:"+today

使用set类型储存某天登录用户id：key值："login:user:"+today，防止重复

在用户登录时将数据储存到redis中，用户已存在，不做任何操作，用户不存在，判断redis中是否存在当日统计数量，
不存在进行新增key值并初始化为0，存在则加一


