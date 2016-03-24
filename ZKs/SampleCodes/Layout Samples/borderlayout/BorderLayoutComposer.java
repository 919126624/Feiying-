package ${package};

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class BorderLayoutComposer extends GenericForwardComposer{
	
	ListModelList menuModel = new ListModelList();
	Listbox menuListbox;
	MenuNodeSelectListener listener = new MenuNodeSelectListener();
	MenuNodeItemRenderer renderer = new MenuNodeItemRenderer();
	Div contentDiv;
	
	
	public BorderLayoutComposer(){
		menuModel.add(new MenuNode("Function 1","${name}_fn1.zul"));
		menuModel.add(new MenuNode("Function 2","${name}_fn2.zul"));
		menuModel.add(new MenuNode("Function 3","${name}_fn3.zul"));
	}
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		menuListbox.setModel(menuModel);
		menuListbox.setItemRenderer(renderer);
		menuListbox.addEventListener(Events.ON_SELECT,listener);
	}

	class MenuNode {
		String label;
		String link;
		public MenuNode(String label,String link){
			this.label = label;
			this.link = link;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
	}
	
	class MenuNodeItemRenderer implements ListitemRenderer{

		public void render(Listitem item, Object data) throws Exception {
			MenuNode node = (MenuNode)data;
			item.setImage("icon-24x24.png");
			item.setLabel(node.getLabel());
			item.setValue(node);
		}
	}
	
	class MenuNodeSelectListener implements EventListener{
		public void onEvent(Event event) throws Exception {
			Listitem item = menuListbox.getSelectedItem();
			contentDiv.getChildren().clear();
			if(item!=null){
				MenuNode node = (MenuNode)item.getValue();
				Executions.createComponents(node.getLink(),contentDiv,null);
			}
		}		
	}
	
}
