package com;

public class Vertex {
	public double x;
	public double y;
	public double z;
	public int degree;
	public int original_degree;
	public int color;
	public int ID;
	
	public Vertex(double x, double y){
		this.x = x;
		this.y = y;
		color = 0;
		degree = 0;
		original_degree = 0;
		ID = 0;
	}
	
}
