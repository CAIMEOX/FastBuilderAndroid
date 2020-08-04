[TOC]

# FastBuilder Catalina: Open Source

> Give people wonderful tools, and they'll do wonderful things. - Apple

## FastBuilder Catalina 开源计划版本

### 项目结构

app [FastBuilder Catalina Application]

bushe_application_security [**已被剥离** | BuShe 安全机制]

FastBuilder Core Config [FastBuilder Core 构建信息]

其余目录由 Android Studio, Gradle 的相关组成文件构成。

### 说明

项目为了保护数据安全，对部分第三方依赖所需要的授权/鉴权文件进行了剥离，在实际构建过程中，可根据 build.gradle 中所使用的依赖寻找依赖方，自行获取需要文件。

开源版本中的代码经过处理，不进行优化直接编译可能会有很多潜在问题。

项目为了提高开发门槛，对项目结构进行了混淆，在实际开发中并不影响关键操作。

项目保留了 FastBuilder Core: Go。

项目保留了 BuShe ID 数据库依赖：Bmob SDK 初始化密钥，**请勿滥用**。

若有疑问，请咨询 QQ: 32348197。