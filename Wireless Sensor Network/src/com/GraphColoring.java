package com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GraphColoring {
	private Map<Vertex, List<Vertex>> adjacency_list;
	private Map<Integer, Deque<Vertex>> degree_list;
	private Map<Integer, List<Vertex>> color_list;
	private Stack<Vertex> stack;
	private int max_degree;
	private int max_color;
	private final int capacity;
	private int max_delete;
	private List<Vertex> vertex_slo;

	public int getMax_delete() {
		return max_delete;
	}

	public int[] getColor_distribution() {
		int[] color_distribution = new int[max_color + 1];
		for (int i : color_list.keySet()) {
			color_distribution[i] = color_list.get(i).size();
		}

		return color_distribution;
	}

	public int getMax_color() {
		return max_color;
	}

	public Map<Integer, List<Vertex>> getColor_list() {
		return color_list;
	}

	public GraphColoring(Map<Vertex, List<Vertex>> map, int max_degree) {
		adjacency_list = new HashMap<>();
		// deep clone
		for (Map.Entry<Vertex, List<Vertex>> entry : map.entrySet()) {
			adjacency_list.put(entry.getKey(), new ArrayList<Vertex>(entry.getValue()));
		}

		degree_list = new HashMap<>();
		color_list = new HashMap<>();
		stack = new Stack<>();
		this.max_degree = max_degree;
		capacity = map.size();
		max_color = 0;
		max_delete = 0;
		vertex_slo = new ArrayList<>();

		for (int i = 0; i <= max_degree; i++)
			degree_list.put(i, new LinkedList<>());

		for (Vertex v : adjacency_list.keySet()) {
			degree_list.get(v.original_degree).addLast(v);
		}

		small_last_ordering();

		small_last_coloring(map);

		File file = new File("/Users/wangsimin/Desktop/original_degree.txt");
		// out stream
		FileWriter out = null;
		BufferedWriter bw = null;

		try {
			out = new FileWriter(file);
			bw = new BufferedWriter(out);

			for (Vertex v : vertex_slo) {
				bw.write(new String(String.valueOf(v.original_degree)));
				bw.newLine();
				bw.flush();
			}

			out.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void small_last_coloring(Map<Vertex, List<Vertex>> map) {
		// TODO Auto-generated method stub

		System.out.println("current degree: ");
		File file = new File("/Users/wangsimin/Desktop/curr_degree.txt");
		// out stream
		FileWriter out = null;
		BufferedWriter bw = null;

		try {
			out = new FileWriter(file);
			bw = new BufferedWriter(out);
			while (!stack.isEmpty()) {
				Vertex v = stack.pop();
				// System.out.println(v.degree);
				bw.write(new String(String.valueOf(v.degree)));
				bw.newLine();
				bw.flush();
				vertex_slo.add(v);
				int color_num = 1;
				boolean flag = true;
				while (flag) {
					for (Vertex v1 : map.get(v)) {
						if (v1.color == color_num) {
							color_num++;
							flag = false;
							break;
						}
					}

					if (flag) {
						v.color = color_num;
						max_color = Math.max(color_num, max_color);
						color_list.putIfAbsent(color_num, new ArrayList<>());
						color_list.get(v.color).add(v);
						flag = false;
					} else
						flag = true;
				}
			}

			out.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void small_last_ordering() {
		// TODO Auto-generated method stub
		int count = 1;
		while (stack.size() != capacity) {
			for (int i = 0; i <= max_degree; i++) {
				if (degree_list.get(i).size() != 0 && degree_list.get(i).getFirst() != null) {
					Vertex v = degree_list.get(i).getFirst();

					if (i != 0) {
						for (Vertex neighbor_v : adjacency_list.get(v)) {
							adjacency_list.get(neighbor_v).remove(v);
							degree_list.get(neighbor_v.degree).remove(neighbor_v);
							degree_list.get(--neighbor_v.degree).addLast(neighbor_v);
						}
					}

					adjacency_list.remove(v);
					degree_list.get(i).remove(v);
					stack.push(v);
					if (v.original_degree == this.max_degree)
						max_delete = count;

					break;
				}

			}
			count++;
		}

	}

}
