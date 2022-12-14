# 单点登录（sso）



单点登录（Single Sign On），简称为 SSO，是比较流行的企业业务整合的解决方案之一。SSO的定义是在多个应用系统中，用户只需要登录一次就可以访问所有相互信任的应用系统。



## 实现单点登录的三种方式

### Cookies方式实现

Cookie通常也叫做网站cookie,浏览器cookie或者httpcookie,是保存在用户浏览器端的，并在发出http请求时会默认携带的一段文本片段。它可以用来做用户认证，服务器校验等通过文本数据可以处理的问题。



1、用户在任意系统中登录成功后，将登录信息存放到redis，其key值是生成的唯一值，value值是登录信息。



2、将key值（或者key加密后的值）存入该用户的cookie 中，用户每次访问都带着这个cookie。



3、用户在访问其他模块发送请求时，都会带着客户端的 cookie 进行请求，其他模块在处理用户的请求时，先获取用户 cookie中的 key 值（或者将其解密后得到key值），然后拿着这个 key 值到 redis 中进行查询，即可得到用户的登录信息。



### Session方式实现

Session：在计算机中，尤其是在网络应用中，称为“会话控制”。session对象存储特定用户会话所需的属性及配置信息。这样，当用户在应用程序的Web页之间跳转时，存储在Session对象中的变量将不会丢失，而是在整个用户会话中一直存在下去。当用户请求来自应用程序的 Web页时，如果该用户还没有会话，则Web服务器将自动创建一个 Session对象。当会话过期或被放弃后，服务器将终止该会话。Session 对象最常见的一个用法就是存储用户的首选项。例如，如果用户指明不喜欢查看图形，就可以将该信息存储在Session对象中。有关使用Session 对象的详细信息，请参阅“ASP应用程序”部分的“管理会话”。注意会话状态仅在支持cookie的浏览器中保留。



1、在用户登录了一个模块后，这个模块的服务器会将用户的登录信息写入**分布式session**中。



2、将session存入该用户的cookie 中，用户每次访问都带着这个cookie。



3、用户带着cookie在请求其他模块时，通过 cookie中的sessionId从**分布式Session**中获取session。



#### 分布式Session

##### （1）session复制

早期系统使用的一种服务器集群Session管理机制。应用服务器开启Web 容器的session复制功能，在集群中的几台服务器之间同步session对象，使得每台服务器上都保存所有用户的session信息,这样任何一台机器宕机都不会导致 session数据丢失，而服务器使用session时，也只需在本机获取。

缺点：每台服务器都有备份，集群规模较大时复制浪费大量网络资源，用户量多时会造成内存不够session使用。

##### （2）基于redis存储session方案

将session存储在redis中，每次直接从redis获取session。



### Token方式实现(推荐)

json web token（JWT）是一个开放标准（rfc7519），它定义了一种紧凑的、自包含的方式，用于在各方之间以JSON对象安全地传输信息。它是以JSON形式作为Web应用中的令牌,用于在各方之间安全地将信息作为JSON对象传输。在数据传输过程中还可以完成数据加密、签名等相关处理。

#### jwt的组成部分

 标准的jwt令牌分为三部分，分别是Header、payload、signature；

##### （1）Header

它的组成部分包括两点

```json
{
    "typ":"JWT", 	//参数类型
    "alg":"HS256"	//签名算法
}
```



##### （2）Payload

其实就是自定义的数据，一般存储用户Id，用户名、过期时间等信息。也就是JWT的核心所在，因为这些数据就是使后端知道此token是哪个用户已经登录的凭证。而且这些数据是存在token里面的，由前端携带，所以后端几乎不需要保存任何数据。

```json
{
    "userId": 10001,
    "userName": "root",
    "exp":1626159839
}
```



##### （3）Signature

 它是由3个部分组成，先是用 Base64 编码的 header 和 payload ，再用加密算法加密一下，加密的时候要放进去一个 secret ，这个相当于是一个秘钥，这个秘钥秘密地存储在服务端。

**secret密钥是保存在服务器端的，jwt的签发生成也是在服务器端的，secret密钥就是用来进行jwt的签发和jwt的验证。所以，secret密钥就是你服务端的私钥，在任何场景都不应该泄露出去。一旦非法客户端得知这个secret密钥, 那就意味着非法客户端可以自我签发jwt了。**

**token具体实现：**

```json
{
    "msg": "登录成功",
    "code": 200,	       
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzc28iLCJ1c2VySWQiOjEsImV4cCI6MTY3MTAwNzA1MX0.NiOYhBnzOD5VJX1XKsYsfikm3RUglGmRKJ6PtHf2YLU"
}
```



1、在用户登录了一个模块后，将登陆信息按照jwt规则生成token，将token返回给用户。

2、用户将token存储在自己的请求头之中或者url后面，访问其他模块时，可以通过token解析出登录信息。



**存在问题1：**用户退出登录时，token依旧可以解析。

**解决办法：**

（1）将列出一个token黑名单存到redis中，把退出登录的token拉入黑名单，每次请求都要查看token是否处于黑名单中，如果处于黑名单中则拒绝访问。

（2）每次登录成功后将token存到redis中，每次请求都要查看redis中是否存在token，如果不存在则拒绝访问。（比较推荐）



**存在问题2：**重复登录

**解决办法：**

生成token后，将userId（或用户唯一标识）生成一个key值，将token存储到redis中。

再次登录时，先通过token解析出userId（或用户唯一标识）生成key值，去redis查找是否存在token，再根据限制用户同时在线多少判断是否能登录。

请求其他模块时，也是先通过token解析出userId（或用户唯一标识）生成key值，去redis查找是否存在token。



**项目地址**(springboot+jwt+redis)：https://github.com/shenshanshi/sso











