package test2;

import java.util.ArrayList;

public class Truck {
	static final int MAX_COUNT = 10;
	
	int id;
	int location_id;
	int loaded_bikes_count;
	int[] command;
	int count = 0;
	
	public Truck(int id, int location_id, int loaded_bikes_count) {
		super();
		this.id = id;
		this.location_id = location_id;
		this.loaded_bikes_count = loaded_bikes_count;
		command = new int[MAX_COUNT];
	}

	public boolean isLoaded() {
		return this.loaded_bikes_count !=0;
	}
	
	public boolean canMove() {
		return this.count < MAX_COUNT;
	}
	
	public boolean isThere(int i, int j, int size) {
		return i + (j * size) == this.location_id;
	}

	public boolean findNotEnough(int mean, int size, long[][] map) {
		
		if (!this.isLoaded()) return false;
		if (!this.canMove()) return false;
		
		int currI = this.location_id % size;
		int currJ = this.location_id / size;
		int maxCount = size * 2 - 2;
		int i, j;
		if (map[currI][currJ] < mean) {
			this.unload(currI, currJ, map);
			return false;
		}
		
		for (int count = 1; count<=maxCount; count++) {
			for (int r = 0; r<=count; r++) {
				i = currI + r;
				j = currJ + count-r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] < mean) {
						this.move(r, count-r, size);
						return true;
					}
				} 
				
				i = currI - r;
				j = currJ + count-r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] < mean) {
						this.move(-r, count-r, size);
						return true;
					}
				} 
				
				i = currI + r;
				j = currJ - count+r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] < mean) {
						this.move(r, -count+r, size);
						return true;
					}
				} 
				
				i = currI - r;
				j = currJ - count+r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] < mean) {
						this.move(-r, -count+r, size);
						return true;
					}
				} 
				
			}//for end
		}//for end
		return false;
	}
	
	public void findExceeding(int mean, int size, long[][] map) {
		
		int currI = this.location_id % size;
		int currJ = this.location_id / size;

		if (!this.canMove()) return;
		
		int maxCount = size * 2 - 2;
		int i, j;
		if (map[currI][currJ] > mean) {
			this.load(currI, currJ, map);
			return;
		}
		
		for (int count = 1; count<=maxCount; count++) {
			for (int r = 0; r<=count; r++) {
				i = currI + r;
				j = currJ + count-r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] > mean) {
						this.move(r, count-r, size);
						return;
					}
				} 
				
				i = currI - r;
				j = currJ + count-r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] > mean) {
						this.move(-r, count-r, size);
						return;
					}
				} 
				
				i = currI + r;
				j = currJ - count+r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] > mean) {
						this.move(r, -count+r, size);
						return;
					}
				} 
				
				i = currI - r;
				j = currJ - count+r;
				if (i>=0 && i<size && j>=0 && j<size) {
					if (map[i][j] > mean) {
						this.move(-r, -count+r, size);
						return;
					}
				} 
				
			}//for end
		}//for end
		
	}
	
	@Override
	public String toString() {
		return "Truck [id=" + id + ", location_id=" + location_id + ", loaded_bikes_count=" + loaded_bikes_count + "]";
	}
	
	public void load(int i, int j, long[][] map) {
		map[i][j]--;
		this.loaded_bikes_count++;
		this.command[this.count] = 5;
		this.count++;
		return;
	}
	
	public void unload(int i, int j, long[][] map) {
		map[i][j]++;
		this.loaded_bikes_count--;
		this.command[this.count] = 6;
		this.count++;
		return;
	}
	
	public void move(int i, int j, int size) {
		// i ���� : + ���� - �Ʒ���
		// j ���� + ������ - ���� 
		int currI = this.location_id % size;
		int currJ = this.location_id / size;
		int move = 0;
		if (i != 0 ) {
			if ( i> 0) {
				this.command[this.count] = 1;
				this.count++;
				move = 1;
			}else {
				this.command[this.count] = 3;
				this.count++;
				move = -1;
			}
			currI = currI + move;
			this.location_id = currI + currJ * size;
		}else {
			if ( j> 0) {
				this.command[this.count] = 2;
				this.count++;
				move = 1;
			}else {
				this.command[this.count] = 4;
				this.count++;
				move = -1;
			}
			currJ = currJ + move;
			this.location_id = currI + currJ * size;
		}
		
	}
	
	
	
}