package Model.MatrixOperations;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class MatrixMultiplication {
	
	/*
	 * 
	 * Strassens multiplication reference taken from "Introduction to Algorithm- Cormen"
	 */
	
	public double[][] matrixMultiply(double[][] first, double[][] second)
	{
		int first_matrix_rows=first.length;
		int first_matrix_cols=first[0].length;
		int second_matrix_rows=second.length;
		int second_matrix_cols=second[0].length;
		double[][] output;
		if(first_matrix_cols!=second_matrix_rows)
		{
			System.out.println("incompatable matrix for multiplication");
			return null;
		}
		else
		{
			if(first_matrix_rows==first_matrix_cols &&second_matrix_rows==second_matrix_cols)
			{
				output=strassen_multiply(first,second);
			}
			else
			{
				output=multiply_general(first,second);
			}
		}
		return output;
	}
   
 //General Matrix Multiplication  
   private static double[][] multiply_general(double[][] first, double[][] second)
   {
	   int c, d, k;
	   double sum=(double)0.0;
	   int first_matrix_rows=first.length;
		int first_matrix_cols=first[0].length;
		int second_matrix_rows=second.length;
		int second_matrix_cols=second[0].length;
       double multiply[][] = new double[first_matrix_rows][second_matrix_cols];
       for ( c = 0 ; c < first_matrix_rows ; c++ )
       {
          for ( d = 0 ; d < second_matrix_cols ; d++ )
          {   
             for ( k = 0 ; k < first_matrix_cols ; k++ )
             {
                sum = sum + first[c][k]*second[k][d];
             }

             multiply[c][d] = sum;
             sum = 0;
          }
       }
       return multiply;
   }
 
 //Strassen Matrix Multiplication  
   private static double[][] strassen_multiply(double[][] A, double[][] B)
   {        
       int n = A.length;
       double[][] R = new double[n][n];
       //Base case
       if (n <=16)// didn't give better performance for for n=1,2,4,8 ; But worked for n=16 and row_size=512
           if(n==1)
           {
    	   R[0][0] = A[0][0] * B[0][0];
           }
           else
           {
    	   R=multiply_general(A,B);
           }
       else
       {
           double[][] A11 = new double[n/2][n/2];
           double[][] A12 = new double[n/2][n/2];
           double[][] A21 = new double[n/2][n/2];
           double[][] A22 = new double[n/2][n/2];
           double[][] B11 = new double[n/2][n/2];
           double[][] B12 = new double[n/2][n/2];
           double[][] B21 = new double[n/2][n/2];
           double[][] B22 = new double[n/2][n/2];

           //Divide Matrix A
           split(A, A11, 0 , 0);
           split(A, A12, 0 , n/2);
           split(A, A21, n/2, 0);
           split(A, A22, n/2, n/2);
           //Divide Matrix B
           split(B, B11, 0 , 0);
           split(B, B12, 0 , n/2);
           split(B, B21, n/2, 0);
           split(B, B22, n/2, n/2);

            //Strassens Formula
             /*M1 = (A11 + A22)(B11 + B22)
             M2 = (A21 + A22) B11
             M3 = A11 (B12 - B22)
             M4 = A22 (B21 - B11)
             M5 = (A11 + A12) B22
             M6 = (A21 - A11) (B11 + B12)
             M7 = (A12 - A22) (B21 + B22)*/
           

           double [][] M1 = strassen_multiply(add(A11, A22), add(B11, B22));
           double [][] M2 = strassen_multiply(add(A21, A22), B11);
           double [][] M3 = strassen_multiply(A11, sub(B12, B22));
           double [][] M4 = strassen_multiply(A22, sub(B21, B11));
           double [][] M5 = strassen_multiply(add(A11, A12), B22);
           double [][] M6 = strassen_multiply(sub(A21, A11), add(B11, B12));
           double [][] M7 = strassen_multiply(sub(A12, A22), add(B21, B22));

           /*
             C11 = M1 + M4 - M5 + M7
             C12 = M3 + M5
             C21 = M2 + M4
             C22 = M1 - M2 + M3 + M6
           */
           double [][] C11 = add(sub(add(M1, M4), M5), M7);
           double [][] C12 = add(M3, M5);
           double [][] C21 = add(M2, M4);
           double [][] C22 = add(sub(add(M1, M3), M2), M6);

           //Join the 4 Above Matrx
           join(C11, R, 0 , 0);
           join(C12, R, 0 , n/2);
           join(C21, R, n/2, 0);
           join(C22, R, n/2, n/2);
       }
       
       
       return R;
   }
   
   
   private static double[][] sub(double[][] A, double[][] B)
   {
       int n = A.length;
       double[][] C = new double[n][n];
       for (int i = 0; i < n; i++)
           for (int j = 0; j < n; j++)
               C[i][j] = A[i][j] - B[i][j];
       return C;
   }
   
   
   private static double[][] add(double[][] A, double[][] B)
   {
       int n = A.length;
       double[][] C = new double[n][n];
       for (int i = 0; i < n; i++)
           for (int j = 0; j < n; j++)
               C[i][j] = A[i][j] + B[i][j];
       return C;
   }
   
   
   private static void split(double[][] P, double[][] C, int iB, int jB) 
   {
       for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
           for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
               C[i1][j1] = P[i2][j2];
   }
   

   private static void join(double[][] C, double[][] P, int iB, int jB) 
   {
       for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
           for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
               P[i2][j2] = C[i1][j1];
   }
   
   public static Mat multiplyMat(Mat a,Mat b,Mat c)
   {
	   Core.multiply(a, b, c);
	   return c;
   }

}
