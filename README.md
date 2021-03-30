A very simple tool that converts Swagger Api Document to Html File.

Swagger接口文档转为html文档小工具

## 使用

```sh
# 下载源码
git clone https://github.com/iflyendless/Swagger2Html.git

# 编译打包
mvn clean package

# 使用target目录下的jar包, 参数是swagger接口数据
java -jar Swagger2Html-1.0-SNAPSHOT-jar-with-dependencies.jar http://localhost:8080/v2/api-docs
```

## 项目由来

很多人用`swagger2markup`以及`asciidoctor-maven-plugin`插件来生成html格式的文档。

由于`swagger2markup`依赖的asm库版本较低, 且依赖较多, 容易与项目中依赖的核心库冲突。  

所以, 干脆把Swagger接口文档转为html文档独立出来, 做成一个小工具。