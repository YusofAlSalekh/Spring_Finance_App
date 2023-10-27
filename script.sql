create table user
(
    id       SERIAL PRIMARY KEY,
    mail     varchar(255) NOT NULL,
    password varchar(255) not null
);

create table account
(
    id      SERIAL PRIMARY KEY,
    name    varchar(255) NOT NULL,
    balance decimal(10, 2),
    user_id int references user (id)
);


create table category
(
    id      SERIAL PRIMARY KEY,
    name    varchar(255) NOT NULL,
    user_id int references user (id)
);


create table transaction
(
    id                  SERIAL PRIMARY KEY,
    datetime            timestamp,
    amount              decimal,
    sender_account_id   int references account (id),
    receiver_account_id int references account (id)
);

create table transaction_to_category
(
    transaction_id int references transaction (id),
    category_id    int references category (id)
);

select u.mail,
       a.name
from account as a
         join user as u on u.id = a.user_id
WHERE u.id = user_id_of_interest;

select t.datetime as transaction_datetime,
       t.amount,
       a.name,
       u.mail
from transaction as t
         join account as a on t.sender_account_id = a.id
         join user as u on a.user_id = u.id
where u.id = user_id_of_interest
  and date_trunc('day', t.datetime) = current_date - interval '1 day';

select u.mail,
       sum(a.balance) as total_balance
from account as a
         join user as u on u.id = a.user_id
group by u.mail;

