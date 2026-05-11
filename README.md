# FitFit 科研项目管理系统

一个用于管理科研项目的后台管理系统，支持项目管理、科研人员管理、经费管理和数据统计。

## 技术栈

- **后端**: Java 21 + Spring Boot 3.2 + Spring Data JPA + H2 Database + JWT
- **前端**: React 18 + Vite + Ant Design 5

## 功能模块

- 用户认证（JWT登录）
- 数据概览仪表盘
- 科研项目管理（CRUD、搜索、筛选）
- 科研人员管理
- 经费记录管理

## 快速开始

### 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 http://localhost:8080

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端运行在 http://localhost:5173

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/auth/login | POST | 用户登录 |
| /api/auth/register | POST | 用户注册 |
| /api/dashboard | GET | 获取统计数据 |
| /api/projects | GET/POST | 项目列表/创建项目 |
| /api/projects/{id} | GET/PUT/DELETE | 项目详情/更新/删除 |
| /api/researchers | GET/POST | 人员列表/添加人员 |
| /api/researchers/{id} | GET/PUT/DELETE | 人员详情/更新/删除 |
| /api/funding | GET/POST | 经费记录列表/创建 |

## 项目结构

```
├── backend/                   # Spring Boot 后端
│   ├── src/main/java/com/fitfit/research/
│   │   ├── config/           # 安全配置、数据初始化
│   │   ├── controller/       # REST 控制器
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # JPA 实体
│   │   ├── repository/       # 数据访问层
│   │   ├── service/          # 业务逻辑层
│   │   └── util/             # 工具类（JWT）
│   └── pom.xml
├── frontend/                  # React 前端
│   ├── src/
│   │   ├── components/       # 布局组件
│   │   ├── pages/            # 页面组件
│   │   └── services/         # API 服务
│   └── package.json
└── AGENTS.md
```
