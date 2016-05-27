package Client;

import java.io.Serializable;

public class Protocol implements Serializable{
  
    int cmd, index;
    String msg;
    boolean Back_Block[][];

    public int getCmd() {
        return cmd;
    }
    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public boolean[][] getBlockState(){
    	return Back_Block;
    }
    public void SetBlockState(boolean[][] Blocks){
    	Back_Block = Blocks;
    }
}
