package cn.itcast.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cn.itcast.snake.listener.SnakeListener;
import cn.itcast.snake.util.Global;

public class Snake {
	
	public static final int UP = -1;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = -2;
	
	private int oldDirection,newDirection;
	
	private Point oldTail;
	
	private LinkedList<Point> body = new LinkedList<Point>();
	
	private Set<SnakeListener> listeners = new HashSet<SnakeListener>();
	
	private boolean life;
	
	public Snake(){
		init();
	}
	
	public void init(){
		int x = Global.WIDTH / 2;
		int y = Global.HEIGHT / 2;
		for(int i = 0; i < 2; i++){
			body.addLast(new Point(x--,y));
		}
		oldDirection = newDirection = RIGHT;
		life = true;
	}
	
	public void die(){
		life = false;
		System.out.println("游戏结束！");
	}

	public void move(){
		//System.out.println("Snake's move");
		
		if(!(oldDirection + newDirection == 0)){
			oldDirection = newDirection;
		}
		
		//1.去尾
		oldTail = body.removeLast();
		
		int x = body.getFirst().x;
		int y = body.getFirst().y;
		switch(oldDirection){
		case UP:
			y--;
			if(y < 0){
				y = Global.HEIGHT - 1;
			}
			break;
		case DOWN:
			y++;
			if(y >= Global.HEIGHT){
				y = 0;
			}
			break;
		case LEFT:
			x--;
			if(x < 0){
				x = Global.WIDTH - 1;
			}
			break;
		case RIGHT:
			x++;
			if(x >= Global.WIDTH){
				x = 0;
			}
			break;			
		}
		Point newHead = new Point(x,y);
		
		//2.加头
		body.addFirst(newHead);
	}
	
	public void changeDirection(int direction){
		//System.out.println("Snake's changeDirection");
		
			newDirection = direction;	
	}
	
	public void eatFood(){
		System.out.println("吃到食物！");
		
		body.addLast(oldTail);
	}
	
	public boolean isEatBody(){
		//System.out.println("Snake's isEatBody");
		
		for(int i = 1; i < body.size(); i++){
			if(body.get(i).equals(this.getHead())){
				return true;
			}
		}
		return false;
	}
	
	public void drawMe(Graphics g){
		//System.out.println("Snake's drawMe");
		
		g.setColor(Color.BLUE);
		for(Point p : body){
			g.fill3DRect(p.x * Global.CELL_SIZE, p.y * Global.CELL_SIZE,
					Global.CELL_SIZE, Global.CELL_SIZE, true);			
		}
	}
	
	public Point getHead(){
		return body.getFirst();
	}
	
	private class SnakeDriver implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while(life){
				move();
				for(SnakeListener l : listeners){
					l.snakeMoved(Snake.this);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void start(){
		new Thread(new SnakeDriver()).start();
		System.out.println("开始游戏！");
	}
	
	public void addSnakeListener(SnakeListener l){
		if(l !=null){
			this.listeners.add(l);
		}
	}
}
