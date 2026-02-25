	-- Billing tables
	CREATE TABLE IF NOT EXISTS billing (
		bill_id BIGSERIAL PRIMARY KEY,
		invoice_number VARCHAR(50),
		customer_name VARCHAR(255),
		customer_phone VARCHAR(20),
		patient_gender VARCHAR(10),
		patient_age VARCHAR(10),
		doctor_name VARCHAR(255),
		referred_by VARCHAR(255),
		amount DOUBLE PRECISION,
		item_count INT,
		date VARCHAR(50),
		store_mobile VARCHAR(20),
		store_id BIGINT,
		email VARCHAR(255)
	);

	CREATE TABLE IF NOT EXISTS billing_line_item (
		id BIGSERIAL PRIMARY KEY,
		bill_id BIGINT REFERENCES billing(bill_id),
		medicine_name VARCHAR(255),
		brand VARCHAR(255),
		composition VARCHAR(255),
		batch VARCHAR(50),
		qty INT,
		unit_price DOUBLE PRECISION,
		total DOUBLE PRECISION
	);
	-- Removed stray value tuples that caused SQL syntax error
-- More sample medicines
INSERT INTO medicine_list (name, brand, composition, category, batch, expiry, quantity, price, store_mobile, store_id, email)
VALUES
	('Amoxycillin', 'Abbott', 'Amoxycillin 500mg', 'Antibiotic', 'BATCH004', '2027-08-10', 75, 15.00, '9632587486', 7, 'store7@gmail.com'),
	('Metformin', 'Sun Pharma', 'Metformin 500mg', 'Diabetic', 'BATCH005', '2028-01-20', 120, 18.50, '9632587487', 8, 'store8@gmail.com'),
	('Aspirin', 'Bayer', 'Aspirin 100mg', 'Cardiac', 'BATCH006', '2027-11-30', 60, 20.00, '9632587488', 9, 'store9@gmail.com'),
	('Ibuprofen', 'Cipla', 'Ibuprofen 400mg', 'Painkiller', 'BATCH007', '2027-03-15', 90, 14.00, '9632587489', 10, 'store10@gmail.com');
ALTER TABLE IF EXISTS medical_store_list ADD COLUMN IF NOT EXISTS role VARCHAR(50);
ALTER TABLE IF EXISTS medical_store_list ADD COLUMN IF NOT EXISTS gstin_number VARCHAR(100);
ALTER TABLE IF EXISTS medical_store_list ADD COLUMN IF NOT EXISTS pharmacy_code VARCHAR(100);
ALTER TABLE IF EXISTS medical_store_list ADD COLUMN IF NOT EXISTS address VARCHAR(500);
ALTER TABLE IF EXISTS medical_store_list ADD COLUMN IF NOT EXISTS store_mobile VARCHAR(20);
UPDATE medical_store_list
SET store_mobile = mobile
WHERE store_mobile IS NULL AND mobile IS NOT NULL;
UPDATE medical_store_list
SET mobile = store_mobile
WHERE mobile IS NULL AND store_mobile IS NOT NULL;
ALTER TABLE IF EXISTS medical_store_list ALTER COLUMN mobile DROP NOT NULL;
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS vendor_id BIGINT;
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS store_id BIGINT;
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS mobile VARCHAR(20);
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS store_mobile VARCHAR(20);
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS email VARCHAR(255);
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS spent NUMERIC(12,2);
ALTER TABLE IF EXISTS customer_list ADD COLUMN IF NOT EXISTS visited DATE;
UPDATE customer_list
SET store_id = vendor_id
WHERE store_id IS NULL AND vendor_id IS NOT NULL;
UPDATE customer_list
SET store_mobile = mobile
WHERE store_mobile IS NULL AND mobile IS NOT NULL;

CREATE TABLE IF NOT EXISTS medicine_list (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	brand VARCHAR(255) NOT NULL,
	composition VARCHAR(500) NOT NULL,
	category VARCHAR(255) NOT NULL,
	batch VARCHAR(255) NOT NULL,
	expiry DATE NOT NULL,
	quantity INT NOT NULL,
	price NUMERIC(12,2) NOT NULL,
	store_mobile VARCHAR(20) NOT NULL,
	store_id BIGINT NOT NULL,
	email VARCHAR(255) NOT NULL
);

-- Sample medicine data for testing
INSERT INTO medicine_list (name, brand, composition, category, batch, expiry, quantity, price, store_mobile, store_id, email)
VALUES
  ('Dolo 650', 'Micro Labs', 'Paracetamol 650mg', 'Allopathic', 'BATCH001', '2027-02-25', 100, 12.50, '9632587485', 6, 'test125@gmail.com'),
  ('Crocin Advance', 'GSK', 'Paracetamol 500mg', 'Allopathic', 'BATCH002', '2026-12-31', 50, 10.00, '9632587485', 6, 'test125@gmail.com'),
  ('Cetrizine', 'Dr. Reddy''s', 'Cetirizine 10mg', 'Allopathic', 'BATCH003', '2027-05-15', 200, 8.00, '9632587485', 6, 'test125@gmail.com');

ALTER TABLE IF EXISTS medicine_list ADD COLUMN IF NOT EXISTS store_id BIGINT;
ALTER TABLE IF EXISTS medicine_list ADD COLUMN IF NOT EXISTS store_mobile VARCHAR(20);
ALTER TABLE IF EXISTS medicine_list ADD COLUMN IF NOT EXISTS email VARCHAR(255);
