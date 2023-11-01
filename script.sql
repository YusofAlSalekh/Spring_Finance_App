create table client
(
    id       SERIAL PRIMARY KEY,
    email    varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL
);

create table account
(
    id        SERIAL PRIMARY KEY,
    name      varchar(255) NOT NULL,
    balance   integer      NOT NULL,
    client_id int references client (id),
    unique (name, client_id)
);


create table category
(
    id        SERIAL PRIMARY KEY,
    name      varchar(255) NOT NULL,
    client_id int references client (id),
    unique (name, client_id)
);


create table transaction
(
    id                  SERIAL PRIMARY KEY,
    created_date        timestamp      NOT NULL,
    amount              decimal(10, 2) NOT NULL,
    sender_account_id   int references account (id),
    receiver_account_id int references account (id)
);

create table transaction_to_category
(
    transaction_id int references transaction (id),
    category_id    int references category (id)
);

select name, balance
from account
WHERE client_id = your_client_id;

select t.created_date as transaction_created_date,
       t.amount,
       a.name,
       c.email
from transaction as t
         join account as a on t.sender_account_id = a.id
         join client as c on a.client_id = c.id
where c.id = your_client_id
  and date_trunc('day', t.created_date) = current_date - interval '1 day';

select c.email,
       sum(a.balance) as total_balance
from account as a
         join client as c
              on c.id = a.client_id
group by c.id;

