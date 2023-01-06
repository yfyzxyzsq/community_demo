### Spring MVC
- 三层架构：表现层、业务层、数据访问层
- MVC: Model/View/Controller
- 核心组件：前端控制器--DispatcherServlet

### MyBatis
- 核心组件
    - SqlSessionFactory:用于创建SqlSession的工厂类
    - SqlSession:MyBatis的核心组件，用于向数据库执行SQL
    - 主配置文件:XML配置文件，可以对MyBatis的底层行为做出详细的配置
    - Mapper接口:DAO接口，在MyBatis中习惯称之为Mapper
    - Mapper映射器:用于编写SQL，并将SQL和实体类映射的组件，采用XML、注解均可实现
  
### 事务管理

#### 第一类丢失更新
某一个事务的回滚导致另一个事务已更新的数据丢失

#### 第二类丢失更新
某一个事务的提交导致另一个事务已更新的数据丢失

#### 脏读
一个事务读取到另一个事务未提交的事务

#### 不可重复读
一个事务对同一个数据前后读取的结果不一致

#### 幻读
一个事务对同一个表前后查询到的行数不一致

隔离级别                第一类丢失更新 | 脏读 | 第二类丢失更新 | 不可重复读 | 幻读
Read Uncommitted          Y           Y        Y            Y        Y 
Read Commited             N           N        Y            Y        Y
Repeatable Read           N           N        N            N        Y 
Serializable              N           N        N            N        N

#### 事务传播机制 !!!

### spring事务管理

#### 声明式事务(xml,annotation)

#### 编程式事务(TransactionTemplate)