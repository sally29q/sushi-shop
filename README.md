<h1> Read Me </h1>
<h2>功能</h2>
<ol>
  <li>提供使用者預訂餐點</li>
  <li>提供管理者管理食材、商品、訂單</li>
</ol>

<h2>環境設定說明</h2>
<ol>
  <li>開發工具</li>
  <ul>
  <li>IDE: VS Code</li>
  <li>版本控制工具: Git</li>
  <li>建置工具: Maven</li>
</ul>
  <li>後端技術</li>
  <ul>
  <li>Java JDK: 版本 17 以上</li>
  <li>Spring Boot: 建議使用 3.x 系列</li>
  <li>資料庫連線池: HikariCP</li>
  <li>資料存取技術: Spring Data JPA</li>
</ul>
  <li>資料庫</li>
  <ul>
  <li>資料庫類型: SQL Server</li>
</ul>
  <li>REST API 測試工具</li>
  <ul>
  <li>Postman：用於測試 API 介面</li>
</ul>
</ol>

<h2>執行步驟</h2>
<ol>
  <li>先到SQL資料夾，下載sql檔案並執行，建立資料庫並輸入資料</li>
  <li>java-sushi資料夾為後端程式碼</li>
  <li>front-for-user資料夾為前端程式碼，提供使用者使用的，建議埠號為5173</li>
  <li>front-for-admin資料夾為前端程式碼，提供管理者使用的，建議埠號為5174</li>
  <li>請參考下方測試方式，並開始測試</li>
</ol>
<h2>測試方式</h2>
<ol>
  <li>資料庫完成後會有以下這些資料(product_ingredients表資料太多不展示)</li>
  (https://github.com/sally29q/sushi-shop/blob/c301a1a2894ab96f517c1f8aebc204aa107095c1/pic/%E5%95%86%E5%93%81%E6%95%B8%E6%93%9A.png)
  (https://github.com/sally29q/sushi-shop/blob/c301a1a2894ab96f517c1f8aebc204aa107095c1/pic/%E9%A3%9F%E6%9D%90%E6%95%B8%E6%93%9A.png)
  (https://github.com/sally29q/sushi-shop/blob/c301a1a2894ab96f517c1f8aebc204aa107095c1/pic/%E8%A8%82%E5%96%AE%E6%95%B8%E6%93%9A.png)
  (https://github.com/sally29q/sushi-shop/blob/c301a1a2894ab96f517c1f8aebc204aa107095c1/pic/%E8%A8%82%E5%96%AE%E6%98%8E%E7%B4%B0%E6%95%B8%E6%93%9A.png)
  <li>run 後端程式碼</li>
  <li>開啟使用者網頁測試</li>
  <ul>
    <li>進入首頁，點擊開始點餐</li>
    <li>在菜單畫面，選擇想要的商品，會自動加入右上角的購物車，點擊右上角購物車預訂</li>
    <li>進入預訂頁面，填寫資料</li>
    <li>送出資料，完成預訂</li>
  </ul>
  <li>開啟管理者網頁測試(登入與驗證功能尚未完成)</li>
    <ul>
    <li>進入首頁，可以看到各種管理選項</li>
    <li>各個管理頁面裡可編輯、查看或是刪除各種資料</li>
  </ul>
</ol>




