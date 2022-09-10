# njector
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build](https://github.com/mbaracz/njector/actions/workflows/maven.yml/badge.svg)](https://github.com/mbaracz/njector/actions/workflows/maven.yml)
[![](https://jitpack.io/v/mbaracz/njector.svg)](https://jitpack.io/#mbaracz/njector)

Njector is an lightweight spring-like dependency injector for Java with small memory footprint.

### Installation
To build our project through maven, look at the following steps
```
$ git clone https://github.com/mbaracz/njector.git
```
```
$ cd njector
$ mvn clean install
```
You also can download this, as a dependency using the following setup.
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.mbaracz</groupId>
    <artifactId>njector</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Features & usage
1. Application context initialization

```java
public class Main {
    
    public static void main(String[] args) {
        NjectorApplicationContext ctx = Njector.createApplicationContext(Main.class);
    }
}
```

2. `@Component` - used to create instance of a class for later field, constructor or method injection

```java
@Component
public class UserService {
    // ...
}
```

3. `@Inject` - used to inject previously created instance (note: `@Component` is **required** to perform injection)
- Field injection 
```java
@Component
public class OrderService {
    
    @Inject 
    private UserService userService;
}
```
- Constructor injection (along with use Lombok `@AllArgsConstructor` or `@RequiredArgsConstructor`)
```java
@Component
public class OrderService {

    private final UserService userService;
    
    public OrderService(UserService userService) {
        this.userService = userService;
    }
}
```
When more than one constructor is present in the class, `@Inject` annotation on constructor is required.

- Setter injection

```java
public class OrderService {
    
    private UserService userService;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```
4. `@Qualifier` - used to differentiate beans when we have more than one bean of the same type
```java
public interface Database {
    // ...
}

@Component
@Qualifier("flatDatabase")
public class FlatDatabase implements Database {
    // ...
}

@Component
@Qualifier("mysqlDatabase")
public class MySqlDatabase implements Database {
    // ...
}

@Component
private class OrderService {
    
    private final Database database;
    
    public OrderService(@Qualifier("mysqlDatabase") Database database) {
        this.database = database;
    }
}
```
5. `@Configuration` - used to mark class as configuration class, where you can create own bean producers methods
6. `@Bean` - used to register and create specified class instance, like `@Component` but only applicable to producer methods in configuration classes

```java
@Configuration
public class AppConfig {

    @Bean
    public MessageReplacer createMessageReplacer() {
        return new MessageReplacer()
                .setEnableUndefinedVariableException(true)
                .setPrefix("%")
                .setSuffix("%");
    }

    @Bean("someString")
    public String registerSomeString() {
        return "someString";
    }

    @Bean("exampleString")
    public String registerExampleString() {
        return "exampleString";
    }

    @Bean
    public int registerSomeNumber() {
        return 1337;
    }
}

@Component
public class AppModule {
    // ...
    public AppModule(
            MessageReplacer replacer,
            @Qualifier("someString") String someString,
            @Qualifier("exampleString") String exampleString,
            int someNumber
    ) {
        // ...
    }
}
```
When more than one bean of given type is present, you have to specify their names in `@Bean` annotation.

### To-do list
- [ ] Add prototype scope 
- [ ] Add @PreConstruct annotation 
- [ ] Add lazy initialization

### License
Repository is under [MIT License](https://github.com/mbaracz/njector/blob/master/LICENSE).
