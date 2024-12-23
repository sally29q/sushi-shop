SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products](
	[product_id] [int] IDENTITY(1,1) NOT NULL,
	[product_name] [nvarchar](50) NOT NULL,
	[price] [int] NOT NULL,
	[category] [nvarchar](20) NULL,
	[photo] [varbinary](max) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[products] ADD  CONSTRAINT [PK_produdcts] PRIMARY KEY CLUSTERED 
(
	[product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ingredients](
	[ingredient_id] [int] IDENTITY(1,1) NOT NULL,
	[ingredient_name] [nvarchar](50) NOT NULL,
	[total_quantity] [int] NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ingredients] ADD  CONSTRAINT [PK_ingredients] PRIMARY KEY CLUSTERED 
(
	[ingredient_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ingredients] ADD  CONSTRAINT [DEFAULT_ingredients_total_quantity]  DEFAULT ((0)) FOR [total_quantity]
GO
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[product_ingredients](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [int] NOT NULL,
	[ingredient_id] [int] NOT NULL,
	[ingredient_quantity] [int] NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[product_ingredients] ADD  CONSTRAINT [PK_product_ingredients] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[product_ingredients] ADD  CONSTRAINT [DEFAULT_set_item_quantity]  DEFAULT ((1)) FOR [ingredient_quantity]
GO
ALTER TABLE [dbo].[product_ingredients]  WITH CHECK ADD  CONSTRAINT [FK_product_ingredients_ingredient] FOREIGN KEY([ingredient_id])
REFERENCES [dbo].[ingredients] ([ingredient_id])
GO
ALTER TABLE [dbo].[product_ingredients] CHECK CONSTRAINT [FK_product_ingredients_ingredient]
GO
ALTER TABLE [dbo].[product_ingredients]  WITH CHECK ADD  CONSTRAINT [FK_product_ingredients_product] FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[product_ingredients] CHECK CONSTRAINT [FK_product_ingredients_product]
GO
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[user_phone] [varchar](10) NOT NULL,
	[order_type] [nvarchar](10) NOT NULL,
	[total_price] [int] NOT NULL,
	[order_status] [int] NOT NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[orders] ADD  CONSTRAINT [PK_orders] PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[orders] ADD  CONSTRAINT [DEFAULT_orders_order_status]  DEFAULT ((0)) FOR [order_status]
GO
ALTER TABLE [dbo].[orders] ADD  CONSTRAINT [DEFAULT_orders_created_at]  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[orders] ADD  CONSTRAINT [DEFAULT_orders_updated_at]  DEFAULT (getdate()) FOR [updated_at]
GO

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[order_details](
	[order_details_id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [int] NOT NULL,
	[product_id] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [int] NOT NULL,
	[subtotal] [int] NOT NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[order_details] ADD  CONSTRAINT [PK_order_details] PRIMARY KEY CLUSTERED 
(
	[order_details_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD  CONSTRAINT [FK_orders_order] FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details] CHECK CONSTRAINT [FK_orders_order]
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD  CONSTRAINT [FK_products_product] FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_details] CHECK CONSTRAINT [FK_products_product]
GO
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[admin](
	[admin_id] [int] IDENTITY(1,1) NOT NULL,
	[account] [varchar](20) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[name] [nvarchar](50) NULL
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[admin] ADD  CONSTRAINT [PK_admin] PRIMARY KEY CLUSTERED 
(
	[admin_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
ALTER TABLE [dbo].[admin] ADD UNIQUE NONCLUSTERED 
(
	[account] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
