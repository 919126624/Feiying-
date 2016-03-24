package ${package};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.breeze.Themes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MaximizeEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

public class BussinessPortalCtrl extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	/**
	 * portletInfos contains <Title String, PortletInfo> in it
	 */
	private static final List<PortletInfo> PORTLET_INFOS = new ArrayList<PortletInfo>();
	static {
		// Initialize Portlet info objects
		PORTLET_INFOS
				.add(new PortletInfo(
						"Major World Indices",
						"http://www.gmodules.com/ig/ifr?url=http://hosting.gmodules.com/ig/gadgets/file/105680194559159823787/world_indices.xml&amp;synd=open&amp;w=220&amp;h=300&amp;title=Major+World+Indices%3A+Live+Status&amp;border=%23ffffff%7C3px%2C1px+solid+%23999999&amp;source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%252F%252Fhosting.gmodules.com%252Fig%252Fgadgets%252Ffile%252F105680194559159823787%252Fworld_indices.xml",
						"http://ventura1.acesphere.com/Equity/WorldIndices.aspx", 
						"Iframe", "no", "180px"));
		PORTLET_INFOS
				.add(new PortletInfo(
						"The Wall Street Journal",
						"http://www.gmodules.com/ig/ifr?url=http://hosting.gmodules.com/ig/gadgets/file/100674619146546250953/wsj.xml&amp;up_entries=5&amp;up_refresh=60&amp;synd=open&amp;w=250&amp;h=300&amp;title=&amp;border=%23ffffff%7C3px%2C1px+solid+%23999999&amp;source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%2F%2Fhosting.gmodules.com%2Fig%2Fgadgets%2Ffile%2F100674619146546250953%2Fwsj.xml%26pt%3D%2526context%253Dd%2526type%253Dgadgets%2526synd%253Dig%2526lang%253Den-gb%2526.lang%253Den-gb%2526country%253Dau%2526.country%253Dau%2526start%253D0%2526num%253D1%2526target%253DASsh%2526objs%253D%26sn%3DASsh",
						"http://online.wsj.com/public/page/news-financial-markets-stock.html?mod=WSJ_footer",
						"Iframe", "no", "410px"));
		PORTLET_INFOS.add(new PortletInfo("Personal",
						"/${name}/personal_portlet.zul",
						"/${name}/personal_portlet_max.zul", 
						"Include", null, "210px"));
		PORTLET_INFOS
				.add(new PortletInfo(
						"ZK News",
						"http://www.gmodules.com/ig/ifr?url=http://hosting.gmodules.com/ig/gadgets/file/102970516680803748444/kienthuctaichinh2.xml&up_title=&up_tabFontSize=10px&up_showFeedDesc=0&up_feed1=http%3A%2F%2Ffeeds.feedburner.com%2Fzkoss&up_feedTitle1=News&up_feed2=http%3A%2F%2Ffeeds.feedburner.com%2Fthezkblog&up_feedTitle2=Blog&up_feed3=%20&up_feedTitle3=%20%20&up_feed4=%20&up_feedTitle4=%20&up_entries=5&up_summaries=100&up_renderHtml=1&up_showTimestamp=0&up_selectedTab=&synd=open&w=240&h=300&title=&border=%23ffffff%7C3px%2C1px+solid+%23999999&source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%252F%252Fhosting.gmodules.com%252Fig%252Fgadgets%252Ffile%252F102970516680803748444%252Fkienthuctaichinh2.xml",
						"http://www.gmodules.com/ig/ifr?url=http://hosting.gmodules.com/ig/gadgets/file/102970516680803748444/kienthuctaichinh2.xml&up_title=&up_tabFontSize=10px&up_showFeedDesc=1&up_feed1=http%3A%2F%2Ffeeds.feedburner.com%2Fzkoss&up_feedTitle1=News&up_feed2=http%3A%2F%2Ffeeds.feedburner.com%2Fthezkblog&up_feedTitle2=Blog&up_feed3=%20&up_feedTitle3=%20%20&up_feed4=%20&up_feedTitle4=%20&up_entries=5&up_summaries=0&up_renderHtml=1&up_showTimestamp=0&up_selectedTab=1&synd=open&w=240&h=300&title=&border=%23ffffff%7C3px%2C1px+solid+%23999999&source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%252F%252Fhosting.gmodules.com%252Fig%252Fgadgets%252Ffile%252F102970516680803748444%252Fkienthuctaichinh2.xml",
						"Iframe", "yes", "380px"));
		PORTLET_INFOS
				.add(new PortletInfo(
						"BBC News",
						"http://www.gmodules.com/ig/ifr?url=http://nvmodules.netvibes.com/widget/gspec%3FuwaUrl%3Dhttp%253A%252F%252Fwww.netvibes.com%252Fmodules%252FmultipleFeeds%252FmultipleFeeds.php%253Fprovider%253Dcustom%2526url%253Dhttp%25253A%25252F%25252Feco.netvibes.com%25252Fuwa%25252Fmultifeed%25252F306%25252F394%26title%3DBBC%2BFinance%2BNews%26description%3DMultFeed%2BWidget%2B-%2BBBC%2BBusiness%2BNews%2BUK%2B-%2BBusiness%252CPersonal%2BFinance%252CEconomy%2Band%2BCompanies%2BNews%26author%3Dkdhuk%26email%3Dkdharrowmailbox-netvibes%2540yahoo.co.uk&up_category=&up_view=&up_nbTitles=4&up_details=1&up_showDate=0&up_openOutside=0&up_videoAutoPlay=false&up_numberTabs=1&up_selectedTab=0&up_title=MultipleFeeds&up_lookForHtmlThumbnail=true&up_provider=google&up_url=&up_lastSearch=__undefined__&up_showTweet=1&up_setNbTitles=true&synd=open&w=350&h=400&title=&border=%23ffffff%7C3px%2C1px+solid+%23999999&source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%2F%2Fnvmodules.netvibes.com%2Fwidget%2Fgspec%253FuwaUrl%253Dhttp%25253A%25252F%25252Fwww.netvibes.com%25252Fmodules%25252FmultipleFeeds%25252FmultipleFeeds.php%25253Fprovider%25253Dcustom%252526url%25253Dhttp%2525253A%2525252F%2525252Feco.netvibes.com%2525252Fuwa%2525252Fmultifeed%2525252F306%2525252F394%2526title%253DBBC%252BFinance%252BNews%2526description%253DMultFeed%252BWidget%252B-%252BBBC%252BBusiness%252BNews%252BUK%252B-%252BBusiness%25252CPersonal%252BFinance%25252CEconomy%252Band%252BCompanies%252BNews%2526author%253Dkdhuk%2526email%253Dkdharrowmailbox-netvibes%252540yahoo.co.uk%26pt%3D%2526context%253Dd%2526type%253Dgadgets%2526synd%253Dig%2526lang%253Dzh-tw%2526.lang%253Dzh-tw%2526country%253Dtw%2526.country%253Dtw%2526start%253D0%2526num%253D1%2526target%253D3758%2526objs%253D%26sn%3D3758",
						"http://www.gmodules.com/ig/ifr?url=http://nvmodules.netvibes.com/widget/gspec%3FuwaUrl%3Dhttp%253A%252F%252Fwww.netvibes.com%252Fmodules%252FmultipleFeeds%252FmultipleFeeds.php%253Fprovider%253Dcustom%2526url%253Dhttp%25253A%25252F%25252Feco.netvibes.com%25252Fuwa%25252Fmultifeed%25252F306%25252F394%26title%3DBBC%2BFinance%2BNews%26description%3DMultFeed%2BWidget%2B-%2BBBC%2BBusiness%2BNews%2BUK%2B-%2BBusiness%252CPersonal%2BFinance%252CEconomy%2Band%2BCompanies%2BNews%26author%3Dkdhuk%26email%3Dkdharrowmailbox-netvibes%2540yahoo.co.uk&up_category=&up_view=&up_nbTitles=6&up_details=1&up_showDate=1&up_openOutside=0&up_videoAutoPlay=true&up_numberTabs=4&up_selectedTab=0&up_title=MultipleFeeds&up_lookForHtmlThumbnail=true&up_provider=google&up_url=&up_lastSearch=__undefined__&up_showTweet=1&up_setNbTitles=true&synd=open&w=350&h=400&title=&border=%23ffffff%7C3px%2C1px+solid+%23999999&source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%2F%2Fnvmodules.netvibes.com%2Fwidget%2Fgspec%253FuwaUrl%253Dhttp%25253A%25252F%25252Fwww.netvibes.com%25252Fmodules%25252FmultipleFeeds%25252FmultipleFeeds.php%25253Fprovider%25253Dcustom%252526url%25253Dhttp%2525253A%2525252F%2525252Feco.netvibes.com%2525252Fuwa%2525252Fmultifeed%2525252F306%2525252F394%2526title%253DBBC%252BFinance%252BNews%2526description%253DMultFeed%252BWidget%252B-%252BBBC%252BBusiness%252BNews%252BUK%252B-%252BBusiness%25252CPersonal%252BFinance%25252CEconomy%252Band%252BCompanies%252BNews%2526author%253Dkdhuk%2526email%253Dkdharrowmailbox-netvibes%252540yahoo.co.uk%26pt%3D%2526context%253Dd%2526type%253Dgadgets%2526synd%253Dig%2526lang%253Dzh-tw%2526.lang%253Dzh-tw%2526country%253Dtw%2526.country%253Dtw%2526start%253D0%2526num%253D1%2526target%253D3758%2526objs%253D%26sn%3D3758",
						"Iframe", "no", "595px"));
		PORTLET_INFOS
				.add(new PortletInfo(
						"Hurricane Watcher",
						"http://www.gmodules.com/ig/ifr?url=http://www.daylightmap.com/clouds/hurricane_gadget.xml&amp;up_atlantic=1&amp;up_east_pacific=1&amp;up_central_pacific=1&amp;up_west_pacific=1&amp;up_indian=1&amp;up_units=miles%20per%20hour&amp;synd=open&amp;w=300&amp;h=310&amp;title=&amp;border=%23ffffff%7C3px%2C1px+solid+%23999999&amp;source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%2F%2Fwww.daylightmap.com%2Fclouds%2Fhurricane_gadget.xml%26pt%3D%2526context%253Dd%2526type%253Dgadgets%2526synd%253Dig%2526lang%253Den-gb%2526.lang%253Den-gb%2526country%253Dau%2526.country%253Dau%2526start%253D0%2526num%253D1%2526target%253DYU8%2526objs%253D%26sn%3DYU8",
						"http://www.gmodules.com/ig/ifr?url=http://www.daylightmap.com/clouds/hurricane_gadget.xml&amp;up_atlantic=1&amp;up_east_pacific=1&amp;up_central_pacific=1&amp;up_west_pacific=1&amp;up_indian=1&amp;up_units=miles%20per%20hour&amp;synd=open&amp;w=300&amp;h=310&amp;title=&amp;border=%23ffffff%7C3px%2C1px+solid+%23999999&amp;source=http%3A%2F%2Fwww.gmodules.com%2Fig%2Fcreator%3Fsynd%3Dopen%26url%3Dhttp%253A%2F%2Fwww.daylightmap.com%2Fclouds%2Fhurricane_gadget.xml%26pt%3D%2526context%253Dd%2526type%253Dgadgets%2526synd%253Dig%2526lang%253Den-gb%2526.lang%253Den-gb%2526country%253Dau%2526.country%253Dau%2526start%253D0%2526num%253D1%2526target%253DYU8%2526objs%253D%26sn%3DYU8",
						"Iframe", "no", "400px"));

	}
	private static final String KEY_PANEL_INFO = "KEY_PANEL_INFO";

	// Autowired : Add Button
	Button addbtn;
	// Autowired : Radio box group
	Radiogroup columnSelect;
	// Autowired : Select demo list
	Listbox widgetListbox;
	// Autowired : Portallayout
	Portallayout bussinessPortal;
	Portalchildren ptc1, ptc2, ptc3;
	// Autowired : Column change Buttons
	Button singleColumnBtn, twoColumnBtn, threeColumnBtn;

	public void doAfterCompose(Component Comp) throws Exception {
		super.doAfterCompose(Comp);
		// Add Theme Class to window for customize Breeze theme (Customize.css)
		((Window) Comp).setSclass(Themes.getTheme(execution));
		// Initialize widgets collection list
		widgetListbox.setModel(new ListModelList(PORTLET_INFOS));
		widgetListbox.setSelectedIndex(0);

		initPortlets();

	}

	// Event Handling
	/**
	 * Action of click the add Button(addbtn)
	 */
	public void onClick$addbtn(Event e) {
		int selectedColumnIdx = columnSelect.getSelectedIndex();
		Portalchildren pc = (Portalchildren) bussinessPortal.getChildren().get(selectedColumnIdx);
		pc.appendChild(createPanel(PORTLET_INFOS.get(widgetListbox.getSelectedIndex())));
	}

	public void onClick$singleColumnBtn(Event e) {
		ptc1.setWidth("100%");
		ptc2.setVisible(false);
		ptc3.setVisible(false);
		singleColumnBtn.setDisabled(true);
		twoColumnBtn.setDisabled(false);
		threeColumnBtn.setDisabled(false);
	}

	public void onClick$twoColumnBtn(Event e) {
		ptc1.setWidth("50%");
		ptc2.setWidth("50%");
		ptc2.setVisible(true);
		ptc3.setVisible(false);
		singleColumnBtn.setDisabled(false);
		twoColumnBtn.setDisabled(true);
		threeColumnBtn.setDisabled(false);
	}

	public void onClick$threeColumnBtn(Event e) {
		ptc1.setWidth("30%");
		ptc2.setWidth("40%");
		ptc2.setVisible(true);
		ptc3.setWidth("30%");
		ptc3.setVisible(true);
		singleColumnBtn.setDisabled(false);
		twoColumnBtn.setDisabled(false);
		threeColumnBtn.setDisabled(true);
	}

	private void initPortlets() {
		// Add widget to portallayout column 1
		ptc1.appendChild(createPanel(PORTLET_INFOS.get(0)));
		ptc1.appendChild(createPanel(PORTLET_INFOS.get(1)));
		// Add widget to portallayout column 2
		ptc2.appendChild(createPanel(PORTLET_INFOS.get(2)));
		ptc2.appendChild(createPanel(PORTLET_INFOS.get(3)));
		// Add widget to portallayout column 3
		ptc3.appendChild(createPanel(PORTLET_INFOS.get(4)));
	}

	private Panel createPanel(PortletInfo info) {
		Panel p = initPanel(new Panel());// Get a panel template
		p.setAttribute(KEY_PANEL_INFO, info);
		p.setHeight(info.getMinSize());
		p.setTitle(info.getName());
		if (info.getType().equals("Iframe")) {
			Iframe ifr = new Iframe();
			ifr.setSrc(info.getUrlMin());
			ifr.setScrolling(info.getScroll());
			ifr.setWidth("100%");
			ifr.setHeight("100%");
			p.getPanelchildren().appendChild(ifr);
		} else {
			Include inc = new Include(info.getUrlMin());
			inc.setWidth("100%");
			inc.setHeight("100%");
			p.getPanelchildren().appendChild(inc);
		}
		return p;
	}

	private Panel initPanel(Panel panel) {
		panel.setBorder("normal");
		panel.setCollapsible(true);
		panel.setClosable(true);
		panel.setMaximizable(true);
		panel.appendChild(new Panelchildren());
		panel.addEventListener(Events.ON_MAXIMIZE, new EventListener() {
			// This event listener handle the click of maximize button of panel
			@Override
			public void onEvent(Event event) throws Exception {
				MaximizeEvent maxEvent = (MaximizeEvent) event;
				Panel p = (Panel) maxEvent.getTarget();
				PortletInfo pInfo = (PortletInfo) p.getAttribute(KEY_PANEL_INFO);

				Component innerComp = (Component) p.getPanelchildren().getChildren().get(0);
				if (pInfo.getType().equals("Iframe")) { // External Resource
					((Iframe) innerComp).setSrc(maxEvent.isMaximized() ? pInfo.getUrlMax() : pInfo
							.getUrlMin());
				} else {// Internal Resource - we do not use Iframe
					((Include) innerComp).setSrc(maxEvent.isMaximized() ? pInfo.getUrlMax() : pInfo
							.getUrlMin());
				}
			}
		});
		return panel;
	}
}

/**
 * PortletInfo save resource of each portlet <br />
 * 1. urlMin : Widget default url <br />
 * 2. urlMax : Widget maximized url <br />
 * 3. type : use Iframe or Include <br />
 * 4. scroll : Weather the inner Iframe scrolling <br />
 * 4. minSize : Default height of widget
 * 
 * @author Ryan
 * 
 */
class PortletInfo {
	private String urlMin;
	private String urlMax;
	private String type;
	private String scroll;
	private String minSize;
	private String name;

	public PortletInfo(String name, String urlMin, String urlMax, String type, String scroll,
			String minSize) {
		this.urlMin = urlMin;
		this.urlMax = urlMax;
		this.type = type;
		this.scroll = scroll;
		this.minSize = minSize;
		this.name = name;
	}

	public String getUrlMin() {
		return urlMin;
	}

	public String getUrlMax() {
		return urlMax;
	}

	public String getType() {
		return type;
	}

	public String getScroll() {
		return scroll;
	}

	public String getMinSize() {
		return minSize;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
}
