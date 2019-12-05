# Seckill

## Developers

15307110200

## Schedule

### Stage 1

Build a web application for online shopping that satisfies the basic requirements for the scenario

1. Decide the user cases.

2. Identify the entities, map to relational models and design the database schema.

3. Decide the interaction between the client and the server and formulate a REST api document.

4. Decide the behaviors of and interactions among the entities. Decide the methods of the entities and map the methods to operations on the database tables.

5. Code and test

6. Deploy the application on the server

### Stage 2

Find out the bottlenecks of the application and eliminate such bottlenecks by redesigning the architecture of the application to enable high scalability

1. Decompose the project into micro-services.

2. Design the data pipelines in the application. Established middleware platforms may be helpful.

## Analysis

### 执行者分析

1. 消费者
2. 商家

### 系统分析

1. 登录系统
2. 支付系统
3. 商品发布系统
4. 购物系统

### 用况分析

#### 消费者

1. 注册
    1. （通过发送邮件或短信的方式）进行注册
    
2. 下单
    1. 登录
    2. 浏览秒杀商品界面
        在秒杀开始之前会有倒计时和商品余量，秒杀开始时按钮颜色恢复，点击之后有一定概率成功添加到购物车中，商品售完之后余量为0，界面同样变灰
    3. 退出登录
    
3. 支付
    1. 登录
    2. 购物车
    购物车中有订单列表。
    选择订单，点击下单进行支付，账户余额充足情况下才能成功支付。三十分钟之内未成功支付则取消订单
    
#### 商家（只做一位）
    
1. 注册
    1. （通过发送邮件或短信的方式）进行注册

2. 发布商品
    1. 登录
    2. 发布商品
        设置发布商品的名称、图片、数量、价格等信息
    3. 登出

3. 查看商品销售情况
    1. 登录
    2. 查看所有商品的销售情况
    
### OOA

#### 实体类
```java
class User{
    long id;//primary key
    String username;//unique
    Account account;//foreign key
    long revenue;
};

class Consumer extends User{
};

class Seller extends User{
};

class Account{
    long id;//primary key
    long balance;
};

class Product{
    long id;
    String name;
    String description;
    long price;
    int count;
    Image image;//TODO: representation of images
};

class Order{
    long id;
    Consumer consumer;
    Product product;
    long timestamp;
    boolean payed;
};
```

### REST API

#### Authentication
```
//registration
method: POST
url:/authentication/register
parameters:
request body:
{
    'name':<string>,
    'role':<string>,
    'password':<string>,
}
response body:
{
    'isSucessful':<boolean>,
    'status code':<string>,
    'message':<string>
}

sql:
insert into role values(username, password.encrypt())


//sign in
method: POST
url: /authentication/sign_in
parameters:
request body:
{
    'name':<string>,
    'role':<string>,
    'password':<string>,
}
response body:
{
    'isSucessful':<boolean>,
    'status code':<string>,
    'message':<string>
}


//sign out
method: POST
url: /authentication/sign_out
parameters:
request body:
{
    'name':<string>,
    'role':<string>,
}
response body:
{
    'isSuccessful':<boolean>,
    'status code':<string>,
    'message':<string>
}

```

#### Seller
```

//release
//prerequisite: The seller has already logged in
method: POST
url: /seller/release
parameters:
request body:
{
    'data':{
        'name':<string>,
        'description':<string>,
        'price':<long>,//cent as the unit
        'count':<long>,
        'image':<string>//base64
    }
}

response body:
{
    'isSucessful':<boolean>
    'status code':<string>,
    'message':<string>
    'data':{
        'id':<long>
    }
}

//get information
method: GET
url: /seller/info
parameters: username<string>
request body:
response body:
{
    'user_id':<long>,
    'name':<string>,
    'revenue':<long>,
    'sales':[
        {
            'id':<long>,
            'name':<string>,
            'description':<string>,
            'price':<string>,
            'count':<string>,
            //TODO: 'image':<string>,
        },
    ]
}
```

#### Consumer
```
//get product infomation
//prerequisite: logged in already
method: GET
url: /consumer/product_info
parameters:
request body:
response body:
{
    'isSucessful':<boolean>,
    'status code':<string>,
    'message':<string>,
    'data':{
        num:<integer>,
        products:[
            {
                'id':<long>,
                'sellerName':<string>,
                'name':<string>,
                'description':<string>,
                'price':<string>,
                'count':<string>,
                //TODO: 'image':<string>,
            },
            //...
        ]
    }
}
sql:
select *
from product
where count > 0

//order
//prerequisite: logged in already
method: POST
url: /consumer/order
parameters: id
request body:

response body:{
    'isSuccessful':<boolean>,
    'statusCode':<string>,
    'message':<string>,
    'data':<Order>
}


//get cart information
//prerequisite: logged in
method: GET
url: /consumer/cart_info
parameters:
request body:
response body:

//pay

//get paid order information
```