CREATE TABLE IF NOT EXISTS regular_payments.payment_withdrawals
(
    id                      BIGSERIAL       NOT NULL,
    payment_id              BIGINT          NOT NULL,
    amount                  DECIMAL(10, 2)  NOT NULL CHECK(amount > 0),
    created_at              TIMESTAMP       NOT NULL,
    status                  VARCHAR(1) CHECK (status IN ('A', 'S')) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT payment_withdrawals_payment_id_fk
        FOREIGN KEY (payment_id) REFERENCES regular_payments.payments (id)
            ON DELETE CASCADE
);

CREATE INDEX payment_id_idx ON regular_payments.payment_withdrawals(payment_id);