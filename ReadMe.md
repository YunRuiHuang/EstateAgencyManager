# ReadMe

**Author : yunrui huang**

**05/27/2022**

***

这份readme是 *Estate Agency Manager* 程序的说明文档，主要用于说明数据库的表格结构以及方法参数的结构

程序运行环境为：

IntelliJ IDEA 2021

Java1.8

MySQL connector java 8.0.29

***

## 数据库结构

| House                         | User                       | Manager                       |
| ----------------------------- | -------------------------- | ----------------------------- |
| (primary key) House_ID : int  | (primary key)User_ID : int | (primary key)manager_ID : int |
| City : Varchar(15)            | U_name : Varchar(10)       | M_name : Varchar(10)          |
| Address : Varchar(50)         | U_gender : Varchar(1)      | M_gender : Varchar(1)         |
| Total_price : int             | U_phone : Varchar(12)      | M_phone : Varchar(12)         |
| Size : int                    | U_email : Varchar(50)      | M_email : Varchar(50)         |
| Evaluation : Varchar(255)     |                            | Salary : int                  |
| (foreign key)User_ID : int    |                            |                               |
| (foreign key)Manager_ID : int |                            |                               |



## 方法参数

### 构造器

```java
public EstateAgencyManager(String user, String password, String url)
```



### 查询方法

```java
 public void queryToExcel(String fileName, ArrayList<String> selectList)
```

- selectList 参数设置为如下，如不需要着设置为**`“*”`**

| 0    | 1           | 2    | 3          | 4      | 5      |
| ---- | ----------- | ---- | ---------- | ------ | ------ |
| city | Total_Price | Size | Manager_ID | M_name | U_name |

### 

### 新增数据/修改数据

***

#### House

```java
public boolean addHouseData(ArrayList<String> data);
public boolean updateHouseData(ArrayList<String> data);    
```

* data 参数设置如下

| 0              | 1                  | 2                     | 3                 | 4          | 5                         |
| -------------- | ------------------ | --------------------- | ----------------- | ---------- | ------------------------- |
| House_ID : Int | City : Varchar(15) | Address : Varchar(50) | Total_price : int | Size : int | Evaluation : Varchar(255) |
| **6**          | **7**              |                       |                   |            |                           |
| User_ID : int  | Manager_ID : int   |                       |                   |            |                           |

***

#### User

```java
public boolean addUserData(ArrayList<String> data);
public boolean updateUserData(ArrayList<String> data);
```

- data 参数设置如下

| 0             | 1                    | 2                     | 3                     | 4                     |
| ------------- | -------------------- | --------------------- | --------------------- | --------------------- |
| User_ID : int | U_name : Varchar(10) | U_gender : Varchar(1) | U_phone : Varchar(12) | U_email : Varchar(50) |

***

#### Manager

```java
public boolean addManagerData(ArrayList<String> data);
public boolean updateManagerData(ArrayList<String> data);
```

- data 参数设置如下

| 0                | 1                    | 2                     | 3                     | 4                     | 5            |
| ---------------- | -------------------- | --------------------- | --------------------- | --------------------- | ------------ |
| Manager_ID : int | M_name : Varchar(10) | M_gender : Varchar(1) | M_phone : Varchar(12) | M_email : Varchar(50) | Salary : int |











