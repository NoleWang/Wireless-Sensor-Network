package com;

import java.util.*;

import processing.core.PApplet;
import processing.core.PShape;

public class Main extends PApplet {

	public static void main(String[] args) {
		PApplet.main("com.Main");
	}

	public void settings() {
		size(680, 680);
		// noSmooth();
	}

	PShape disk;
	PShape square;
	PShape point;
	PShape point_back;
	PShape point_min_degree;
	PShape point_max_degree;
	PShape point1;
	PShape point2;
	PShape point3;
	PShape point4;
	PShape point5;
	PShape point6;
	PShape point7;
	GraphGeneration gg;
	GraphColoring gc;
	BackboneSelection bs;
	Set<Vertex> set_backbone1;
	Set<Vertex> set_neighbor1;
	Set<Vertex> set_backbone2;
	Set<Vertex> set_neighbor2;
	int command;
	String graph;

	public void setup() {
		square = createShape(RECT, 0, 0, 680, 680);
		square.setFill(color(255, 255, 255));
		point = createShape(ELLIPSE, 0, 0, 6, 6);
		point.setFill(color(255, 223, 223));
		point_back = createShape(ELLIPSE, 0, 0, 4, 4);
		point_back.setFill(color(255, 223, 223));
		point_min_degree = createShape(ELLIPSE, 0, 0, 15, 15);
		point_min_degree.setFill(color(0, 255, 0));
		point_max_degree = createShape(ELLIPSE, 0, 0, 20, 20);
		point_max_degree.setFill(color(0, 0, 255));
		point1 = createShape(ELLIPSE, 0, 0, 6, 6);
		point1.setFill(color(255, 0, 0));
		point2 = createShape(ELLIPSE, 0, 0, 6, 6);
		point2.setFill(color(0, 255, 0));
		point3 = createShape(ELLIPSE, 0, 0, 6, 6);
		point3.setFill(color(0, 255, 255));
		point4 = createShape(ELLIPSE, 0, 0, 6, 6);
		point4.setFill(color(127, 0, 255));
		point5 = createShape(ELLIPSE, 0, 0, 6, 6);
		point5.setFill(color(192, 192, 192));
		point6 = createShape(ELLIPSE, 0, 0, 6, 6);
		point6.setFill(color(255, 153, 51));
		point7 = createShape(ELLIPSE, 0, 0, 6, 6);
		point7.setFill(color(153, 153, 255));
		disk = createShape(ELLIPSE, 340, 340, 680, 680);
		disk.setFill(color(255, 255, 255));
		command = 0;
		
		long startTime =System.currentTimeMillis();
		graph = "Square";
		gg = new GraphGeneration(128, 64000, graph);
		System.out.println("Max_degree = " + gg.getMax_degree().original_degree);
		System.out.println("Min_degree = " + gg.getMin_degree().original_degree);
		System.out.println("radius = " + gg.getR());
		System.out.println("avg_degree = " + gg.getR_avg());
		System.out.println("# of edges = " + gg.getEdges());
		System.out.println("Degree distribution: ");
		int[] arr = gg.getDegree_list();
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}

		gc = new GraphColoring(gg.getAdjacent_list(), gg.getMax_degree().original_degree);
		System.out.println("# of color: " + gc.getMax_color());
		System.out.println("Max_degree Vertex deleleted at: " + gc.getMax_delete());
		System.out.println("Color distribution: ");
		int[] result = gc.getColor_distribution();
		for (int i = 1; i < result.length; i++) {
			System.out.println(result[i]);
		}

		bs = new BackboneSelection(gc.getColor_list(), gg.getAdjacent_list());
		for (List<Vertex> list : bs.getBackbone()) {
			System.out.println("backbone size = " + list.size());
		}

		set_backbone1 = new HashSet<>();
		System.out.print("Backbone 1 color: ");
		for (Vertex v : bs.getBackbone().get(0)) {
			set_backbone1.add(v);
			System.out.print(v.color + " ");
		}
		System.out.println();
		set_neighbor1 = new HashSet<>();
		for (Vertex v : set_backbone1) {
			for (Vertex nv : gg.getAdjacent_list().get(v)) {
				if (!set_backbone1.contains(nv))
					set_neighbor1.add(nv);
			}
		}
		System.out.println("coverage 1 = " + (set_backbone1.size() + set_neighbor1.size()));

