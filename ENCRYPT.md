## 添加加密插件

<https://github.com/roseboy/classfinal>

```xml
<!--注意：该插件要放在spring-boot-maven-plugin插件之后-->
<!--
1、加密后，方法体被清空,保留方法参数、注解等信息,主要兼容swagger文档注解扫描，
2、方法体被清空后，反编译只能看到方法名和注解，看不到方体内容
3、加密后的项目需要设置 javaagent来启动，启动过程解密class文件,完全内存解密，不留下任何痕迹
-->
<plugin>
    <groupId>net.roseboy</groupId>
    <artifactId>classfinal-maven-plugin</artifactId>
    <version>1.2.1</version>
    <configuration>
        <password>#</password> <!--表示启动时不需要密码-->
        <excludes>org.spring</excludes>
        <packages>${project.groupId}</packages><!--需要加密的包,多个用逗号隔开-->
        <cfgfiles><!--需要加密的配置文件,多个用逗号隔开-->
            application.yml,application-dev.yml,application-prod.yml,application-test.yml
        </cfgfiles>
        <libjars>hutool-all.jar</libjars><!--jar包下lib下面需要加密的jar包,多个用逗号隔开-->
        <code>xxx</code> <!--指定的机器码启动-->
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>classFinal</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

## 机器绑定

机器绑定只允许加密的项目在特定的机器上运行；
在需要绑定的机器上执行以下命令，生成机器码

```bash
java -jar classfinal-fatjar.jar -C
```

## 项目启动

- 无密码启动

```bash
 java -javaagent:xxx-encrypted.jar -jar xxx-encrypted.jar
```

- 有密码启动

```bash
 java -javaagent:xxx-encrypted.jar='-pwd=密码' -jar xxx-encrypted.jar
```

