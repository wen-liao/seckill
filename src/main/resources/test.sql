#购物系统
CREATE TABLE consumer(
	id INT AUTO_INCREMENT,
	name VARCHAR(30) UNIQUE NOT NULL,
	passwordDigest VARCHAR(70),
	bankAccount INT,

	PRIMARY KEY(id)/*,
	FOREIGN KEY (bankAccount) REFERENCES account(id)*/
);

CREATE TABLE seller(
	id INT AUTO_INCREMENT,
	name VARCHAR(30) UNIQUE NOT NULL,
	passwordDigest VARCHAR(70),
	bankAccount INT,

	PRIMARY KEY(id)/*,
	FOREIGN KEY(bankAccount) REFERENCES account(id)*/
);

CREATE TABLE product(
	id INT AUTO_INCREMENT,
	sellerId INT,
	name VARCHAR(30),
	description VARCHAR(200),
	count INT default 0,
	price DECIMAL(20, 2) DEFAULT 0,
	/*image BLOB, #65K at most*/

	PRIMARY KEY(id),
	FOREIGN KEY(sellerId) REFERENCES seller(id)
);

CREATE TABLE order_( #'order' is a key word
	id INT AUTO_INCREMENT,
	productId INT,
	consumerId INT,
	timestamp TIMESTAMP,
	paid SMALLINT, # 0 - unpaid, 1- paid

	PRIMARY KEY(id),
	FOREIGN KEY(productId) REFERENCES product(id),
	FOREIGN KEY(consumerId) REFERENCES consumer(id)
);

#交易系统, 假定不保存交易记录
CREATE TABLE account(
	id INT AUTO_INCREMENT, #account ID
	personId varchar(18), #ID number
	username VARCHAR(30) NOT NULL,
	balance DECIMAL(20,2) DEFAULT 0,
	PRIMARY KEY(id)
);