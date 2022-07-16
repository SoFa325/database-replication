package com.company;

public class Main
{
    public static void main(String[] args){
        try {
            BDChange bd = new BDChange();
            bd.initialize();
            bd.getColAdd();
            bd.getColDel();
            bd.write();
            bd.add();
            bd.delete();
            bd.endOfWriting();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}