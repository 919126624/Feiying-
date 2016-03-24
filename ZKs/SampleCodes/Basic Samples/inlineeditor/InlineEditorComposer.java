package ${package};

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

public class InlineEditorComposer extends GenericForwardComposer{
	
	ListModelList model = new ListModelList();
	Listbox listbox;
	InlineItemRenderer renderer = new InlineItemRenderer();
	
	
	public InlineEditorComposer(){
		model.add(new Bean("Brian","Engineer"));
		model.add(new Bean("John","Tester"));
		model.add(new Bean("Sala","Project Manager"));
		model.add(new Bean("Peter","Architect"));
	}
	
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		listbox.setModel(model);
		listbox.setItemRenderer(renderer);
	}

	public class Bean {
		public String name;
		public String title;

		public Bean(String name, String title) {
			this.name = name;
			this.title = title;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}
	
	class InlineItemRenderer implements ListitemRenderer{

		public void render(Listitem item, Object data) throws Exception {
			final Bean node = (Bean)data;
			item.setValue(node);
			
			new InlineSwitcher(item,new Label(),new Textbox()){
				protected void initialValue(){
					label.setValue(node.getName());
					textbox.setValue(node.getName());
				}
				protected void commitValue(){
					node.setName(textbox.getValue());
					label.setValue(textbox.getValue());
				}
			};
			
			new InlineSwitcher(item,new Label(),new Textbox()){
				protected void initialValue(){
					label.setValue(node.getTitle());
					textbox.setValue(node.getTitle());
				}
				protected void commitValue(){
					node.setTitle(textbox.getValue());
					label.setValue(textbox.getValue());
				}
			};
		}
		
	}
	
	class InlineSwitcher extends Div implements EventListener{

		Label label;
		Textbox textbox;
		Listitem item;
		public InlineSwitcher(Listitem item,Label label,Textbox textbox){
			Listcell cell = new Listcell();
			cell.setParent(item);
			this.item = item;
			this.label = label;
			this.textbox = textbox;
			textbox.setWidth("99%");
			this.appendChild(label);
			this.appendChild(textbox);
			
			this.addEventListener(Events.ON_CLICK,this);
			label.addEventListener(Events.ON_CLICK,this);
			textbox.addEventListener(Events.ON_OK,this);
			textbox.addEventListener(Events.ON_BLUR,this);
			textbox.setVisible(false);
			
			initialValue();
			this.setParent(cell);
		}
		
		public void onEvent(Event event) throws Exception {
			String evt = event.getName();
			if(Events.ON_CLICK.equals(evt)){
				label.setVisible(false);
				textbox.setVisible(true);
				textbox.focus();
				listbox.setSelectedItem(item);
			}else if(Events.ON_OK.equals(evt) || Events.ON_BLUR.equals(evt)){
				label.setValue(textbox.getValue());
				label.setVisible(true);
				textbox.setVisible(false);
				commitValue();
				listbox.focus();
			}
		}
		
		protected void initialValue(){
			
		}
		
		protected void commitValue(){
			
		}
		
		
	}
	
}
