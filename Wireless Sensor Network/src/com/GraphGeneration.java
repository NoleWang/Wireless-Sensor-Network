package com;

import java.util.*;

public class GraphGeneration {

	private List<Vertex> list;
	private Map<Integer, List<Vertex>> map_cell;
	private Map<Vertex, List<Vertex>> adjacency_list;
	private double r;
	private int n;
	private int total_degree;
	private Vertex max_degree;
	private Vertex min_degree;
	private String graph;

	public Vertex getMin_degree() {
		return min_degree;
	}

	public List<Vertex> getList() {
		return list;
	}

	public int[] getDegree_list() {
		int[] degree_distribution = new int[max_degree.original_degree + 1];
		for (Vertex v : list) {
			degree_distribution[v.degree]++;
		}

		return degree_distribution;
	}

	public Vertex getMax_degree() {
		return max_degree;
	}

	public int getR_avg() {
		return total_degree / n;
	}

	public double getR() {
		return r;
	}

	public int getN() {
		return n;
	}

	public int getEdges() {
		return total_degree / 2;
	}

	public GraphGeneration(int avg, int n, String graph) {
		list = new ArrayList<>();
		map_cell = new HashMap<>();
		adjacency_list = new HashMap<>();
		this.n = n;
		total_degree = 0;
		max_degree = null;
		min_degree = null;
		this.graph = graph;

		if (graph.equals("Square")) {
			r = Math.sqrt((float) avg / ((float) n * Math.PI));
			SquareGeneration();
		} else if (graph.equals("Disk")) {
			r = Math.sqrt((float) avg / ((float) n * 4));
			DiskGeneration();
		} else {
			r = Math.sqrt((float) avg * 4.0 / ((float) n));
			SphereGeneration();
		}

		// cell method, map all vertices into length * length matrix
		cell_method();

		int max = 0;
		int min = Integer.MAX_VALUE;
		for (Vertex v : adjacency_list.keySet()) {
			v.degree = adjacency_list.get(v).size();
			v.original_degree = v.degree;
			total_degree += v.original_degree;
			if (v.original_degree > max) {
				max = v.original_degree;
				max_degree = v;
			}

			if (v.original_degree < min) {
				min = v.original_degree;
				min_degree = v;
			}
		}

	}

	public Map<Vertex, List<Vertex>> getAdjacent_list() {
		return adjacency_list;
	}

	private void SphereGeneration() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			double theta = rand.nextDouble() * 2 * Math.PI;
			double u = rand.nextDouble() * 2 - 1;

			double x = Math.sqrt(1 - Math.pow(u, 2)) * Math.cos(theta);
			double y = Math.sqrt(1 - Math.pow(u, 2)) * Math.sin(theta);

			Vertex v = new Vertex(x, y);
			v.z = u;
			list.add(v);
			getAdjacent_list().put(v, new ArrayList<>());
		}
	}

	private void DiskGeneration() {
		// TODO Auto-generated method stub
		Random rand = new Random();

		while (list.size() != n) {
			double x = rand.nextDouble();
			double y = rand.nextDouble();

			if ((Math.pow((x - 0.5), 2) + Math.pow((y - 0.5), 2)) <= 0.25) {
				Vertex v = new Vertex(x, y);
				v.ID = list.size() + 1;
				list.add(v);
				adjacency_list.put(v, new ArrayList<>());
				// System.out.println(v.x + " " + v.y);
			}
		}
	}

	private void SquareGeneration() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			Vertex v = new Vertex(rand.nextDouble(), rand.nextDouble());
			v.ID = i + 1;
			list.add(v);
			adjacency_list.put(v, new ArrayList<>());
			// System.out.println(v.x + " " + v.y);
		}

	}

	private void cell_method() {
		int length = 0;
		if (!graph.equals("Sphere"))
			length = (int) Math.ceil(1 / r);
		else
			length = (int) Math.ceil(2 / r);

		for (Vertex v : list) {
			// System.out.println("x = " + v.x + "y = " + v.y);
			int cell = 0;
			if (!graph.equals("Sphere"))
				cell = (int) (Math.ceil(v.x / r) + (Math.ceil(v.y / r) - 1) * length);
			else
				cell = (int) ((Math.ceil((1 - v.y) / r) - 1) * length + Math.ceil((v.x + 1) / r));

			map_cell.putIfAbsent(cell, new ArrayList<>());
			map_cell.get(cell).add(v);
		}

		// System.out.println(map_cell);
		// generate adjacent list if two vertices are close enough (<= r)
		for (int i = 1; i <= length * length; i++) {
			if (map_cell.get(i) == null)
				continue;

			for (int p = 0; p < map_cell.get(i).size(); p++) {
				Vertex v = map_cell.get(i).get(p);

				// cell itself
				compareWithNeighborCell(i, v);

				// right neighbor cell
				if (i % length != 0) {
					compareWithNeighborCell(i + 1, v);
				}

				// upper-right neighbor cell
				if (i > length && i % length != 0) {
					compareWithNeighborCell(i - length + 1, v);
				}

				// lower neighbor cell
				if (i <= length * (length - 1)) {
					compareWithNeighborCell(i + length, v);
				}

				// lower-right neighbor cell
				if (i <= length * (length - 1) && i % length != 0) {
					compareWithNeighborCell(i + length + 1, v);
				}

				// total_degree += v.original_degree;
				// max_degree = Math.max(max_degree, v.original_degree);
			}

		}

	}

	private void compareWithNeighborCell(int i, Vertex v) {
		if (map_cell.get(i) == null)
			return;

		for (int q = 0; q < map_cell.get(i).size(); q++) {
			Vertex t = map_cell.get(i).get(q);
			if (!graph.equals("Sphere")) {
				if (Math.sqrt(Math.pow((v.x - t.x), 2) + Math.pow((v.y - t.y), 2)) <= r) {
					if (v != t) {
						if (!adjacency_list.get(v).contains(t)) {
							adjacency_list.get(v).add(t);
							// v.degree++;
							// v.original_degree++;
						}

						if (!adjacency_list.get(t).contains(v)) {
							adjacency_list.get(t).add(v);
							// t.degree++;
							// t.original_degree++;
						}

					}
				}
			} else {
				if (Math.sqrt(Math.pow((v.x - t.x), 2) + Math.pow((v.y - t.y), 2) + Math.pow(v.z - t.z, 2)) <= r) {
					if (v != t) {
						if (!adjacency_list.get(v).contains(t)) {
							adjacency_list.get(v).add(t);
							// v.degree++;
							// v.original_degree++;
						}

						if (!adjacency_list.get(t).contains(v)) {
							adjacency_list.get(t).add(v);
							// t.degree++;
							// t.original_degree++;
						}

					}
				}
			}

		}
	}

}
