CREATE TABLE advertisement_payments (
    advertisement_id BIGINT,
    pay_id BIGINT,
    CONSTRAINT advertisement_payments_pkey PRIMARY KEY (advertisement_id, pay_id),
    CONSTRAINT advertisement_payments_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id),
    CONSTRAINT advertisement_payments_fk_pays FOREIGN KEY (pay_id) REFERENCES pays(id)
);

CREATE TABLE advertisement_deliveries (
    advertisement_id BIGINT,
    delivery_id BIGINT,
    CONSTRAINT advertisement_deliveries_pkey PRIMARY KEY (advertisement_id, delivery_id),
    CONSTRAINT advertisement_deliveries_fk_advertisements FOREIGN KEY (advertisement_id) REFERENCES advertisements(id),
    CONSTRAINT advertisement_deliveries_fk_deliveries FOREIGN KEY (delivery_id) REFERENCES deliveries(id)
);