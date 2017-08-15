package com;

import java.awt.Point;
import java.util.*;

public class BackboneSelection {

	private Map<Integer, List<Vertex>> color_list;
	private int[] selected_color;
	private List<List<Vertex>> backbone_list;
	private List<List<Vertex>> backbone;

	public List<List<Vertex>> getBackbone() {
		return backbone;
	}

	public List<List<Vertex>> getBackbone_list() {
		return backbone_list;
	}

	public BackboneSelection(Map<Integer, List<Vertex>> color, Map<Vertex, List<Vertex>> adjacency_list) {
		color_list = color;
		selected_color = selectFourColor();
		backbone_list = new ArrayList<>();
		backbone = new ArrayList<>();

		// find six possible bipartite subgraphs
		for (int i = 0; i < selected_color.length - 1; i++) {
			List<Vertex> bipart_list = new ArrayList<>(color_list.get(selected_color[i]));

			for (int j = i + 1; j < selected_color.length; j++) {
				bipart_list.addAll(color_list.get(selected_color[j]));
				// System.out.println("bipart_list = " + bipart_list.size());
				backbone_list.add(findBackbone(bipart_list, selected_color[i], selected_color[j], adjacency_list));
				bipart_list.removeAll(color_list.get(selected_color[j]));
			}

		}

		// find two largest backbones
		List<List<Vertex>> backbone_list_copy = new ArrayList<>(backbone_list);
		for (int i = 0; i < 2; i++) {
			int max = 0;
			int degree = 0;
			int index = 0;
			int edges = 0;
			Set<Vertex> set = new HashSet<>();
			for (int j = 0; j < backbone_list_copy.size(); j++) {
				if (i == 0)
					System.out.print("Bipartite subgraph " + (j + 1) + ": ");
				for (Vertex v : backbone_list_copy.get(j)) {
					degree += v.original_degree;
					for (Vertex nv : adjacency_list.get(v)) {
						if(!set.contains(nv) && backbone_list_copy.get(j).contains(nv))
							edges++;
					}
					
					set.add(v);
				}

				if (edges > max) {
					max = edges;
					index = j;
				}

				if (i == 0)
					System.out.println(edges + " edges, "
							+ backbone_list_copy.get(j).size() + " vertices, " + degree + " degree");
				degree = 0;
				edges = 0;
				set.clear();
			}

			backbone.add(backbone_list_copy.get(index));
			backbone_list_copy.remove(index);
		}

	}

	private List<Vertex> findBackbone(List<Vertex> bipart_list, int i, int j,
			Map<Vertex, List<Vertex>> adjacency_list) {
		// TODO Auto-generated method stub
		Queue<Vertex> q = new LinkedList<>();
		List<Vertex> result = new ArrayList<>();

		for (Vertex v : bipart_list) {
			Set<Vertex> visited = new HashSet<>();
			q.add(v);
			visited.add(v);

			List<Vertex> current_list = new ArrayList<>();
			current_list.add(v);
			while (!q.isEmpty()) {
				Vertex curr_vertex = q.poll();
				for (Vertex nv : adjacency_list.get(curr_vertex)) {
					if ((nv.color == i || nv.color == j) && !visited.contains(nv)) {
						q.add(nv);
						visited.add(nv);
						current_list.add(nv);
					}
				}
			}

			if (current_list.size() > result.size())
				result = current_list;
		}

		return result;
	}

	private int[] selectFourColor() {
		// TODO Auto-generated method stub
		Queue<Point> q = new PriorityQueue<Point>(color_list.size(), new Comparator<Point>() {
			public int compare(Point n1, Point n2) {
				if (n1.y < n2.y)
					return 1;
				else if (n1.y > n2.y)
					return -1;
				else
					return 0;
			}
		});

		for (Integer i : color_list.keySet()) {
			q.add(new Point(i, color_list.get(i).size()));
		}

		int[] color = new int[4];
		// System.out.println("queue size:" + q.size());
		for (int i = 0; i < color.length; i++) {
			color[i] = q.poll().x;
		}

		return color;
	}
}
