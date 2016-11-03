package rda.agent.deletor.handler;

import com.ibm.agent.exa.Message;
import com.ibm.agent.exa.MessageHandler;

/**
 * DISPOSEメッセージのメッセージハンドラ． エージェントの削除を行う．
 */
public class DisposeHandler extends MessageHandler {

    @Override
    public Object onMessage(Message msg) throws Exception {
        try {
            //エージェントの削除．
            disposeAgent();
            return getAgentKey() + " was disposed";
        } catch (Exception e) {
            throw e;
        }
    }
}
