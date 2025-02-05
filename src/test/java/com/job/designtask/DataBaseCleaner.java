package com.job.designtask;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static liquibase.database.DatabaseFactory.getInstance;

@Service
@ActiveProfiles("test")
@Value
public class DataBaseCleaner {
    Connection connection;
    Database database;
    Liquibase liquibase;

    public DataBaseCleaner() throws DatabaseException, SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:test", "user", "password");
        this.database = getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        this.liquibase = new Liquibase("changelog-master-test.xml", new ClassLoaderResourceAccessor(), database);
    }

    public void cleanUp() throws LiquibaseException {
        liquibase.dropAll();
        liquibase.update(new Contexts());
    }
}
