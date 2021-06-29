package cn.uni.starter.jpa.utils;

import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * 老系统得id生成
 *
 * @author xiaoyin
 */
public class StringSequenceIdentifier implements IdentifierGenerator, Configurable {

    private String sequenceCallSyntax;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService(JdbcEnvironment.class);
        final Dialect dialect = jdbcEnvironment.getDialect();

        final String defaultSequenceName = params.getProperty("sequence");

        sequenceCallSyntax = dialect.getSequenceNextValString(ConfigurationHelper.getString(defaultSequenceName, params, defaultSequenceName));
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        long seqValue = ((Number) ((Session) session)
            .createSQLQuery(sequenceCallSyntax)
            .uniqueResult()).longValue();
        return String.valueOf(seqValue);
    }


}
