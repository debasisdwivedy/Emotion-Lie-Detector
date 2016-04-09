package MatrixOperations;

public class Transpose {
	
	public double[][] transposeMatrix(double[][] matrix)
	{
		int rows=matrix.length;
		int cols=matrix[0].length;
		double[][] transposed_matrix=new double[cols][rows];
		
		for(int i=0;i<cols;i++)
		{
			for(int j=0;j<rows;j++)
			{
				transposed_matrix[i][j]=matrix[j][i];
			}
		}
		return transposed_matrix;
	}

}
