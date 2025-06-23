-- CREATE DATABASE warehouse_clothing;
-- USE warehouse_clothing;



-- ===================
-- ROLES & ACCOUNTS
-- ===================
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT 
);

CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role_id INT NOT NULL,
    profile_image VARCHAR(255),
    status INT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===================
-- SUPPLIERS & PRODUCTS
-- ===================
CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    tax_id VARCHAR(50),
    contact_person VARCHAR(100),
    notes TEXT,
    status INT DEFAULT 1
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE,
    name VARCHAR(100),
    description TEXT,
    size VARCHAR(10),
    color VARCHAR(30),
    material VARCHAR(50),
    unit VARCHAR(10),
    cost_price DECIMAL(12,2),
    sale_price DECIMAL(12,2),
    status INT DEFAULT 1,
    image VARCHAR(255),
    min_stock_level INT DEFAULT 0
);

CREATE TABLE product_suppliers (
    product_id INT,
    supplier_id INT,
    delivery_duration INT,
    estimated_price DECIMAL(12,2),
    policies TEXT,
    PRIMARY KEY (product_id, supplier_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ===================
-- WAREHOUSE & INVENTORY
-- ===================
CREATE TABLE warehouses (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    location VARCHAR(255),
    manager_account_id INT,
    status INT DEFAULT 1,
    FOREIGN KEY (manager_account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE inventory (
    product_id INT,
    warehouse_id INT,
    quantity INT DEFAULT 0,
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id, warehouse_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===================
-- CUSTOMERS
-- ===================
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    note TEXT,
    status INT DEFAULT 1
);

-- ===================
-- IMPORT - EXPORT
-- ===================
CREATE TABLE import_orders (
    import_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT,
    warehouse_id INT,
    account_id INT,
    import_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    -- ✅ Trường bổ sung theo nghiệp vụ mua hàng:
    code VARCHAR(50) UNIQUE,                                   -- Mã đơn nhập (P00026)
    order_deadline DATETIME,                                   -- Hạn yêu cầu gửi báo giá
    expected_arrival DATETIME,                                 -- Ngày dự kiến nhận hàng
    confirm_date DATETIME,                                     -- Ngày nhà cung cấp xác nhận đơn
    invoice_status VARCHAR(20) DEFAULT 'No'                    -- Trạng thái hóa đơn
        CHECK (invoice_status IN ('No', 'Partial', 'Invoiced')),
    activity_note VARCHAR(255),                                -- Ghi chú nội bộ

    note TEXT,
    total_cost DECIMAL(12,2) DEFAULT 0.00,

    status VARCHAR(20) DEFAULT 'Draft'                         -- Trạng thái đơn mua
        CHECK (status IN ('Draft', 'RFQ Sent', 'Purchase Order', 'Received', 'Cancelled')),

    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);


CREATE TABLE import_order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    import_id INT,
    product_id INT,
    quantity INT,
    cost_price DECIMAL(12,2),

    -- ✅ Bổ sung nghiệp vụ
    tax_percent DECIMAL(5,2) DEFAULT 0.00,             -- Thuế theo %

    quantity_received INT DEFAULT 0,                   -- Số lượng đã nhận
    quantity_invoiced INT DEFAULT 0,                   -- Số lượng đã lập hóa đơn

    import_status VARCHAR(20) DEFAULT 'Scheduled'
        CHECK (import_status IN ('Scheduled', 'Done', 'Cancel')),

    FOREIGN KEY (import_id) REFERENCES import_orders(import_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE export_orders (
    export_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    warehouse_id INT,
    account_id INT,

    export_date DATETIME DEFAULT CURRENT_TIMESTAMP,           -- Ngày xuất đơn
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,            -- Ngày tạo đơn

    -- ➕ Trường bổ sung hữu ích
    code VARCHAR(50) UNIQUE,                                  -- Mã đơn (ví dụ: S00037)
    confirm_date DATE,                                        -- Ngày khách hàng xác nhận

    expiration_date DATE,                                     -- Ngày hết hạn báo giá
    due_date DATE,                                            -- Hạn thanh toán

    total_amount DECIMAL(12,2) DEFAULT 0.00,                  -- Tổng tiền đơn hàng

    invoice_status VARCHAR(20) DEFAULT 'No'                   -- Trạng thái hóa đơn
        CHECK (invoice_status IN ('No', 'Partial', 'Invoiced')),

    reason VARCHAR(255),
    note TEXT,
    activity_note VARCHAR(255) DEFAULT NULL,
    export_status VARCHAR(20) DEFAULT 'Scheduled'
        CHECK (export_status IN ('Scheduled', 'Done', 'Cancel')),

    -- Liên kết khóa ngoại
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);


CREATE TABLE export_order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    export_id INT,
    product_id INT,
    quantity INT,
    sale_price DECIMAL(12,2),
    tax_percent DECIMAL(5,2), -- ✅ Thêm: Thuế VAT từng sản phẩm
    quantity_delivered INT DEFAULT 0, -- ✅ Thêm: Theo dõi số lượng đã giao
    quantity_invoiced INT DEFAULT 0,  -- ✅ Thêm: Theo dõi số lượng đã xuất hóa đơn
    
    export_status VARCHAR(20) DEFAULT 'Scheduled' CHECK (export_status IN ('Scheduled', 'Done', 'Cancel')),
    FOREIGN KEY (export_id) REFERENCES export_orders(export_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===================
-- ADJUSTMENTS & STOCK CHECKS
-- ===================
CREATE TABLE adjustments (
    adjustment_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    warehouse_id INT,
    account_id INT,
    adjustment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    quantity_change INT,
    old_quantity INT,
    reason VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE stock_checks (
    check_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_id INT,
    account_id INT,
    check_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_date DATETIME,
    status VARCHAR(20) DEFAULT 'Scheduled' CHECK (status IN ('Scheduled', 'Done', 'Cancel')),
    note TEXT,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE stock_check_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    check_id INT,
    product_id INT,
    actual_quantity INT,
    system_quantity INT,
    FOREIGN KEY (check_id) REFERENCES stock_checks(check_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===================
-- TRANSFERS
-- ===================
CREATE TABLE transfer_orders (
    transfer_id INT AUTO_INCREMENT PRIMARY KEY,
    source_warehouse_id INT NOT NULL,
    destination_warehouse_id INT NOT NULL,
    account_id INT,
    transfer_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Pending',
    note TEXT,
    FOREIGN KEY (source_warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (destination_warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE transfer_order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transfer_id INT,
    product_id INT,
    quantity INT,
    note TEXT,
    FOREIGN KEY (transfer_id) REFERENCES transfer_orders(transfer_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ===================
-- INVENTORY TRANSACTION LOG
-- ===================
CREATE TABLE inventory_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    warehouse_id INT NOT NULL,
    quantity_change INT,
    transaction_type VARCHAR(20),
    reference_id INT,
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);
