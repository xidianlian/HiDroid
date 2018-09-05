package com.webrelax.util;

public class Matrix {
	private int r,c;
	private int[][] x; 
	public Matrix(int n,int m) {
		r=n;c=m;
		x=new int[r][c];
	}
	public Matrix(Matrix mat) {
		r=mat.getR();
		c=mat.getC();
		int[][] temp=mat.getX();
		x=new int[r][c];
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				x[i][j]=temp[i][j];
			}
		}
	}
	public Matrix(int[][] xx) {
		if(xx==null)
		{
			r=c=0;
			x=null;
			return;
		}
		r=xx.length;
		c=xx[0].length;
		x=xx;
	}
	public int getR() {
		return r;
	}
	public int getC() {
		return c;
	}
	public int[][] getX() {
		return x;
	}
	public void setX(int i,int j,int values) {
		x[i][j]=values;
	}
	
	public Matrix mulMatrix(Matrix mat){
		if(c!=mat.getR())return null;
		int[][] res=new int[r][mat.getC()];
		int[][] matx=mat.getX();
		
		for(int i=0;i<r;i++) {
			for(int j=0;j<mat.getC();j++) {
				int sum=0;
				for(int k=0;k<c;k++) {
					sum+=x[i][k]*matx[k][j];
				}
				res[i][j]=sum;
			}
		}
		
		return new Matrix(res);
	}
	public void setZero() {
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				x[i][j]=0;
			}
		}
	}
	public Matrix andMatrix(Matrix mat){
		if(r!=mat.getR()||c!=mat.getC())return null;
		int[][] matx=mat.getX();
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				matx[i][j]=(x[i][j]+matx[i][j])/2;
			}
		}
		return new Matrix(matx);
	}
	public Matrix T() {
		int[][] res = new int[c][r];
		for(int i=0;i<c;i++) {
			for(int j=0;j<r;j++) {
				res[i][j]=x[j][i];
			}
		}
		return new Matrix(res);
	}
	public void outputMatrix() {
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				System.out.print(x[i][j]+"");
			}
			System.out.println();
		}
	}
}
