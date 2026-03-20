# Project Overview 项目说明

This project is a tourism transportation booking platform based in Japan.

这是一个面向国际游客的日本旅游车务平台，主要提供以下服务：

- Airport Transfer（机场接送）
- Point-to-Point Transfer（点对点接送）
- Private Charter（包车服务）
- Fixed Itinerary Tours（固定行程包车）
- Hourly Charter（按小时包车）
- Shared Ride（拼车服务）

The platform allows tourists to browse itineraries, book transportation services, and manage orders online.

主要目标用户：
- 欧美游客
- 东南亚游客
- 中国大陆 / 香港 / 台湾游客

The system focuses on reliable booking, clear pricing, and efficient order management.

---

# Tech Stack 技术栈

Backend
- Java
- Spring Boot 2
- JEECG Boot
- MySQL

Frontend
- Uni-app
- Vue 3

Infrastructure (planned)
- Google Cloud
- REST API

---

# Project Structure 项目结构

The backend follows layered architecture.

Controller  
Handles HTTP requests.

Service  
Contains business logic.

Repository / Mapper  
Handles database access.

DTO  
Used for API request/response objects.

Important principle:

Controller should NOT contain business logic.

---

# Main Modules 主要模块

Order Module  
订单系统，负责订单创建、状态流转和订单查询。

Vehicle Module  
车辆信息管理。

Driver Module  
司机信息管理。

Itinerary Module  
旅游行程与推荐路线管理。

Pricing Engine  
价格计算逻辑，包括季节价格与车型价格。

Customer Module  
客户信息管理。

---

# Database Tables 主要数据表

Important tables include:

orders  
order_detail  
vehicle  
driver  
itinerary  
customer  
pricing_rule

Large tables (orders, order_detail) must be optimized for performance.

---

# Order Workflow 订单流程

Typical order lifecycle:

CREATED  
CONFIRMED  
IN_SERVICE  
COMPLETED  
CANCELLED

AI should pay special attention to order state transitions and transaction safety.

---

# AI Tasks AI需要完成的任务

When analyzing this project, focus on:

Code Review
- detect potential bugs
- identify bad architecture

SQL Optimization
- check index usage
- detect full table scans
- optimize join queries

Performance Analysis
- detect slow queries
- identify inefficient API logic

Security Review
- check input validation
- detect possible injection risks

---

# Coding Rules 编码规则

Use layered architecture.

Business logic must be in Service layer.

Controller should only handle request and response.

Use DTO objects for API responses.

Avoid N+1 database queries.

Use pagination for list APIs.

Use transactions for critical operations such as:

- order creation
- payment processing

---

# SQL Optimization Rules SQL优化规则

Avoid SELECT * in large tables.

Use indexes for frequently queried columns.

Use pagination for large query results.

Check join performance for multi-table queries.

Important tables such as orders must have proper indexes.

---

# Performance Considerations 性能注意事项

The order system is a critical module.

AI should check for:

- slow SQL queries
- missing indexes
- unnecessary joins
- repeated database queries

High traffic APIs should be optimized.

---

# Language Rules 输出语言规则

Always respond in Simplified Chinese when explaining the system.

Code examples should remain in their original language (Java / SQL).

Explanation should be clear and structured.

---

# Review Focus 审查重点

When reviewing this project, prioritize:

- order workflow correctness
- SQL performance
- transaction safety
- concurrency issues
- API response performance