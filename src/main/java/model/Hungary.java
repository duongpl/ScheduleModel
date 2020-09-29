package model;

import java.util.Arrays;
import java.util.Random;

public class Hungary {
    public static final int INF = Integer.MAX_VALUE / 2 - 10;
    int nx, ny, maty[], frm[], matx[];
    int n;
    int cst[][], fx[], fy[], dst[];
    boolean used[];
    public Hungary(int nx, int ny) {
        this.nx = nx;
        this.ny = ny;
        this.n = Math.max(nx, ny);
        this.fx = new int[n + 1];
        this.fy = new int[n + 1];
        this.maty = new int[n + 1];
        this.used = new boolean[n + 1];
        this.matx = new int[n + 1];
        this.dst = new int[n + 1];
        this.frm = new int[n + 1];
        Arrays.fill(fx, 0);
        Arrays.fill(fy, 0);
        Arrays.fill(maty, 0);
        cst = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(cst[i], INF);
        }
    }
    void add(int x, int y, int c) {
        cst[x][y] = c;
    }
    int mincost() {
        for (int x = 1; x <= n; x++) {
            int y0 = 0;
            maty[0] = x;
            for (int y = 0; y <= n; y++) {
                dst[y] = INF + 1;
                used[y] = false;
            }
            do {
                used[y0] = true;
                int x0 = maty[y0];
                int y1 = 0;
                int delta = INF + 1;
                for (int y = 1; y <= n; y++) if (!used[y]) {
                    int curdst = cst[x0][y] - fx[x0] - fy[y];
                    if (dst[y] > curdst) {
                        dst[y] = curdst;
                        frm[y] = y0;
                    }
                    if (delta > dst[y]) {
                        delta = dst[y];
                        y1 = y;
                    }
                }
                for (int y = 0; y <= n; y++) if (used[y]) {
                    fx[maty[y]] += delta;
                    fy[y] -= delta;
                }
                else {
                    dst[y] -= delta;
                }
                y0 = y1;
            }
            while (maty[y0] != 0);

            do {
                int y1 = frm[y0];
                maty[y0] = maty[y1];
                y0 = y1;
            }
            while (y0 != 0);
        }
        int res = 0;
        Arrays.fill(matx, 0);
        for (int y = 1; y <= n; y++) {
            int x = maty[y];
            if (cst[x][y] != INF) matx[x] = y;
            if (cst[x][y] < INF) res += cst[x][y];
        }
        return res;
    }

    int[] getMatching() {
        this.mincost();
        return this.matx;
    }

    public static void main(String[] args) {
        int n = 12;
        Hungary hungary = new Hungary(100, n);
        Random random = new Random();
        for(int i = 0 ; i < n; i++) {
            for(int j =0 ; j < n; j++) {
//                hungary.add(i, j, random.nextInt(100));
            }
        }

        hungary.getMatching();
        System.out.println("asdf");
    }
}
