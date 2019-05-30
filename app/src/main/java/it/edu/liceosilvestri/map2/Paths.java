package it.edu.liceosilvestri.map2;

public class Paths {

    private int mCount;

    public Paths(){

    }

    public int getCount(){
        return 3;
    }

    public String getPathIdAt(int pos){
        String id;
        switch(pos){
            case 1:
                id = "pietrarsa";
                break;
            case 2:
                id = "reggia_di_portici";
                break;
            case 3:
                id = "via_diaz";
                break;
            default:
                id = "";
        }

        return id;
    }
}
