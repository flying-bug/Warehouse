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
