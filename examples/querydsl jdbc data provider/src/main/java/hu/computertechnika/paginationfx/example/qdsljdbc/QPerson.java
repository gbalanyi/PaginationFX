package hu.computertechnika.paginationfx.example.qdsljdbc;


import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QPerson is a Querydsl query type for QPerson
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QPerson extends com.querydsl.sql.RelationalPathBase<QPerson> {

    private static final long serialVersionUID = 860455599;

    public static final QPerson person = new QPerson("PERSON");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final DatePath<java.sql.Date> dob = createDate("dob", java.sql.Date.class);

    public final StringPath firstName = createString("firstName");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final com.querydsl.sql.PrimaryKey<QPerson> constraint8 = createPrimaryKey(id);

    public QPerson(String variable) {
        super(QPerson.class, forVariable(variable), "PUBLIC", "PERSON");
        addMetadata();
    }

    public QPerson(String variable, String schema, String table) {
        super(QPerson.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPerson(String variable, String schema) {
        super(QPerson.class, forVariable(variable), schema, "PERSON");
        addMetadata();
    }

    public QPerson(Path<? extends QPerson> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "PERSON");
        addMetadata();
    }

    public QPerson(PathMetadata metadata) {
        super(QPerson.class, metadata, "PUBLIC", "PERSON");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(age, ColumnMetadata.named("AGE").withIndex(4).ofType(Types.INTEGER).withSize(10));
        addMetadata(dob, ColumnMetadata.named("DOB").withIndex(6).ofType(Types.DATE).withSize(10));
        addMetadata(firstName, ColumnMetadata.named("FIRST_NAME").withIndex(2).ofType(Types.VARCHAR).withSize(50));
        addMetadata(gender, ColumnMetadata.named("GENDER").withIndex(5).ofType(Types.VARCHAR).withSize(10));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(lastName, ColumnMetadata.named("LAST_NAME").withIndex(3).ofType(Types.VARCHAR).withSize(50));
    }

}

