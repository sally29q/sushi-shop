-----------------------------------------------------------
-- 刪除表內所有資料
DELETE FROM [dbo].[orders];
-- 重置自增 ID
DBCC CHECKIDENT ('[dbo].[orders]', RESEED, 0);
-----------------------------------------------------------
INSERT INTO [dbo].[orders] ([user_phone], [order_type], [total_price], [order_status])
VALUES
    (N'0987654321', N'外帶', 310, 0),
    (N'0987654322', N'內用', 520, 0),
    (N'0987654323', N'內用', 280, 0),
    (N'0987654324', N'外帶', 210, 0),
    (N'0987654325', N'外帶', 1000, 0)


-----------------------------------------------------------
-- 刪除表內所有資料
DELETE FROM [dbo].[order_details];
-- 重置自增 ID
DBCC CHECKIDENT ('[dbo].[order_details]', RESEED, 0);
-----------------------------------------------------------
INSERT INTO [dbo].[order_details] ([order_id], [product_id], [quantity], [price], [subtotal])
VALUES
    (1, 25, 2, 40, 80),
    (1, 1, 2, 40, 80),
    (1, 49, 1, 100, 100),
    (1, 50, 1, 50, 50),

    (2, 31, 1, 60, 60),
    (2, 6, 1, 40, 40),
    (2, 30, 1, 60, 60),
    (2, 14, 1, 40, 40),
    (2, 20, 1, 40, 40),
    (2, 15, 1, 40, 40),
    (2, 4, 2, 40, 80),
    (2, 7, 1, 40, 40),
    (2, 8, 1, 40, 40),
    (2, 13, 1, 40, 40),
    (2, 24, 1, 40, 40),

    (3, 15, 1, 40, 40),
    (3, 16, 1, 40, 40),
    (3, 38, 1, 40, 40),
    (3, 25, 1, 40, 40),
    (3, 14, 1, 40, 40),
    (3, 3, 1, 40, 40),
    (1, 34, 1, 40, 40),

    (4, 51, 1, 160, 160),
    (4, 50, 1, 50, 50),

    (5, 51, 1, 160, 160),
    (5, 52, 1, 200, 200),
    (5, 53, 1, 160, 160),
    (5, 54, 1, 200, 200),
    (5, 55, 1, 280, 280)