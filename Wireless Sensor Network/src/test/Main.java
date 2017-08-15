package test;

import java.util.*;

import com.BackboneSelection;
import com.GraphGeneration;
import com.Vertex;

import processing.core.PApplet;
import processing.core.PShape;

public class Main extends PApplet{
	
	public static void main(String[] args) {
		PApplet.main("test.Main");
	}
	
	public void settings(){
		size(680,680);
		//noSmooth();
	}
	
	PShape disk;
	PShape square;
	PShape point_n;
	PShape point;
	PShape point0;
	PShape point1;
	PShape point2;
	PShape point3;
	PShape point4;
	PShape point5;
	PShape point6;
	PShape bpoint1;
	PShape bpoint3;
	PShape bpoint4;
	PShape bpoint5;
	PShape bpoint6;
	PShape line;
	GraphGeneration gg;
	BackboneSelection bs;
	List<Vertex> copy;
	Map<Vertex, List<Vertex>> adjacency_list_copy;
	Map<Integer, Deque<Vertex>> degree_list;
	Map<Integer, List<Vertex>> color_list;
	Stack<Vertex> stack;
	int command;
	public void setup(){
		command = 0;
		square = createShape(RECT, 0, 0, 680, 680);
		square.setFill(color(255, 255, 255));
		point_n = createShape(ELLIPSE, 0, 0, 15, 15);
		point_n.setFill(color(192,192,192));
		point = createShape(ELLIPSE, 0, 0, 15, 15);
		point.setFill(color(204,229,255));
		point0 = createShape(ELLIPSE, 0, 0, 10, 10);
		point1 = createShape(ELLIPSE, 0, 0, 15, 15);
		point1.setFill(color(253, 153, 255));
		point2 = createShape(ELLIPSE, 0, 0, 25, 25);
		point2.setFill(color(164,164,164));
		point3 = createShape(ELLIPSE, 0, 0, 15, 15);
		point3.setFill(color(255,255,51));
		point4 = createShape(ELLIPSE, 0, 0, 15, 15);
		point4.setFill(color(0,255,255));
		point5 = createShape(ELLIPSE, 0, 0, 15, 15);
		point5.setFill(color(127,0,255));
		point6 = createShape(ELLIPSE, 0, 0, 15, 15);
		point6.setFill(color(255,102,102));
		bpoint1 = createShape(ELLIPSE, 0, 0, 20, 20);
		bpoint1.setFill(color(204, 102, 0));
		bpoint3 = createShape(ELLIPSE, 0, 0, 20, 20);
		bpoint3.setFill(color(255,255,51));
		bpoint4 = createShape(ELLIPSE, 0, 0, 20, 20);
		bpoint4.setFill(color(0,255,255));
		bpoint5 = createShape(ELLIPSE, 0, 0, 20, 20);
		bpoint5.setFill(color(127,0,255));
		bpoint6 = createShape(ELLIPSE, 0, 0, 20, 20);
		bpoint6.setFill(color(255,102,102));
		line = createShape(LINE,0,0,0,0);
		line.setStroke(126);
		line.setStrokeWeight(5);
		disk = createShape(ELLIPSE,340,340,680,680);
		disk.setFill(color(255,255,255));
		disk.addChild(point1);
		gg = new GraphGeneration(8, 16, "Square");
		System.out.println("Max_degree = " + gg.getMax_degree().original_degree);
		System.out.println("Min_degree = " + gg.getMin_degree().original_degree);
		System.out.println("radius = " + gg.getR());
		System.out.println("avg_degree = " + gg.getR_avg());
		System.out.println("# of edges = " + gg.getEdges());
		copy = new ArrayList<>(gg.getList());
		adjacency_list_copy = new HashMap<>();
		//deep clone	
		for (Map.Entry<Vertex, List<Vertex>> entry : gg.getAdjacent_list().entrySet()) {
			adjacency_list_copy.put(entry.getKey(), 
					new ArrayList<Vertex>(entry.getValue()));
		}
		
		for (Vertex v : gg.getList()) {
			System.out.print("Vertex" + v.ID + ": ");
			for (Vertex nv : gg.getAdjacent_list().get(v)) {
				System.out.print(nv.ID + " ");
			}
			System.out.println();
		}
		
		degree_list = new HashMap<>();
		stack = new Stack<>();
		
		for(int i = 0; i <= gg.getMax_degree().degree; i++) 
			degree_list.put(i, new LinkedList<>());
		
		for (Vertex v : gg.getAdjacent_list().keySet()){
			degree_list.get(v.original_degree).addLast(v);
		}
		
		for(int d : degree_list.keySet()){
			System.out.print("Degree " + d + ": ");
			for (Vertex v : degree_list.get(d)) {
				System.out.print(v.ID + " ");
			}
			System.out.println();
		}
		
		color_list = new HashMap<>();
	}
	
