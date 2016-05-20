package Db;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
/**
 * ���ݿ���ʰ�����
 * 
 * @author ocean
 * 
 */
public class SqlHelper {
 
	private static String connName="db";	//�����ļ��е���������
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static CallableStatement callableStatement = null;
 
    
   
    /**
     * ���ڲ�ѯ�����ؽ����
     * 
     * @param sql
     *            sql���
     * @return �����
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List query(String sql) throws SQLException {
 
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();
 
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
 
    }
 
    /**
     * ���ڴ������Ĳ�ѯ�����ؽ����
     * 
     * @param sql
     *            sql���
     * @param paramters
     *            ��������
     * @return �����
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List query(String sql, Object... paramters)
            throws SQLException {
 
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * ���ص��������ֵ����count\min\max�ȵ�
     * 
     * @param sql
     *            sql���
     * @return �����
     * @throws SQLException
     */
    public static Object getSingle(String sql) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
 
    }
 
    /**
     * ���ص������ֵ����count\min\max��
     * 
     * @param sql
     *            sql���
     * @param paramters
     *            �����б�
     * @return ���
     * @throws SQLException
     */
    public static Object getSingle(String sql, Object... paramters)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * ������ɾ��
     * 
     * @param sql
     *            sql���
     * @return Ӱ������
     * @throws SQLException
     */
    public static int update(String sql) throws SQLException {
 
        try {
            getPreparedStatement(sql);
 
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * ������ɾ�ģ���������
     * 
     * @param sql
     *            sql���
     * @param paramters
     *            sql���
     * @return Ӱ������
     * @throws SQLException
     */
    public static int update(String sql, Object... paramters)
            throws SQLException {
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * ����ֵ�󷵻�����ֵ
     * 
     * @param sql
     *            ����sql���
     * @return ���ؽ��
     * @throws Exception
     */
    public static Object insertWithReturnPrimeKey(String sql)
            throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
        	conn = DBConnectionManager.getInstance().getConnection(connName);
            preparedStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
 
    /**
     * ����ֵ�󷵻�����ֵ
     * 
     * @param sql
     *            ����sql���
     * @param paramters
     *            �����б�
     * @return ���ؽ��
     * @throws SQLException
     */
    public static Object insertWithReturnPrimeKey(String sql,
            Object... paramters) throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
        	conn = DBConnectionManager.getInstance().getConnection(connName);
            preparedStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
 
    }
 
    /**
     * ���ô洢����ִ�в�ѯ
     * 
     * @param procedureSql
     *            �洢����
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List callableQuery(String procedureSql) throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * ���ô洢���̣���������,ִ�в�ѯ
     * 
     * @param procedureSql
     *            �洢����
     * @param paramters
     *            ������
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public static List callableQuery(String procedureSql, Object... paramters)
            throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
 
            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * ���ô洢���̣���ѯ����ֵ
     * 
     * @param procedureSql
     * @return
     * @throws SQLException
     */
    public static Object callableGetSingle(String procedureSql)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * ���ô洢����(������)����ѯ����ֵ
     * 
     * @param procedureSql
     * @param parameters
     * @return
     * @throws SQLException
     */
    public static Object callableGetSingle(String procedureSql,
            Object... paramters) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
 
            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    public static Object callableWithParamters(String procedureSql)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            callableStatement.registerOutParameter(0, Types.OTHER);
            callableStatement.execute();
            return callableStatement.getObject(0);
 
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
 
    }
 
    /**
     * ���ô洢���̣�ִ����ɾ��
     * 
     * @param procedureSql
     *            �洢����
     * @return Ӱ������
     * @throws SQLException
     */
    public static int callableUpdate(String procedureSql) throws SQLException {
        try {
            getCallableStatement(procedureSql);
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * ���ô洢���̣�����������ִ����ɾ��
     * 
     * @param procedureSql
     *            �洢����
     * @param parameters
     * @return Ӱ������
     * @throws SQLException
     */
    public static int callableUpdate(String procedureSql, Object... parameters)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            for (int i = 0; i < parameters.length; i++) {
                callableStatement.setObject(i + 1, parameters[i]);
            }
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * ������������
     * 
     * @param sqlList
     *            һ��sql
     * @return
     */
    public static int[] batchUpdate(List<String> sqlList) {
 
        int[] result = new int[] {};
        Statement statenent = null;
        try {
        	conn = DBConnectionManager.getInstance().getConnection(connName);
            conn.setAutoCommit(false);
            statenent = conn.createStatement();
            for (String sql : sqlList) {
                statenent.addBatch(sql);
            }
            result = statenent.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                throw new ExceptionInInitializerError(e1);
            }
            throw new ExceptionInInitializerError(e);
        } finally {
            free(statenent, null);
        }
        return result;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static List ResultToListMap(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            Map map = new HashMap();
            for (int i = 1; i < md.getColumnCount(); i++) {
                map.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
 
    /**
     * ��ȡPreparedStatement
     * 
     * @param sql
     * @throws SQLException
     */
    private static void getPreparedStatement(String sql) throws SQLException {
    	conn = DBConnectionManager.getInstance().getConnection(connName);
        preparedStatement = conn.prepareStatement(sql);
    }
 
    /**
     * ��ȡCallableStatement
     * 
     * @param procedureSql
     * @throws SQLException
     */
    private static void getCallableStatement(String procedureSql)
            throws SQLException {
    	conn = DBConnectionManager.getInstance().getConnection(connName);
        callableStatement = conn.prepareCall(procedureSql);
    }
 
    /**
     * �ͷ���Դ
     * 
     * @param rs
     *            �����
     */
    public static void free(ResultSet rs) {
        if(conn!=null){
        	try {
        		conn.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        if(preparedStatement!=null){
        	try {
        		preparedStatement.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        if(rs!=null){
        	try {
        		rs.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        
    }
 
    /**
     * �ͷ���Դ
     * 
     * @param statement
     * @param rs
     */
    public static void free(Statement statement, ResultSet rs) {
    	if(conn!=null){
        	try {
        		conn.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        if(statement!=null){
        	try {
        		statement.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        if(rs!=null){
        	try {
        		rs.close();
        	} catch (SQLException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
    }
 
    /**
     * �ͷ���Դ
     */
    public static void free() {
 
        free(null);
    }
 
}