# 仿·QQ
基于Java socket编程的仿QQ通讯软件(服务器端+客户端)，具有用户登录、注册、文字聊天、文件传输、NAT穿透等功能。客户端使用JavaFX编写。

此项目为我计算机网络实验课项目，节选部分报告如下：

---

## 注意事项

1. 运行客户端时需要安装Oracle JDK/JRE1.8或者安装了OpenJFX的JDK/JRE
2. 使用maven生成包含依赖的可执行客户端jar包: 在命令行中输入`mvn clean install`，从命令行输入` java -jar xxx.jar`执行（当然也可以打包为对应操作系统的可执行文件）
3. 使用IDEA等IDE将服务器端打包为jar包部署到服务器，使用` java -jar xxx.jar`执行

### 实验设计

* 使用Java socket这一API进行编程

* 传输协议为TCP

* 软件设计模式为C/S模式

* 传输内容包括在TCP数据包的Data中

  ![image-20200522215906047](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522215906047.png)

* 为了便于传输，采用小量多次的策略：对于可以一次传输的内容，例如注册、登录、文字聊天等信息，一次进行传输；对于较大的数据，例如文件传输，以1024字节为单位传输(Java socket具备丢包重传功能)

* 除文件传输外，其他内容的传输均使用InfoUser类，使用jackson将该类转为json后进行传输，收到数据包后将其中的data部分再利用jaskon转回Java类读取数据

  ![image-20200522165306881](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522165520327.png)

  *InfoUser类中的字段*

  ![image-20200522165348251](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522170327253.png)

  *发送数据包的方法，发送和接受均要指定编码为UTF-8以保证汉字传输正确*

  ![image-20200522165520327](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522165306881.png)

  *发送数据包的实例*

  数据包中有两个字段infoType和chatInfo,infoType标识该数据包的类型，chatInfo提供状态信息(聊天时也用于传输文字消息)。其中的消息类型由两个枚举类确定

* 对于服务器端，一个socket由一个JsonProcessService线程处理，其完成接收数据包、处理、发送数据包的过程；对于客户端，由于交互需要，创建一个线程接收和发送数据包并将其保存在两个列表中以便其他线程调用。

* 典型的状态转换过程是客户端中聊天时收到文件的实现：在聊天页面的控制器(Java FX编程的约定，前端页面由一个FXML文件确定，响应由一个Controller确定，两者绑定)中新建一个后台线程，当监测到有文件传输的数据包时调用页面所在线程打开接收文件的页面：

  ![image-20200522170327253](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522170511212.png)

  *后台线程*

  ![image-20200522170346587](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522172712393.png)

  *打开新页面*

* 创建一个页面用于错误处理，遇到错误（例如密码错误等）新建该页面并将错误信息传入:

  ![image-20200522170511212](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522172641868.png)

* 用户交互流程:

  ### 用户注册

  1. 输入用户名(不可重复，重复会弹出窗口提示重新输入)、密码、邮箱
  2. 从邮箱中接收验证码输入

  ### 用户登录

  1. 输入用户名和密码
  2. 可以点击下方“添加好友”，输入用户名即可添加
  3. 点击“刷新好友列表”可以刷新好友数量和在线状态
  4. 不在线的好友不允许聊天（会弹出警告窗口）
  5. 点击在线好友进行聊天，可以发送文件
  6. 聊天中收到文件会弹出接收窗口

### UI设计

以Java FX原生控件为主，对一部分控件加了CSS:

<img src="https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522101841404.png" alt="image-20200522172641868" style="zoom:50%;" />

*部分CSS*

<img src="https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522173103549.png" alt="image-20200522172712393" style="zoom:50%;" />

*主界面*

### 框架设计

#### 数据库

```sql
use QQ;
set names utf8;
drop table if exists user;
create table user
(
    uid int not null auto_increment,
    username varchar(100)not null,
    password varchar(100)not null,
    email varchar(100),
    code varchar(50),
    isInUse char(1),
    primary key(uid),
    unique key AK_nq_username(username),
    unique key AK_nq_code(code)
);
drop table if exists friends;
create table friends
(
    myUsername varchar(100),
    friendUsername varchar(100)
);
```

#### 服务器端

项目结构如下:

<img src="https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522173720532.png" alt="image-20200522101841404" style="zoom:50%;" />

对各项进行讲解。其中用到了多线程编程的思想，我会将相关内容加粗。

* mybatis-config.xml:连接数据库的配置文件

* log4j.properties:日志配置文件

* Main：启动类

  <img src="https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522102629947.png" alt="image-20200522173720532" style="zoom:67%;" />

* MailUtils:发送邮件，**静态方法(java在执行静态方法时，会在内存中拷贝一份,所以是适用于高并发情况的)**

* PackageSender:传入socket和InfoUser,发送数据包，**静态方法**

* UserSocketUtils:管理socket，在map中以username:socket的形式保存socket.**为了在高并发环境中保证线程安全，使用并发包下的CocurrentHashMap**。同时在类中定义了一个线程用来定时对Map中所有socket发送心跳包

* UuidUtil:生成注册时的验证码,**静态方法**

* domain包下为使用Mabatis连接数据库、封装socket传输数据时所用到的实体类

* dao包下为查询数据库的接口，使用Mabatis数据库注解开发

  ![image-20200522173103549](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522215951198.png)

* UserServiceImpl:与数据库相关的操作

* SocksetService:监听端口，收到新的socket之后就创建一个JsonProcessService线程处理该socket,**这保证了并发环境下数据的及时处理**

* JsonProcessService:负责对一个socket的读写，解析收到的Json格式的数据，根据类型不同进行不同的操作，将输出数据封装为InfoUser格式再变为Json发送

#### 客户端

项目结构如下:

<img src="https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522165348251.png" alt="image-20200522102629947" style="zoom:50%;" />

* AppMain是程序的启动类，Main是首页的控制器类
* fxml文件对应的是页面布局
* 每个页面布局与一个Controller对应
* SocketService进行数据包的接收和发送
* PackageList中定义了两个队列，分别用于存放接收到的和要发送的数据包，**静态方法字段**

### 关键功能

* 对于注册，数据库中用户表中有一项是是否通过验证码激活，未激活的用户不允许登录

* 对于文字聊天，直接将文字内容放入chatInfo字段中，注意**在将String转为byte时必须指定使用统一的编码，否则汉字会出现乱码问题**

* 对于文件传输，因为无法很好地将自主设计的传输类InfoUser和文件字节流相结合，所以选择了直接传输字节流。在传输前先传输一个数据包，表示要进入到文件传输状态，同时说明文件名和文件大小

* 对于NAT穿透，参考我之前写的一篇博客文章：https://www.cnblogs.com/jiading/p/12029450.html 。通过客户端和服务器端双方定时发送心跳包来实现。这里要注意发送和接收文件的时候要暂停心跳包的发送，一是因为没有必要，二是因为文件传输的格式（纯字节流）和其他包（Json格式）不一样，会影响文件的传输

  ![image-20200522215951198](https://gitee.com/jiadingMayun/Pic/raw/master/img/image-20200522170346587.png)

  *心跳包实例*