package com.company;

public class Main
{
    public static void main(String[] args){

        try {
            BDReplication bd = new BDReplication();
            bd.initialize();
            bd.getColAdd();
            bd.getColDel();
            bd.connectToSecDB();
            bd.add();
            bd.delete();
            bd.update();
            bd.closeConnect();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}