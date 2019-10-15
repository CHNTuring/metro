package metro;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalComboBoxButton;

import metro.Dijkstra;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

class BackgroundPanel extends JPanel {

	/** 
	 *  
	 */
	private static final long serialVersionUID = -6352788025440244338L;

	private Image image = null;

	public BackgroundPanel(Image image) {
		this.image = image;
	}

	// 固定背景图片，允许这个JPanel可以在图片上添加其他组件
	protected void paintComponent(Graphics g) {
		g.drawImage(image, this.getWidth() * 13 / 36, 0, this.getWidth() * 2 / 3, this.getHeight(), this);
	}
}

public class Subway extends JFrame implements ActionListener {

	private JPanel toolBar = new JPanel();
	private BackgroundPanel workPane = new BackgroundPanel(new ImageIcon("src/pic.png").getImage());
	private JComboBox<String> comboBox = null;
	private JComboBox<String> comboBox_1 = null;
	private JComboBox<String> comboBox_2 = null;
	private JComboBox<String> comboBox_3 = null;
	private JLabel startLab = new JLabel();
	private JLabel endLab = new JLabel();
	private JTextArea result = new JTextArea();
	private JButton btnOk = new JButton("确定");
	private JButton btnCancel = new JButton("重置");

	private List<String> lines = null/* new ArrayList(map.keySet()) */;
	private Map<String, List<Station>> m = null;
	private List<Station> allStations = null;
	private int[][] graph = null;

	/**
	 * Create the application.
	 */
	public Subway(Map<String, List<Station>> m, List<Station> allStations, int[][] graph) {
		JOptionPane.showMessageDialog(null, "欢迎使用北京地铁换乘查询系统", "北京地铁换乘查询系统提醒您", JOptionPane.INFORMATION_MESSAGE);
		this.lines = new LinkedList(m.keySet());
		this.allStations = allStations;
		this.m = m;
		this.graph = graph;

		this.setTitle("北京地铁换乘查询系统");
		this.setIconImage(new ImageIcon("src/logo.png").getImage());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "感谢您的使用，再见", "北京地铁换乘查询系统提醒您", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		});
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);

		initialize();

		this.workPane.setLayout(null);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		int inWidth = (int) width * 3 / 5;
		int inHeight = (int) height * 2 / 3;

		this.setSize((int) width * 3 / 5, (int) height * 2 / 3);

		this.setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);

		this.btnOk.addActionListener(this);
		this.btnCancel.addActionListener(this);

		this.result.setBorder(new LineBorder(null, 1));
		this.result.setFont(new Font("宋体", Font.BOLD, 15));

		this.startLab.setText("请选择起点");
		this.endLab.setText("请选择终点");

		workPane.add(comboBox);
		workPane.add(comboBox_1);
		workPane.add(comboBox_2);
		workPane.add(comboBox_3);
		workPane.add(startLab);
		workPane.add(endLab);
		workPane.add(result);

		this.workPane.addComponentListener(new ComponentAdapter() {// 拖动窗口监听
			public void componentResized(ComponentEvent e) {
				int inWidth = workPane.getWidth();// 获取窗口宽度
				int inHeight = workPane.getHeight();// 获取窗口高度
				comboBox.setBounds(inWidth / 50, inHeight / 20, inWidth / 7, inHeight / 20);
				comboBox_1.setBounds(inWidth / 50, inHeight * 1 / 8, inWidth / 7, inHeight / 20);
				comboBox_2.setBounds(inWidth * 8 / 45, inHeight / 20, inWidth / 7, inHeight / 20);
				comboBox_3.setBounds(inWidth * 8 / 45, inHeight * 1 / 8, inWidth / 7, inHeight / 20);
				result.setBounds(inWidth / 50, inHeight * 1 / 5, inWidth * 21 / 70, inHeight * 4 / 5);

				startLab.setBounds(inWidth / 50, inHeight / 160, inWidth / 7, inHeight / 20);
				endLab.setBounds(inWidth * 8 / 45, inHeight / 160, inWidth / 7, inHeight / 20);
			}

		});
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.validate();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] lineStaions = new String[this.lines.size() + 1];
		((LinkedList) this.lines).addFirst("请选择地铁线路");
		((LinkedList) this.lines).toArray(lineStaions);
		lineStaions[0] = "请选择地铁线路";

		this.comboBox = new JComboBox<>(lineStaions);
		this.comboBox_1 = new JComboBox<>(new String[] { "请先选择地铁线路" });
		this.comboBox_2 = new JComboBox<>(lineStaions);
		this.comboBox_3 = new JComboBox<>(new String[] { "请先选择地铁线路" });

		this.validate();
		String lineName1 = comboBox.getSelectedItem().toString();
		String lineName2 = comboBox_2.getSelectedItem().toString();

		// Line line1 = lineMap.get(lineName1);
		// Line line2 = lineMap.get(lineName2);
		this.comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String ftName = comboBox.getSelectedItem().toString();
					comboBox_1.setModel(selectCMB(ftName));
				}
			}
		});
		this.comboBox_2.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String ftName = comboBox_2.getSelectedItem().toString();
					comboBox_3.setModel(selectCMB(ftName));
				}
			}
		});
	}

	public ComboBoxModel selectCMB(String ftName) {

		List<Station> openStations = this.m.get(ftName);

		int size = -1;
		if (openStations == null) {
			size = 0;
		} else {
			size = openStations.size();
		}
		String[] stationNames = new String[size + 1];
		stationNames[0] = "请选择站点";
		int index = 1;
		for (int k = 1; k <= size; k++) {
			stationNames[k] = openStations.get(k - 1).getStaName();

		}

		ComboBoxModel aModel1 = new DefaultComboBoxModel(stationNames);
		return aModel1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String startLineName = null;
		String endLineName = null;
		Station startStation = null;
		Station endStation = null;
		if (e.getSource() == this.btnCancel) {
			this.comboBox.setSelectedIndex(0);
			this.comboBox_2.setSelectedIndex(0);
			this.result.setText(null);
		} else {
			if (this.comboBox.getSelectedIndex() < 1 || this.comboBox_1.getSelectedIndex() < 1) {
				JOptionPane.showMessageDialog(null, "请选择起始站点", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			startLineName = this.comboBox.getSelectedItem().toString();
			if (this.comboBox_2.getSelectedIndex() < 1 || this.comboBox_3.getSelectedIndex() < 1) {
				JOptionPane.showMessageDialog(null, "请选择目的站点", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			endLineName = this.comboBox_2.getSelectedItem().toString();

			startStation = this.m.get(startLineName).get(this.comboBox_1.getSelectedIndex() - 1);
			endStation = this.m.get(endLineName).get(this.comboBox_3.getSelectedIndex() - 1);
			Dijkstra dijkstra = new Dijkstra(allStations.size(), this.m);
			this.result.setText(dijkstra.dijkstra(graph, allStations, startStation.getStaId(), endStation.getStaId()));
		}
	}

}
