# 减重管理系统 (Weight Loss Management System)

一个基于 Spring Boot + Vue 3 的前后端分离减重管理系统，帮助用户记录和追踪体重、饮食、运动等健康数据。

## 技术栈

### 后端
- Spring Boot 3.2.3
- Spring Data JPA
- Spring Security + JWT
- MySQL 8.0+
- Lombok
- Springdoc OpenAPI (Swagger)

### 前端
- Vue 3 (Composition API)
- Vite 5.0+
- Vue Router 4.x
- Pinia 2.x
- Axios 1.6+
- Element Plus 2.5+
- ECharts 5.4+
- Day.js 1.11+

## 项目结构

```
weight-loss-work/
├── backend/                    # 后端 Spring Boot 项目
│   ├── src/main/java/cn/com/zhangaiyu/dagger/weightloss/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── service/           # 服务层
│   │   ├── repository/        # 数据访问层
│   │   ├── entity/            # 实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── security/          # 安全相关
│   │   ├── exception/         # 异常处理
│   │   └── util/              # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml    # 应用配置
│   │   └── schema.sql         # 数据库脚本
│   └── build.gradle.kts
├── frontend/                   # 前端 Vue 3 项目
│   ├── src/
│   │   ├── api/               # API 接口
│   │   ├── components/        # 组件
│   │   ├── views/             # 页面
│   │   ├── router/            # 路由
│   │   ├── store/             # 状态管理
│   │   └── utils/             # 工具函数
│   ├── package.json
│   └── vite.config.js
├── build.gradle.kts
└── settings.gradle.kts
```

## 功能模块

### 1. 用户管理
- 用户注册
- 用户登录 (JWT 认证)
- 个人信息管理

### 2. 体重管理
- 体重记录的增删改查
- BMI 自动计算
- 体重趋势图表展示
- 目标体重设置

### 3. 饮食管理
- 饮食记录的增删改查
- 卡路里统计
- 营养成分分析 (蛋白质、碳水化合物、脂肪)
- 每日饮食统计图表

### 4. 运动管理
- 运动记录的增删改查
- 卡路里消耗统计
- 运动类型分类
- 运动统计图表

### 5. 仪表盘
- 最新体重、BMI 显示
- 体重趋势图
- 今日饮食和运动统计
- 目标进度展示

## 快速开始

### 前置要求

- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Gradle 8.0+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE weight_loss_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本：
```bash
mysql -u root -p weight_loss_db < backend/src/main/resources/schema.sql
```

3. 修改数据库配置（如需要）：
编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/weight_loss_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 后端启动

```bash
# 在项目根目录执行
./gradlew :backend:bootRun
```

后端服务将在 `http://localhost:8080` 启动。

访问 Swagger 文档：`http://localhost:8080/api/swagger-ui.html`

### 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:3000` 启动。

### 构建生产版本

**后端：**
```bash
./gradlew :backend:build
```

生成的 JAR 文件位于 `backend/build/libs/`

**前端：**
```bash
cd frontend
npm run build
```

生成的静态文件位于 `frontend/dist/`

## API 文档

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录

### 用户接口
- `GET /api/users/profile` - 获取用户信息
- `PUT /api/users/profile` - 更新用户信息

### 体重记录接口
- `GET /api/weight-records` - 获取所有体重记录
- `POST /api/weight-records` - 创建体重记录
- `PUT /api/weight-records/{id}` - 更新体重记录
- `DELETE /api/weight-records/{id}` - 删除体重记录
- `GET /api/weight-records/trend` - 获取体重趋势
- `GET /api/weight-records/latest` - 获取最新体重记录

### 饮食记录接口
- `GET /api/diet-records` - 获取所有饮食记录
- `POST /api/diet-records` - 创建饮食记录
- `PUT /api/diet-records/{id}` - 更新饮食记录
- `DELETE /api/diet-records/{id}` - 删除饮食记录
- `GET /api/diet-records/date/{date}` - 获取指定日期的饮食记录
- `GET /api/diet-records/statistics` - 获取每日卡路里统计

### 运动记录接口
- `GET /api/exercise-records` - 获取所有运动记录
- `POST /api/exercise-records` - 创建运动记录
- `PUT /api/exercise-records/{id}` - 更新运动记录
- `DELETE /api/exercise-records/{id}` - 删除运动记录
- `GET /api/exercise-records/date/{date}` - 获取指定日期的运动记录
- `GET /api/exercise-records/statistics` - 获取每日消耗统计

## 默认配置

### JWT 配置
- Token 过期时间：24 小时
- Secret Key：需要在生产环境中修改（`application.yml` 中的 `jwt.secret`）

### 服务器配置
- 后端端口：8080
- 前端端口：3000
- 前端代理：`/api` 代理到 `http://localhost:8080`

## 注意事项

1. **安全性**：生产环境中务必修改 JWT Secret Key
2. **数据库**：确保 MySQL 服务已启动且配置正确
3. **跨域**：后端已配置允许 `http://localhost:3000` 访问
4. **时区**：系统默认使用 `Asia/Shanghai` 时区

## 开发建议

1. 后端开发时可以使用 Swagger UI 测试 API
2. 前端开发时确保后端服务已启动
3. 使用 Git 进行版本控制
4. 遵循代码规范和最佳实践

## 许可证

MIT License
