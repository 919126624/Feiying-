package ${package};

import org.zkoss.Version;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

public class HelloZKComposer extends GenericForwardComposer{

	Button sayHelloBtn;
	Window main;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		main.setTitle("Hello ZK");
		main.setBorder("normal");
	}
	
	public void onClick$sayHelloBtn(){
		showMsg();
	}
	
	void showMsg(){
		String message = "You are running ZK Successfully, The ZK Version is "+Version.UID;
		alert(message);
	}
	
}
