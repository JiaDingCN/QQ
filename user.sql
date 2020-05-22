use QQ;
set names utf8;
/*
        * ID
        * 用户名
        * 密码(AES_ENCRYPT加密)
        * 邮箱
        * 激活码
        * 是否已激活
        */
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