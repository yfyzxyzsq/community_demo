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