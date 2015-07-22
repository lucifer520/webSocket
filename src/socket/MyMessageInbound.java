package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import util.MessageUtil;

public class MyMessageInbound extends MessageInbound {

	private String name;
	public MyMessageInbound() {
		super();
	}

	public MyMessageInbound(String name) {
		super();
		this.name = name;
	}

	@Override  
    protected void onBinaryMessage(ByteBuffer arg0) throws IOException {  
        // TODO Auto-generated method stub 
          
    }  
  
    @Override  
    protected void onTextMessage(CharBuffer msg) throws IOException { 
    	//用户所发消息处理后的map
    	HashMap<String,String> messageMap = MessageUtil.getMessage(msg);
    	//上线用户集合类map
    	HashMap<String, MessageInbound> userMsgMap = InitServlet.getSocketList();
    	
    	String fromName = messageMap.get("fromName");
    	
  
    	String toName = messageMap.get("toName");
    	//获取该用户
    	MessageInbound messageInbound = userMsgMap.get(toName);
    	
    	
    	
    	if(messageInbound!=null){
    	 	WsOutbound outbound = messageInbound.getWsOutbound(); 
    	 	
    	 	
    	 	String content = messageMap.get("content");  //获取消息内容
    	 	String msgContentString = fromName + "     " + content;
    	 	
    	 	//发出去内容
    	 	CharBuffer toMsg =  CharBuffer.wrap(msgContentString.toCharArray());
            outbound.writeTextMessage(toMsg);  
            outbound.flush();
    	}
     
    	
    	
      /*  for (MessageInbound messageInbound : InitServlet.getSocketList()) {  
            CharBuffer buffer = CharBuffer.wrap(msg);  
            WsOutbound outbound = messageInbound.getWsOutbound();  
            outbound.writeTextMessage(buffer);  
            outbound.flush();  
        }  */
          
    }  
  
    @Override  
    protected void onClose(int status) {  
        InitServlet.getSocketList().remove(this);  
        super.onClose(status);  
    }  
  
    @Override  
    protected void onOpen(WsOutbound outbound) {  
        super.onOpen(outbound);  
        //登录的用户注册进去
        if(name!=null){
        	InitServlet.getSocketList().put(name, this);  
        }
//        InitServlet.getSocketList().add(this);  
    }  
      
      
}
