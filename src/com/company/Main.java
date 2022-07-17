package com.company;

public class Main
{
    public static void main(String[] args){

        try {
            BDReplication bd = new BDReplication();
            bd.run();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}