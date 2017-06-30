package rda.agent.deletor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import com.ibm.agent.exa.AgentKey;
import com.ibm.agent.exa.AgentManager;
import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageFactory;
import com.ibm.agent.exa.client.AgentClient;
import com.ibm.agent.exa.client.AgentExecutor;
import java.util.Set;

/**
 * 全エージェントに削除指令を示すDISPOSEメッセージを送信するエグゼキュータ． エージェントはDISPOSEメッセージを受信すると，自ら削除処理を行う．
 * この削除処理はエージェントメッセージハンドラを抜けた後に行われる．
 */
public class Dispose implements AgentExecutor, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7920252877911662281L;

	@Override
	/**
	 * 全エージェントからのDISPOSEメッセージの返答を取得し，そのまま返す．
	 * resultsは，キーがエージェントキー，値がエージェントからの返答文字列
	 * であるHashMapのコレクションである．HashMapはエージェント実行環境 ごとに生成されたものである．
	 */
	public Object complete(Collection<Object> results) {
		return results;
	}

	@Override
	/**
	 * 全エージェントにDISPOSEメッセージを送信する．
	 */
	public Object execute() {
		try {
			AgentManager agentManager = AgentManager.getAgentManager();
			// DISPOSEメッセージの生成
			MessageFactory factory = MessageFactory.getFactory();
			Message msg = factory.getMessage("dispose");

			// DISPOSEメッセージをSYNC-BROADCASTで全エージェントに送信
			HashMap<AgentKey, Object> ret = agentManager.sendMessage(msg);
			return ret;
		} catch (IllegalAccessException | InstantiationException e) {
			return e;
		}
	}

	public void delete(AgentClient client) {
		try {
			// エージェントエグゼキュータを生成
			Dispose executor = new Dispose();

			// エージェントエグゼキュータを全エージェント実行環境に転送し，
			// その集約結果を取得．集約結果は，completeメソッドの戻り値．
			Object ret = client.execute(executor);

			// 全エージェント実行環境からの結果を取得
			Collection<Object> retFromAllServers = (Collection<Object>) ret;
			for (Object o : retFromAllServers) {
				// 各エージェント実行環境でのDISPOSEメッセージの戻り値を取得．
				// DISPOSEメッセージはSYNC-BROADCASTで送信されているので，
				// 処理結果はHashMapとなる．
				HashMap<AgentKey, Object> retFromAgents = (HashMap<AgentKey, Object>) o;
				Set<AgentKey> keySet = retFromAgents.keySet();
				for (AgentKey agentKey : keySet) {
					String message = (String) retFromAgents.get(agentKey);
					//System.out.println("Delete [" + agentKey + "] was deleted. Reply is [" + message + "]");
				}
			}
		} catch (Exception e) {
		}
	}
}
