package ${package};

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class DatabindingComposer extends GenericForwardComposer {

	List<Person> model = new ArrayList<Person>();
	List<String> titleModel = new ArrayList<String>();
	Person selected;

	public DatabindingComposer() {
	}

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		model.add(new Person("Brian", "Engineer"));
		model.add(new Person("John", "Tester"));
		model.add(new Person("Sala", "Manager"));
		model.add(new Person("Peter", "Architect"));

		titleModel.add("Engineer");
		titleModel.add("Tester");
		titleModel.add("Manager");
		titleModel.add("Architect");
	}

	public List getModel() {
		return model;
	}

	public List getTitleModel() {
		return titleModel;
	}

	public Person getSelected() {
		return selected;
	}

	public void setSelected(Person selected) {
		this.selected = selected;
	}

	public class Person {
		public String name;
		public String title;

		public Person(String name, String title) {
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

}
