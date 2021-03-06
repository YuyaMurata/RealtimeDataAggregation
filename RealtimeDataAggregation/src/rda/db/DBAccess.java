package rda.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import com.ibm.agent.soliddb.catalog.RegionCatalog;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBCで対応するリージョンのレプリカ用solidDBにアクセスする．
 * エージェントエグゼキュータは，エージェント実行環境のサーバにて，
 * そのサーバのリージョンに対応するレプリカ用solidDBにJDBCで
 * 接続して，そのリージョンの顧客属性レコードの数を得る．最終的に
 * 全リージョンの顧客属性レコードの数を表示する．
 */
public class DBAccess implements AgentExecutor, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8284826433740843048L;

	public DBAccess() {
	}
	
	String app, agenttype;
	public DBAccess(String app, String agenttype) {
		this.app = app;
		this.agenttype = agenttype;
	}
	
	@Override
	/**
	 * 各リージョンの顧客属性レコード数のコレクションを返す
	 */
	public Object complete(Collection<Object> results) {
		return results;
	}

	@Override
	/**
	 * エージェント実行環境のサーバにて，そのサーバのリージョンに対応する
	 * レプリカ用solidDBにJDBCで接続して，顧客属性レコードの数を得る
	 */
	public Object execute() {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			// このサーバのリージョン名を取得する
			AgentManager agentManager = AgentManager.getAgentManager();
			String regionName = agentManager.getRegionName();
			
			// JDBCドライバの接続パラメータにリージョン名を指定する
			Properties props = new Properties();
			props.put("region", regionName);
			
			// JDBC接続を得る
			con = DriverManager.getConnection("jdbc:ceta:"+app, props);
			
			System.out.println("app="+app);
			System.out.println("type="+agenttype);
			
			// レコード数を得るSQLを生成し，検索を行う．
			stmt = con.prepareStatement("select * from "+agenttype);
			ResultSet rs = stmt.executeQuery();
			
			Map map = new HashMap();
			while(rs.next())
				map.put(rs.getString(1),rs.getLong(2));
			
			return map;
		} catch(Exception e) {
			e.printStackTrace();
			return e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Object dump(AgentClient client, String app, String type) {
		try {
			//System.out.println("region names:" + RegionCatalog.getInstance("localhost:2809").getRegionNameList());
			DBAccess executor = new DBAccess(app, type);
			
			Object ret = client.execute(executor);
			Collection<Object> col = (Collection<Object>)ret;
			
			Map map = new HashMap();
			for(Object o : col) {
				map.putAll((Map)o);
			}
			
			return map;
		} catch(Exception e) {
			e.printStackTrace();
			return e;
		}
	}
}
