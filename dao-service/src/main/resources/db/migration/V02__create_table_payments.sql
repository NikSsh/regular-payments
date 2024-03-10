CREATE TABLE IF NOT EXISTS regular_payments.payments
(
    id                      BIGSERIAL       NOT NULL,
    payer_full_name         VARCHAR(255)    NOT NULL,
    payer_inn               TEXT            NOT NULL,
    payer_card_number       TEXT            NOT NULL,
    recipient_account       VARCHAR(29)     NOT NULL,
    recipient_mfo           VARCHAR(6)      NOT NULL,
    recipient_okpo          VARCHAR(8)      NOT NULL,
    recipient_name          VARCHAR(160)    NOT NULL,
    amount                  DECIMAL(10, 2)  NOT NULL,
    withdrawal_period       NUMERIC(21,0)   NOT NULL,
    created_at              TIMESTAMP       NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX payment_inn_idx ON regular_payments.payments(payer_inn);

CREATE INDEX payment_okpo_idx ON regular_payments.payments(recipient_okpo);