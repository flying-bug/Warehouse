-- =======================================
-- INSERT DATA FOR warehouse_clothing DB
-- =======================================

-- Rolesaccountsadjustments
INSERT INTO roles (role_name, description) VALUES
('Admin', 'Quản trị viên hệ thống với toàn quyền truy cập và quản lý.'),
('Store Manager', 'Quản lý cửa hàng, có quyền xem báo cáo, quản lý nhân viên và một số hoạt động kho.'),
('Warehouse Staff', 'Nhân viên kho, chịu trách nhiệm nhập xuất, kiểm kê hàng hóa.'),
('Purchasing Staff', 'Nhân viên mua hàng, phụ trách đặt hàng từ nhà cung cấp.'),
('Sales Staff', 'Nhân viên bán hàng, phụ trách tạo đơn xuất và quản lý khách hàng.');

-- Accounts
INSERT INTO accounts (username, password, full_name, email, phone, role_id, profile_image, status, created_at) VALUES
('admin', 'hashed_password', 'Nguyễn Văn Quang', 'admin@warehouse.vn', '0909000001', 1, NULL, 1, NOW()),
('store_manager', 'hashed_password', 'Phạm Thị Hà', 'ha@store.vn', '0909000005', 2, NULL, 1, NOW()),
('nhanvienkho_hn', 'hashed_password', 'Trần Thị Mai', 'mai@warehouse.vn', '0909000002', 3, NULL, 1, NOW()),
('nhanviennhap', 'hashed_password', 'Lê Hoàng', 'hoang@warehouse.vn', '0909000003', 4, NULL, 1, NOW()),
('nhanvienban', 'hashed_password', 'Ngô Văn Tâm', 'tam@warehouse.vn', '0909000004', 5, NULL, 1, NOW());

-- Suppliers
INSERT INTO suppliers (name, phone, email, address, tax_id, contact_person, notes, status) VALUES
('Dệt May Việt Tiến', '02838245678', 'lienhe@viettien.com.vn', '7 Lê Minh Xuân, Tân Bình, TP.HCM', '0301234567', 'Nguyễn Thị Hoa', 'Chuyên cung cấp sơ mi', 1),
('May 10', '02438624590', 'info@may10.vn', '765 Nguyễn Văn Cừ, Long Biên, Hà Nội', '0102345678', 'Trần Hữu Nam', 'Đồng phục văn phòng', 1);

-- Products
INSERT INTO products (product_code, name, description, size, color, material, unit, cost_price, sale_price, status, image, min_stock_level) VALUES
('SM001', 'Sơ mi trắng nam', 'Áo sơ mi trắng công sở, cotton chống nhăn', 'L', 'Trắng', 'Cotton', 'Cái', 180000, 250000, 1, 'https://example.com/somi.jpg', 20),
('QJ001', 'Quần jean nam', 'Quần jean nam xanh đậm', '32', 'Xanh đậm', 'Denim', 'Cái', 220000, 350000, 1, 'https://example.com/jean.jpg', 15),
('AK001', 'Áo khoác kaki nữ', 'Áo khoác form rộng, có mũ', 'M', 'Be', 'Kaki', 'Cái', 260000, 390000, 1, 'https://example.com/khoac.jpg', 10);

-- Product-Suppliers
INSERT INTO product_suppliers (product_id, supplier_id, delivery_duration, estimated_price, policies) VALUES
(1, 1, 3, 180000, 'Giao hàng sau 3 ngày'),
(2, 2, 5, 220000, 'Bảo hành đường may 6 tháng'),
(3, 2, 4, 260000, 'Giao hàng nội thành miễn phí');

-- Warehouses
INSERT INTO warehouses (name, location, manager_account_id, status) VALUES
('Kho Hà Nội', 'KCN Bắc Thăng Long, Đông Anh, Hà Nội', 2, 1),
('Kho TP.HCM', 'KCN Tân Tạo, Bình Tân, TP.HCM', 2, 1);

-- Inventory
INSERT INTO inventory (product_id, warehouse_id, quantity) VALUES
(1, 1, 100),
(2, 1, 70),
(3, 2, 50);

-- Customers
INSERT INTO customers (name, phone, email, address, note, status) VALUES
('Cửa hàng An Fashion', '0934567890', 'an@fashion.vn', '123 Kim Mã, Ba Đình, Hà Nội', 'Khách sỉ ưu thích sơ mi', 1),
('Shop Gấu Boutique', '0908765432', 'gau@boutique.vn', '205 Lý Tự Trọng, Quận 1, TP.HCM', 'Chuyên hàng nữ', 1);

-- Import Orders
INSERT INTO import_orders (supplier_id, warehouse_id, account_id, import_date, note, total_cost, status) VALUES
(1, 1, 4, '2025-06-10 08:00:00', 'Nhập sơ mi từ Việt Tiến', 9000000, 'Completed');

INSERT INTO import_order_details (import_id, product_id, quantity, cost_price) VALUES
(1, 1, 50, 180000);

-- Export Orders
INSERT INTO export_orders (customer_id, warehouse_id, account_id, export_date, reason, note, total_sale_price) VALUES
(1, 1, 5, '2025-06-11 10:30:00', 'Bán buôn cho khách sỉ', 'Giao hàng bằng GHTK', 5000000);

INSERT INTO export_order_details (export_id, product_id, quantity, sale_price) VALUES
(1, 1, 20, 250000);