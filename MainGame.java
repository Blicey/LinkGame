package linkgame;

import java.util.*;

public class MainGame {
	
	public final static int Size = 40;
	public final static int Width = 10;
	public final static int Height = 6;
	
	private int[][] board;	// 0:No block; 1-8:Color
	private final int typesCnt = 8;
	private List<int[]> remain;
	private int[] remainColor;
	private List<int[]> selected;
	
	public MainGame() {
		board = new int[Width][Size];
		remain = new ArrayList<int[]>();
		remainColor = new int[typesCnt + 1];
		selected = new ArrayList<int[]>();
	}
	
	public void init() {
		fill();
	}
	
	public int color(int[] block) {
		int x = block[0];
		int y = block[1];
		return board[x][y];
	}
	
	public boolean isSelected(int[] block) {
		int x = block[0];
		int y = block[1];
		for(int i = 0; i < selected.size(); i++) {
			if(selected.get(i)[0] == x && selected.get(i)[1] == y) { return true; }
		}
		return false;
	}
	
	private void fill() {
		int pairCnt = Width * Height / 2;
		int baseColorCnt = pairCnt / typesCnt;
		int moreColorCnt = pairCnt % typesCnt;
		for(int i = 1; i <= typesCnt; i++) {
			if(i <= moreColorCnt) { remainColor[i] = baseColorCnt + 1; }
			else { remainColor[i] = baseColorCnt; }
		}
		for(int x = 0; x < Width; x++) {
			for(int y = 0; y < Height; y++) {
				remain.add(new int[] { x, y });
			}
		}
		paint();
	}
	
	public void paint() {
		Random rand = new Random();
		List<int[]> uncolored = new ArrayList<int[]>(remain);
		for(int i = 1; i <= typesCnt; i++) {
			for(int j = 0; j < remainColor[i]; j++) {
				int randLoc1 = rand.nextInt(uncolored.size());
				int[] block1 = uncolored.get(randLoc1);
				uncolored.remove(randLoc1);
				int randLoc2 = rand.nextInt(uncolored.size());
				int[] block2 = uncolored.get(randLoc2);
				uncolored.remove(randLoc2);
				board[block1[0]][block1[1]] = i;
				board[block2[0]][block2[1]] = i;
			}
		}
	}
	
	public void select(int[] block) {
		int x = block[0];
		int y = block[1];
		if(x < 0) { x = 0; }
		if(x > Width - 1) { x = Width - 1; }
		if(y < 0) { y = 0; }
		if(y > Height - 1) { y = Height - 1; }
		if(board[x][y] == 0) { return; }
		selected.add(block);
		if(selected.size() < 2) { return; }
		int[] s1 = selected.get(0);
		int[] s2 = selected.get(1);
		if(match(s1, s2)) { removeBlock(s1, s2); }
		selected.clear();
	}
	
	private void removeBlock(int[] block1, int[] block2) {
		int x1 = block1[0];
		int y1 = block1[1];
		int x2 = block2[0];
		int y2 = block2[1];
		int color = board[x1][y1];
		remainColor[color]--;
		board[x1][y1] = 0;
		board[x2][y2] = 0;
		for(int i = 0; i < remain.size(); /*NOP*/) {
			int[] b = remain.get(i);
			if(b[0] == x1 && b[1] == y1 || b[0] == x2 && b[1] == y2) { remain.remove(i); }
			else { i++; }
		}
	}
	
	private boolean match(int[] block1, int[] block2) {
		if(block1[0] == block2[0] && block1[1] == block2[1]) { return false; }
		if(board[block1[0]][block1[1]] != board[block2[0]][block2[1]]) { return false; }
		// TODO
		int x1 = block1[0];
		int y1 = block1[1];
		int x2 = block2[0];
		int y2 = block2[1];
		if(x1 != x2) {	// Different X, Horizontal search
			int y1Min, y1Max;
			for(y1Min = y1 - 1; y1Min >= -1; y1Min--) {
				if(y1Min == -1) { continue; }
				if(board[x1][y1Min] != 0) { break; }
			}
			y1Min++;
			for(y1Max = y1 + 1; y1Max <= Height; y1Max++) {
				if(y1Max == Height) { continue; }
				if(board[x1][y1Max] != 0) { break; }
			}
			y1Max--;
			int y2Min, y2Max;
			for(y2Min = y2 - 1; y2Min >= -1; y2Min--) {
				if(y2Min == -1) { continue; }
				if(board[x2][y2Min] != 0) { break; }
			}
			y2Min++;
			for(y2Max = y2 + 1; y2Max <= Height; y2Max++) {
				if(y2Max == Height) { continue; }
				if(board[x2][y2Max] != 0) { break; }
			}
			y2Max--;
			int yMin = Math.max(y1Min, y2Min);
			int yMax = Math.min(y1Max, y2Max);
			for(int y = yMin; y <= yMax; y++) {
				if(directConnectable(new int[] { x1, y }, new int[] { x2, y })) { return true; }
			}
		}
		if(y1 != y2) {	// Different Y, Vertical search
			int x1Min, x1Max;
			for(x1Min = x1 - 1; x1Min >= -1; x1Min--) {
				if(x1Min == -1) { continue; }
				if(board[x1Min][y1] != 0) { break; }
			}
			x1Min++;
			for(x1Max = x1 + 1; x1Max <= Width; x1Max++) {
				if(x1Max == Width) { continue; }
				if(board[x1Max][y1] != 0) { break; }
			}
			x1Max--;
			int x2Min, x2Max;
			for(x2Min = x2 - 1; x2Min >= -1; x2Min--) {
				if(x2Min == -1) { continue; }
				if(board[x2Min][y2] != 0) { break; }
			}
			x2Min++;
			for(x2Max = x2 + 1; x2Max <= Width; x2Max++) {
				if(x2Max == Width) { continue; }
				if(board[x2Max][y2] != 0) { break; }
			}
			x2Max--;
			int xMin = Math.max(x1Min, x2Min);
			int xMax = Math.min(x1Max, x2Max);
			for(int x = xMin; x <= xMax; x++) {
				if(directConnectable(new int[] { x, y1 }, new int[] { x, y2 })) { return true; }
			}
		}
		return false;
	}
	
	private boolean directConnectable(int[] block1, int[] block2) {
		if(block1[0] == block2[0]) {	// Same X
			int x= block1[0];
			if(x == -1 || x == Width) { return true; }
			int smallY = Math.min(block1[1], block2[1]);
			int largeY = Math.max(block1[1], block2[1]);
			for(int y = smallY + 1; y < largeY; y++) {
				if(board[x][y] != 0) { return false; }
			}
			return true;
		}
		else if(block1[1] == block2[1]) {	// Same Y
			int y = block1[1];
			if(y == -1 || y == Height) { return true; }
			int smallX = Math.min(block1[0], block2[0]);
			int largeX = Math.max(block1[0], block2[0]);
			for(int x = smallX + 1; x < largeX; x++) {
				if(board[x][y] != 0) { return false; }
			}
			return true;
		}
		return false;
	}
	
}
