package metro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class ReaderTxt {

	static Set<String> lines = null;
	static List<Station> allStations = null;
	static Map<String, List<Station>> map = new LinkedHashMap<>();
	static List<String> start_end = new LinkedList<>();
	static int[][] graph = null;

	public ReaderTxt(String fileName) {
		readFileContent(fileName);
		initialize();
		new Subway(map, allStations, graph);
	}

	private void readFileContent(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		lines = new LinkedHashSet<>();
		allStations = new LinkedList<>();
		List<Station> line_stations = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tempStr = null;
			int staId = 0;// 站点编号，唯一
			int time = 0;
			while ((tempStr = reader.readLine()) != null) {// 判断是否读到文件末尾
				if (time == 0) {// 如果读取的是第一条数据则初始化站点列表对象
					time++;
					line_stations = new LinkedList<>();
				}
				String[] message = tempStr.split("\\s+");// 分割站点信息
				lines.add(message[2]);// 读地铁线路
				if (message[3].equals("1")) {// 读取开通的地铁站
					// 读取同一条线路的站点，读完则更新map并重新创建新线路的站点列表
					if (!allStations.isEmpty()
							&& !((Station) ((LinkedList) allStations).getLast()).getLine().equals(message[2])) {
						map.put(((Station) ((LinkedList) allStations).getLast()).getLine(), line_stations);
						line_stations = new LinkedList<>();
					}
					Station station = new Station(staId++, message[1], message[2], message[4].equals("1"));
					allStations.add(station);
					line_stations.add(station);
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void initialize() {
		int count = 0;
		int loop = 0;

		allStations = new LinkedList<>();
		for (String st : map.keySet()) {
			boolean flag = false;
			count += map.get(st).size();
			for (Station s : map.get(st)) {
				s.setStaId(s.getStaId() - loop);
				allStations.add(s);
			}
			if (((Station) (((LinkedList) map.get(st)).getFirst())).getStaName()
					.equals(((Station) (((LinkedList) map.get(st)).getLast())).getStaName())) {// 判断该条地铁线是否为环线
				count -= 1;
				loop += 1;
				flag = true;
			}
			if (flag) {
				Station s = (Station) ((LinkedList) allStations).removeLast();
				System.out.println(s.getStaName());
				start_end.add(s.getStaName());
			}
		}

		int i = 0;
		map = new LinkedHashMap<>();
		List<Station> stations = new LinkedList<>();
		String st = null;
		for (Station s : allStations) {
			if (st == null) {
				st = s.getLine();
				stations = new LinkedList<>();
			}
			if (!s.getLine().equals(st)) {
				map.put(st, stations);
				stations = new LinkedList<>();
				st = s.getLine();
			}
			stations.add(s);
			if (s.getStaId() == ((Station) ((LinkedList) allStations).getLast()).getStaId()) {
				map.put(st, stations);
			}
		}

		System.out.println(count == allStations.size());
		graph = new int[count][count];
		for (i = 0; i < count; i++) {
			for (int j = 0; j < count; j++)
				graph[i][j] = -1;
		}
		for (i = 0; i < count; i++) {
			String name = allStations.get(i).getStaName();
			String line = allStations.get(i).getLine();
			graph[i][i] = 0;
			for (Station s : allStations) {
				if (s.getStaName().equals(name)) {
					int id = s.getStaId();
					if (id - 1 >= 0) {
						if (allStations.get(id - 1).getLine().equals(allStations.get(id).getLine())) {
							graph[i][id - 1] = 1;
							graph[id - 1][i] = 1;
						}
					}
					if (id + 1 < count) {
						if (allStations.get(id + 1).getLine().equals(allStations.get(id).getLine())) {
							graph[i][id + 1] = 1;
							graph[id + 1][i] = 1;

						}
					}
				}
			}
			// for (String str : start_end) {// 有问题
			// if (str.equals(((Station) ((LinkedList)
			// map.get(line)).getFirst()).getLine())) {// 处理地铁环线
			// int startIndex = ((Station) ((LinkedList)
			// map.get(line)).getFirst()).getStaId();
			// int endIndex = ((Station) ((LinkedList) map.get(line)).getLast()).getStaId();
			// graph[startIndex][endIndex] = 1;
			// graph[endIndex][startIndex] = 1;
			// System.out.println(1);
			// }
			// }
		}
	}

}
