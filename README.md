# ygst_sob_login_demo
不知道有没有小伙伴，跟我一样，刚刚接触阳光沙滩就卡在了接入登陆API上的，这个项目主要是一个简单的登录api接入例子。

阳光沙滩的登录demo，没有其他的框架，代码是Java用到okHttp发送请求，使用Glide加载验证码的图片。

登陆主要是要在post的时候用到的三个参数，账号、密码、验证码，还有一个就是请求头参数l_c_i
![image](https://user-images.githubusercontent.com/13102787/149120278-7d2d546a-476f-409f-ae0a-643b25402c74.png)

其中要获取账号、密码、验证码这三个参数比较容易，主要的难点还是在于获取请求头参数l_c_i，其实也不难
首先，让我们先在Postman上面直接请求一下验证码api看看
![image](https://user-images.githubusercontent.com/13102787/149130555-3e71613f-e0d8-444a-8b09-150aa47dc7b0.png)
![image](https://user-images.githubusercontent.com/13102787/149130646-1c4a6a77-4d54-42d4-8da8-9200f65c6bb9.png)

这样就能够得到请求头参数l_c_i，非常容易，那么让我们回到Android端，来获取一下请求头参数l_c_i
