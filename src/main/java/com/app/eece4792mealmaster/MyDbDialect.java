package com.app.eece4792mealmaster;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class MyDbDialect extends MySQL5InnoDBDialect {
    public MyDbDialect() {
        super();
        registerFunction("recipeCanBeMade", new StandardSQLFunction("recipeCanBeMade"));
    }
}