	public void draw(){
		noLoop();
		shape(square);
		
		if(command == 0){
			for (Vertex v : adjacency_list_copy.keySet()) {
				for (Vertex v1 : adjacency_list_copy.get(v)) {
					line((float)v.x*680, (float)v.y*680, (float)v1.x*680, (float)v1.y*680);
				}
			}
			
			for (Vertex v : copy) {
				if(v.original_degree == gg.getMax_degree().original_degree)
					shape(point2, (float)v.x*680, (float)v.y*680);
				else if(v.original_degree == gg.getMin_degree().original_degree)
					shape(point1, (float)v.x*680, (float)v.y*680);
				else
					shape(point, (float)v.x*680, (float)v.y*680);
				textSize(10);
				fill(50);
				textAlign(CENTER);
				text(v.ID, (float)v.x*680, ((float)v.y*680 + 3));
			}
			
		}else if(command > 0 && command < 17) {	
			Vertex v_min = null;
			for(int i = 0; i <= gg.getMax_degree().original_degree; i++) {
				if(degree_list.get(i).size() != 0 && degree_list.get(i).getFirst() != null){
					v_min = degree_list.get(i).getFirst();
					break;
				}
			}
			
			for (Vertex v : adjacency_list_copy.keySet()) {
				for (Vertex v1 : adjacency_list_copy.get(v)) {
					line((float)v.x*680, (float)v.y*680, (float)v1.x*680, (float)v1.y*680);
				}
			}
			
			for (Vertex v : copy) {
				if(v == v_min)
					shape(point1, (float)v.x*680, (float)v.y*680);
				else
					shape(point, (float)v.x*680, (float)v.y*680);
				textSize(10);
				fill(50);
				textAlign(CENTER);
				text(v.ID, (float)v.x*680, ((float)v.y*680 + 3));
			}
		}else if(command == 17){
			for (Vertex v : gg.getAdjacent_list().keySet()) {
				for (Vertex v1 : gg.getAdjacent_list().get(v)) {
					line((float)v.x*680, (float)v.y*680, (float)v1.x*680, (float)v1.y*680);
				}
			}
			
			for (Vertex v : gg.getList()) {
				if(v.color == 1)
					shape(point1, (float)v.x*680, (float)v.y*680);
				else if(v.color == 2)
					shape(point3, (float)v.x*680, (float)v.y*680);
				else if(v.color == 3)
					shape(point4, (float)v.x*680, (float)v.y*680);
				else if(v.color == 4)
					shape(point5, (float)v.x*680, (float)v.y*680);
				else if(v.color == 5)
					shape(point6, (float)v.x*680, (float)v.y*680);
				else
					shape(point, (float)v.x*680, (float)v.y*680);
				textSize(10);
				fill(50);
				textAlign(CENTER);
				text(v.ID, (float)v.x*680, ((float)v.y*680 + 3));
			}
		}else if(command == 18){
			Set<Vertex> set_backbone = new HashSet<>();
			for (Vertex v : bs.getBackbone().get(0)) {
					set_backbone.add(v);
			}
			
			Set<Vertex> set_neighbor = new HashSet<>();
			for (Vertex v : set_backbone) {
				for (Vertex nv : gg.getAdjacent_list().get(v)) {
					if(!set_backbone.contains(nv))
						set_neighbor.add(nv);
				}
			}
			
			for (Vertex v : gg.getAdjacent_list().keySet()) {
				for (Vertex v1 : gg.getAdjacent_list().get(v)) {
					line((float)v.x*680, (float)v.y*680, (float)v1.x*680, (float)v1.y*680);
				}
			}
			
			for (Vertex v : gg.getList()) {
				if(set_backbone.contains(v)){
					if(v.color == 1)
						shape(bpoint1, (float)v.x*680, (float)v.y*680);
					else if(v.color == 2)
						shape(bpoint3, (float)v.x*680, (float)v.y*680);
					else if(v.color == 3)
						shape(bpoint4, (float)v.x*680, (float)v.y*680);
					else if(v.color == 4)
						shape(bpoint5, (float)v.x*680, (float)v.y*680);
					else if(v.color == 5)
						shape(bpoint6, (float)v.x*680, (float)v.y*680);
					
					textSize(10);
					fill(50);
					textAlign(CENTER);
					text(v.ID, (float)v.x*680, ((float)v.y*680 + 3));
				}else 
					shape(point0, (float)v.x*680, (float)v.y*680);			
			}
		}else if(command == 19){
			Set<Vertex> set_backbone = new HashSet<>();
			for (Vertex v : bs.getBackbone().get(1)) {
					set_backbone.add(v);
			}
			
			Set<Vertex> set_neighbor = new HashSet<>();
			for (Vertex v : set_backbone) {
				for (Vertex nv : gg.getAdjacent_list().get(v)) {
					if(!set_backbone.contains(nv))
						set_neighbor.add(nv);
				}
			}
			
			for (Vertex v : gg.getAdjacent_list().keySet()) {
				for (Vertex v1 : gg.getAdjacent_list().get(v)) {
					line((float)v.x*680, (float)v.y*680, (float)v1.x*680, (float)v1.y*680);
				}
			}
			
			for (Vertex v : gg.getList()) {
				if(set_backbone.contains(v)){
					if(v.color == 1)
						shape(bpoint1, (float)v.x*680, (float)v.y*680);
					else if(v.color == 2)
						shape(bpoint3, (float)v.x*680, (float)v.y*680);
					else if(v.color == 3)
						shape(bpoint4, (float)v.x*680, (float)v.y*680);
					else if(v.color == 4)
						shape(bpoint5, (float)v.x*680, (float)v.y*680);
					else if(v.color == 5)
						shape(bpoint6, (float)v.x*680, (float)v.y*680);
					
					textSize(10);
					fill(50);
					textAlign(CENTER);
					text(v.ID, (float)v.x*680, ((float)v.y*680 + 3));
				}else 
					shape(point0, (float)v.x*680, (float)v.y*680);			
			}
		}
		
	}
	
