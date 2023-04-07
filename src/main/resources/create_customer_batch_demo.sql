CREATE TABLE IF NOT EXISTS `customer_batch_demo`
(
    `id`      int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name`    varchar(50)      NOT NULL,
    `address` varchar(255)     NOT NULL,
    primary key (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

insert into customer_batch_demo(name, address)
values ('a', 'address'),
       ('b', 'bddress'),
       ('c', 'cddress'),
       ('e', 'eddress'),
       ('f', 'fddress');
