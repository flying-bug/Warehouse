DROP TRIGGER IF EXISTS trg_insert_inventory_import;
DROP TRIGGER IF EXISTS trg_insert_inventory_export;
DROP TRIGGER IF EXISTS trg_insert_inventory_transfer;
DROP TRIGGER IF EXISTS trg_insert_inventory_adjustment;

DROP TRIGGER IF EXISTS trg_after_update_import_status;
DROP TRIGGER IF EXISTS trg_after_update_export_status;

DROP TRIGGER IF EXISTS trg_after_update_import_done;
DROP TRIGGER IF EXISTS trg_after_update_export_done;
DROP TRIGGER IF EXISTS trg_after_update_stockcheck_done;

DROP TRIGGER IF EXISTS trg_after_export_detail_change;
DROP TRIGGER IF EXISTS trg_after_export_detail_update;
DROP TRIGGER IF EXISTS trg_after_export_detail_delete;

DROP TRIGGER IF EXISTS trg_after_import_detail_change;
DROP TRIGGER IF EXISTS trg_after_import_detail_update;
DROP TRIGGER IF EXISTS trg_after_import_detail_delete;

DROP TRIGGER IF EXISTS trg_generate_export_code;
DROP PROCEDURE IF EXISTS update_export_order_total;
DROP PROCEDURE IF EXISTS update_import_order_total;







DELIMITER $$

CREATE TRIGGER trg_insert_inventory_import
AFTER INSERT ON import_order_details
FOR EACH ROW
BEGIN
    INSERT INTO inventory_transactions (
        product_id,
        warehouse_id,
        quantity_change,
        transaction_type,
        reference_id,
        note
    )
    VALUES (
        NEW.product_id,
        (SELECT warehouse_id FROM import_orders WHERE import_id = NEW.import_id),
        NEW.quantity,
        'import',
        NEW.import_id,
        'Tự động ghi log nhập kho'
    );
END $$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_insert_inventory_export
AFTER INSERT ON export_order_details
FOR EACH ROW
BEGIN
    INSERT INTO inventory_transactions (
        product_id,
        warehouse_id,
        quantity_change,
        transaction_type,
        reference_id,
        note
    )
    VALUES (
        NEW.product_id,
        (SELECT warehouse_id FROM export_orders WHERE export_id = NEW.export_id),
        -NEW.quantity,
        'export',
        NEW.export_id,
        'Tự động ghi log xuất kho'
    );
END $$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_insert_inventory_transfer
AFTER INSERT ON transfer_order_details
FOR EACH ROW
BEGIN
    DECLARE src_warehouse INT;
    DECLARE dest_warehouse INT;

    SELECT source_warehouse_id, destination_warehouse_id
    INTO src_warehouse, dest_warehouse
    FROM transfer_orders
    WHERE transfer_id = NEW.transfer_id;

    -- Xuất khỏi kho nguồn
    INSERT INTO inventory_transactions (
        product_id,
        warehouse_id,
        quantity_change,
        transaction_type,
        reference_id,
        note
    )
    VALUES (
        NEW.product_id,
        src_warehouse,
        -NEW.quantity,
        'transfer',
        NEW.transfer_id,
        'Xuất kho chuyển kho'
    );

    -- Nhập vào kho đích
    INSERT INTO inventory_transactions (
        product_id,
        warehouse_id,
        quantity_change,
        transaction_type,
        reference_id,
        note
    )
    VALUES (
        NEW.product_id,
        dest_warehouse,
        NEW.quantity,
        'transfer',
        NEW.transfer_id,
        'Nhập kho chuyển kho'
    );
END $$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_insert_inventory_adjustment
AFTER INSERT ON adjustments
FOR EACH ROW
BEGIN
    INSERT INTO inventory_transactions (
        product_id,
        warehouse_id,
        quantity_change,
        transaction_type,
        reference_id,
        note
    )
    VALUES (
        NEW.product_id,
        NEW.warehouse_id,
        NEW.quantity_change,
        'adjustment',
        NEW.adjustment_id,
        NEW.reason
    );
END $$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_after_update_import_status
AFTER UPDATE ON import_orders
FOR EACH ROW
BEGIN
    IF NEW.status <> OLD.status THEN
        UPDATE import_order_details
        SET import_status = NEW.status
        WHERE import_id = NEW.import_id;
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_after_update_export_status
AFTER UPDATE ON export_orders
FOR EACH ROW
BEGIN
    IF NEW.export_status <> OLD.export_status THEN
        UPDATE export_order_details
        SET export_status = NEW.export_status
        WHERE export_id = NEW.export_id;
    END IF;
END$$

DELIMITER ;






DROP TRIGGER IF EXISTS trg_after_update_import_done;
DROP TRIGGER IF EXISTS trg_after_update_export_done;
DROP TRIGGER IF EXISTS trg_after_update_stockcheck_done;

DELIMITER $$

