package metro;

import java.util.*;

public class Dijkstra {
	/* ���� */

	private Queue visited = null;// �ѷ���վ��

	int[] distance = null;;// ·������

	private Map<String, List<Station>> line_stations = null;

	static String nowLine = null;// ��ǰ������

	static String[] nextLine = null;// ��ǰ�ڵ����һ���˵�����

	static HashMap path = null;// ·��

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
		String result = "ϵͳ��ʱ�޷���ѯ�����";
		for (int h = 0; h < list.size(); h++) {

			if (h == dest) {
				result = "��" + list.get(v).getStaName() + "��" + list.get(h).getStaName() + "֮��ľ��廻�˽���Ϊ��\n\n-->Start"
						+ list.get(v).getLine() + path.get(h).toString();
				System.out.println();
				break;
			}
		}
		return result;
	}

	public String dijkstra(int[][] weight, List<Station> list, int v, int dest) {
		this.list = list;
		// ·��HashMap path;
		path = new HashMap();
		for (int i = 0; i < list.size(); i++)
			path.put(i, "");

		// ��ʼ��·����������distance
		for (int i = 0; i < list.size(); i++) {
			path.put(i, path.get(i) + "" + list.get(v).getStaName());
			if (i == v)
				distance[i] = 0;
			// ��ͨվ��
			else if (weight[v][i] != -1) {
				distance[i] = weight[v][i];
				// ��ȡ��ǰվ�������ĵ�����
				nowLine = list.get(v).getLine();
				StringBuffer sbf = new StringBuffer();
				for (Station s : line_stations.get(nowLine)) {
					sbf.append(s.getStaName());
				}
				// ���վ����һվ���Ƿ�����ͬһ������
				if (sbf.indexOf(list.get(i).getStaName()) != -1) {
					path.put(i, path.get(i) + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = nowLine;
				} else {
					path.put(i, path.get(i) + "\n-->����" + list.get(i).getLine() + "\n\t-->" + list.get(i).getStaName());
					nextLine[i] = list.get(i).getLine();
				}
			}
			// ����ͨ
			else
				distance[i] = Integer.MAX_VALUE;
		}
		visited.add(v);

		// ����Ѱ��������·
		while (visited.size() < list.size()) {
			int k = getIndex(visited, distance);// ��ȡδ���ʵ��о���Դ������ĵ�
			visited.add(k);
			if (k != -1) {

				for (int j = 0; j < list.size(); j++) {
					if (weight[k][j] != -1)// �ж�k���ܹ�ֱ�ӵ���ĵ�
					{
						// ͨ���������㣬�Ƚ��Ƿ��бȵ�ǰ���̵�·�����еĻ��������distance��������path��
						if (distance[j] > distance[k] + weight[k][j]) {
							distance[j] = distance[k] + weight[k][j];

							nowLine = nextLine[k];
							StringBuffer sbf = new StringBuffer();
							for (Station s : line_stations.get(nowLine)) {
								sbf.append(s.getStaName());
							}
							// �жϵ���һվ���Ƿ���Ҫ����
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
												path.get(k) + "\n-->����" + str + "\n\t-->" + list.get(j).getStaName());
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