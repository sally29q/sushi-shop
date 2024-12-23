-----------------------------------------------------------
-- 刪除表內所有資料
DELETE FROM [dbo].[ingredients];
-- 重置自增 ID
DBCC CHECKIDENT ('[dbo].[ingredients]', RESEED, 0);
-----------------------------------------------------------
INSERT INTO [dbo].[ingredients] ([ingredient_name], [total_quantity])
VALUES
    (N'玉子', 300),
    (N'長鰭鮪', 300),
    (N'鹽味牛排', 300),
    (N'鮪魚', 300),
    (N'鮮蝦', 300),
    (N'甜蝦', 300),
    (N'生鮮蝦', 300),
    (N'花枝', 300),
    (N'小帆立貝', 300),
    (N'北寄貝', 300),

    (N'鮭魚', 300),
    (N'鮭魚肚', 300),
    (N'鯖魚', 300),
    (N'鯡魚卵', 300),
    (N'赤蝦', 300),
    (N'烏賊', 300),
    (N'蟹肉棒', 300),
    (N'甜醬烤牛肉', 300),
    (N'豆皮', 300),
    (N'竹筴魚', 300),

    (N'貝柱', 300),
    (N'鰻魚', 300),
    (N'大切生鮭魚', 300),
    (N'黑鮪魚中腹', 300),
    (N'黑鮪魚大腹', 300),
    (N'沙丁魚', 300),
    (N'玉米沙拉', 300),
    (N'鮪魚沙拉', 300),
    (N'納豆', 300),
    (N'蟹肉', 300),

    (N'鮭魚卵', 300),
    (N'飛魚卵', 300),
    (N'蟹膏', 300),
    (N'鮟鱇魚肝', 300),
    (N'鱈魚子沙拉', 300),
    (N'小黃瓜', 300),
    (N'炸蝦', 300),
    (N'酪梨', 300),
    (N'炸物拼盤', 300),
    (N'薯條', 300)