CREATE TRIGGER trg_after_update_import_done
AFTER UPDATE ON import_orders
FOR EACH ROW
BEGIN
    IF NEW.status = 'Done' AND OLD.status != 'Done' THEN
        INSERT INTO inventory (product_id, warehouse_id, quantity)
        SELECT iod.product_id, NEW.warehouse_id, iod.quantity
        FROM import_order_details iod
        WHERE iod.import_id = NEW.import_id
        ON DUPLICATE KEY UPDATE quantity = inventory.quantity + VALUES(quantity);
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_after_update_export_done
AFTER UPDATE ON export_orders
FOR EACH ROW
BEGIN
    IF NEW.export_status = 'Done' AND OLD.export_status != 'Done' THEN
        INSERT INTO inventory (product_id, warehouse_id, quantity)
        SELECT eod.product_id, NEW.warehouse_id, -eod.quantity
        FROM export_order_details eod
        WHERE eod.export_id = NEW.export_id
        ON DUPLICATE KEY UPDATE quantity = inventory.quantity + VALUES(quantity);
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_after_update_stockcheck_done
AFTER UPDATE ON stock_checks
FOR EACH ROW
BEGIN
    IF NEW.status = 'Done' AND OLD.status != 'Done' THEN
        UPDATE inventory i
        JOIN stock_check_details scd ON i.product_id = scd.product_id
        SET i.quantity = scd.actual_quantity
        WHERE i.warehouse_id = NEW.warehouse_id
          AND scd.check_id = NEW.check_id;
    END IF;
END$$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_after_export_detail_change
AFTER INSERT ON export_order_details
FOR EACH ROW
BEGIN
    CALL update_export_order_total(NEW.export_id);
END $$

CREATE TRIGGER trg_after_export_detail_update
AFTER UPDATE ON export_order_details
FOR EACH ROW
BEGIN
    IF NEW.quantity <> OLD.quantity
       OR NEW.sale_price <> OLD.sale_price
       OR IFNULL(NEW.tax_percent, 0) <> IFNULL(OLD.tax_percent, 0)
    THEN
        CALL update_export_order_total(NEW.export_id);
    END IF;
END $$

CREATE TRIGGER trg_after_export_detail_delete
AFTER DELETE ON export_order_details
FOR EACH ROW
BEGIN
    CALL update_export_order_total(OLD.export_id);
END $$

DELIMITER ;



DELIMITER $$

CREATE TRIGGER trg_after_import_detail_change
AFTER INSERT ON import_order_details
FOR EACH ROW
BEGIN
    CALL update_import_order_total(NEW.import_id);
END $$

CREATE TRIGGER trg_after_import_detail_update
AFTER UPDATE ON import_order_details
FOR EACH ROW
BEGIN
    IF NEW.quantity <> OLD.quantity
       OR NEW.cost_price <> OLD.cost_price
       OR IFNULL(NEW.tax_percent, 0) <> IFNULL(OLD.tax_percent, 0)
    THEN
        CALL update_import_order_total(NEW.import_id);
    END IF;
END $$

CREATE TRIGGER trg_after_import_detail_delete
AFTER DELETE ON import_order_details
FOR EACH ROW
BEGIN
    CALL update_import_order_total(OLD.import_id);
END $$

DELIMITER ;


DROP PROCEDURE IF EXISTS update_import_order_total;



DELIMITER $$

CREATE PROCEDURE update_import_order_total(IN p_import_id INT)
BEGIN
    DECLARE total DECIMAL(12,2);

    SELECT 
        SUM(quantity * cost_price + IFNULL(quantity * cost_price * tax_percent / 100, 0))
    INTO total
    FROM import_order_details
    WHERE import_id = p_import_id;

    UPDATE import_orders
    SET total_cost = IFNULL(total, 0.00)
    WHERE import_id = p_import_id;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE update_export_order_total(IN p_export_id INT)
BEGIN
    DECLARE total DECIMAL(12,2);

    SELECT 
        SUM(quantity * sale_price + IFNULL(quantity * sale_price * tax_percent / 100, 0))
    INTO total
    FROM export_order_details
    WHERE export_id = p_export_id;

    UPDATE export_orders
    SET total_amount = IFNULL(total, 0.00)
    WHERE export_id = p_export_id;
END $$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_generate_export_code
BEFORE INSERT ON export_orders
FOR EACH ROW
BEGIN
    DECLARE new_code VARCHAR(50);
    DECLARE next_id INT;

    IF NEW.code IS NULL OR NEW.code = '' THEN
        -- Lấy ID lớn nhất hiện có (nếu có), sau đó cộng 1
        SELECT IFNULL(MAX(export_id), 0) + 1 INTO next_id FROM export_orders;

        -- Tạo mã code theo định dạng S + số có 6 chữ số
        SET new_code = CONCAT('S', LPAD(next_id, 5, '0'));

        -- Gán vào NEW.code
        SET NEW.code = new_code;
    END IF;
END$$

DELIMITER ;