	public void mousePressed() {
		if(command > 0 && command < 17) {
			for(int i = 0; i <= gg.getMax_degree().original_degree; i++) {
				if(degree_list.get(i).size() != 0 && degree_list.get(i).getFirst() != null){
					Vertex v = degree_list.get(i).getFirst();
	
					if(i != 0) {
						for (Vertex neighbor_v : adjacency_list_copy.get(v)) {
							adjacency_list_copy.get(neighbor_v).remove(v);
								degree_list.get(neighbor_v.degree).remove(neighbor_v);
								degree_list.get(--neighbor_v.degree).addLast(neighbor_v);					
						}
					}
					
					adjacency_list_copy.remove(v);
					degree_list.get(i).remove(v);
					copy.remove(v);
					stack.push(v);
					//System.out.println("Remove Vertex" + v.ID + ", curr_degree = " + 
					// v.degree + ", original_degree = " + v.original_degree);
					break;
				}
			}
		}
		
		if(command == 16) {
			System.out.println("graph coloring:");
			while(!stack.isEmpty()){
				Vertex v = stack.pop();
				int color_num = 1;
				boolean flag = true;
				while(flag){
					for (Vertex v1 : gg.getAdjacent_list().get(v)) {
						if(v1.color == color_num){
							color_num++;
							flag = false;
							break;
						}
					}
					
					if(flag){
						v.color = color_num;
						color_list.putIfAbsent(color_num, new ArrayList<>());
						color_list.get(v.color).add(v);
						flag = false;
					}else
						flag = true;
				}			
				System.out.println("Vertex" + v.ID + " color = " + v.color + 
						", curr_degree = " + 
						 v.degree + ", original_degree = " + v.original_degree);
			}
			
			bs = new BackboneSelection(color_list, gg.getAdjacent_list());
			for (int i = 0; i < bs.getBackbone_list().size(); i++) {
				System.out.print("Bipartite subgraph " + (i + 1) + ": ");
				for (Vertex v : bs.getBackbone_list().get(i)) {
					System.out.print(v.ID + " ");
				}
				System.out.println();
			}
		}
		
		command++;
		redraw();
	}
	
			
}
