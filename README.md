# PaginationFX

## Introduction

This component use an extensible `DataProvider` API for pagination. It is support some built in DataProvider for Collections and SQL, but you can create own if necessary. For more information please check the examples!

## Requirements

*   Java 8+
*   JavaFX 8+

## Maven

Add a dependency for use:

```xml
<dependency>
	<groupId>hu.computertechnika</groupId>
	<artifactId>paginationfx</artifactId>
	<version>1.0.0</version>
</dependency>
```

If you want to use the QueryDSL JDBC DataProvider add the following:

```xml
<dependency>
	<groupId>hu.computertechnika</groupId>
	<artifactId>paginationfx-qdsl-jdbc</artifactId>
	<version>1.0.0</version>
</dependency>
```

If you want to use the QueryDSL JPA DataProvider add the following:

```xml
<dependency>
	<groupId>hu.computertechnika</groupId>
	<artifactId>paginationfx-qdsl-jpa</artifactId>
	<version>1.0.0</version>
</dependency>
```
