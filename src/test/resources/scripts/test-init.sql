/* === CREATE TABLES === */
CREATE TABLE IF NOT EXISTS users (
  id            BIGINT PRIMARY KEY NOT NULL,
  reference     VARCHAR(15)        NOT NULL,
  idate         TIMESTAMP          NOT NULL,
  user_name     VARCHAR(20)        NOT NULL,
  mobile_number VARCHAR(20)        NOT NULL,
  email         VARCHAR(30)        NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
  id        BIGINT PRIMARY KEY NOT NULL,
  reference VARCHAR(15)        NOT NULL,
  idate     TIMESTAMP          NOT NULL,
  balance   DECIMAL(20, 2)     NOT NULL,
  currency  VARCHAR(3)         NOT NULL,
  user_id   BIGINT             NOT NULL,
  FOREIGN KEY (user_id) references users (id)
);

CREATE TABLE IF NOT EXISTS transfers (
  id                  BIGINT PRIMARY KEY  NOT NULL,
  reference           VARCHAR(15)         NOT NULL,
  idate               TIMESTAMP           NOT NULL,
  from_amount         DECIMAL(20, 2)      NOT NULL,
  to_amount           DECIMAL(20, 2)      NOT NULL,
  conversion_unit     VARCHAR(15)         NOT NULL,
  exchange_rate       DECIMAL(20, 2)      NOT NULL,
  receiver_account_id BIGINT              NOT NULL,
  sender_account_id   BIGINT              NOT NULL,
  FOREIGN KEY (receiver_account_id) references accounts (id),
  FOREIGN KEY (sender_account_id) references accounts (id)
);

/* === CREATE INDEXES === */
CREATE INDEX IX_users_reference
  ON users (reference);
CREATE INDEX IX_users_user_name
  ON users (user_name);
CREATE INDEX IX_users_mobile_number
  ON users (mobile_number);

CREATE INDEX IX_accounts_reference
  ON accounts (reference);
CREATE INDEX IX_accounts_user
  ON accounts (user_id);

CREATE INDEX IX_transfers_reference
  ON transfers (reference);
CREATE INDEX IX_transfers_receiver
  ON transfers (receiver_account_id);
CREATE INDEX IX_transfers_sender
  ON transfers (sender_account_id);

/* === INSERT INITIAL DATA === */
INSERT INTO users VALUES
  (1, 'PBXDGMH02XGOGIF', NOW(), 'nikolay', '+442033228352', 'email@revolut.com'),
  (2, 'PVYJMUZDHY7OICK', NOW(), 'taavet', '+442036950999', 'email@transferwise.com'),
  (3, '0E29V4RBTGKCF4S', NOW(), 'tom', '+442038720620', 'email@monzo.com'),
  (4, 'G6KTYQTVG9MMWUQ', NOW(), 'valentin', '+4930364286880', 'revolut@n26.com');

INSERT INTO accounts VALUES
  (1, 'X2QCL6BNB7ZL49K', NOW(), 300, 'EUR', 1),
  (2, 'CHQ7DAURKL1O8FB', NOW(), 200, 'GBP', 1),
  (3, '820JRQSJ7KNWHKZ', NOW(), 600, 'TRY', 2),
  (4, '1Q50NS12D0DP348', NOW(), 500, 'EUR', 2),
  (5, 'IV4EFMREFLIIIQO', NOW(), 100, 'GBP', 2),
  (6, 'F62QHCMPLHFOHID', NOW(), 825, 'EUR', 3),
  (7, 'M5VHFXTZ8BC3TOO', NOW(), 10, 'TRY', 3),
  (8, 'EGHD4TKYCVGFXNO', NOW(), 1100, 'GBP', 4),
  (9, 'BZKQIOR4F20TRG7', NOW(), 50, 'EUR', 4),
  (10, '8K3PK6VU8D232PE', NOW(), 600, 'TRY', 4);

INSERT INTO transfers VALUES
  (1, 'N8TNO6BMB7KL49K', NOW(), 382.80, 344.52, 'EUR_GBP', 0.9, 2, 6);