drop
database OO;
create
database OO;
use
OO;
CREATE TABLE `user`
(
    `id`       varchar(20) NOT NULL DEFAULT '',
    `password` varchar(20) NOT NULL DEFAULT '',
    `ip`       varchar(255)         DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;
CREATE TABLE `house`
(
    `id`       int(11) NOT NULL,
    `name`     varchar(20) NOT NULL DEFAULT '',
    `password` varchar(20) NOT NULL DEFAULT '',
    `ip`       varchar(255)         DEFAULT '',
    `host`     varchar(20) NOT NULL DEFAULT '',
    CONSTRAINT fk_host FOREIGN KEY (`host`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;
CREATE TABLE `user_house`
(
    `user_id`  varchar(20) NOT NULL DEFAULT '',
    `house_id` int(11) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_house FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;
CREATE TABLE `user_house_message`
(
    `user_id`  varchar(20)  NOT NULL DEFAULT '',
    `house_id` int(11) NOT NULL,
    `message`  varchar(200) NOT NULL DEFAULT '',
    `dateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_message FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_house_message FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;
