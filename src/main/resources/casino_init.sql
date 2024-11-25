CREATE TABLE IF NOT EXISTS finance_result
(
    income  numeric(9, 2),
    outcome numeric(9, 2)
);
CREATE TABLE IF NOT EXISTS game
(
    id         serial primary key not null,
    first_sym  varchar(1),
    second_sym varchar(1),
    third_sym  varchar(1)
);