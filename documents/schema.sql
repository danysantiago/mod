CREATE TABLE `modstore`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(30) NOT NULL,
  `user_password` VARCHAR(32) NOT NULL,
  `first_name` VARCHAR(35) NOT NULL,
  `middle_name` VARCHAR(35) NULL,
  `last_name` VARCHAR(35) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `is_admin` TINYINT(1) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));

CREATE TABLE `modstore`.`address` (
  `id` INT(12) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `line1` VARCHAR(40) NOT NULL,
  `line2` VARCHAR(40) NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NULL,
  `country` VARCHAR(45) NOT NULL,
  `zipcode` VARCHAR(9) NOT NULL,
  `is_primary` VARCHAR(1) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_address_1_idx` (`user_id` ASC),
  CONSTRAINT ``
    FOREIGN KEY (`user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`seller_review` (
  `id` INT(12) NOT NULL AUTO_INCREMENT,
  `reviewer_user_id` INT(11) NOT NULL,
  `reviewee_user_id` INT(11) NOT NULL,
  `rate` INT(1) NOT NULL,
  `message` VARCHAR(255) NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_seller_review_1_idx` (`reviewer_user_id` ASC),
  INDEX `fk_seller_review_2_idx` (`reviewee_user_id` ASC),
  CONSTRAINT `fk_seller_review_1`
    FOREIGN KEY (`reviewer_user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_seller_review_2`
    FOREIGN KEY (`reviewee_user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`credit_card` (
  `id` INT(12) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `address_id` INT(12) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` TINYINT(1) NOT NULL,
  `number` VARCHAR(19) NOT NULL,
  `security_code` VARCHAR(4) NOT NULL,
  `expiration_date` DATE NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_new_table_1_idx` (`address_id` ASC),
  INDEX `fk_new_table_2_idx` (`user_id` ASC),
  CONSTRAINT `fk_new_table_1`
    FOREIGN KEY (`address_id`)
    REFERENCES `modstore`.`address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_new_table_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`order` (
  `id` INT(13) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `credit_card_id` INT(12) NOT NULL,
  `address_id` INT(12) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_order_1_idx` (`address_id` ASC),
  INDEX `fk_order_2_idx` (`credit_card_id` ASC),
  INDEX `fk_order_3_idx` (`user_id` ASC),
  CONSTRAINT `fk_order_1`
    FOREIGN KEY (`address_id`)
    REFERENCES `modstore`.`address` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_2`
    FOREIGN KEY (`credit_card_id`)
    REFERENCES `modstore`.`credit_card` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_3`
    FOREIGN KEY (`user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`category` (
  `id` INT(5) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `modstore`.`category_parent` (
  `child_category_id` INT(5) NOT NULL,
  `parent_category_id` INT(5) NOT NULL,
  PRIMARY KEY (`child_category_id`, `parent_category_id`),
  INDEX `fk_category_parent_2_idx` (`parent_category_id` ASC),
  CONSTRAINT `fk_category_parent_1`
    FOREIGN KEY (`child_category_id`)
    REFERENCES `modstore`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_category_parent_2`
    FOREIGN KEY (`parent_category_id`)
    REFERENCES `modstore`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`product` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `seller_user_id` INT(11) NOT NULL,
  `category_id` INT(5) NOT NULL,
  `description` TEXT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `brand` VARCHAR(100) NULL,
  `model` VARCHAR(45) NULL,
  `dimensions` VARCHAR(45) NULL,
  `buy_price` DECIMAL(8,2) NULL,
  `quantity` INT(6) NOT NULL,
  `starting_bid_price` DECIMAL(8,2) NULL,
  `auction_end_ts` VARCHAR(45) NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_product_1_idx` (`seller_user_id` ASC),
  INDEX `fk_product_2_idx` (`category_id` ASC),
  CONSTRAINT `fk_product_1`
    FOREIGN KEY (`seller_user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_2`
    FOREIGN KEY (`category_id`)
    REFERENCES `modstore`.`category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`order_detail` (
  `id` INT(14) NOT NULL AUTO_INCREMENT,
  `order_id` INT(13) NOT NULL,
  `product_id` INT(20) NOT NULL,
  `quantity` INT(6) NOT NULL,
  `final_price` DECIMAL(8,2) NOT NULL,
  `tracking_number` VARCHAR(255) NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_order_detail_1_idx` (`order_id` ASC),
  INDEX `fk_order_detail_2_idx` (`product_id` ASC),
  CONSTRAINT `fk_order_detail_1`
    FOREIGN KEY (`order_id`)
    REFERENCES `modstore`.`order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_detail_2`
    FOREIGN KEY (`product_id`)
    REFERENCES `modstore`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`bid` (
  `id` INT(30) NOT NULL,
  `bidder_user_id` INT(11) NOT NULL,
  `product_id` INT(20) NOT NULL,
  `bid_amount` DECIMAL(8,2) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_bid_1_idx` (`product_id` ASC),
  INDEX `fk_bid_2_idx` (`bidder_user_id` ASC),
  CONSTRAINT `fk_bid_1`
    FOREIGN KEY (`product_id`)
    REFERENCES `modstore`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bid_2`
    FOREIGN KEY (`bidder_user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`cart` (
  `id` INT(30) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `product_id` INT(20) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_cart_1_idx` (`user_id` ASC),
  INDEX `fk_cart_2_idx` (`product_id` ASC),
  CONSTRAINT `fk_cart_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `modstore`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_2`
    FOREIGN KEY (`product_id`)
    REFERENCES `modstore`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `modstore`.`product_image` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT(20) NOT NULL,
  `image_src` VARCHAR(255) NOT NULL,
  `created_ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_product_image_1_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_image_1`
    FOREIGN KEY (`product_id`)
    REFERENCES `modstore`.`product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);