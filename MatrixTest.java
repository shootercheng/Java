package com.hzfc.soar.zfbz.qhd.jtcyxx.matrix;

/**
 * @author chengdu
 * @date 2018/6/29.
 * 矩阵
 */
public class MatrixTest {

    public int getRow(int[][] mat){
        return mat.length;
    }

    public int getColumn(int[][] mat){
        int maxcolumn = 0;
        for(int i = 0; i < mat.length; i++){
            if(mat[i].length > maxcolumn){
                maxcolumn = mat[i].length;
            }
        }
        return maxcolumn;
    }

    public boolean checkMatColumn(int[][] mat){
        int firstColumn = mat[0].length;
        for(int i = 1; i < mat.length; i++){
            if(mat[i].length != firstColumn){
                return false;
            }
        }
        return true;
    }

    public boolean checkMatMultiple(int[][] mata, int[][] matb){
        int matacolumn = getColumn(mata);
        int matbrow = getRow(matb);
        if(matacolumn != matbrow){
            return false;
        }else{
            return true;
        }
    }

    public int[] getColumnArray(int[][] mat, int column){
        int[] columnResult = new int[mat.length];
        for(int i = 0; i < mat.length; i++){
            columnResult[i] = mat[i][column];
        }
        return columnResult;
    }

    public int getRowMultiColum(int[] a, int b[]){
        int result = 0;
        for(int i = 0; i < a.length; i++){
            result = result + a[i]*b[i];
        }
        return result;
    }

    public int[][] multipleMat(int[][] mata, int[][] matb){
        if(!checkMatColumn(mata) || !checkMatColumn(matb)){
            throw new RuntimeException("矩阵不符合规则!");
        }
        if(!checkMatMultiple(mata,matb)){
            throw new RuntimeException("矩阵相乘不符合规则!");
        }
        int matarow = getRow(mata);
        int matacolumn = getColumn(mata);
        int matbrow = getRow(matb);
        int matbcolumn = getColumn(matb);
        int minrow = matarow;
        int mincolumn = matacolumn;
        if(matarow > matbrow){
            minrow = matarow;
        }
        if(matacolumn > matbcolumn){
            mincolumn = matbcolumn;
        }
        int[][] matresult = new int[minrow][mincolumn];
        //根据矩阵的结果列表计算矩阵'
        for(int i = 0; i < minrow; i++){
            for(int j = 0; j < mincolumn; j++){
                matresult[i][j] = getRowMultiColum(mata[i], getColumnArray(matb,j));
            }
        }

        return matresult;
    }


    public void displayMat(int[][] mat){
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[i].length; j++){
                System.out.println(mat[i][j]);
            }
        }
    }

    public static void main(String[] args){
//        int[][] mata = {{1,2,3},{4,5,6},{3,4}};
//        int[][] matb = new int[2][3];
//        System.out.println(mata.length);
//        for(int i = 0; i < mata.length; i++){
//            System.out.println(mata[i].length);
//        }
        MatrixTest mt = new MatrixTest();
//        System.out.println(mt.checkMatColumn(mata));
//        mt.multipleMat(mata,matb);
        int mata[][] = {{1,2},{1,2}};
        int matb[][] = {{1,2},{1,2}};
//        mt.displayMat(matb);
//        int[] array0 = mt.getColumnArray(mata,1);
//        for(int i = 0; i < array0.length; i++){
//            System.out.println(array0[i]);
//        }
        mt.multipleMat(mata, matb);

    }
}