		set_backbone2 = new HashSet<>();
		System.out.print("Backbone 2 color: ");
		for (Vertex v : bs.getBackbone().get(1)) {
			set_backbone2.add(v);
			System.out.print(v.color + " ");
		}
		System.out.println();
		set_neighbor2 = new HashSet<>();
		for (Vertex v : set_backbone2) {
			for (Vertex nv : gg.getAdjacent_list().get(v)) {
				if (!set_backbone2.contains(nv))
					set_neighbor2.add(nv);
			}
		}
		System.out.println("coverage 2 = " + (set_backbone2.size() + set_neighbor2.size()));
		long endTime = System.currentTimeMillis();
		System.out.println("Running time: " + (endTime - startTime));
	}

	public void draw() {
		noLoop();
		shape(square);
		
		if(!graph.equals("Sphere")){
			if (command == 0) {
				for (Vertex v : gg.getList()) {
					if (v.original_degree == gg.getMax_degree().original_degree)
						shape(point_max_degree, (float) v.x * 680, (float) v.y * 680);
					else if (v.original_degree == gg.getMin_degree().original_degree)
						shape(point_min_degree, (float) v.x * 680, (float) v.y * 680);
					else
						shape(point, (float) v.x * 680, (float) v.y * 680);
				}
				
				shape(point_max_degree, (float) gg.getMax_degree().x * 680, (float) gg.getMax_degree().y * 680);
				shape(point_min_degree, (float) gg.getMin_degree().x * 680, (float) gg.getMin_degree().y * 680);
			} else if (command == 1) {
				for (Vertex v : gg.getAdjacent_list().keySet()) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						line((float) v.x * 680, (float) v.y * 680, (float) v1.x * 680, (float) v1.y * 680);
					}
				}

				for (Vertex v : gg.getList()) {
					if (v.original_degree == gg.getMax_degree().original_degree)
						shape(point_max_degree, (float) v.x * 680, (float) v.y * 680);
					else if (v.original_degree == gg.getMin_degree().original_degree)
						shape(point_min_degree, (float) v.x * 680, (float) v.y * 680);
					else
						shape(point, (float) v.x * 680, (float) v.y * 680);
				}
				
				shape(point_max_degree, (float) gg.getMax_degree().x * 680, (float) gg.getMax_degree().y * 680);
				shape(point_min_degree, (float) gg.getMin_degree().x * 680, (float) gg.getMin_degree().y * 680);
			} else if (command == 2) {
				for (Vertex v : set_backbone1) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						if (set_backbone1.contains(v1))
							line((float) v.x * 680, (float) v.y * 680, (float) v1.x * 680, (float) v1.y * 680);
					}
				}

				for (Vertex v : set_backbone1) {
					if (v.color == 1)
						shape(point1, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 2)
						shape(point2, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 3)
						shape(point3, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 4)
						shape(point4, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 5)
						shape(point5, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 6)
						shape(point6, (float) v.x * 680, (float) v.y * 680);
					else
						shape(point7, (float) v.x * 680, (float) v.y * 680);
				}
			} else if (command == 3) {
				for (Vertex v : set_backbone2) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						if (set_backbone2.contains(v1))
							line((float) v.x * 680, (float) v.y * 680, (float) v1.x * 680, (float) v1.y * 680);
					}
				}

				for (Vertex v : set_backbone2) {
					if (v.color == 1)
						shape(point1, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 2)
						shape(point2, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 3)
						shape(point3, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 4)
						shape(point4, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 5)
						shape(point5, (float) v.x * 680, (float) v.y * 680);
					else if (v.color == 6)
						shape(point6, (float) v.x * 680, (float) v.y * 680);
					else
						shape(point7, (float) v.x * 680, (float) v.y * 680);
				}
			}

		}else{
			if (command == 0) {
				for (Vertex v : gg.getList()) {
					if (v.original_degree == gg.getMax_degree().original_degree)
						shape(point_max_degree, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.original_degree == gg.getMin_degree().original_degree)
						shape(point_min_degree, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if(v.z < 0)
						shape(point_back, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else
						shape(point, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
				}
				
				shape(point_max_degree, ((float) gg.getMax_degree().x + 1) * 340, (1 - (float) gg.getMax_degree().y) * 340);
				shape(point_min_degree, ((float) gg.getMin_degree().x + 1) * 340, (1 - (float) gg.getMin_degree().y) * 340);
			} else if (command == 1) {
				for (Vertex v : gg.getAdjacent_list().keySet()) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						line(((float) v.x + 1) * 340, (1 - (float) v.y) * 340, ((float) v1.x + 1) * 340, (1 - (float) v1.y) * 340);
					}
				}

				for (Vertex v : gg.getList()) {
					if (v.original_degree == gg.getMax_degree().original_degree)
						shape(point_max_degree, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.original_degree == gg.getMin_degree().original_degree)
						shape(point_min_degree, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if(v.z < 0)
						shape(point_back, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else
						shape(point, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
				}
				
				shape(point_max_degree, ((float) gg.getMax_degree().x + 1) * 340, (1 - (float) gg.getMax_degree().y) * 340);
				shape(point_min_degree, ((float) gg.getMin_degree().x + 1) * 340, (1 - (float) gg.getMin_degree().y) * 340);
			} else if (command == 2) {
				for (Vertex v : set_backbone1) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						if (set_backbone1.contains(v1))
							line(((float) v.x + 1) * 340, (1 - (float) v.y) * 340, ((float) v1.x + 1) * 340, (1 - (float) v1.y) * 340);
					}
				}

				for (Vertex v : set_backbone1) {
					if (v.color == 1)
						shape(point1, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 2)
						shape(point2, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 3)
						shape(point3, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 4)
						shape(point4, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 5)
						shape(point5, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 6)
						shape(point6, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else
						shape(point7, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
				}
			} else if (command == 3) {
				for (Vertex v : set_backbone2) {
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						if (set_backbone2.contains(v1))
							line(((float) v.x + 1) * 340, (1 - (float) v.y) * 340, ((float) v1.x + 1) * 340, (1 - (float) v1.y) * 340);
					}
				}

				for (Vertex v : set_backbone2) {
					if (v.color == 1)
						shape(point1, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 2)
						shape(point2, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 3)
						shape(point3, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 4)
						shape(point4, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 5)
						shape(point5, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else if (v.color == 6)
						shape(point6, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
					else
						shape(point7, ((float) v.x + 1) * 340, (1 - (float) v.y) * 340);
				}
			}

		}
		
	}

	public void mousePressed() {
		command++;
		redraw();
	}

}
