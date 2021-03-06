package com.jeesite.common.persistence.interceptor;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import com.jeesite.common.config.Global;
import com.jeesite.common.persistence.Page;
import com.jeesite.common.persistence.dialect.Dialect;
import com.jeesite.common.persistence.dialect.db.DB2Dialect;
import com.jeesite.common.persistence.dialect.db.DerbyDialect;
import com.jeesite.common.persistence.dialect.db.H2Dialect;
import com.jeesite.common.persistence.dialect.db.HSQLDialect;
import com.jeesite.common.persistence.dialect.db.MySQLDialect;
import com.jeesite.common.persistence.dialect.db.OracleDialect;
import com.jeesite.common.persistence.dialect.db.PostgreSQLDialect;
import com.jeesite.common.persistence.dialect.db.SQLServer2005Dialect;
import com.jeesite.common.persistence.dialect.db.SybaseDialect;
import com.jeesite.common.utils.Reflections;

import java.io.Serializable;
import java.util.Properties;

/**
 * Mybatis分页拦截器基类
 *
 * @since JDK 1.7.0+
 * @version V1.2.1 2017年3月17日
 * @author www.jeegit.org
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {

	private static final long serialVersionUID = 1L;

	protected static final String PAGE = "page";

	protected static final String DELEGATE = "delegate";

	protected static final String MAPPED_STATEMENT = "mappedStatement";

	protected Log log = LogFactory.getLog(this.getClass());

	protected Dialect DIALECT;

	/**
	 * 对参数进行转换和检查
	 * 
	 * @param parameterObject
	 *            参数对象
	 * @param page
	 *            分页对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {

		if (parameterObject instanceof Page) {
			return (Page<Object>) parameterObject;
		} else {
			return (Page<Object>) Reflections.getFieldValue(parameterObject, PAGE);
		}

	}

	/**
	 * 设置属性，支持自定义方言类和制定数据库的方式 </br>
	 * <code>dialectClass</code> </br>
	 * 自定义方言类。可以不配置这项 </br>
	 * <ode>dbms</ode> 数据库类型 插件支持的数据库 </br>
	 * <code>sqlPattern</code> 需要拦截的SQL ID
	 * 
	 * @param p
	 *            属性
	 */
	protected void initProperties(Properties p) {
		Dialect dialect = null;
		String dbType = Global.getConfig("jdbc.type");
		if ("db2".equals(dbType)) {
			dialect = new DB2Dialect();
		} else if ("derby".equals(dbType)) {
			dialect = new DerbyDialect();
		} else if ("h2".equals(dbType)) {
			dialect = new H2Dialect();
		} else if ("hsql".equals(dbType)) {
			dialect = new HSQLDialect();
		} else if ("mysql".equals(dbType)) {
			dialect = new MySQLDialect();
		} else if ("oracle".equals(dbType)) {
			dialect = new OracleDialect();
		} else if ("postgre".equals(dbType)) {
			dialect = new PostgreSQLDialect();
		} else if ("mssql".equals(dbType) || "sqlserver".equals(dbType)) {
			dialect = new SQLServer2005Dialect();
		} else if ("sybase".equals(dbType)) {
			dialect = new SybaseDialect();
		}
		if (dialect == null) {
			throw new RuntimeException("mybatis dialect error.");
		}
		DIALECT = dialect;

	}
}
