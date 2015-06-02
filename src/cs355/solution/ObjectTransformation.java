package cs355.solution;

import java.awt.Color;

import cs355.solution.shapes.Utilities;

public class ObjectTransformation {
	public double r = 128;
	public double g = 128;
	public double b = 128;
	public double x = 0.0;
	public double y = 0.0;
	public double z = 0.0;
	public double angle = 0.0;
	public ObjectTransformation() {}
	public ObjectTransformation(double x, double y, double z, double angle) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
	}
	public ObjectTransformation setLocation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	public ObjectTransformation setAngle(double angle) {
		this.angle = angle;
		return this;
	}
	public ObjectTransformation setColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}
	public float X() {
		return (float)this.x;
	}
	public float Y() {
		return (float)this.y;
	}
	public float Z() {
		return (float)this.z;
	}
	public float R() {
		return (float)this.r/255.0f;
	}
	public float G() {
		return (float)this.g/255.0f;
	}
	public float B() {
		return (float)this.b/255.0f;
	}
	
	public Color getColor() {
		return new Color((int)this.r, (int)this.g, (int)this.b);
	}
	
	public double[][] getTranslateMatrix() {
		double[][] result = Utilities.new3dIdentityMatrix();
		result[0][3] = this.x;
		result[1][3] = this.y;
		result[2][3] = this.z;
		return result;
	}
	
	public double[][] getRotateMatrix() {
		double[][] result = Utilities.new3dIdentityMatrix();
		double cos = Math.cos(this.angle);
		double sin = Math.sin(this.angle);
		result[0][0] = cos;
		result[2][2] = cos;
		result[0][2] = -sin;
		result[2][0] = sin;
		return result;
	}
}
