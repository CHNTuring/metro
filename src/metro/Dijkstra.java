package metro;

import java.util.*;

public class Dijkstra {
	/* 属性 */

	private Queue visited = null;// 已访问站点

	int[] distance = null;;// 路径长度

	private Map<String, List<Station>> line_stations = null;

	static String nowLine = null;// 当前地铁线

	static String[] nextLine = null;// 当前节点的下一换乘地铁线

	static HashMap path = null;// 路径

	static List<Station> list = null;

	public Dijkstra(int len, Map<String, List<Station>> line_stations) {
		this.visited = new LinkedList();
		this.distance = new int[len];
		this.line_stations = line_stations;
		this.nextLine = new String[len];
	}

	private int getIndex(Queue q, int[] dis) {
		int k = -1;
		int min_num = Integer.MAX_VALUE;
		for (int i = 0; i < dis.length; i++) {
			if (!q.contains(i)) {
				if (dis[i] < min_num) {
					min_num = dis[i];
					k = i;
				}
			}
		}
		return k;
	}

	public String shortestPath(int v, int dest) {
		String result = "系统暂时无法查询到结果";
		for (int h = 0; h < list.size(); h++) {

			if (h == dest) {
				result = "从" + list.get(v).getStaName() + "到" + list.get(h).getStaName() + "之间的具体换乘建议为：\n\n-->Start"
						+ list.get(v).getLine() + path.get(h).toString();
				System.out.println();
				break;
			}
		}
		return result;
	}

	public String dijkstra(int[][] weight, List<Station> list, int v, int dest) {
		this.list = list;
		// 路径HashMap path;
		path = new HashMap();
		for (int i = 0; i < list.size(); i++)
			path.put(i, "");

		// 初始化路径长度数组distance
		for (int i = 0; i < list.size(); i++) {
			path.put(i, path.get(i) + "" + list.get(v).getStaName());
			if (i == v)
				distance[i] = 0;
			// 连通站点
			else if (weight[v][i] != -1) {
				distance[i] = weight[v][i];
				// 获取当前站点所属的地铁线
				nowLine = list.get(v).getLine();
				StringBuffer sbf = new StringBuffer();
				for (Station s : line_stations.get(nowLine)) {
					sbf.append(s.getStaName());
				}
				// 起点站和下一站点是否属于同一地铁线
				if (sbf.indexOf(list.get(i).getStaName()) != -1) {
					path.put(i, path.get(i) + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = nowLine;
				} else {
					path.put(i, path.get(i) + "\n-->换乘" + list.get(i).getLine() + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = list.get(i).getLine();
				}
			}
			// 不连通
			else
				distance[i] = Integer.MAX_VALUE;
		}
		visited.add(v);

		// 迭代寻找最优线路
		while (visited.size() < list.size()) {
			int k = getIndex(visited, distance);// 获取未访问点中距离源点最近的点
			visited.add(k);
			if (k != -1) {

				for (int j = 0; j < list.size(); j++) {
					if (weight[k][j] != -1)// 判断k点能够直接到达的点
					{
						// 通过遍历各点，比较是否有比当前更短的路径，有的话，则更新distance，并更新path。
						if (distance[j] > distance[k] + weight[k][j]) {
							distance[j] = distance[k] + weight[k][j];

							nowLine = nextLine[k];
							StringBuffer sbf = new StringBuffer();
							for (Station s : line_stations.get(nowLine)) {
								sbf.append(s.getStaName());
							}
							// 判断到下一站点是否需要换乘
							if (sbf.indexOf(list.get(j).getStaName()) != -1) {
								path.put(j, path.get(k) + "\n\t-->" + list.get(j).getStaName());
								nextLine[j] = nowLine;
							} else {
								StringBuffer tmpSbf = new StringBuffer();

								for (String str : line_stations.keySet()) {
									tmpSbf = new StringBuffer();
									for (Station s : line_stations.get(str)) {
										tmpSbf.append(s.getStaName());
									}
									if (tmpSbf.indexOf(list.get(j).getStaName()) != -1
											&& tmpSbf.indexOf(list.get(k).getStaName()) != -1) {
										path.put(j,
												path.get(k) + "\n-->换乘" + str + "\n\t-->" + list.get(j).getStaName());
										nextLine[j] = str;
									}
								}
							}
						}
					}
				}
			}
		}

		visited.clear();
		return this.shortestPath(v, dest);
	}

}