import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;  // Grid size
    private boolean[][] grid;  // Grid of open/blocked sites
    private final WeightedQuickUnionUF uf;  // Union-find structure
    private final int virtualTop;  // Virtual top site
    private final int virtualBottom;  // Virtual bottom site
    private int openSites;  // Number of open sites

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0");
        this.n = n;
        this.grid = new boolean[n][n];
        this.uf = new WeightedQuickUnionUF(n * n + 2);  // n*n + 2 for virtual top and bottom
        this.virtualTop = 0;
        this.virtualBottom = n * n + 1;
        this.openSites = 0;
    }

    // Converts (row, col) to a 1D index in the union-find data structure
    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1) + 1;
    }

    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Row and col must be between 1 and " + n);
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;  // Open the site
            openSites++;

            int index = getIndex(row, col);

            // Connect to neighboring open sites
            if (row == 1) uf.union(index, virtualTop);  // Connect to virtual top
            if (row == n) uf.union(index, virtualBottom);  // Connect to virtual bottom

            if (row > 1 && isOpen(row - 1, col)) uf.union(index, getIndex(row - 1, col));  // Above
            if (row < n && isOpen(row + 1, col)) uf.union(index, getIndex(row + 1, col));  // Below
            if (col > 1 && isOpen(row, col - 1)) uf.union(index, getIndex(row, col - 1));  // Left
            if (col < n && isOpen(row, col + 1)) uf.union(index, getIndex(row, col + 1));  // Right
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Row and col must be between 1 and " + n);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Row and col must be between 1 and " + n);
        return isOpen(row, col) && uf.find(getIndex(row, col)) == uf.find(virtualTop);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }
}
