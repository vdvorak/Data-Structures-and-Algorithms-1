package StrassenAlgorithm;

import java.util.Arrays;

/**
 * Created by jerdys on 20.04.17.
 */

public class StrassenAlgorithm {
    private static int[][] a = new int[64][64];
    private static int[][] b = new int[64][64];

    private final static int SIZE = 64;

/*
    public static void main(String[] args) {
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                a[i][j] = 2;
                b[i][j] = 5;
            }
        }

        System.out.println(Arrays.deepToString(strassenR(a, b)));
        //System.out.println(a.length + ", " + b.length);
    }
*/

    public static int[][] ikjAlgorithm(int[][] a, int[][] b) {
        int n = a.length;
        int[][] c = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                for (int j = 0; j < n; j++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return c;
    }

    private static int[][] add(int[][] a, int[][] b) {
        int n = a.length;
        int[][] c = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }

        return c;
    }

    private static int[][] subtract(int[][] a, int[][] b) {
        int n = a.length;
        int[][] c = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c[i][j] = a[i][j] - b[i][j];
            }
        }

        return c;
    }

    private static int nextPowerOfTwo(int n) {
        int log2 = (int) Math.ceil(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, log2);
    }


    private static int[][] strassenR(int[][] a, int[][] b) {
        int[][] aResult, bResult;

        int n = a.length;

        if (n <= SIZE) {
            return ikjAlgorithm(a, b);
        }
        else {
            // initializing the new sub-matrices
            int newSize = n / 2;
            int[][] a11 = new int[newSize][newSize];
            int[][] a12 = new int[newSize][newSize];
            int[][] a21 = new int[newSize][newSize];
            int[][] a22 = new int[newSize][newSize];

            int[][] b11 = new int[newSize][newSize];
            int[][] b12 = new int[newSize][newSize];
            int[][] b21 = new int[newSize][newSize];
            int[][] b22 = new int[newSize][newSize];

            // dividing the matrices in 4 sub-matrices:
            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    a11[i][j] = a[i][j]; // top left
                    a12[i][j] = a[i][j + newSize]; // top right
                    a21[i][j] = a[i + newSize][j]; // bottom left
                    a22[i][j] = a[i + newSize][j + newSize]; // bottom right

                    b11[i][j] = b[i][j]; // top left
                    b12[i][j] = b[i][j + newSize]; // top right
                    b21[i][j] = b[i + newSize][j]; // bottom left
                    b22[i][j] = b[i + newSize][j + newSize]; // bottom right
                }
            }

            // Calculating p1 to p7:
            aResult = add(a11, a22);
            bResult = add(b11, b22);

            int[][] p1 = strassenR(aResult, bResult); //p1 = (a11+a22) * (b11+b22)

            aResult = add(a21, a22); //a21 + a22
            int[][] p2 = strassenR(aResult, b11); //p2 = (a21+a22) * (b11)

            bResult = subtract(b12, b22); //b12 - b22
            int[][] p3 = strassenR(a11, bResult); //p3 = (a11) * (b12 - b22)

            bResult = subtract(b21, b11); //b21 - b11
            int[][] p4 = strassenR(a22, bResult); //p4 = (a22) * (b21 - b11)

            aResult = add(a11, a12); //a11 + a12
            int[][] p5 = strassenR(aResult, b22); //p5 = (a11+a12) * (b22)

            aResult = subtract(a21, a11); //a21 - a11
            bResult = add(b11, b12); //b11 + b12
            int[][] p6 = strassenR(aResult, bResult); //p6 = (a21-a11) * (b11+b12)

            aResult = subtract(a12, a22); //a12 - a22
            bResult = add(b21, b22); //b21 + b22
            int[][] p7 = strassenR(aResult, bResult); //p7 = (a12-a22) * (b21+b22)

            //calculating c21, c21, c11, c22:
            int[][] c12 = add(p3, p5); //c12 = p3 + p5
            int[][] c21 = add(p2, p4); //c21 = p2 + p4

            aResult = add(p1, p4); //p1 + p4
            bResult = add(aResult, p7); //p1 + p4 + p7
            int[][] c11 = subtract(bResult, p5); //c11 = p1 + p4 - p5 + p7

            aResult = add(p1, p3); //p1 + p3
            bResult = add(aResult, p6); //p1 + p3 + p6
            int[][] c22 = subtract(bResult, p2); //c22 = p1 + p3 - p2 + p6

            //Grouping the results obtained in a single matrix:
            int[][] c = new int[n][n];

            for (int i = 0; i < newSize; i++) {
                for (int j = 0; j < newSize; j++) {
                    c[i][j] = c11[i][j];
                    c[i][j + newSize] = c12[i][j];
                    c[i + newSize][j] = c21[i][j];
                    c[i + newSize][j + newSize] = c22[i][j];
                }
            }

            return c;
        }
    }

    protected static void execute(int [][] A, int [][] B, int [][] C) {
        // Make the matrices bigger so that you can apply the Strassen
        // algorithm recursively without having to deal with odd
        // matrix sizes
        int n = A.length;
        int m = nextPowerOfTwo(n);
        int[][] APrep = new int[m][m];
        int[][] BPrep = new int[m][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                APrep[i][j] = A[i][j];
                BPrep[i][j] = B[i][j];
            }
        }

        int[][] CPrep = strassenR(APrep, BPrep);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = CPrep[i][j];
            }
        }
    }
}