-- Insert roles
INSERT INTO roles (role_id, role_name, description) VALUES (1, 'Admin', 'Quản trị hệ thống');
INSERT INTO roles (role_id, role_name, description) VALUES (2, 'Store Manager', 'Quản lý lưu trữ');
INSERT INTO roles (role_id, role_name, description) VALUES (3, 'Warehouse Staff', 'Nhân viên kho');
INSERT INTO roles (role_id, role_name, description) VALUES (4, 'Purchasing Staff', 'Nhân viên mua hàng');
INSERT INTO roles (role_id, role_name, description) VALUES (5, 'Sales Staff', 'Nhân viên bán hàng');

-- Insert accounts
INSERT INTO accounts (account_id, username, password, full_name, email, phone, role_id, profile_image) VALUES (1, 'admin', 'hashed_password', 'Nguyễn Văn A', 'admin@example.com', '0909123456', 1, 'admin.png');
INSERT INTO accounts (account_id, username, password, full_name, email, phone, role_id, profile_image) VALUES (2, 'manager1', 'hashed_password', 'Trần Thị B', 'manager@example.com', '0912345678', 2, 'manager.png');
INSERT INTO accounts (account_id, username, password, full_name, email, phone, role_id, profile_image) VALUES (3, 'sales1', 'hashed_password', 'Lê Văn C', 'sales@example.com', '0934567890', 5, 'sales.png');

-- Insert suppliers
INSERT INTO suppliers (supplier_id, name, phone, email, address, tax_id, contact_person, notes) VALUES (1, 'Nhà cung cấp A', '0987654321', 'supA@example.com', '123 Trần Hưng Đạo', 'MST12345', 'Nguyễn Thị D', 'Giao hàng đúng hẹn');

-- Insert products
INSERT INTO products (product_id, product_code, name, description, size, color, material, unit, cost_price, sale_price, image) VALUES (1, 'SP001', 'Áo thun', 'Áo thun nam', 'L', 'Đỏ', 'Cotton', 'Cái', 50000, 70000, 'aothun.jpg');
INSERT INTO products (product_id, product_code, name, description, size, color, material, unit, cost_price, sale_price, image) VALUES (2, 'SP002', 'Quần jean', 'Quần jean nữ', 'M', 'Xanh', 'Jean', 'Cái', 120000, 150000, 'quanjean.jpg');

-- Insert product_suppliers
INSERT INTO product_suppliers (product_id, supplier_id, delivery_duration, estimated_price, policies) VALUES (1, 1, 3, 52000, 'Giao hàng trong 3 ngày');
INSERT INTO product_suppliers (product_id, supplier_id, delivery_duration, estimated_price, policies) VALUES (2, 1, 5, 125000, 'Giao hàng trong 5 ngày');

-- Insert warehouses
INSERT INTO warehouses (warehouse_id, name, location, manager_account_id) VALUES (1, 'Kho Hà Nội', 'Số 1 Nguyễn Trãi', 2);
INSERT INTO warehouses (warehouse_id, name, location, manager_account_id) VALUES (2, 'Kho HCM', 'Số 99 Trường Chinh', 2);

-- Insert inventory
INSERT INTO inventory (product_id, warehouse_id, quantity) VALUES (1, 1, 100);
INSERT INTO inventory (product_id, warehouse_id, quantity) VALUES (2, 2, 50);

-- Insert customers
INSERT INTO customers (customer_id, name, phone, email, address, note) VALUES (1, 'Công ty ABC', '0988888888', 'abc@company.com', '12 Lê Lợi, Đà Nẵng', 'Khách hàng lâu năm');
INSERT INTO customers (customer_id, name, phone, email, address, note) VALUES (2, 'Cửa hàng XYZ', '0977777777', 'xyz@store.com', '45 Hai Bà Trưng, Huế', 'Mới hợp tác');

-- ===================
-- IMPORT ORDERS
-- ===================
INSERT INTO import_orders (supplier_id, warehouse_id, account_id, code, order_deadline, expected_arrival, confirm_date, invoice_status, activity_note, note, total_cost, status)
VALUES
(1, 1, 1, 'P00001', '2025-06-10', '2025-06-12', '2025-06-09', 'No', 'Chờ xác nhận từ NCC', 'Nhập hàng đợt 1', 2000000, 'RFQ Sent');

-- IMPORT ORDER DETAILS
INSERT INTO import_order_details (import_id, product_id, quantity, cost_price, tax_percent, quantity_received, quantity_invoiced)
VALUES
(1, 1, 50, 20000, 10.00, 0, 0),
(1, 2, 100, 15000, 5.00, 0, 0);

-- ===================
-- EXPORT ORDERS
-- ===================
INSERT INTO export_orders (customer_id, warehouse_id, account_id, code, confirm_date, expiration_date, due_date, total_amount, invoice_status, reason, note, activity_note, export_status)
VALUES
(1, 1, 1, 'S00001', '2025-06-08', '2025-06-13', '2025-06-18', 3500000, 'Partial', 'Giao hàng theo yêu cầu', 'Khách lấy sớm', 'Khách hàng thân thiết', 'Scheduled');

-- EXPORT ORDER DETAILS
INSERT INTO export_order_details (export_id, product_id, quantity, sale_price, tax_percent, quantity_delivered, quantity_invoiced)
VALUES
(1, 1, 20, 50000, 10.00, 10, 5),
(1, 2, 30, 40000, 8.00, 20, 15);

-- ===================
-- ADJUSTMENTS
-- ===================
INSERT INTO adjustments (product_id, warehouse_id, account_id, quantity_change, old_quantity, reason)
VALUES
(1, 1, 1, 10, 40, 'Điều chỉnh sai lệch kiểm kho'),
(2, 1, 1, -5, 80, 'Hàng hỏng trong kho');

-- ===================
-- STOCK CHECKS
-- ===================
INSERT INTO stock_checks (warehouse_id, account_id, check_date, end_date, status, note)
VALUES
(1, 1, '2025-06-01', '2025-06-02', 'Done', 'Kiểm kê định kỳ tháng 6');

INSERT INTO stock_check_details (check_id, product_id, actual_quantity, system_quantity)
VALUES
(1, 1, 45, 50),
(1, 2, 95, 100);

-- ===================
-- TRANSFERS
-- ===================
INSERT INTO transfer_orders (source_warehouse_id, destination_warehouse_id, account_id, status, note)
VALUES
(1, 2, 1, 'Pending', 'Chuyển hàng bổ sung kho 2');

INSERT INTO transfer_order_details (transfer_id, product_id, quantity, note)
VALUES
(1, 1, 10, 'Chuyển hàng size M'),
(1, 2, 20, 'Chuyển hàng size L');
